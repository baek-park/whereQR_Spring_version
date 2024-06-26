package whereQR.project.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.jwt.TokenInfo;
import whereQR.project.jwt.JwtTokenProvider;
import whereQR.project.properties.AuthProperties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthProperties authProperties;


    public TokenInfo updateToken(Member member){

        // Token 발급
        String accessToken = jwtTokenProvider.getAccessToken(member);
        String refreshToken = jwtTokenProvider.getRefreshToken(member);

        return new TokenInfo(accessToken, refreshToken);
    }

    @Transactional
    public void removeRefreshToken(Member member){
        member.updateToken(null);
    }

    public void removeRefreshTokenInCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("refresh-token", null);
        cookie.setPath("/");
        cookie.setDomain(authProperties.getDomain());
        cookie.setSecure(authProperties.getSecure());
        cookie.setHttpOnly(authProperties.getHttpOnly());

        // cookie를 저장
        response.addCookie(cookie);
    }

    public void refreshTokenToCookie(String accessToken, HttpServletResponse response){

        Cookie cookie = new Cookie("refresh-token", accessToken);
        cookie.setPath("/");
        cookie.setDomain(authProperties.getDomain());
        cookie.setSecure(authProperties.getSecure());
        cookie.setHttpOnly(authProperties.getHttpOnly());
        cookie.setMaxAge(86400); // 1일로 설정. access token

        // cookie를 저장
        response.addCookie(cookie);
    }

    @Transactional
    public String updateRefreshToken(Member member, String refreshToken){
        return member.updateToken(refreshToken);
    }
}
