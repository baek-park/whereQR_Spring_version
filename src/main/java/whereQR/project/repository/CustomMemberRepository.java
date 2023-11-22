package whereQR.project.repository;

import whereQR.project.entity.Member;
import whereQR.project.entity.Role;

import java.util.Optional;

public interface CustomMemberRepository {

    Boolean existsMemberByUsernameAndRole(String username, Role role);

    Boolean existsMemberByKakaoIdAndRole(Long kakaoId, Role role);

    Optional<Member> findMemberByKakaoId(Long kakaoId);
}
