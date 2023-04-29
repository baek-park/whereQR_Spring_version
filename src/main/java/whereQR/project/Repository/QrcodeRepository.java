package whereQR.project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whereQR.project.entity.qrcode.Qrcode;

public interface QrcodeRepository extends JpaRepository<Qrcode,Long> {
}
