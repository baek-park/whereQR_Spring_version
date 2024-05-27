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

    public Boolean hasCondition(){
        if(this.search != null || this.lostedDistrict!= null || this.lostedDistrict!= null || this.startDate != null|| this.endDate!= null){
            // 하나라도 조건이 있다면 그 컨디션만 적용되어야함
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
}