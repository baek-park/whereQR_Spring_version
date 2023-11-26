package whereQR.project.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import whereQR.project.exception.CustomException;
import whereQR.project.exception.CustomExceptions.ForbiddenException;
import whereQR.project.exception.ErrorResponse;
import whereQR.project.jwt.JwtAuthenticationExceptionFilter;
import whereQR.project.jwt.JwtAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationExceptionFilter jwtAuthenticationExceptionFilter;

    @Bean
    @Profile("local")
    SecurityFilterChain filterChainLocal(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers("/member/**").permitAll()
                    .antMatchers("/qrcode/create").hasRole("ADMIN")
                    .antMatchers("/qrcode/update/**").hasRole("USER")
                    .antMatchers("/qrcode/register/**").hasRole("USER")
                    .antMatchers("/qrcode/qrcode-list").hasRole("USER")
                    .antMatchers("/qrcode/scan/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedEntryPoint()) // 인증되지 않은 요청에 대한 처리
                    .accessDeniedHandler(accessDeniedHandler()) // 권한이 없는 경우의 처리
                .and()
                .addFilterBefore(jwtAuthenticationFilter, RequestHeaderAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationExceptionFilter, JwtAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    @Profile("dev")
    SecurityFilterChain filterChainDev(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/member/**").permitAll()
                .antMatchers("/qrcode/create").hasRole("ADMIN")
                .antMatchers("/qrcode/update/**").hasRole("USER")
                .antMatchers("/qrcode/register/**").hasRole("USER")
                .antMatchers("/qrcode/qrcode-list").hasRole("USER")
                .antMatchers("/qrcode/scan/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint()) // 인증되지 않은 요청에 대한 처리
                .accessDeniedHandler(accessDeniedHandler()) // 권한이 없는 경우의 처리
                .and()
                .addFilterBefore(jwtAuthenticationFilter, RequestHeaderAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationExceptionFilter, JwtAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {

            CustomException customException = new ForbiddenException("forbidden",this.getClass().toString());
            ErrorResponse errorResponse =
                    new ErrorResponse(customException.getErrorType(), customException.getMessage(), customException.getPath());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", "FAILED");
            responseBody.put("data", errorResponse);

            response.setStatus(200);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
