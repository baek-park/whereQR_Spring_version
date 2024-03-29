package whereQR.project.entity;

import lombok.AllArgsConstructor;
import whereQR.project.entity.dto.chat.ResponseMessageDto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
public class Message extends EntityBase implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender", nullable = false)
    private Member sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver", nullable = false)
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom", nullable = false)
    private Chatroom chatRoom;

    @Column(length = 3000)
    private String content;

    private boolean isRead = false;

    // method
    public Message(){

    }

    public Message(Member sender, Member receiver, Chatroom chatroom, String content){
        this.sender = sender;
        this.receiver = receiver;
        this.chatRoom = chatroom;
        this.content = content;
    }

    // method
    public void readMessage(){
        this.isRead = true;
    }

    public ResponseMessageDto toResponseMessageDto(){
        return new ResponseMessageDto(
                this.id,
                this.sender.getId().toString(),
                this.receiver.getId().toString(),
                this.content,
                this.isRead,
                this.createdAt
        );
    }

    public boolean isReceiver(Member member){
        if(this.receiver.equals(member)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender.getId() +
                ", receiver=" + receiver.getId() +
                ", content='" + content + '\'' +
                '}';
    }
}
