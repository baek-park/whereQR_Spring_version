package whereQR.project.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import whereQR.project.exception.CustomException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static whereQR.project.jwt.CustomHandleAuthenticationException.handleAuthenticationException;

@Component
@Slf4j
public class JwtAuthenticationExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response); // jwtAuthentication 실행
        }catch(Exception e){
            log.error("{}",e);
            if (e instanceof CustomException) {// 정의한 error에 속할 경우
                CustomException customException = (CustomException) e;
                handleAuthenticationException(response, customException.getMessage());
            }else{
                handleAuthenticationException(response, "internal server error"); // 전체 internal로 처리
            }
        }
    }

}
