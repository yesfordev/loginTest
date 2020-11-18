package me.yes.practice.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by yes on 2020/11/17
 */
@Data
public class SignUpReq {
    private String name;
    private String password;
    private String part;

    //프로필 사진 객체
    private MultipartFile profile;

    //프로필 사진 저장 url 주소
    private String profileUrl;
}
