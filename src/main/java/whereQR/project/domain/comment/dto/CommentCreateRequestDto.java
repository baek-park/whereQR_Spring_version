package whereQR.project.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CommentCreateRequestDto {
    private String content;
    @JsonProperty("dashboard_id")
    private UUID dashboardId;

    @JsonProperty("parent_id")
    private UUID parentId;
}