package whereQR.project.domain.chatroom.dto;

import lombok.Data;
import whereQR.project.domain.member.Member;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatroomProjectionDto {

    private UUID id;
    private Member starter;

    private Member participant;
    private Long notReadMessageCount;

    private LocalDateTime lastDate;
    private String lastContent;

    public ChatroomProjectionDto(UUID id, Member starter, Member participant, Long notReadMessageCount, LocalDateTime lastDate, String lastContent){
        this.id = id;
        this.starter = starter;
        this.participant = participant;
        this.notReadMessageCount = notReadMessageCount;
        this.lastDate = lastDate;
        this.lastContent = lastContent;
    }

    public ChatroomResponseDto toChatroomResponseDto(UUID memberId){

        if(memberId.equals(this.starter.getId())){
            return new ChatroomResponseDto(this.id,this.participant, this.notReadMessageCount, this.lastDate.plusHours(9), this.lastContent);
        }else{
            return new ChatroomResponseDto(this.id, this.starter, this.notReadMessageCount, this.lastDate.plusHours(9), this.lastContent);
        }

    }


}
