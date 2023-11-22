package whereQR.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import whereQR.project.jwt.JwtAuthenticationFilter;
import whereQR.project.jwt.JwtTokenProvider;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

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
//                .antMatchers("/member/signup").permitAll()
//                .antMatchers("/member/login").permitAll()
//                .antMatchers("/member/detail").hasAnyRole("ADMIN","USER")
                .antMatchers("/member/**").permitAll()
                .antMatchers("/qrcode/create").permitAll()
                .antMatchers("/qrcode/update/**").hasRole("USER")
                .antMatchers("/qrcode/register/**").hasRole("USER")
                .antMatchers("/qrcode/qrcode-list").hasRole("USER")
                .antMatchers("/qrcode/scan/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), RequestHeaderAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    @Profile("dev")
    SecurityFilterChain filterChainProd(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/member/signup").permitAll()
                .antMatchers("/member/login").permitAll()
                .antMatchers("/member/logout").permitAll()
                .antMatchers("/member/detail").hasAnyRole("ADMIN","USER")
                .antMatchers("/qrcode/create").hasRole("ADMIN")
                .antMatchers("/qrcode/update/**").hasRole("USER")
                .antMatchers("/qrcode/register/**").hasRole("USER")
                .antMatchers("/qrcode/qrcode-list").hasRole("USER")
                .antMatchers("/qrcode/scan/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
