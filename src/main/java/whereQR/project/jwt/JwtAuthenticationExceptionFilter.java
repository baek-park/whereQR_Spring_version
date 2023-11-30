package whereQR.project.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import whereQR.project.exception.CustomException;
import whereQR.project.utils.response.ResponseEntity;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthenticationExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response); // jwtAuthentication 실행
            log.info("JwtAuthenticationExceptionFilter");
        }catch(Exception e){
            log.error("JwtAuthenticationExceptionFilter error");
            if (e instanceof CustomException) {// 정의한 error에 속할 경우
                CustomException customException = (CustomException) e;
                handleAuthenticationException(response, customException.getMessage());
            }else{
                handleAuthenticationException(response, "internal server error"); // 전체 internal로 처리
            }
        }
    }

    private void handleAuthenticationException(HttpServletResponse response,String message ) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        Map<String, Object> responseBodyData = new HashMap<>();
        responseBodyData.put("message", message);
        responseBody.put("status", "FAILED");
        responseBody.put("data", responseBodyData);

        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(responseBody));
        writer.flush();
        writer.close();
    }
}
