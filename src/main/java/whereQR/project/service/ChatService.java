package whereQR.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.Chatroom;
import whereQR.project.entity.Member;
import whereQR.project.entity.Message;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.exception.CustomExceptions.InternalException;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import whereQR.project.repository.chatroom.ChatroomRepository;
import whereQR.project.repository.member.MemberRepository;
import whereQR.project.repository.message.MessageRepository;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 채팅방 :
     * 채팅방 이름 X. -> update X
     * 삭제 (아직 X.) -> delete X
     */
    public Chatroom getChatroomById(UUID id){
        return chatroomRepository.findById(id).orElseThrow(() -> new NotFoundException("해당하는 채팅방이 존재하지 않습니다.", this.getClass().toString()));
    }

    public Message getMessageById(UUID id){
        return messageRepository.findById(id).orElseThrow(() -> new NotFoundException("해당하는 메시지가 존재하지 않습니다.", this.getClass().toString()));
    }

    @Transactional
    public Chatroom createChatroom(Member starter, Member participant){
        if(chatroomRepository.existChatroomByUsers(starter, participant)) {
            throw new BadRequestException("이미 진행 중인 채팅방이 존재합니다.", this.getClass().toString());
        }
        Chatroom chatroom = new Chatroom(starter, participant);
        return chatroomRepository.save(chatroom);
    }

    /**
     * 채팅
     */
    @Transactional
    public void sendMessage(Chatroom chatroom, Member sender, String content){

        // get receiver
        Member receiver = chatroom.getReceiverBySender(sender);
        // 1. 메시지 생성
        Message message = new Message(sender, receiver, chatroom, content);

        // 2. 메시지 저장
        messageRepository.save(message);

        // 3. 메시지 전송
        try{
            simpMessagingTemplate.convertAndSend("/sub/" + chatroom.id, message);
        } catch (MessagingException exception){
            throw new InternalException(exception.getFailedMessage().toString(), this.getClass().toString());
        }
    }

    @Transactional
    public void readMessage(Chatroom chatroom, Message message){

        // 1. 메시지 읽기
        message.readMessage();

        // 2. 메시지 전송
        try{
            simpMessagingTemplate.convertAndSend("/sub/read/" + chatroom.id, message);
        } catch (MessagingException exception){
            throw new InternalException(exception.getFailedMessage().toString(), this.getClass().toString());
        }

    }


}
