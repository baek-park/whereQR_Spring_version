package whereQR.project.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
    public Boolean existsMemberByPhoneNumberAndRole(String phoneNumber, Role role) {
        return queryFactory
                .selectFrom(member)
                .where(member.phoneNumber.eq(phoneNumber).and(member.role.eq(role)))
                .fetchFirst() !=null;
    }

    @Override
    public Boolean existsMemberByEmailAndRole(String email, Role role) {
        return queryFactory
                .selectFrom(member)
                .where(member.email.eq(email).and(member.role.eq(role))) // to eamil
                .fetchFirst() !=null;
    }

    @Override
    public Optional<Member> findMemberByPhoneNumberAndRole(String phoneNumber, Role role) {
        return Optional.ofNullable(queryFactory
                .selectFrom(member)
                .where(member.phoneNumber.eq(phoneNumber).and(member.role.eq(role)))
                .fetchFirst());
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

    @Override
    public Optional<Member> findMemberByEmailAndRole(String email, Role role) {
        return Optional.ofNullable(queryFactory
                .selectFrom(member)
                .where(member.email.eq(email).and(member.role.eq(role)))
                .fetchFirst());
    }
}
