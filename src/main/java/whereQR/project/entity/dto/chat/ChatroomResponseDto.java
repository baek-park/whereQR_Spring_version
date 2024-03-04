package whereQR.project.entity.dto.chat;

import lombok.Data;
import whereQR.project.entity.Member;

import java.util.UUID;

@Data
public class ChatroomResponseDto {
    private String id;
    private String opponentUsername;

    private Long notReadMessageCount;
    

    public ChatroomResponseDto(UUID id, Member opponent, Long notReadMessageCount) {
        this.id = id.toString();
        this.opponentUsername = opponent.getUsername();
        this.notReadMessageCount = notReadMessageCount;
    }

}
