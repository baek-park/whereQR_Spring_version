package whereQR.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.Dashboard;
import whereQR.project.entity.Favorite;
import whereQR.project.entity.Member;
import whereQR.project.repository.favorite.FavoriteRepository;
import whereQR.project.repository.dashboard.DashboardRepository;
import whereQR.project.repository.member.MemberRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final DashboardRepository dashboardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public UUID addFavorite(UUID memberId, UUID dashboardId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid dashboard ID: " + dashboardId));

        Favorite favorite = new Favorite(dashboard, member);
        favorite = favoriteRepository.save(favorite);
        return favorite.getLikeId();
    }

    @Transactional(readOnly = true)
    public Page<Favorite> getFavoritesByMember(UUID memberId, Pageable pageable) {
        // FavoriteRepository에 custom query 메서드를 추가해야
        return favoriteRepository.findAll(pageable); // 임시, 실제로는 멤버 기반 조회 필요
    }

    @Transactional
    public void deleteFavorite(UUID likeId) {
        if (!favoriteRepository.existsById(likeId)) {
            throw new IllegalArgumentException("Favorite not found with ID: " + likeId);
        }
        favoriteRepository.deleteById(likeId);
    }
}
