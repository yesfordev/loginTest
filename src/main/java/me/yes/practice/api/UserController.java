package me.yes.practice.api;

import lombok.extern.slf4j.Slf4j;
import me.yes.practice.dto.User;
import me.yes.practice.model.SignUpReq;
import me.yes.practice.service.UserService;
import me.yes.practice.utils.auth.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static me.yes.practice.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created by yes on 2020/11/17
 */
@Slf4j
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity getUser(@RequestParam("name") final Optional<String> name) {
        try {
            //name이 null일 경우 false, null이 아닐 경우 true
            if(name.isPresent()) return new ResponseEntity<>(userService.findByName(name.get()), HttpStatus.OK);
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity signup(SignUpReq signUpReq, @RequestPart(value = "profile", required = false) final MultipartFile profile) {
        try {
            if(profile != null) signUpReq.setProfile(profile);
            return new ResponseEntity<>(userService.save(signUpReq), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 정보 수정 API
     * 인증 필요
     * @param userIdx 회원 고유 번호
     * @param signUpReq 수정할 회원 정보 객체
     * @return ResponseEntity
     */
    @Auth
    @PutMapping("/{userIdx}")
    public ResponseEntity signUp(
            @PathVariable(value = "userIdx") final int userIdx,
            SignUpReq signUpReq,
            @RequestPart(value = "profile", required = false) final MultipartFile profile) {
        try {
            if(profile != null) signUpReq.setProfile(profile);
            return new ResponseEntity<>(userService.update(userIdx, signUpReq), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 정보 삭제 API
     * 인증 필요
     * @param userIdx 회원 고유 번호
     * @return ResponseEntity
     */

    @Auth
    @DeleteMapping("/{userIdx}")
    public ResponseEntity deleteUser(@PathVariable(value = "userIdx") final int userIdx) {
        try {
            return new ResponseEntity<>(userService.deleteByUserIdx(userIdx), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}