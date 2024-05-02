package whereQR.project.domain.chatroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.domain.member.Member;
import whereQR.project.domain.chatroom.dto.ChatroomMemberDto;
import whereQR.project.domain.chatroom.dto.ChatroomProjectionDto;
import whereQR.project.domain.chatroom.dto.ChatroomResponseDto;
import whereQR.project.domain.member.MemberService;
import whereQR.project.domain.message.MessageService;
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
