package whereQR.project.entity.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import whereQR.project.entity.Member;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatroomResponseDto {
    private String id;
    private String opponentUsername;

    private Long notReadMessageCount;

    private String lastContent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastDate;

    public ChatroomResponseDto(UUID id, Member opponent, Long notReadMessageCount, LocalDateTime lastDate, String lastContent) {
        this.id = id.toString();
        this.opponentUsername = opponent.getUsername();
        this.notReadMessageCount = notReadMessageCount;
        this.lastDate = lastDate;
        this.lastContent = lastContent;
    }

}
