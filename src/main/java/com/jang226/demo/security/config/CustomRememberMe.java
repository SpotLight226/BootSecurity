package com.jang226.demo.security.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomRememberMe implements AuthenticationSuccessHandler {

    private String redirectPage;

    // 생성자 : redirect 경로를 받아와서 redirectPage 문자열 선언
    public CustomRememberMe(String redirectPage) {
        this.redirectPage = redirectPage;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("나중에 사용자가 다시 브라우저에 들어올 때, 리멤버미가 성공하면 이 후에 실행된다");

        // authentication 를 사용해서 권한별로 처리~~
        
        // 권한 처리 후, 원하는 곳으로 redirect 를 날릴 수 있다
        response.sendRedirect(redirectPage);
    }
}
