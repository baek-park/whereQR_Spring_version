package whereQR.project.entity.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardPageResponseDto {
    private List<DashboardResponseDto> data;
    private PageInfoDto pageInfo;
}
