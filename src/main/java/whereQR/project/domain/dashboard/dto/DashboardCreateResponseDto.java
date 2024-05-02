package whereQR.project.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DashboardCreateResponseDto {
    private UUID dashboardId;
}
