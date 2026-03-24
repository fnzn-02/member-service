package com.example.member_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 에러 컨트롤 타워
public class GlobalExceptionHandler {

    // @Valid 검사에 걸렸을 때 (회원가입 양식 틀렸을 때) 낚아챔
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e){
        Map<String, String> errorResponse = new HashMap<>();

        // 에러 보따리에서 제일 첫 번째 에러 메시지만 출력
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        // 프론트엔드가 쓰기 좋게 JSON 상자에 담기
        errorResponse.put("errorMessage", errorMessage);

        // 400 상태 코드와 함께 상자 반환
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Service에서 IllegalArgumentException 터지면 내가 낚아챔
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e){

        // 프론트엔드한테 줄 빈 JSON 상자(Map) 준비
        Map<String, String> errorResponse = new HashMap<>();

        // 상자 안에 "errorMessage"라는 이름표로, 비밀번호 일치하지 않는다는 메시지를 담음
        errorResponse.put("errorMessage", e.getMessage());

        // 500 에러 말고, 400 상태 코드와 함께 상자를 반환
        return  ResponseEntity.badRequest().body(errorResponse);
    }
}

