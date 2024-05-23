package whereQR.project.domain.dashboard.dto;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class DashboardCreateRequestDto {
    private String title;
    private String content;
    private String lostedType;
    private String lostedCity;
    private String lostedDistrict;
    private List<UUID> images;
}
