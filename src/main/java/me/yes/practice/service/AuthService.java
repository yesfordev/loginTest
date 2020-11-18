package me.yes.practice.service;

import me.yes.practice.dto.User;
import me.yes.practice.mapper.UserMapper;
import me.yes.practice.model.DefaultRes;
import me.yes.practice.model.LoginReq;
import me.yes.practice.utils.ResponseMessage;
import me.yes.practice.utils.StatusCode;
import org.springframework.stereotype.Service;

/**
 * Created by yes on 2020/11/18
 */
@Service
public class AuthService {

    private final UserMapper userMapper;

    private final JwtService jwtService;

    public AuthService(final UserMapper userMapper, JwtService jwtService) {
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    /**
     * 로그인 서비스
     * @param loginReq
     * @return
     */
    public DefaultRes<JwtService.TokenRes> login(final LoginReq loginReq) {
        final User user = userMapper.findByNameAndPassword(loginReq.getName(), loginReq.getPassword());
        if(user != null) {
            //토큰 생성
            final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(user.getUserIdx()));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenDto);
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
    }
}
