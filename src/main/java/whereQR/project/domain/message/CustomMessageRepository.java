package whereQR.project.domain.message;

import whereQR.project.domain.chatroom.Chatroom;
import whereQR.project.domain.member.Member;

import java.util.List;
import java.util.UUID;

public interface CustomMessageRepository {

    List<Message> findNotReadMessageByChatroomAndReceiver(Chatroom chatroom, Member receiver);

    List<Message> findMessagesByChatroomId(UUID chatroomId);

    void updateReadByMessageIds(List<UUID> messageIds);
}
