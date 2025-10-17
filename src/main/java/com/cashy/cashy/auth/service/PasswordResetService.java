package com.cashy.cashy.auth.service;

import com.cashy.cashy.auth.dto.ForgotPasswordDTO;
import com.cashy.cashy.auth.dto.ResetPasswordDTO;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

    public void forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        // TODO: Implement password reset logic
    }

    public void sendResetToken(String email) {
        // TODO: Implement send reset token logic
    }

    public void resetPassword(String token, String newPassword) {
        // TODO: Implement password reset logic
    }

    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        // TODO: Implement password reset logic
    }

    public String refreshJwtToken(String token) {
        // TODO: Implement JWT refresh logic
        return "new-token";
    }

    public void logout(String token) {
        // TODO: Implement logout logic
    }
}
