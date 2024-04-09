package whereQR.project.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        log.info("JwtAuthenticationFilter");
        // 토큰을 추출
        String token = jwtTokenProvider.extractTokenFromHeader((HttpServletRequest) request);

        if(token==null){
            // 우선 통과시키고 아닐 경우 아래에서 검증
            log.info("no token");
        }else{
            try{
                boolean isValid = jwtTokenProvider.validateToken(token);
                if (isValid) {
                    // 토큰이 유효한 경우 인증 정보 설정
                    MemberDetails memberDetails = jwtTokenProvider.getMemberByToken(token);
                    WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request);
                    CustomAuthenticationToken authentication = new CustomAuthenticationToken(memberDetails,details);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            catch (Exception e)
            {
                throw e;
            }

        }

        chain.doFilter(request, response);

    }

    private void handleAuthenticationException(HttpServletResponse response, String message ) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        Map<String, Object> responseBodyData = new HashMap<>();
        responseBodyData.put("message", message);
        responseBodyData.put("errorType", "UNAUTHORIZED");
        responseBody.put("status", "FAILED");
        responseBody.put("data", responseBodyData);

        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(responseBody));
        writer.flush();
        writer.close();
    }

}
