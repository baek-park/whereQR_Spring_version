package whereQR.project.domain.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.member.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    Optional<Favorite> findByDashboardAndMember(Dashboard dashboard, Member member);
    long countByDashboard(Dashboard dashboard);

    List<Favorite> findByMember(Member member);
    List<Favorite> findByDashboard(Dashboard dashboard);


}
