package whereQR.project.domain.file;

import java.util.List;
import java.util.UUID;

public interface CustomFileRepository{

    List<File> findImagesByIds(List<UUID> images);
}
