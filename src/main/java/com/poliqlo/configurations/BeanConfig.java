package com.poliqlo.configurations;

import com.poliqlo.repositories.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {
    private final TaiKhoanRepository taiKhoanRepository;



    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

                return taiKhoanRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
            }
        };
    }
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
