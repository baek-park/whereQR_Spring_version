package whereQR.project.domain.dashboard.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DashboardSearchCriteria {
    private String search;
    private String lostedDistrict;
    private String lostedType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}