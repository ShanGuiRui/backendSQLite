package com.xinrui.manager.s2c;

import lombok.Data;

@Data
public class S2cLogin {
    private String token;
    private String username;
    private String role;
}