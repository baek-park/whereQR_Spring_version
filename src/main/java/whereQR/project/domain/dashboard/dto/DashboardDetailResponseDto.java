// DashboardDetailResponseDto.java
package whereQR.project.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import whereQR.project.domain.comment.dto.CommentInfoDto;
import whereQR.project.domain.file.dto.FileResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DashboardDetailResponseDto {
    private UUID dashboard_id;
    private String title;
    private String content;
    private String author_id;
    private String authorName;
    private String lostedDistrict;
    private String lostedType;
    private boolean isFavorite;
    private long favoriteCount;
    private List<CommentInfoDto> comments;
    private List<FileResponseDto> images;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public boolean getIsFavorite() {
        return isFavorite;
    }
}