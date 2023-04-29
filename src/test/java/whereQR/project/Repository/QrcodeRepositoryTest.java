package whereQR.project.Repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.qrcode.Qrcode;
import whereQR.project.entity.member.Member;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class QrcodeRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    QrcodeRepository qrcodeRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("qrcode insert test")
    public void insertQrcodeToMember(){
        //given
        Member member1 = new Member("username","email",10);
        em.persist(member1);

        Qrcode qrcode = new Qrcode("title","memo");
        qrcode.changeQrcode(member1);
        em.persist(qrcode);

        //when
        int count = (int) qrcodeRepository.count();

        //then
        Assertions.assertThat(count).isEqualTo(1);

    }

}