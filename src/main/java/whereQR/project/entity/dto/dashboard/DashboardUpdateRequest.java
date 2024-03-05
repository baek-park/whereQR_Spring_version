package whereQR.project.entity.dto.dashboard;

import lombok.Data;

import java.util.UUID;

@Data
public class DashboardUpdateRequest {
    private UUID dashboardId;
    private String title;
    private String content;
    private String lostedType;
    private String lostedCity;
    private String lostedDistrict;
}
