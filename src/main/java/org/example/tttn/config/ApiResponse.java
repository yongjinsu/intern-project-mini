package org.example.tttn.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tttn.constants.MessageCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String code;
    private String message;
    private T data;

    /**
     * Trả về response thành công với dữ liệu
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, MessageCode.SUCCESS.getCode(), MessageCode.SUCCESS.getMessage(), data);
    }

    /**
     * Trả về response thành công với dữ liệu và messageCode tùy chọn
     */
    public static <T> ApiResponse<T> success(MessageCode messageCode, T data) {
        return new ApiResponse<>(200, messageCode.getCode(), messageCode.getMessage(), data);
    }

    /**
     * Trả về response lỗi với messageCode tùy chọn
     */
    public static <T> ApiResponse<T> error(int status, MessageCode messageCode) {
        return new ApiResponse<>(status, messageCode.getCode(), messageCode.getMessage(), null);
    }

    /**
     * Trả về response lỗi với messageCode tùy chọn và message tùy chọn
     */
    public static <T> ApiResponse<T> error(int status, MessageCode messageCode, String customMessage) {
        return new ApiResponse<>(status, messageCode.getCode(), customMessage, null);
    }
}
