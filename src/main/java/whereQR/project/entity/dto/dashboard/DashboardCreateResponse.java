package whereQR.project.entity.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DashboardCreateResponse {
    private UUID dashboardId;
}
