package whereQR.project.domain.chatroom;

import whereQR.project.domain.member.Member;
import whereQR.project.domain.chatroom.dto.ChatroomProjectionDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomChatroomRepository {

    Boolean existChatroomByUsers(Member user1, Member user2);
    List<ChatroomProjectionDto> findChatroomsByMember(Member member);

    Optional<UUID> findChatroomByMemberIds(UUID starterId, UUID participantId);
}
