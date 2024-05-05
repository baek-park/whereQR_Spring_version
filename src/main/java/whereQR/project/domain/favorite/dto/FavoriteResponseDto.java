package whereQR.project.domain.favorite.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FavoriteResponseDto {
    private UUID likeId;
    private boolean isLiked;

    public FavoriteResponseDto(UUID likeId, boolean isLiked) {
        this.likeId = likeId;
        this.isLiked = isLiked;
    }

    public boolean isLiked() {
        return isLiked;
    }
}