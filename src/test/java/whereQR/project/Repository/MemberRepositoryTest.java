package whereQR.project.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.Address;
import whereQR.project.entity.member.Member;
import whereQR.project.entity.PhoneNumber;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void insertMemberTest() {

        //given
        Member member = new Member("username", "email", 10);


        //when
        em.persist(member);

        //then
        Assertions.assertThat(member.getEmail()).isEqualTo("email");
    }

    @Test
    @DisplayName("find all Member")
    public void findAllMember(){

        //given
        Member member1 = new Member("user1","user1@email.com",10);
        Member member2 = new Member("user2","user2@email.com",20);

        em.persist(member1);
        em.persist(member2);

        //when
        List<Member> members =  memberRepository.findAll();

        //then
        Assertions.assertThat(members.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("updateMember")
    @Commit
    public void updateMember(){

        //given
        Member member1 = new Member("user1","user1@email.com",10);
        em.persist(member1);

        //when
        Long memberId = member1.getId();
        Member findMember = memberRepository.findById(memberId).orElseThrow();
        Address address = new Address("city","1","11");
        PhoneNumber phoneNumber = new PhoneNumber("82","010-8750-9331");
        findMember.updateMember("user2",10,address ,phoneNumber);

        //then
        Assertions.assertThat(member1.getUsername()).isEqualTo("user2");
        Assertions.assertThat(member1.getPhoneNumber()).isEqualTo(phoneNumber);

    }

}