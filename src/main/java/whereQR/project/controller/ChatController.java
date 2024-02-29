package whereQR.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Chatroom;
import whereQR.project.entity.Member;
import whereQR.project.entity.Message;
import whereQR.project.entity.dto.chat.ChatroomMemberDto;
import whereQR.project.entity.dto.chat.ChatroomResponseDto;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.service.ChatService;
import whereQR.project.service.MemberService;
import whereQR.project.utils.MemberUtil;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MemberService memberService;

    //chatting room
    @PostMapping("chat/create/room")
    public ResponseEntity createChatroom(@RequestBody ChatroomMemberDto chatroomMemberDto){
        Member starter = memberService.getMemberById(UUID.fromString(chatroomMemberDto.getStarter()));
        Member participant = memberService.getMemberById(UUID.fromString(chatroomMemberDto.getParticipant()));

        Chatroom chatroom = chatService.createChatroom(starter, participant);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(chatroom.id)
                .build();
    }

    /**
     * tip) client -> app/send/uuid (command : publish) / subscribe : message/uuid
     * @param chatRoomId
     * @param content
     * @return
     */
    @MessageMapping("/send/{chatRoomId}")
    public ResponseEntity sendMessage(@DestinationVariable String chatRoomId, String content) {

        Chatroom chatroom = chatService.getChatroomById(UUID.fromString(chatRoomId));
        Member currentMember = MemberUtil.getMember();

        if(!chatroom.isChatroomMember(currentMember)){
            throw new BadRequestException("사용자가 올바르지 않습니다.", this.getClass().toString());
        }

        Message message = chatService.sendMessage(chatroom, currentMember, content);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(message)
                .build();
    }

    /**
     * 수정 중
     * tip) client -> app/read/uuid (command: subscribe) / subscribe : message/read/uuid
     * @param chatRoomId
     * @return
     */
    @MessageMapping("/read/{chatRoomId}")
    public ResponseEntity readMessage(@DestinationVariable UUID chatRoomId) {

        Chatroom chatroom = chatService.getChatroomById(chatRoomId);

        Member currentMember = MemberUtil.getMember();
        if(!chatroom.isChatroomMember(currentMember)){
            throw new BadRequestException("사용자가 올바르지 않습니다.", this.getClass().toString());
        }

        // read message
        chatService.readMessage(chatroom, currentMember);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data("read")
                .build();
    }


    // Todo : 읽지 않은 메시지 수
    /**
     * getNotReadCount
     */


    // Todo : pagination적용 message query
    /**
     * getMessagesByChatroom
     */


    // Todo : chatroom 목록
    @GetMapping("chat/chatrooms")
    public ResponseEntity getChatroomsByMember(){
        Member currentMember = MemberUtil.getMember();
        List<Chatroom> chatrooms = chatService.getChatroomsByMember(currentMember);

        List<ChatroomResponseDto> chatroomResponses = chatrooms.stream().map(Chatroom::toChatroomResponseDto).collect(Collectors.toList());

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(chatroomResponses)
                .build();
    }


    @GetMapping("chat/chatroom")
    public ResponseEntity getChatroomByMember(@RequestBody ChatroomMemberDto chatroomMemberDto){

        UUID starterId = UUID.fromString(chatroomMemberDto.getStarter());
        UUID participantId = UUID.fromString(chatroomMemberDto.getParticipant());
        String chatroomId = chatService.getChatroomByIds(starterId, participantId);
        log.info(chatroomId);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(chatroomId)
                .build();
    }


}
