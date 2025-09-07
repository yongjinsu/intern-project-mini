package org.example.tttn.service.provider;

import lombok.RequiredArgsConstructor;
import org.example.tttn.service.interfaces.IAIService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIServiceImplement implements IAIService {

    private final ChatClient chatClient;

    @Override
    public String generatePasswordSuggestions(String userId) {
        String prompt = "Tạo 1 mật khẩu mạnh nhất có 12-16 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt. Chỉ trả về mật khẩu, không giải thích gì thêm.";

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content()
                .trim();
    }
}