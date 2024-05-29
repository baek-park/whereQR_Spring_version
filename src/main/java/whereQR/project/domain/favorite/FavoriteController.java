package whereQR.project.domain.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import whereQR.project.domain.dashboard.dto.DashboardResponseDto;
import whereQR.project.domain.favorite.dto.FavoriteResponseDto;
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

        if (member == null) {
            String message = "로그인이 필요한 서비스입니다.";
            return ResponseEntity.builder()
                    .status(Status.FAILED)
                    .data(message)
                    .build();
        }

        UUID favoriteId = favoriteService.getFavoriteId(dashboardId, member);

        boolean isfavorited  = favoriteId == null;

        if (favoriteId != null) {
            favoriteService.deleteFavorite(dashboardId, member);
            isfavorited = false;
        }
        if (favoriteId == null) {
            favoriteId = favoriteService.createFavorite(dashboardId, member);
            isfavorited = true;
        }


        FavoriteResponseDto responseDto = new FavoriteResponseDto(favoriteId, isfavorited);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(responseDto)
                .build();
    }

}