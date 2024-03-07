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
import whereQR.project.entity.dto.chat.ChatroomMemberDto;
import whereQR.project.entity.dto.chat.ChatroomProjectionDto;
import whereQR.project.entity.dto.chat.ChatroomResponseDto;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.service.ChatroomService;
import whereQR.project.service.MemberService;
import whereQR.project.service.MessageService;
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

    private final ChatroomService chatroomService;
    private final MemberService memberService;

    private final MessageService messageService;

    //chatting room
    @PostMapping("chat/create/room")
    public ResponseEntity createChatroom(@RequestBody ChatroomMemberDto chatroomMemberDto){
        Member starter = memberService.getMemberById(UUID.fromString(chatroomMemberDto.getStarter()));
        Member participant = memberService.getMemberById(UUID.fromString(chatroomMemberDto.getParticipant()));

        Chatroom chatroom = chatroomService.createChatroom(starter, participant);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(chatroom.id)
                .build();
    }

    /**
     * tip) client -> app/send/uuid/uuid (command : publish) / subscribe : message/uuid
     * @param chatRoomId
     * @param content
     * @return
     */
    @MessageMapping("/send/{memberId}/{chatRoomId}")
    public void sendMessage(@DestinationVariable String memberId, @DestinationVariable String chatRoomId,  @RequestBody String content) throws JsonProcessingException {

        Chatroom chatroom = chatroomService.getChatroomById(UUID.fromString(chatRoomId));
        Member currentMember = memberService.getMemberById(UUID.fromString(memberId));

        if(!chatroom.isChatroomMember(currentMember)){
            throw new BadRequestException("사용자가 올바르지 않습니다.", this.getClass().toString());
        }
        Message message = messageService.sendMessage(chatroom, currentMember, content);
    }

    @GetMapping("chat/chatrooms")
    public ResponseEntity getChatroomsByMember(){
        Member currentMember = MemberUtil.getMember();
        UUID currentMemberId = currentMember.getId();
        List<ChatroomProjectionDto> chatrooms = chatroomService.getChatroomsByMember(currentMember);

        List<ChatroomResponseDto> chatroomResponses = chatrooms.stream().map(chatroom -> chatroom.toChatroomResponseDto(currentMemberId)).collect(Collectors.toList());

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(chatroomResponses)
                .build();
    }

    @GetMapping("chat/chatroom")
    public ResponseEntity getChatroomByMembers(@RequestParam String starterId, @RequestParam String participantId){

        String chatroomId = chatroomService.getChatroomByIds(UUID.fromString(starterId), UUID.fromString(participantId));
        log.info(chatroomId);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(chatroomId)
                .build();
    }


}
