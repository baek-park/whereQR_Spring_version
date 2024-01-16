package whereQR.project.entity;

import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

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

    public Member getReceiverBySender(Member sender){
        if(this.starter.equals(sender)){
            return this.participant;
        }else{
            return this.starter;
        }
    }

    public Boolean isChatroomMember(Member member){
        if (member.equals(starter) || member.equals(participant)) {
            return true;
        }
        return false;
    }

}
