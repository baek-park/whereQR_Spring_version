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
    public DashboardSearchCriteria(String search, String lostedDistrict, String lostedType, LocalDateTime startDate, LocalDateTime endDate) {
        this.search = search;
        this.lostedDistrict = lostedDistrict;
        this.lostedType = lostedType;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}