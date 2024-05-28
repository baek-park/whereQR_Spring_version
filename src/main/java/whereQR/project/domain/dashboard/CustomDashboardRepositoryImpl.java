package whereQR.project.domain.dashboard;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import whereQR.project.domain.dashboard.dto.DashboardSearchCriteria;
import whereQR.project.domain.favorite.QFavorite;
import whereQR.project.domain.file.QFile;
import whereQR.project.domain.member.Member;


import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomDashboardRepositoryImpl implements CustomDashboardRepository {

    private final JPAQueryFactory queryFactory;
    private QDashboard dashboard = QDashboard.dashboard;

    @Override
    public List<Dashboard> findDashboardsByPaginationAndSearch(DashboardSearchCriteria condition, Pageable pageable){

        BooleanBuilder builder = getBooleanBuilder(condition);

        return queryFactory
                .selectFrom(dashboard)
                .where(builder)
                .orderBy(dashboard.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
    @Override
    public List<Dashboard> searchByKeyword(String keyword, Pageable pageable) {
        return queryFactory
                .selectFrom(dashboard)
                .where(dashboard.title.contains(keyword).or(dashboard.content.contains(keyword)))
                .orderBy(dashboard.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
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

    @Override
    public List<Dashboard> findFavoriteDashboardsByPaginationAndMemberId(UUID memberId, Pageable pageable){

        QFavorite favorite = QFavorite.favorite;

        return queryFactory
                .selectFrom(dashboard)
                .leftJoin(dashboard.favorites, favorite)
                .where(favorite.member.id.eq(memberId))
                .orderBy(dashboard.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long countByDashboardsCondition(DashboardSearchCriteria condition) {
        BooleanBuilder builder = getBooleanBuilder(condition);

        return queryFactory
                .select(dashboard.count())
                .from(dashboard)
                .where(builder)
                .fetchOne();
    }
    public Long countByDashboards(List<Dashboard> dashboards){
        return queryFactory
                .select(dashboard.count())
                .from(dashboard)
                .where(dashboard.in(dashboards))
                .fetchOne();
    }

    public Long countByFavoriteDashboardByMemberId(UUID memberId){
        QFavorite favorite = QFavorite.favorite;

        return queryFactory
                .select(dashboard.count())
                .from(dashboard)
                .leftJoin(dashboard.favorites, favorite)
                .where(favorite.member.id.eq(memberId))
                .orderBy(dashboard.createdAt.desc())
                .fetchOne();

    }

    private BooleanBuilder getBooleanBuilder(DashboardSearchCriteria condition){

        BooleanBuilder builder = new BooleanBuilder();
        if(condition.hasCondition() == Boolean.FALSE){
            return builder;
        }

        // title or content
        if (condition.getSearch() != null) {
            log.info("search -> {}", condition.getSearch());
            builder.and(dashboard.title.contains(condition.getSearch()).or(dashboard.content.contains(condition.getSearch())));
        }

        // district
        if(condition.getLostedDistrict() != null){
            log.info("district -> {}", condition.getLostedDistrict());
            builder.and(dashboard.lostedDistrict.eq(condition.getLostedDistrict()));
        }

        // type
        if(condition.getLostedType() != null){
            log.info("getLostedType -> {}", condition.getLostedType());
            builder.and(dashboard.lostedType.eq(condition.getLostedType()));
        }

        if(condition.getStartDate() != null){
            log.info("getStartDate -> {}", condition.getStartDate());
            builder.and(dashboard.createdAt.goe(condition.getStartDate()));
        }

        if(condition.getEndDate() != null){
            log.info("getEndDate -> {}", condition.getLostedType());
            builder.and(dashboard.createdAt.loe(condition.getEndDate()));
        }

        return builder;
    }
}