package com.SJTB.framework.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// SecurityContext의 유저 정보를 확인하는 클래스
public class SecurityUserUtil {
    // SecurityContext의 유저 정보중 id를 리턴해주는 메소드
    // SecurityContext를 생성할 때 username으로 사용자의 id를 저장해서 사용가능한 메소드임
    public static String getCurrentUser(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }
}