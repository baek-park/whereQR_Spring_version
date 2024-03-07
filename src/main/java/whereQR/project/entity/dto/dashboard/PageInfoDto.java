package whereQR.project.entity.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageInfoDto {
    private long totalCount;
    private boolean hasNext;

}
