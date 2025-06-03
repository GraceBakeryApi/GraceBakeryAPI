package cohort46.gracebakeryapi.accounting.security;

import cohort46.gracebakeryapi.accounting.dao.UserRepository;
import cohort46.gracebakeryapi.accounting.model.RoleEnum;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import cohort46.gracebakeryapi.accounting.security.JWT.JwtRequestFilter;
import cohort46.gracebakeryapi.accounting.security.OAuth2.CustomOAuth2UserService;
import cohort46.gracebakeryapi.other.exception.OAuth2EmailNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;
    private final AuthenticationSuccessHandler oauth2SuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, AuthenticationSuccessHandler oauth2SuccessHandler, UserRepository userRepository, CustomOAuth2UserService customOAuth2UserService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.oauth2SuccessHandler = oauth2SuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.cors(AbstractHttpConfigurer::disable)//httpBasic(Customizer.withDefaults())//.httpBasic(AbstractHttpConfigurer::disable) // Отключаем HTTP Basic
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/api/user/oauth"))
                        .successHandler(oauth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService))
                )

                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers(
                        "/api/user/login",
                                "/api/user/reg",
                                "/api/user/oauth",
                                "/oauth2/**",

                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/v3/api-docs/**",
                                "/webjars/**",
                                "/swagger-resources/**"

                                ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/products/isactive/true",
                                "/api/product/*",
                                "/api/products/category/*/isactive/true",
                                "/api/products/price/*/*",
                                "/api/products/filters",
                                "/api/products/category/*",
                                "/api/categories/section/*/isactive/true",
                                "/api/categories/isactive/true",
                                "/api/sections/isactive/true"

                                ).permitAll()

                        .requestMatchers(
                                "/api/users",
                                "/api/user/*",
                                "/api/user/*/*",
                                "/api/products/isactive/true",
                                "/api/products/closeddate"
                        ).hasAnyRole("ADMIN", "ROOT")


                        .requestMatchers(
                                "/api/**"
                        ).authenticated()
                )

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)



                .build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException) throws IOException, jakarta.servlet.ServletException {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Authentication failed: Invalid credentials\"}");
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
