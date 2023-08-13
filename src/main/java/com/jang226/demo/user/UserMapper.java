package com.jang226.demo.user;

import com.jang226.demo.command.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void join(UserVO vo); // 가입

    UserVO login(String username);
}
