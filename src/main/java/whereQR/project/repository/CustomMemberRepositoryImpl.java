package whereQR.project.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import whereQR.project.entity.Member;
import whereQR.project.entity.QMember;
import whereQR.project.entity.Role;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository{

    private final JPAQueryFactory queryFactory;
    private QMember member = QMember.member;

    @Override
    public Boolean existsMemberByUsernameAndRole(String username, Role role) {
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username).and(member.role.eq(role)))
                .fetchFirst() != null;
    }

    @Override
    public Boolean existsMemberByKakaoIdAndRole(Long kakaoId, Role role){
        return queryFactory
                .selectFrom(member)
                .where(member.kakaoId.eq(kakaoId).and(member.role.eq(role)))
                .fetchFirst() !=null;
    }

    @Override
    public Optional<Member> findMemberByKakaoId(Long kakaoId){
        return Optional.ofNullable(queryFactory
                .selectFrom(member)
                .where(member.kakaoId.eq(kakaoId))
                .fetchFirst());
    }
}
