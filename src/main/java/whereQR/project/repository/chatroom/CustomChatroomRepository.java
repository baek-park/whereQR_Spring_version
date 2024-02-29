package whereQR.project.repository.chatroom;

import whereQR.project.entity.Chatroom;
import whereQR.project.entity.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomChatroomRepository {

    Boolean existChatroomByUsers(Member user1, Member user2);
    List<Chatroom> findChatroomsByMember(Member member);

    Optional<UUID> findChatroomByMemberIds(UUID starterId, UUID participantId);
}
