package whereQR.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.member.TokenInfo;
import whereQR.project.jwt.JwtTokenProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenInfo updateToken(Member member){
        // Token 발급
        String accessToken = jwtTokenProvider.getAccessToken(member);
        String refreshToken = jwtTokenProvider.getRefreshToken(member);

        // Refresh Token 저장
        member.setRefreshToken(refreshToken);

        return new TokenInfo(accessToken, refreshToken);
    }

    @Transactional
    public void removeRefreshToken(Member member){
        member.setRefreshToken(null);
    }

    public void removeAccessTokenInCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("WhereQr-AccessToken", null);
        cookie.setPath("/");
        cookie.setSecure(false); // Todo : ssl 구매 후 변경 & local은 postman -> false
        cookie.setHttpOnly(true);

        // cookie를 저장
        response.addCookie(cookie);
    }

    public void accessTokenToCookie(String accessToken, HttpServletResponse response){

        Cookie cookie = new Cookie("WhereQr-AccessToken", accessToken);
        cookie.setPath("/");
        cookie.setSecure(false); // Todo : ssl 구매 후 변경 & local은 postman -> false
        cookie.setHttpOnly(true);
        cookie.setMaxAge(86400); // 1일로 설정. access token

        // cookie를 저장
        response.addCookie(cookie);
    }

}
