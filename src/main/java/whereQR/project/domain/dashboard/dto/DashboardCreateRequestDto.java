package whereQR.project.domain.dashboard.dto;

import lombok.Getter;

@Getter
public class DashboardCreateRequestDto {
    private String title;
    private String content;
    private String lostedType;
    private String lostedCity;
    private String lostedDistrict;
}
