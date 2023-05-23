package whereQR.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whereQR.project.entity.Qrcode;

@Repository
public interface QrcodeRepository extends JpaRepository<Qrcode,Long> {
}
