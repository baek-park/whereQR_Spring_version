package whereQR.project.domain.favorite;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.dashboard.DashboardRepository;
import whereQR.project.domain.dashboard.dto.DashboardResponseDto;
import whereQR.project.domain.favorite.dto.FavoriteCountDto;
import whereQR.project.domain.member.Member;
import whereQR.project.domain.member.MemberRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final DashboardRepository dashboardRepository;
    private final MemberRepository memberRepository;


    public UUID getFavoriteId(UUID dashboardId, Member member) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new EntityNotFoundException("Dashboard not found"));

        Optional<Favorite> favorite = favoriteRepository.findByDashboardAndMember(dashboard, member);
        return favorite.map(Favorite::getId).orElse(null);
    }

    @Transactional
    public UUID createFavorite(UUID dashboardId, Member member) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new EntityNotFoundException("Dashboard not found"));

        Favorite newFavorite = new Favorite(dashboard, member);
        favoriteRepository.save(newFavorite);
        return newFavorite.getId();
    }

    @Transactional
    public void deleteFavorite(UUID dashboardId, Member member) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new EntityNotFoundException("Dashboard not found"));

        Favorite favorite = favoriteRepository.findByDashboardAndMember(dashboard, member)
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }


    public FavoriteCountDto getFavoriteCountByDashboardId(UUID dashboardId) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new EntityNotFoundException("Dashboard not found"));
        long count = favoriteRepository.countByDashboard(dashboard);
        return new FavoriteCountDto(dashboardId, count);
    }

    public List<DashboardResponseDto> getFavoritesByMember(Member member) {
        List<Favorite> favorites = favoriteRepository.findByMember(member);
        return favorites.stream()
                .map(favorite -> {
                    Dashboard dashboard = favorite.getDashboard();
                    DashboardResponseDto dto = new DashboardResponseDto(
                            dashboard.getId(),
                            dashboard.getTitle(),
                            dashboard.getContent(),
                            dashboard.getAuthor().getId().toString(),
                            dashboard.getAuthor().getUsername(),
                            dashboard.getLostedCity(),
                            dashboard.getLostedDistrict(),
                            dashboard.getLostedType(),
                            dashboard.getCreatedAt()
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

}