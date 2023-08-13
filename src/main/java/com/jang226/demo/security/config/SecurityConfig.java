package com.jang226.demo.security.config;

import com.jang226.demo.user.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 설정파일
@EnableWebSecurity // 설정파일을 시큐리티 필터에 자동 추가
@EnableGlobalMethodSecurity(prePostEnabled = true) // 어노테이션으로 권한을 지정할 수 있게 한다
public class SecurityConfig {



    // 나를 기억해에서 사용할 UserDetailService
    @Autowired
    private MyUserDetailService myUserDetailService;

    // 비밀번호 암호화 객체
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 설정 빈
    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        // HttpSecurity : 시큐리티를 설정해주는 객체

        // 기본적으로 Builder 패턴이다
        // csrf 토큰 x
        http.csrf().disable();

        // 권한 설정
        // 모든 요청에 대해 권한에 상관없이 허가한다
//        http.authorizeRequests(authorize -> authorize.anyRequest().permitAll());

        // 모든 페이지에 대해서 거부한다
//        http.authorizeRequests(authorize -> authorize.anyRequest().denyAll());

        // 모든 페이지에 대해서 권한이 있는 사용자만 접근 가능
//        http.authorizeRequests(authorize -> authorize.anyRequest().authenticated());

        // user 페이지에 대해서 인증 필요
//        http.authorizeRequests(authorize -> authorize
//                                           .antMatchers("/user/**")
//                                           .authenticated());

        // 권한 설정
        // user 페이지에 대해서 USER 권한이 필요 => 로그인을 통한 인증과 권한(Role) 은 다르다
        // 로그인을 했을 때, 성공 여부와 관계없이 진입 시도한 페이지로 보내준다 ( 기본 )
//        http.authorizeRequests(authorize -> authorize.antMatchers("/user/**").hasRole("USER"));

        // 여러 권한 설정
        // user 페이지는 USER 권한이 필요, admin 페이지는 ADMIN 권한이 필요하다
//        http.authorizeRequests(authorize -> authorize.antMatchers("/user/**").hasRole("USER")
//                                                     .antMatchers("/admin/**").hasRole("ADMIN"));


        // all 페이지는 인증된 사용자만 접근 가능 ( 인증만 되면 됨 )
        // user 모든 페이지는 USER 권한이 있는 사용자만 접근 가능 ( 권한 필요 )
        // admin 모든 페이지는 ADMIN 권한이 있는 사용자만 접근 가능 ( 권한 필요 )
        // 나머지 모든 페이지는 접근이 가능
//        http.authorizeRequests(authorize -> authorize.antMatchers("/all").authenticated()
//                                                     .antMatchers("/user/**").hasRole("USER")
//                                                     .antMatchers(("/admin/**")).hasRole("ADMIN")
//                                                     .anyRequest().permitAll());


        // 권한 앞에는 ROLE_ 가 자동으로 들어간다
        http.authorizeRequests(authorize -> authorize
                //  all 페이지는 인증만 되면 됨
                .antMatchers("/all").authenticated()
                // USER, ADMIN, TESTER 중 어느 권한이든 1개 가지고 있다면 접근 가능
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN", "TESTER")
                // admin 모든 페이지는 ADMIN 권한이 있는 사용자만 접근 가능 ( 권한 필요 )
                .antMatchers(("/admin/**")).hasRole("ADMIN")
                // 나머지 모든 페이지는 접근이 가능
                .anyRequest().permitAll());


        // 시큐리티 설정이 들어가면 기본 페이지 설정이 해제된다
        // 시큐리티가 사용하는 기본 로그인 페이지를 사용한다
        // 권한 or 인증이 되지 않으면 기본으로 선언된 로그인 페이지를 보여주게 된다
//        http.formLogin(Customizer.withDefaults()); // 기본 로그인페이지 사용

        // 로그인
        // 사용자가 제공하는 form 기반 로그인 기능 페이지를 사용한다
        http.formLogin() // 로그인 설정
                .loginPage("/login") // 로그인 화면
                .loginProcessingUrl("/loginForm") // 로그인시도 요청 경로 -> 스프링이 로그인 시도를 낚아채서 UserDetailService객체로 연결
//            .usernameParameter("id"); // 로그인 input 태그의 name 을 변경할 수 있다
                .defaultSuccessUrl("/all") // 로그인 성공시 페이지
                .failureUrl("/login?err=true") // 로그인 실패시 이동할 url -> 다시 login 페이지로 이동, 매개변수 err=true
                // 여기까지 로그인
                .and() // 다시 처음부터 http 객체를 설정할 수 있다
                .exceptionHandling().accessDeniedPage("/deny") // 권한이 없을 때 이동할 리다이렉트 경로, 모든 예외 발생이 이동된다
                // 여기까지 예외발생시 이동 URl
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/hello"); // default 로그아웃 경로 /logout,
                // /logout 주소를 직접 작성할 수 있고 로그아웃시 이동 경로를 설정할 수 있다
        
        // 나를 기억해 rememberMe
        http.rememberMe()
                .key("jang226") // 토큰(쿠키)를 만들 비밀키 (필수)
                .rememberMeParameter("remember-me") // 화면에서 전달받는 check box 의 name 명 (필수)
                .tokenValiditySeconds(60) // 쿠키(토큰)의 생명주기 ( 유효시간 ) (필수)
                .userDetailsService(myUserDetailService) // 토큰이 있을 때 실행시킬 userDetailService 객체 (필수)
                .authenticationSuccessHandler(customRememberMe()); // remember me 가 동작할 때, 실행할 핸들러 객체를 넣는다

        return http.build();
    }
    
    // 핸들러 객체는 이렇게 생성한다
    // customRememberMe
    @Bean
    public CustomRememberMe customRememberMe() {
        CustomRememberMe me = new CustomRememberMe("/all"); // 리멤버미 성공시 실행시킬 리다이렉트 주소
        return me; // 빈으로 등록
    }
}
