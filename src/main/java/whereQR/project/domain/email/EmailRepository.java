package whereQR.project.domain.email;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, UUID>, CustomEmailRepository {
}
