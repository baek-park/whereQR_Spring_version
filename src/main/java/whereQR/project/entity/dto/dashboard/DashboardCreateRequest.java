package whereQR.project.entity.dto.dashboard;

import lombok.Getter;

@Getter
public class DashboardCreateRequest {
    private String title;
    private String content;
    private String lostedType;
    private String lostedCity;
    private String lostedDistrict;
}
