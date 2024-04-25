package whereQR.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import whereQR.project.entity.Chatroom;
import whereQR.project.entity.Dashboard;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.dashboard.*;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import whereQR.project.repository.dashboard.DashboardRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public Dashboard getDashboardById(UUID id) {
        return dashboardRepository.findById(id).orElseThrow(() -> new NotFoundException("해당하는 대시보드가 존재하지 않습니다.", this.getClass().toString()));
    }

    @Transactional
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
            dashboardPage = dashboardRepository.searchByKeyword(search, pageable)
                    .orElse(Page.empty()); // provide an empty Page as default
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

    public DashboardPageResponseDto getDashboardsByMemberId(int offset, int limit, UUID memberId){
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("createdAt").descending());

        List<Dashboard> content = dashboardRepository.findDashboardsByPaginationAndMemberId(memberId, pageable);
        Long totalCount = dashboardRepository.countByDashboards(content);
        Page<Dashboard> dashboardPage = new PageImpl<>(content, pageable, totalCount);

        PageInfoDto pageInfo = new PageInfoDto(dashboardPage.getTotalElements(), dashboardPage.hasNext());

        List<DashboardResponseDto> dashboardDtos = dashboardPage.getContent().stream()
                .map(dashboard -> new DashboardResponseDto(
                        dashboard.getId(),
                        dashboard.getTitle(),
                        dashboard.getContent(),
                        dashboard.getAuthor() != null ? dashboard.getAuthor().getUsername() : "Unknown", // 'username' 추가
                        dashboard.getCreatedAt()))
                .collect(Collectors.toList());

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
