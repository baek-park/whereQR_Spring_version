package whereQR.project.Repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.member.Member;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    @Commit
    public void insertMemberTest() {

        //given
        Member member = new Member("username", "email", 10);


        //when
        em.persist(member);

        //then
        Assertions.assertThat(member.getEmail()).isEqualTo("email");
    }

}