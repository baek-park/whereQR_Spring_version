package whereQR.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whereQR.project.entity.Member;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID>, CustomMemberRepository{
    Optional<Member> findMemberByUsername(String username);
    Optional<Member> findMemberByKakaoId(Long kakaoId);

    Optional<Member> findById(UUID id);
}