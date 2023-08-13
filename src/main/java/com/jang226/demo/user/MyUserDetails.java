package com.jang226.demo.user;

import com.jang226.demo.command.UserVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 화면에 전달이 되는데, 화면에서 사용할 값들은 getter 로 생성해야 한다
public class MyUserDetails implements UserDetails {

    private UserVO vo; // userDetail 의 vo
    
    // 생성자
    public MyUserDetails(UserVO vo) {
        this.vo = vo; // 들어온 vo 를 멤버변수 vo에 넣는다
    }

    // 권한 처리 영역
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        //UserVO 가 가지고 있는 권한을 리스트에 담아 반환시키면, 스프링 시큐리티가 참조해서 사용한다
        // Collection 은 list 의 최고 부모
        ArrayList<GrantedAuthority> list = new ArrayList<>();

        // vo의 role (권한)을 리턴하는 GrantedAuthority 객체를 넣는다
//        list.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return vo.getRole();
//            }
//        });

        list.add((GrantedAuthority) () -> vo.getRole());

        return list;
    }

    @Override
    public String getPassword() {
        return vo.getPassword();
    }

    @Override
    public String getUsername() {
        return vo.getUsername();
    }

    // 부가적으로 사용할 기능이 있다면 만들어준다
    // 화면에서 권한도 사용할 수 있게 해주고 싶다면? getter 를 만든다
    public String getRole() {
        return vo.getRole();
    }

    // 계정 만료 판단 true : 만료 X
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 LOCK 판단 : true : 락 X
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 판단 : true : 비밀번호 만료 X
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 사용가능한 계정 판단 : true : 사용 가능
    @Override
    public boolean isEnabled() {
        return true;
    }
}
