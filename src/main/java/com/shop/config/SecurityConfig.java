package com.shop.config;

import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                    .loginPage("/members/login")
                    .defaultSuccessUrl("/")
                    .usernameParameter("email")     // 디폴트로 username 이름의 파라미터를 전달받는다.
                    .failureUrl("/members/login/error")
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                    .logoutSuccessUrl("/")
                .and()
                    .authorizeRequests()
                    .mvcMatchers("/", "/members/**", "/item/**", "/images/**")
                    .permitAll()
                    .mvcMatchers("/admin/**").hasAnyRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 로그인 처리를 스프링이 username, password(암호화는 빈으로 등록한 Encoder를 통해한다.) 를 가로채서 대신 진행해준다.
    // 그러기 위해서 UserDetailsService 를 구현하고 있는 객체를 알려주고 빈으로 등록한 encoder를 알려줘야함
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((UserDetailsService) memberService).passwordEncoder(passwordEncoder());
    }
}
