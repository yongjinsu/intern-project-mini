package org.example.tttn.constants;

/**
 * Danh sách các mã thông báo lỗi và thành công
 */
public enum MessageCode {
    SUCCESS("MSG_0000", "Operation successful"),
    AUTH_LOGIN_SUCCESS("MSG_2001", "Login successful"),
    AUTH_LOGOUT_SUCCESS("MSG_2002", "Logout successful"),
    AUTH_TOKEN_REFRESHED("MSG_2003", "Token refreshed successfully"),
    AUTH_PASSWORD_RESET_SUCCESS("MSG_2004", "Password reset successfully"),
    AUTH_PASSWORD_RESET_EMAIL_SENT("MSG_2005", "Password reset email sent successfully"),
    AUTH_ALL_DEVICES_LOGGED_OUT("MSG_2006", "All devices logged out successfully"),
    AUTH_INVALID_TOKEN("ERR_2001", "Invalid or expired token"),
    AUTH_TOKEN_REQUIRED("ERR_2002", "Authentication token required"),
    AUTH_LOGIN_FAILED("ERR_2003", "Login failed"),
    AUTH_PASSWORD_INCORRECT("ERR_2004", "Incorrect password"),
    AUTH_PASSWORD_RESET_FAILED("ERR_2005", "Password reset failed"),
    AUTH_EMAIL_SEND_FAILED("ERR_2006", "Failed to send email");

    private final String code;
    private final String message;

    MessageCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
