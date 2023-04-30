package whereQR.project.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import whereQR.project.entity.dto.TokenInfo;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    @Value("${jwt.secretKey}")
    private String secretKey; // JWT 서명에 사용할 비밀 키

    @Value("${jwt.expiration}")
    private long expiration; // 토큰의 만료 시간 (밀리초)

    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration; // 리프레시 토큰의 만료 시간 (밀리초)

    public JwtTokenProvider() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //accesToken을 user 정보를 가지고 생성
    public String  generateAccessToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public TokenInfo generateToken(String username){
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String accessToken = jwtTokenProvider.generateAccessToken(username);
        String refreshToken = jwtTokenProvider.generateRefreshToken(username);

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //JWT claim을 복호화하여 토큰의 정보를 꺼내는 메서드 작성
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    //토큰 정보 검증 메서드를 작성
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 토큰이 유효하지 않음
            log.error("JwtTokenProvider -> validationToken -> error : {}", e);
        }
        return false;
    }

}
