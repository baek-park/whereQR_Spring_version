package whereQR.project.repository;

public interface CustomMemberRepository {

    Boolean existsMemberByUsernameAndRoles(String username, String role);
}
