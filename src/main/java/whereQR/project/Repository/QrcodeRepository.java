package whereQR.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whereQR.project.entity.Member;
import whereQR.project.entity.Qrcode;

import java.util.Optional;

@Repository
public interface QrcodeRepository extends JpaRepository<Qrcode,Long> {
    boolean existsQrcodeByMember(Member member);
    Optional<Qrcode> findQrcodeByQrcodeKey(String key);
}
