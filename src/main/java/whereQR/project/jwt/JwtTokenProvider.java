package whereQR.project.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import whereQR.project.entity.dto.TokenInfo;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    private final String claimName = "auth";
    private final String grantType = "Bearer";

    private final long expiration; // 토큰의 만료 시간 (밀리초)
    private final long refreshExpiration; // 리프레시 토큰의 만료 시간 (밀리초)

    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey,
                            @Value("${jwt.expiration}") long expiration,
                            @Value("${jwt.refreshExpiration}") long refreshExpiration) {

        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfo generateToken(Authentication authentication){

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        String accessToken = getAccessToken(authentication, authorities, now);

        // Refresh Token 생성
        String refreshToken = getRefreshToken(Jwts.builder(), new Date(now + refreshExpiration));

        return TokenInfo.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String getRefreshToken(JwtBuilder builder, Date now) {
        String refreshToken = builder
                .setExpiration(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return refreshToken;
    }

    private String getAccessToken(Authentication authentication, String authorities, long now) {
        Date accessTokenExpiresIn = new Date(now + expiration);
        String accessToken = getRefreshToken(Jwts.builder()
                .setSubject(authentication.getName())
                .claim(claimName, authorities), accessTokenExpiresIn);
        return accessToken;
    }

    // JWT 토큰을 복호화
    public Authentication getAuthentication(String accessToken) {

        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(claimName) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(claimName).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }

    // 토큰 복호화
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //토큰 정보 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("유효하지 않은 JWT Token 입니다.", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT Token 입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 않은 JWT Token 입니다.", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT의 클레임이 비어있습니다.", e);
        }
        return false;
    }

}
