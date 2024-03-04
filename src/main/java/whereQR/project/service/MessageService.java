package whereQR.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.Chatroom;
import whereQR.project.entity.Member;
import whereQR.project.entity.Message;
import whereQR.project.entity.dto.chat.ResponseMessageDto;
import whereQR.project.exception.CustomExceptions.InternalException;
import whereQR.project.repository.message.MessageRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberService memberService;

    private final SimpMessagingTemplate simpMessagingTemplate;



    /**
     * 채팅
     */
    @Transactional
    public Message sendMessage(Chatroom chatroom, Member sender, String content) throws JsonProcessingException {

        // get receiver
        UUID receiverId = chatroom.getReceiverBySender(sender);
        Member receiver = memberService.getMemberById(receiverId);
        // 1. 메시지 생성
        Message message = new Message(sender, receiver, chatroom, content);
        // 2. 메시지 전송
        try{
            simpMessagingTemplate.convertAndSend("/subscribe/" + chatroom.id, message.toString());
            log.info("convertAndSend success");
        } catch (MessagingException exception){
            throw new InternalException(exception.getFailedMessage().toString(), this.getClass().toString());
        }

        // 3. 저장
        return messageRepository.save(message);
    }

    @Transactional
    public void readMessage(Chatroom chatroom, Member member){

        // 1. 안 읽은 메시지 찾기 -> chatroom이 chatroom이고 receiver가 member인 것을 전부 find
        Optional<List<Message>> notReadMessages =  messageRepository.findNotReadMessageByChatroomAndReceiver(chatroom, member);

        // 2. 메시지 읽기
        if (!notReadMessages.isEmpty()){
            notReadMessages.get().forEach(Message::readMessage);
        }

        // 3. 읽은 메시지 병렬 처리
        try{
            List<CompletableFuture<Void>> futures = notReadMessages.stream()
                    .map(message -> CompletableFuture.runAsync(() -> simpMessagingTemplate.convertAndSend("/message/read/" + chatroom.id, message)))
                    .collect(Collectors.toList());
            CompletableFuture.allOf((CompletableFuture<?>) futures).join();
        } catch (MessagingException exception){
            throw new InternalException(exception.getFailedMessage().toString(), this.getClass().toString());
        }

    }

    public List<ResponseMessageDto> getMessagesByChatroomId(UUID chatroomId){
        return messageRepository.findMessagesByChatroomId(chatroomId).stream().map(Message::toResponseMessageDto).collect(Collectors.toList());
    }

}
