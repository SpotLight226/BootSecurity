package com.jang226.demo.user;

import com.jang226.demo.command.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // 서비스 빈으로 선언
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    // loginProcessULR 이 호출될 때, loadUserByUsername 함수를 자동으로 연결
    // password 는 시큐리티가 알아서
    // 화면에서는 기본적으로 username 이라는 파라미터로 지정해야 한다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("username = " + username); // 로그인한 username 
        
        // 로그인 처리 - password 는 시큐리티가 알아서 처리
        UserVO vo = userMapper.login(username);

        System.out.println(vo);
        
        // vo가 null 이 아니라면 회원 정보가 있다
        if(vo != null){
            // 회원 정보가 있다면, MyUserDetails 객체를 생성해 반환
            // 스프링 시큐리티가 이 객체를 받아서 password 를 확인한 후, 정상적인 유저라 판별이 되면, 
            // 시큐리티 세션 ( new 인증객체 ( new MyUserDetails ) ) 형태로 저장시킨다
            // 시큐리티 세션 (new Authentication ( new MyUserDetails ) )
            return new MyUserDetails(vo);
            // 시큐리티 설정파일에 defaultSuccessURL 을 작성
        }

        return null; // 회원 정보가 없다면 null 을 반환받고 끝
    }
}
