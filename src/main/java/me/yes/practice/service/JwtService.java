package me.yes.practice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.auth0.jwt.JWT.require;

/**
 * Created by yes on 2020/11/18
 */
@Slf4j
@Service
public class JwtService {

    @Value("${JWT.ISSUER}")
    private String ISSUER;

    @Value("${JWT.SECRET}")
    private String SECRET;

    /**
     * 토큰 생성
     *
     * @param user_idx 토큰에 담길 로그인한 사용자의 회원 고유 IDX
     * @return 토큰
     */
    public String create(final int user_idx) {
        try {
            //토큰 생성 빌더 객체 생성
            JWTCreator.Builder b = JWT.create();
            b.withIssuer(ISSUER);
            b.withClaim("user_idx", user_idx);
            return b.sign(Algorithm.HMAC256(SECRET))
        } catch (JWTCreationException JwtCreationExcetion) {
            log.info(JwtCreationExcetion.getMessage());
        }
        return null;
    }

    /**
     * 토큰 해독
     *
     * @param token 토큰
     * @return 로그인한 사용자의 회원 고유 IDX
     */
    public Token decode(final String token) {
        try {
            //토큰 해독 객체 생성
            final JWTVerifier jwtVerifier = require(Algorithm.HMAC256(SECRET)).withIssuer(ISSUER).build();
            //토큰 검증
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            //토큰 payload 반환, 정상적인 토큰이라면 토큰 주인(사용자) 고유 ID, 아니라면 -1
            return new Token(decodedJWT.getClaim("user_idx").asLong().intValue());
        } catch (JWTVerificationException jve) {
            log.error(jve.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new Token();
    }

    public static class Token {
        //토큰에 담길 정보 필드
        //초기값을 -1로 설정함으로써 로그인 실패시 -1 반환
        private int user_idx = -1;

        public Token() {
        }

        public Token(final int user_idx) {
            this.user_idx = user_idx;
        }

        public int getUser_idx() {
            return user_idx;
        }
    }

    //반환될 토큰Res
    public static class TokenRes {
        //실제 토큰
        private String token;

        public TokenRes() {
        }

        public TokenRes(final String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public String setToken(String token) {
            this.token = token;
        }
    }
}
