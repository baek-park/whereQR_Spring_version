package whereQR.project.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whereQR.project.entity.Member;

import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID>, CustomMemberRepository{
}