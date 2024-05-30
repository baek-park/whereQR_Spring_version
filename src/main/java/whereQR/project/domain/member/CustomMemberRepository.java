package whereQR.project.domain.member;

import java.util.Optional;

public interface CustomMemberRepository {
    Boolean existsMemberByKakaoIdAndRole(Long kakaoId, Role role);
    Boolean existsMemberByPhoneNumberAndRole(String phoneNumber, Role role);

    Boolean existsMemberByEmailAndRole(String email, Role role);

    Optional<Member> findMemberByPhoneNumberAndRole(String phoneNumber, Role role);

    Optional<Member> findMemberByKakaoIdAndRole(Long kakaoId, Role role);

    Optional<Member> findMemberByRefreshToken(String refreshToken);

    Optional<Member> findMemberByEmailAndRole(String email, Role role);
}
