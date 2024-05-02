package whereQR.project.domain.dashboard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DashboardRepository extends JpaRepository<Dashboard, UUID>, CustomDashboardRepository {
}
