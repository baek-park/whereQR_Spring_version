package whereQR.project.domain.dashboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import whereQR.project.domain.dashboard.dto.DashboardSearchCriteria;
import whereQR.project.domain.member.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomDashboardRepository {
    List<Dashboard> findDashboardsByPaginationAndSearch(DashboardSearchCriteria condition, Pageable pageable);

    List<Dashboard> findFavoriteDashboardsByPaginationAndMemberId(UUID memberId, Pageable pageable);
    List<Dashboard> searchByKeyword(String keyword, Pageable pageable);
    List<Dashboard> findDashboardsByPaginationAndMemberId(UUID memberId, Pageable pageable);
    Long countByDashboards(List<Dashboard> dashboards);
    Long countByDashboardsCondition(DashboardSearchCriteria criteria);

    Long countByFavoriteDashboardByMemberId(UUID memberId);
}