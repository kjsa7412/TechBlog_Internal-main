export function isValidEmail(email: string) {
    // 영어와 숫자만을 허용하며 2단계 도메인까지 처리하는 이메일 패턴 정의
    const emailPattern = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)+$/;
    return emailPattern.test(email);
}

export function isValidPassword(password: string) {
    // 영어, 숫자, 특수문자를 포함하는 비밀번호 패턴 정의
    const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>]).+$/;
    return passwordPattern.test(password);
}