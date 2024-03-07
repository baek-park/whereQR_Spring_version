package whereQR.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import whereQR.project.service.FavoriteService;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

import java.util.UUID;

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
