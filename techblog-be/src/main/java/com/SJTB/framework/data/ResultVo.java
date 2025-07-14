package com.SJTB.framework.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultVo<T> {
    private boolean isError;
    private boolean isSessionError;
    private boolean isWarning;
    private String errorMsg;
    private String warningMsg;
    private T content;

    // 생성자 (DTO 클래스 타입만 지정하여 사용)
    public ResultVo(Class<T> type) {
        // 필요에 따라 타입을 사용하여 초기화할 수 있음
    }

    // 기본 생성자
    public ResultVo() {
        // 기본 생성자는 필드를 초기화하지 않음
    }

    // Getter, Setter 메서드 정의
    // 롬복 어노테이션에 isError 등이 예약어라서 get, set 메서드 따로 생성
    public boolean getIsError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public boolean getIsSessionError() {
        return isSessionError;
    }

    public void setIsSessionError(boolean isSessionError) {
        this.isSessionError = isSessionError;
    }

    public boolean getIsWarning() {
        return isWarning;
    }

    public void setIsWarning(boolean isWarning) {
        this.isWarning = isWarning;
    }
}