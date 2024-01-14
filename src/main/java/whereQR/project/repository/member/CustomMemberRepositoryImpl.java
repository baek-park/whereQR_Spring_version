package whereQR.project.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import whereQR.project.entity.Member;
import whereQR.project.entity.QMember;
import whereQR.project.entity.Role;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomMemberRepositoryImpl implements CustomMemberRepository{

    private final JPAQueryFactory queryFactory;
    private QMember member = QMember.member;

    @Override
    public Boolean existsMemberByKakaoIdAndRole(Long kakaoId, Role role){
        return queryFactory
                .selectFrom(member)
                .where(member.kakaoId.eq(kakaoId).and(member.role.eq(role)))
                .fetchFirst() !=null;
    }

    @Override
    public Optional<Member> findMemberByKakaoIdAndRole(Long kakaoId, Role role){
        return Optional.ofNullable(queryFactory
                .selectFrom(member)
                .where(member.kakaoId.eq(kakaoId).and(member.role.eq(role)))
                .fetchFirst());
    }

    @Override
    public Optional<Member> findMemberByRefreshToken(String refreshToken){
        return Optional.ofNullable(queryFactory
                .selectFrom(member)
                .where(member.refreshToken.eq(refreshToken))
                .fetchFirst());
    }
}
