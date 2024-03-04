package whereQR.project.repository.chatroom;

import whereQR.project.entity.Member;
import whereQR.project.entity.dto.chat.ChatroomProjectionDto;
import whereQR.project.entity.dto.chat.ChatroomResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomChatroomRepository {

    Boolean existChatroomByUsers(Member user1, Member user2);
    List<ChatroomProjectionDto> findChatroomsByMember(Member member);

    Optional<UUID> findChatroomByMemberIds(UUID starterId, UUID participantId);
}
