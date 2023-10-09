package whereQR.project.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import whereQR.project.entity.QMember;

@Repository
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository{

    private final JPAQueryFactory queryFactory;
    private QMember member = QMember.member;

    @Override
    public Boolean existsMemberByUsernameAndRoles(String username, String role) {
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username).and(member.roles.contains(role)))
                .fetchFirst() != null;
    }
}
