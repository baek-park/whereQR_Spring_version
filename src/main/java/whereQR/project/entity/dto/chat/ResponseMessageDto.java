package whereQR.project.entity.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import whereQR.project.entity.Member;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ResponseMessageDto {

    private UUID id;
    private String senderId;
    private String receiverId;
    private String content;

    private Boolean isRead;

    private LocalDateTime createdAt;

    public ResponseMessageDto(){
    }

}
