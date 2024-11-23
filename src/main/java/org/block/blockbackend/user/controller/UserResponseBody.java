package org.block.blockbackend.user.controller;

import java.util.HashMap;
import java.util.Map;

public class UserResponseBody {

    public static Map<String, Object> signupSuccess(){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "회원가입 성공");
        return response;
    }

    public static Map<String, Object> signupError(){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "가입 정보가 올바르지 않습니다.");
        return response;
    }

    public static Map<String, Object> removeSuccess(){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "회원 탈퇴되었습니다.");
        return response;
    }

    public static Map<String, Object> removeError(){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "회원 탈퇴에 실패했습니다.");
        return response;
    }

    public static Map<String, Object> loginError(){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "로그인 정보가 올바르지 않습니다.");
        return response;
    }

    public static Map<String, Object> loginSuccess(){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "로그인 성공");
        return response;
    }
    
    public static Map<String, Object> avatarNotFoundError(){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "아바타를 찾지 못했습니다");
        return response;
    }

}
