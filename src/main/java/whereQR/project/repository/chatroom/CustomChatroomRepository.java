package whereQR.project.repository.chatroom;

import whereQR.project.entity.Chatroom;
import whereQR.project.entity.Member;

import java.util.List;

public interface CustomChatroomRepository {

    Boolean existChatroomByUsers(Member user1, Member user2);
    List<Chatroom> findChatroomsByMember(Member member);
}
