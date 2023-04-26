package whereQR.project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import whereQR.project.entity.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
