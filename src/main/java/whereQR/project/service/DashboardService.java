package whereQR.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whereQR.project.entity.Dashboard;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.dashboard.DashboardCreateRequest;
import whereQR.project.entity.dto.dashboard.DashboardUpdateRequest;
import whereQR.project.repository.dashboard.DashboardRepository;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    @Autowired
    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    public UUID createDashboard(DashboardCreateRequest request, Member author) {
        Dashboard dashboard = new Dashboard(
                request.getTitle(),
                request.getContent(),
                request.getLostedType(),
                request.getLostedCity(),
                request.getLostedDistrict(),
                author
        );
        dashboard = dashboardRepository.save(dashboard);
        return dashboard.getDashboardId();
    }

    public UUID updateDashboard(DashboardUpdateRequest request) {
        Dashboard dashboard = dashboardRepository.findById(request.getDashboardId())
                .orElseThrow(() -> new EntityNotFoundException("Dashboard not found with id: " + request.getDashboardId()));
        dashboard.update(
                request.getTitle(),
                request.getContent(),
                request.getLostedType(),
                request.getLostedCity(),
                request.getLostedDistrict()
        );
        dashboardRepository.save(dashboard);
        return dashboard.getDashboardId();
    }

    public void deleteDashboard(UUID dashboardId) {
        dashboardRepository.deleteById(dashboardId);
    }

}
