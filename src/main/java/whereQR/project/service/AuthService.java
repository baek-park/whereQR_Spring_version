package whereQR.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.Member;
import whereQR.project.jwt.TokenInfo;
import whereQR.project.jwt.JwtTokenProvider;
import whereQR.project.properties.AuthProperties;
import org.springframework.http.ResponseCookie;
import javax.servlet.http.HttpServletResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthProperties authProperties;

    @Transactional
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

    public void removeAccessTokenInCookie(HttpServletResponse response){
        ResponseCookie cookie = ResponseCookie.from("access-token", null)
                .path("/")
                .sameSite("None")
                .httpOnly(authProperties.getHttpOnly())
                .secure(authProperties.getSecure())
                .domain(authProperties.getDomain())
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

    }

    public void accessTokenToCookie(String accessToken, HttpServletResponse response){

        ResponseCookie cookie = ResponseCookie.from("access-token", accessToken)
                .path("/")
                .sameSite("None")
                .httpOnly(authProperties.getHttpOnly())
                .secure(authProperties.getSecure())
                .maxAge(86400)
                .domain(authProperties.getDomain())
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

    }

    @Transactional
    public String updateRefreshToken(Member member, String refreshToken){
        return member.updateToken(refreshToken);
    }
}
