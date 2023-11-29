package whereQR.project.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        // 토큰을 추출
        String token = jwtTokenProvider.extractTokenFromHeader((HttpServletRequest) request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효한 경우 인증 정보 설정
            MemberDetails memberDetails = jwtTokenProvider.getMemberByToken(token);
            WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request);
            CustomAuthenticationToken authentication = new CustomAuthenticationToken(memberDetails,details);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // Todo : token이 필요한데 null일경우에 대한 exception handling
        chain.doFilter(request, response);

    }

}
