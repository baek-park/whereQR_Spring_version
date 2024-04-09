package whereQR.project.domain.dashboard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DashboardRepository extends JpaRepository<Dashboard, UUID>, CustomDashboardRepository {
//    Page<Dashboard> searchByKeyword(String keyword, Pageable pageable);


//    Page<Dashboard> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String title, String content, Pageable pageable);
}
