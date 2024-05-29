package whereQR.project.domain.email;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmailAuthDto {
    private String email;
    private String code;
    private LocalDateTime expiresAt;

    public EmailAuthDto(String email, String randomCode, LocalDateTime plusMinutes) {
        this.email = email;
        this.code = randomCode;
        this.expiresAt = plusMinutes;
    }
}
