package whereQR.project.repository.message;

import whereQR.project.entity.Chatroom;
import whereQR.project.entity.Member;
import whereQR.project.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomMessageRepository {

    Optional<List<Message>> findNotReadMessageByChatroomAndReceiver(Chatroom chatroom, Member receiver);

    List<Message> findMessagesByChatroomId(UUID chatroomId);
}
