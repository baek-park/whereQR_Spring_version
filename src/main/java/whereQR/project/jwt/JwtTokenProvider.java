package whereQR.project.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import whereQR.project.entity.Member;
import whereQR.project.exception.CustomExceptions.IllegalArgumentException;
import whereQR.project.exception.CustomExceptions.MalformedJwtException;
import whereQR.project.exception.CustomExceptions.ExpiredJwtException;
import whereQR.project.exception.CustomExceptions.UnsupportedJwtException;
import whereQR.project.exception.CustomExceptions.SecurityException;
import whereQR.project.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    private final String claimName = "auth";
    private static final String grantType = "Bearer";
    private final long expiration; // 토큰의 만료 시간 (밀리초)
    private final long refreshExpiration; // 리프레시 토큰의 만료 시간 (밀리초)
    private final MemberService memberService;

    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey,
                            @Value("${jwt.expiration}") long expiration,
                            @Value("${jwt.refreshExpiration}") long refreshExpiration, MemberService memberService) {

        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
        this.memberService = memberService;

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Member member, Date expiration){

        log.info("generateToken for member : {}" , member.getId());

        return Jwts.builder()
                .setIssuer("whereQr")
                .setExpiration(expiration)
                .setIssuedAt(new Date())
                .setSubject(member.getId().toString())
                .claim(claimName, member.getRole())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }
    public String getAccessToken(Member member) {

        Date expiry = new Date();
        expiry.setTime(expiry.getTime() + expiration);

        return generateToken(member, expiry);
    }

    public String getRefreshToken(Member member) {

        Date expiry = new Date();
        expiry.setTime(expiry.getTime() + refreshExpiration);

        return generateToken(member, expiry);
    }

    // JWT token 복호화
    public MemberDetails getMemberByToken(String accessToken){
        Claims claim = decodeToken(accessToken);
        String memberId = claim.getSubject();
        Member member = memberService.getMemberById(UUID.fromString(memberId));
        log.info("getMemberByToken -> {}",member);
        return member.toMemberDetails();
    }

    // 토큰 복호화
    public Claims decodeToken(String accessToken){
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            e.printStackTrace();
            return e.getClaims();
        }
    }

    //토큰 정보 검증 메서드
    public boolean validateToken(String token){

        String path = this.getClass().toString();
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            throw new SecurityException("유효하지 않은 JWT Token 입니다.", path);
        } catch(MalformedJwtException e){
            throw new MalformedJwtException("유효하지 않은 JWT Token 입니다.", path);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT Token 입니다.", e);
            throw new ExpiredJwtException("만료된 JWT Token 입니다.", path);
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 않은 JWT Token 입니다.", e);
            throw new UnsupportedJwtException("지원하지 않는 않은 JWT Token 입니다.", path);
        } catch (IllegalArgumentException e) {
            log.error("JWT의 클레임이 비어있습니다.", e);
            throw new IllegalArgumentException("JWT의 클레임이 비어있습니다.", path);
        }
    }

    public static String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(grantType)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
