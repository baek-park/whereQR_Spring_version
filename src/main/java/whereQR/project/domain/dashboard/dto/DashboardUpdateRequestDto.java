package whereQR.project.domain.dashboard.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DashboardUpdateRequestDto {
    private UUID dashboardId;
    private String title;
    private String content;
    private String lostedType;
    private String lostedDistrict;
    private List<UUID> images;
}
