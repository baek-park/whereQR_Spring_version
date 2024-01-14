package whereQR.project.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import whereQR.project.entity.Message;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID>, CustomMessageRepository {
}
