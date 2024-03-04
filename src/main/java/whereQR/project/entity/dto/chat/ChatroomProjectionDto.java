package whereQR.project.entity.dto.chat;

import lombok.Data;
import whereQR.project.entity.Member;

import java.util.UUID;

@Data
public class ChatroomProjectionDto {
    private UUID id;
    private Member starter;

    private Member participant;
    private Long notReadMessageCount;

    public ChatroomProjectionDto(UUID id, Member starter, Member participant, Long notReadMessageCount){
        this.id = id;
        this.starter = starter;
        this.participant = participant;
        this.notReadMessageCount = notReadMessageCount;
    }

    public ChatroomResponseDto toChatroomResponseDto(UUID memberId){

        if(memberId.equals(this.starter.getId())){
            return new ChatroomResponseDto(this.id,this.participant, this.notReadMessageCount);
        }else{
            return new ChatroomResponseDto(this.id, this.starter, this.notReadMessageCount);
        }

    }


}
