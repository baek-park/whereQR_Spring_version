package whereQR.project.domain.dashboard;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.Optional;

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
                        .fetchResults())
                .map(result -> new PageImpl<>(result.getResults(), pageable, result.getTotal()));
    }
}