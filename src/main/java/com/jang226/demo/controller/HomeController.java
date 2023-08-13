package com.jang226.demo.controller;

import com.jang226.demo.command.UserVO;
import com.jang226.demo.user.MyUserDetails;
import com.jang226.demo.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; // 비밀번호 암호화 객체
    
    @Autowired
    private UserMapper userMapper; // mapper 객체

    // 홈
    @GetMapping("/hello")
    public String hello(Authentication auth) {
        
        // 1st - 컨트롤러 매개변수에 Authentication 객체를 선언한다 : 자동 주입
//        if(auth != null){ // 객체가 있을 때
//            // auth 에는 AuthenticationToken 이 들어있다
//            System.out.println("auth = " + auth);
//
//            // 토큰의 principal 을 가지고 온다
//            MyUserDetails details = (MyUserDetails) auth.getPrincipal();
//
//            System.out.println(details.getUsername());
//            System.out.println(details.getPassword());
//            System.out.println(details.getRole());
//        }

        // 2nd - 시큐리티 세션을 직접 사용
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        authentication.getPrincipal(); // MyUserDetail 객체

//        MyUserDetails details = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 같은 타입인지 확인하고 가져온다
        // 인증이 되지 않으면, Abstract 토큰을 가지고 있기 때문에, 확인하고 가져온다
        if(authentication.getPrincipal() instanceof MyUserDetails){ 
            MyUserDetails details = (MyUserDetails) authentication.getPrincipal();
            System.out.println(details);
            System.out.println(details.getUsername());
            System.out.println(details.getPassword());
            System.out.println(details.getRole());
        }

        return "hello";
    }
    
    // 회원가입
    @GetMapping("/join")
    public String join(){ return "join"; }

    // 회원가입 요청
    @PostMapping("/joinForm")
    public String joinForm(UserVO vo) {

        // 비밀번호 암호화
//        String pw = bCryptPasswordEncoder.encode(vo.getPassword());// vo 의 password 를 변환
//        vo.setPassword(pw);
        vo.setPassword(bCryptPasswordEncoder.encode(vo.getPassword()));
        
        // 서비스 영역 생략
        // 회원가입 처리
        userMapper.join(vo);

        return "redirect:/login"; // 가입 이후 로그인 페이지로 이동
    }

    // 커스터마이징한 로그인 페이지
    @GetMapping("/login")
    // 로그인 실패시 들어올 때, err 이라는 변수르 받는다, 반드시 있어야 하는게 아니라서 required = false
    public String login(@RequestParam(value = "err", required = false ) String err,
                        Model model) {
        
        // redirect 로 나가는 게 아니라서 model 로
        if(err != null){
            model.addAttribute("msg", "아이디 비밀번호를 확인하세요");
        }

        return "login";
    }

    //REST API 모두 접근가능한 주소
    @GetMapping("/all")
    public String all() {
        return "all";
    }

    // admin 접근 mypage
    @GetMapping("/admin/mypage")
    public @ResponseBody String adminmypage() {
        return "REST API admin 마이페이지";
    }

    // user 접근 mypage
    @GetMapping("/user/mypage")
    public @ResponseBody String usermypage() {
        return "REST API user 마이페이지";
    }

    // 에러 페이지
    @GetMapping("/deny")
    public @ResponseBody String deny() {
        return "페이지에 접근할 권한이 없습니다";
    }

    // 어노테이션으로 권한 설정
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/test")
    public @ResponseBody String test() {
        return "여기는 preAuthorize로 처리";
    }
}
