package whereQR.project.domain.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID>, CustomFileRepository {
}
