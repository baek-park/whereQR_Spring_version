package whereQR.project.domain.dashboard.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DashboardUpdateRequestDto {
    private UUID dashboardId;
    private String title;
    private String content;
    private String lostedType;
    private String lostedCity;
    private String lostedDistrict;
}
