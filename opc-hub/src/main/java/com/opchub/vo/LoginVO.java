package com.opchub.vo;

import lombok.Data;

@Data
public class LoginVO {
    private Long userId;
    private String username;
    private String nickname;
    private String token;
    private String role;
}