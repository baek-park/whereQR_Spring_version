package whereQR.project.domain.comment.dto;

import lombok.Data;

import java.util.UUID;


@Data
public class CommentUpdateRequestDto {
    private UUID commentId;
    private String content;
}
