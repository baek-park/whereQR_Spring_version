package whereQR.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Chatroom;
import whereQR.project.entity.Member;
import whereQR.project.entity.Message;
import whereQR.project.entity.dto.message.ResponseMessageDto;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.service.ChatroomService;
import whereQR.project.service.MemberService;
import whereQR.project.service.MessageService;
import whereQR.project.utils.MemberUtil;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j

public class MessageController {

    private final MessageService messageService;
    private final ChatroomService chatroomService;
    private final MemberService memberService;

    /**
     * tip) client -> app/send/uuid/uuid (command : publish) / subscribe : message/uuid
     * @param chatRoomId
     * @param content
     * @return
     */
    @MessageMapping("/send/{memberId}/{chatRoomId}")
    public void sendMessage(@DestinationVariable String memberId, @DestinationVariable String chatRoomId, @RequestBody String content) {

        Chatroom chatroom = chatroomService.getChatroomById(UUID.fromString(chatRoomId));
        Member currentMember = memberService.getMemberById(UUID.fromString(memberId));

        if(!chatroom.isChatroomMember(currentMember)){
            throw new BadRequestException("사용자가 올바르지 않습니다.", this.getClass().toString());
        }

        messageService.sendMessage(chatroom, currentMember, content);
    }

    @MessageMapping("/read/{memberId}/{chatroomId}/{messageId}")
    public void readMessage(@DestinationVariable String memberId, @DestinationVariable String chatroomId, @DestinationVariable String messageId) {

        Chatroom chatroom = chatroomService.getChatroomById(UUID.fromString(chatroomId));
        Member currentMember = memberService.getMemberById(UUID.fromString(memberId));
        Message message = messageService.getMessageById(UUID.fromString(messageId));

        if(!chatroom.isChatroomMember(currentMember)){
            throw new BadRequestException("사용자가 올바르지 않습니다.", this.getClass().toString());
        }

        messageService.readMessage(currentMember, chatroom, message);
    }

    @GetMapping("/chat/messages")
    public ResponseEntity getMessagesByChatroomId(@RequestParam  String chatroomId){
        Member currentMember = MemberUtil.getMember();
        Chatroom chatroom = chatroomService.getChatroomById(UUID.fromString(chatroomId));

        if(!chatroom.isChatroomMember(currentMember)){
            throw new BadRequestException("is not contain", this.getClass().toString());
        }

        List<ResponseMessageDto> messages = messageService.getMessagesByChatroomId(UUID.fromString(chatroomId));
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(messages)
                .build();
    }
}
