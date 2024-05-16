package whereQR.project.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DashboardDeleteRequestDto {
    @JsonProperty("dashboardId")
    private UUID dashboardId;
}