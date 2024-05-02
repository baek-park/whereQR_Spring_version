package whereQR.project.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import whereQR.project.utils.PageInfoDto;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardPageResponseDto {
    private List<DashboardResponseDto> data;
    private PageInfoDto pageInfo;
}
