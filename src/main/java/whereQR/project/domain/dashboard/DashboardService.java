package whereQR.project.domain.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import whereQR.project.domain.dashboard.dto.*;
import whereQR.project.domain.favorite.Favorite;
import whereQR.project.domain.favorite.FavoriteRepository;
import whereQR.project.domain.file.File;
import whereQR.project.domain.file.FileRepository;
import whereQR.project.domain.file.FileService;
import whereQR.project.domain.member.Member;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.utils.PageInfoDto;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final FavoriteRepository favoriteRepository;
    private final FileRepository fileRepository;

    public Dashboard getDashboardById(UUID id) {
        return dashboardRepository.findById(id).orElseThrow(() -> new NotFoundException("해당하는 대시보드가 존재하지 않습니다.", this.getClass().toString()));
    }

    @Transactional
    public UUID createDashboard(DashboardCreateRequestDto request, Member author) {
        Dashboard dashboard = new Dashboard(
                request.getTitle(),
                request.getContent(),
                request.getLostedType(),
                request.getLostedCity(),
                request.getLostedDistrict(),
                author
        );

        List<File> dashboardImages = fileRepository.findImagesByIds(request.getImages());
        log.info("here {}", dashboardImages);

        for (File image : dashboardImages) {
            dashboard.addImage(image);
        }

        dashboard = dashboardRepository.save(dashboard);
        return dashboard.getId();
    }
    public DashboardPageResponseDto getDashboards(int offset, int limit, String search) {

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("createdAt").descending());

        Page<Dashboard> dashboardPage;
        if (search == null || search.isEmpty()) {
            dashboardPage = dashboardRepository.findAll(pageable);
        } else {
            log.info("search -> {}", search);
            List<Dashboard> content = dashboardRepository.searchByKeyword(search, pageable);
            Long totalCount = dashboardRepository.countByDashboards(content);
            dashboardPage = new PageImpl<>(content, pageable, totalCount);
        }

        List<DashboardResponseDto> dashboardDtos = dashboardPage.getContent().stream()
                .map(dashboard -> new DashboardResponseDto(
                        dashboard.getId(),
                        dashboard.getTitle(),
                        dashboard.getContent(),
                        dashboard.getAuthor().getId().toString(),
                        dashboard.getAuthor().getUsername(),
                        dashboard.getLostedCity(),
                        dashboard.getLostedDistrict(),
                        dashboard.getLostedType(),
                        dashboard.getImages().stream().map(it -> it.toFileResponseDto()).collect(Collectors.toList()),
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
                        dashboard.getAuthor().getId().toString(),
                        dashboard.getAuthor().getUsername(),
                        dashboard.getLostedCity(),
                        dashboard.getLostedDistrict(),
                        dashboard.getLostedType(),
                        dashboard.getImages().stream().map(it -> it.toFileResponseDto()).collect(Collectors.toList()),
                        dashboard.getCreatedAt()))
                .collect(Collectors.toList());

        return new DashboardPageResponseDto(dashboardDtos, pageInfo);
    }
    @Transactional
    public UUID updateDashboard(DashboardUpdateRequestDto request) {
        Dashboard dashboard = dashboardRepository.findById(request.getDashboardId())
                .orElseThrow(() -> new EntityNotFoundException("Dashboard not found with id: " + request.getDashboardId()));
        dashboard.update(
                request.getTitle(),
                request.getContent(),
                request.getLostedType(),
                request.getLostedCity(),
                request.getLostedDistrict()
        );

        List<File> dashboardImages = fileRepository.findImagesByIds(request.getImages());
        dashboard.deleteImage();
        for (File image : dashboardImages) {
            dashboard.addImage(image);
        }

        return dashboard.getId();
    }

    @Transactional
    public void deleteDashboard(UUID dashboardId) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new NotFoundException("해당하는 대시보드가 존재하지 않습니다.", this.getClass().toString()));

        List<Favorite> favoritesLinkedToDashboard = favoriteRepository.findByDashboard(dashboard);
        favoriteRepository.deleteAll(favoritesLinkedToDashboard);

        dashboardRepository.delete(dashboard);
    }

}
