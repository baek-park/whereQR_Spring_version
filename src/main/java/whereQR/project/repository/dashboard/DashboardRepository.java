package whereQR.project.repository.dashboard;

import org.springframework.data.jpa.repository.JpaRepository;
import whereQR.project.entity.Dashboard;

import java.util.UUID;

public interface DashboardRepository extends JpaRepository<Dashboard, UUID> {
}
