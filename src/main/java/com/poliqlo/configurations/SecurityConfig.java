package com.poliqlo.configurations;

import com.poliqlo.filters.JwtAuthenticationFilter;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.TaiKhoan;
import com.poliqlo.repositories.KhachHangRepository;
import com.poliqlo.repositories.TaiKhoanRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    private static final String[] unAuthURL = { "/sign-in/**", "/sign-in", "/sign-up", "/error", "/logout", "/vendor/**", "/js/**", "/css/**", "/fonts/**", "/iphone/**", "/img/**", "/api/v1/admin/data-list-add-san-pham/**", "/api/v2/san-pham/**", "/api/v2/**", "/iphone/**", "/client/**", "/img/**", "/api/v2/san-pham/**", "/unauth-home", "/verify-account", "/reset-otp"
    };
    private static final String[] customerURLs = {
            "/cart/**"
    };
    private static final String[] employeeURLs = {
            "/admin/**"
    };
    private static final String[] adminOnlyUrls = {
            "/admin/nhan-vien/**"
    };

    private final TaiKhoanRepository taiKhoanRepository;



    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        //Functional interface
        return (request, response, authException) -> {
            if (request.getRequestURI().contains("api")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendRedirect("/sign-in?error=401&successUrl=" + request.getRequestURL().toString());
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, KhachHangRepository khachHangRepository) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.) // Giữ nguyên Stateless cho JWT
//                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint())
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(unAuthURL).permitAll()
                        .requestMatchers(customerURLs).hasRole("USER")
                        .requestMatchers(employeeURLs).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(adminOnlyUrls).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            if (request.getRequestURI().contains("google")) {
                                TaiKhoan taiKhoan = taiKhoanRepository
                                        .findByEmail(((OAuth2User) authentication.getPrincipal()).getAttribute("email"))
                                        .or(() -> taiKhoanRepository.findByGoogleId(authentication.getName()))
                                        .orElseGet(() -> {
                                            TaiKhoan newTaiKhoan = new TaiKhoan();
                                            var oauthAccount=(OAuth2User)authentication.getPrincipal();
                                            var khachHang=new KhachHang();
                                            khachHang.setTaiKhoan(newTaiKhoan);
                                            khachHang.setTen(oauthAccount.getAttribute("name"));
                                            newTaiKhoan.setEmail(oauthAccount.getAttribute("email"));
                                            newTaiKhoan.setGoogleId(oauthAccount.getAttribute("sub"));
                                            newTaiKhoan.setRole("ROLE_USER");
                                            newTaiKhoan.setAnhUrl(oauthAccount.getAttribute("picture"));
                                            newTaiKhoan.setIsDeleted(false);
                                            newTaiKhoan.setIsEnable(true);
                                            newTaiKhoan.setUserName(oauthAccount.getAttribute("email"));
                                            newTaiKhoan.setKhachHang(khachHang);
                                            return taiKhoanRepository.save(newTaiKhoan);
                                        });

                                if (taiKhoan != null) {
                                    var authorities = Collections.singletonList(new SimpleGrantedAuthority(taiKhoan.getRole()));
                                    Authentication auth = new UsernamePasswordAuthenticationToken(taiKhoan, null, authorities);
                                    SecurityContextHolder.getContext().setAuthentication(auth);
                                } else {
                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                                }
                            }
                            if (request.getRequestURI().contains("facebook")) {
                                TaiKhoan taiKhoan = taiKhoanRepository
                                        .findByEmail(((OAuth2User)authentication.getPrincipal()).getAttribute("email"))
                                        .orElse(taiKhoanRepository.findByFacebookId(authentication.getName())
                                                .orElseGet(() -> {
                                                    TaiKhoan newTaiKhoan = new TaiKhoan();
                                                    var oauthAccount=(OAuth2User)authentication.getPrincipal();
                                                    var khachHang=new KhachHang();
                                                    khachHang.setTaiKhoan(newTaiKhoan);
                                                    khachHang.setTen(oauthAccount.getAttribute("name"));
                                                    newTaiKhoan.setEmail(oauthAccount.getAttribute("email"));
                                                    newTaiKhoan.setFacebookId(oauthAccount.getAttribute("id"));
                                                    newTaiKhoan.setRole("ROLE_USER");
                                                    newTaiKhoan.setAnhUrl(oauthAccount.getAttribute("picture"));
                                                    newTaiKhoan.setIsDeleted(false);
                                                    newTaiKhoan.setIsEnable(true);
                                                    newTaiKhoan.setUserName(oauthAccount.getAttribute("email"));
                                                    newTaiKhoan.setKhachHang(khachHang);
                                                    return taiKhoanRepository.save(newTaiKhoan);
                                                })

                                        );
                                if (taiKhoan != null) {
                                    var authorities = Collections.singletonList(new SimpleGrantedAuthority(taiKhoan.getRole()));
                                    Authentication auth = new UsernamePasswordAuthenticationToken(taiKhoan, null, authorities);
                                    SecurityContextHolder.getContext().setAuthentication(auth);
                                } else {
                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                                }
                            }
                            response.sendRedirect("/home");
                        })

                )

                .formLogin(form -> form
                        .loginPage("/login")   // Trang đăng nhập tùy chỉnh
                        .loginProcessingUrl("/do-login") // URL xử lý đăng nhập
                        .usernameParameter("email") // Thay đổi nếu cần
                        .passwordParameter("password") // Thay đổi nếu cần
                        .defaultSuccessUrl("/home", true) // URL sau khi đăng nhập thành công
                        .failureUrl("/login?error=true") // URL khi đăng nhập thất bại
                        .permitAll()

                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/sign-in")
                        .invalidateHttpSession(true)
                        .deleteCookies("Authorization")
                );
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT vẫn hoạt động song song

        return http.build();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Cho phép CORS cho tất cả các đường dẫn
                        .allowedOrigins("http://localhost:8080",
                                "http://127.0.0.1:5500")// Thay bằng nguồn gốc của bạn
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*") // Cho phép tất cả các header
                        .allowCredentials(true); // Cho phép gửi cookie
            }
        };
    }

}
