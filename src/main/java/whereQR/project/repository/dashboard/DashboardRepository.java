package whereQR.project.repository.dashboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import whereQR.project.entity.Dashboard;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface DashboardRepository extends JpaRepository<Dashboard, UUID> {
    @Query("SELECT d FROM Dashboard d WHERE d.title LIKE %:keyword% OR d.content LIKE %:keyword% ORDER BY d.createdAt DESC")
    Page<Dashboard> searchByKeyword(String keyword, Pageable pageable);

//    Page<Dashboard> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String title, String content, Pageable pageable);
}
