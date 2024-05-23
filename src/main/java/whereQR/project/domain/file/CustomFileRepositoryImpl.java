package whereQR.project.domain.file;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CustomFileRepositoryImpl implements CustomFileRepository {

    private final JPAQueryFactory queryFactory;
    private QFile file = QFile.file;

    @Override
    public List<File> findImagesByIds(List<UUID> images) {
        return queryFactory.selectFrom(file)
                .where(file.id.in(images))
                .fetch();
    }
}
