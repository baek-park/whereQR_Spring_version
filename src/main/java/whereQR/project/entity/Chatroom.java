package whereQR.project.entity;

import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SourceType;
import whereQR.project.entity.dto.chat.ChatroomResponseDto;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@DynamicUpdate // 업데이트하고자 하는 필드만 업데이트하기 위해서 추가
public class Chatroom extends EntityBase{

    @ManyToOne(fetch = FetchType.LAZY) // 단방향
    @JoinColumn(name = "starter", nullable = false)
    private Member starter;
    @ManyToOne(fetch = FetchType.LAZY) // 단방향
    @JoinColumn(name = "participant", nullable = false)
    private Member participant;

    public Chatroom() {

    }

    public Chatroom(Member starter, Member participant){
        this.starter = starter;
        this.participant = participant;
    }

    public UUID getReceiverBySender(Member sender){
        if(this.starter.getId().equals(sender.getId())){
            return this.participant.getId();
        }else{
            return this.starter.getId();
        }
    }

    public Boolean isChatroomMember(Member member){

        if (member.getId().equals(this.starter.getId()) || member.getId().equals(this.participant.getId())) {
            return true;
        }
        return false;
    }

    public ChatroomResponseDto toChatroomResponseDto(UUID memberId){

        if(memberId.equals(this.starter.getId())){
            return new ChatroomResponseDto(this.id,this.participant);
        }else{
            return new ChatroomResponseDto(this.id, this.starter);
        }

    }

}
