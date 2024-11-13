package com.em.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private long timestamp;

    // Helper methods to create success and error responses

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", "Request was successful", data, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null, System.currentTimeMillis());
    }
}
