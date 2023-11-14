package whereQR.project.repository;

import whereQR.project.entity.Role;

public interface CustomMemberRepository {

    Boolean existsMemberByUsernameAndRole(String username, Role role);

    Boolean existsMemberByKakaoIdAndRole(Long kakaoId, Role role);
}
