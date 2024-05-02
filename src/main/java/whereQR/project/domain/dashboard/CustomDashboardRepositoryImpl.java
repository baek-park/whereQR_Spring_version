package whereQR.project.domain.dashboard;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomDashboardRepositoryImpl implements CustomDashboardRepository {

    private final JPAQueryFactory queryFactory;
    private QDashboard dashboard = QDashboard.dashboard;

    @Override
    public Optional<Page<Dashboard>> searchByKeyword(String keyword, Pageable pageable) {
        return Optional.ofNullable(queryFactory
                        .selectFrom(dashboard)
                        .where(dashboard.title.contains(keyword).or(dashboard.content.contains(keyword)))
                        .orderBy(dashboard.createdAt.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults()) // deprecated?
                .map(result -> new PageImpl<>(result.getResults(), pageable, result.getTotal()));
    }

    @Override
    public List<Dashboard> findDashboardsByPaginationAndMemberId(UUID memberId, Pageable pageable) {
        return queryFactory
                .selectFrom(dashboard)
                .where(dashboard.author.id.eq(memberId))
                .orderBy(dashboard.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }

    public Long countByDashboards(List<Dashboard> dashboards){
        return queryFactory
                .select(dashboard.count())
                .from(dashboard)
                .where(dashboard.in(dashboards))
                .fetchOne();
    }
}