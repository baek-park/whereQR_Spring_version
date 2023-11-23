package whereQR.project.repository;

import whereQR.project.entity.Member;
import whereQR.project.entity.Role;

import java.util.Optional;

public interface CustomMemberRepository {
    Boolean existsMemberByKakaoIdAndRole(Long kakaoId, Role role);

    Optional<Member> findMemberByKakaoIdAndRole(Long kakaoId, Role role);

    Optional<Member> findMemberByRefreshToken(String refreshToken);
}
