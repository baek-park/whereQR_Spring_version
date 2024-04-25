package whereQR.project.domain.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity addFavorite() {
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                // .data(생성된 즐겨찾기 정보)
                .build();
    }

}
