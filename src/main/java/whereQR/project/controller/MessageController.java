package whereQR.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whereQR.project.entity.Chatroom;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.chat.ResponseMessageDto;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.service.ChatroomService;
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
