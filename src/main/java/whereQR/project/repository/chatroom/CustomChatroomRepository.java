package whereQR.project.repository.chatroom;

import whereQR.project.entity.Member;
import whereQR.project.entity.Role;

public interface CustomChatroomRepository {

    Boolean existChatroomByUsers(Member user1, Member user2);
}
