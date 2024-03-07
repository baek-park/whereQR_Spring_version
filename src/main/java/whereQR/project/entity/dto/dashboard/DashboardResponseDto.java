package whereQR.project.entity.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자를 자동으로 생성
public class DashboardResponseDto {
    private UUID dashboard_id;
    private String title;
    private String content;
    private String author_id; // author의 id. Member 엔티티와 연동 필요
    private LocalDateTime createdAt;
}
