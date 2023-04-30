package whereQR.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whereQR.project.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByUsername(String username);
}
