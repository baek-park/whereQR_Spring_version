package whereQR.project.repository.chatroom;

import whereQR.project.entity.Member;

public interface CustomChatroomRepository {

    Boolean existChatroomByUsers(Member user1, Member user2);
}
