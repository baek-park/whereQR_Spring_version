package whereQR.project.domain.file.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class FileResponseDto {

    UUID fileId;
    String url;

    public FileResponseDto(UUID id, String url) {
        this.fileId = id;
        this.url = url;
    }
}
