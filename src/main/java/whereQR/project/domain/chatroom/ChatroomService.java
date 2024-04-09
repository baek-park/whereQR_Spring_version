package whereQR.project.domain.chatroom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.domain.member.Member;
import whereQR.project.domain.chatroom.dto.ChatroomProjectionDto;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.exception.CustomExceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

    public Chatroom getChatroomById(UUID id){
        return chatroomRepository.findById(id).orElseThrow(() -> new NotFoundException("해당하는 채팅방이 존재하지 않습니다.", this.getClass().toString()));
    }
    public String getChatroomByIds( UUID starterId, UUID participantId){
        return chatroomRepository.findChatroomByMemberIds(starterId, participantId).orElseThrow(() -> new NotFoundException("해당하는 채팅방이 존재하지 않습니다.", this.getClass().toString())).toString();
    }

    public List<ChatroomProjectionDto> getChatroomsByMember(Member member){
        return chatroomRepository.findChatroomsByMember(member);
    }

    @Transactional
    public Chatroom createChatroom(Member starter, Member participant){
        if(chatroomRepository.existChatroomByUsers(starter, participant)) {
            throw new BadRequestException("이미 진행 중인 채팅방이 존재합니다.", this.getClass().toString());
        }

        Chatroom chatroom = new Chatroom(starter, participant);
        return chatroomRepository.save(chatroom);
    }

}
