package whereQR.project.domain.qrcode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrcodeRepository extends JpaRepository<Qrcode,UUID> {
    Optional<Qrcode> findById(UUID id);
}
