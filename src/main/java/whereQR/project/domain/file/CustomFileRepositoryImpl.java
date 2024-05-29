package whereQR.project.domain.file;

import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import whereQR.project.domain.dashboard.QDashboard;
import whereQR.project.domain.member.QMember;

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

    @Override
    public List<String> findDeleteFilesFromStorage() {
        QDashboard dashboard = QDashboard.dashboard;
        QMember member = QMember.member;
        return queryFactory.select(file.url)
                .from(file)
                .leftJoin(file.dashboard, dashboard)
                .leftJoin(file.profile, member)
                .where(file.dashboard.id.isNull().and(file.profile.id.isNull()))
                .fetch();
    }
}
