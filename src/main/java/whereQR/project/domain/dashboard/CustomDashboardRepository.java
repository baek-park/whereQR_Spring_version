package whereQR.project.domain.dashboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomDashboardRepository {
    Optional<Page<Dashboard>> searchByKeyword(String keyword, Pageable pageable);
    List<Dashboard> findDashboardsByPaginationAndMemberId(UUID memberId, Pageable pageable);
    Long countByDashboards(List<Dashboard> dashboards);
}