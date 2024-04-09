package whereQR.project.repository.dashboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whereQR.project.entity.Dashboard;

import java.util.Optional;

public interface CustomDashboardRepository {
    Optional<Page<Dashboard>> searchByKeyword(String keyword, Pageable pageable);
}