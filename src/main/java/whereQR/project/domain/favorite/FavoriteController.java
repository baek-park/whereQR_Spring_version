package whereQR.project.domain.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.member.Member;
import whereQR.project.utils.MemberUtil;
import whereQR.project.domain.favorite.dto.FavoriteRequestDto;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/toggle")
    public ResponseEntity toggleFavorite(@RequestBody FavoriteRequestDto favoriteRequest) {
        UUID dashboardId = favoriteRequest.getDashboardId();
        Member member = MemberUtil.getMember();

        UUID likeId = favoriteService.getFavoriteId(dashboardId, member);

        if (likeId != null) {
            favoriteService.deleteFavorite(dashboardId, member);
        }
        if (likeId == null) {
            likeId = favoriteService.createFavorite(dashboardId, member);
        }

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(likeId)
                .build();
    }


    @GetMapping("/count/{dashboardId}")
    public ResponseEntity getFavoriteCount(@PathVariable UUID dashboardId) {
        long count = favoriteService.countFavoritesByDashboardId(dashboardId);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(count)
                .build();
    }



}