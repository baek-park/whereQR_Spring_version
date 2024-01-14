package whereQR.project.repository.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whereQR.project.entity.Chatroom;

import java.util.UUID;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom,UUID>, CustomChatroomRepository{
}
