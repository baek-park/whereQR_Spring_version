package whereQR.project.domain.dashboard;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD:src/main/java/whereQR/project/repository/dashboard/DashboardRepository.java
import whereQR.project.entity.Dashboard;
import org.springframework.data.jpa.repository.Query;
import whereQR.project.entity.dto.dashboard.DashboardResponseDto;
=======
>>>>>>> develop:src/main/java/whereQR/project/domain/dashboard/DashboardRepository.java

import java.util.UUID;

public interface DashboardRepository extends JpaRepository<Dashboard, UUID>, CustomDashboardRepository {
//    Page<Dashboard> searchByKeyword(String keyword, Pageable pageable);
//    Page<Dashboard> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String title, String content, Pageable pageable);
}
