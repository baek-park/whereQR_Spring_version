package whereQR.project.domain.member;

import java.util.Optional;

public interface CustomMemberRepository {
    Boolean existsMemberByKakaoIdAndRole(Long kakaoId, Role role);

    Optional<Member> findMemberByKakaoIdAndRole(Long kakaoId, Role role);

    Optional<Member> findMemberByRefreshToken(String refreshToken);
}
