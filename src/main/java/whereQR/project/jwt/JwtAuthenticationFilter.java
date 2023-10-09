package whereQR.project.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import whereQR.project.exception.CustomException;
import whereQR.project.exception.CustomExceptions.InternalException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)  {

        // 토큰을 추출
        try{
            String token = jwtTokenProvider.extractTokenFromHeader((HttpServletRequest) request);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰이 유효한 경우 인증 정보 설정
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        }catch (ServletException e) {
            log.error("ServletException");
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("IOException");
            throw new RuntimeException(e);
        }

    }

}
