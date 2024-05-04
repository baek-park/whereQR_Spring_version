package whereQR.project.domain.favorite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

@Getter
public class FavoriteRequestDto {
    @JsonProperty("dashboard_id")
    private UUID dashboardId;
}

