package whereQR.project.domain.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class FavoriteCountDto {
    private UUID dashboardId;
    private long count;
}