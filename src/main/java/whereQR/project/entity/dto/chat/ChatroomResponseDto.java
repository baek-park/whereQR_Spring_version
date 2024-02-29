package whereQR.project.entity.dto.chat;

import lombok.Data;
import whereQR.project.entity.Member;

import java.util.UUID;

@Data
public class ChatroomResponseDto {
    private String id;
    private String participantUsername;

    public ChatroomResponseDto(UUID id, Member participant) {
        this.id = id.toString();
        this.participantUsername = participant.getUsername();
    }

}
