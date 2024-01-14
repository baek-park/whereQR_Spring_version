package whereQR.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import whereQR.project.entity.Chatroom;
import whereQR.project.entity.Member;
import whereQR.project.entity.Message;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.service.ChatService;
import whereQR.project.service.MemberService;
import whereQR.project.utils.MemberUtil;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MemberService memberService;

    //chatting room
    @PostMapping("chat/create/room")
    public ResponseEntity createChatroom(@RequestBody UUID starter, @RequestBody UUID participant){
        Member user1 = memberService.getMemberById(starter);
        Member user2 = memberService.getMemberById(participant);

        Chatroom chatroom = chatService.createChatroom(user1, user2);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(chatroom.id)
                .build();
    }


    @MessageMapping("pub/{chatRoomId}")
    @SendTo("sub/{chatRoomId}") // 구독하는 client에게 전송
    public ResponseEntity sendMessage(@DestinationVariable String chatRoomId, @Payload String content) {

        Chatroom chatroom = chatService.getChatroomById(UUID.fromString(chatRoomId));
        Member currentMember = MemberUtil.getMember();

        if(!chatroom.isChatroomMember(currentMember)){
            throw new BadRequestException("사용자가 올바르지 않습니다.", this.getClass().toString());
        }

        chatService.sendMessage(chatroom, currentMember, content);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data("send success") // Todo : 변경
                .build();
    }

    @MessageMapping("pub/read/{chatRoomId}")
    @SendTo("sub/read/{chatRoomId}") // 구독하는 client에게 전송
    public ResponseEntity readMessage(@DestinationVariable UUID chatRoomId, @Payload UUID messageId) {

        Chatroom chatroom = chatService.getChatroomById(chatRoomId);
        Message message = chatService.getMessageById(chatRoomId);
        Member currentMember = MemberUtil.getMember();

        if(message.isReceiver(currentMember)){
            throw new BadRequestException("사용자가 올바르지 않습니다.", this.getClass().toString());
        }

        // read message
        chatService.readMessage(chatroom, message);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data("read success") //  Todo : 변경
                .build();
    }


    // Todo : 읽지 않은 메시지 수
    /**
     * getNotReadCount
     */

}
