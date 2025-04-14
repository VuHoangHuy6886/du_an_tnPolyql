package com.poliqlo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String field = ex.getName();
        String message = String.format("Tham số không hợp lệ", field);

        return ResponseEntity
                .badRequest()
                .body(Map.of("error", message));
    }

    // Xử lý lỗi truyền thiếu @RequestParam hoặc @PathVariable
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParam(MissingServletRequestParameterException ex) {
        String message = String.format("Thiếu tham số bắt buộc: %s", ex.getParameterName());
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", message));
    }

    // Xử lý lỗi validation từ @Valid + BindingResult
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", message));
    }

    // Xử lý Exception chung chung (nên để cuối)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        // Ghi log đầy đủ nhưng trả về message ngắn gọn
        ex.printStackTrace(); // hoặc log.error(...)
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Đã xảy ra lỗi không mong muốn."));
    }
}
