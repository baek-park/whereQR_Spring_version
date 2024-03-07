package whereQR.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import whereQR.project.entity.Dashboard;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.dashboard.*;
import whereQR.project.repository.dashboard.DashboardRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;


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
        return dashboard.getId();
    }

    public DashboardPageResponseDto getDashboards(int offset, int limit, String search) {

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("createdAt").descending());

        Page<Dashboard> dashboardPage;
        if (search == null || search.isEmpty()) {
            dashboardPage = dashboardRepository.findAll(pageable);
        } else {
            dashboardPage = dashboardRepository.searchByKeyword(search, pageable);
        }

        List<DashboardResponseDto> dashboardDtos = dashboardPage.getContent().stream()
                .map(dashboard -> new DashboardResponseDto(
                        dashboard.getId(),
                        dashboard.getTitle(),
                        dashboard.getContent(),
                        dashboard.getAuthor() != null ? dashboard.getAuthor().getUsername() : "Unknown", // 'username' 추가
                        dashboard.getCreatedAt()))
                .collect(Collectors.toList());

        PageInfoDto pageInfo = new PageInfoDto(dashboardPage.getTotalElements(), dashboardPage.hasNext());

        return new DashboardPageResponseDto(dashboardDtos, pageInfo);
    }

    @Transactional
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
        return dashboard.getId();
    }

    public void deleteDashboard(UUID dashboardId) {
        dashboardRepository.deleteById(dashboardId);
    }

}
