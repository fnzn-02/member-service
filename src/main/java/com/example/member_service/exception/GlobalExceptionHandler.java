package com.example.member_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
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

        // 발생한 에러들 [필드명 : 에러메시지] 형태로 담기
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorResponse.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // 400 상태 코드와 함께 상자 반환
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Service에서 IllegalArgumentException 터지면 내가 낚아챔
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e){
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}