package whereQR.project.domain.dashboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomDashboardRepository {
    Optional<Page<Dashboard>> searchByKeyword(String keyword, Pageable pageable);
}