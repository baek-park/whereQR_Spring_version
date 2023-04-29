package whereQR.project.entity.qrcode;

import lombok.Getter;
import whereQR.project.entity.Address;
import whereQR.project.entity.PhoneNumber;
import whereQR.project.entity.member.Member;

import javax.persistence.*;

@Entity
@Getter
public class Qrcode {

    @Id @GeneratedValue
    private Long id;
    private String title;
    private String memo;

    @Embedded
    Address address;
    @Embedded
    PhoneNumber phoneNumber;
    @Column(name="url", columnDefinition = "MEDIUMBLOB")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Qrcode(String title, String memo){
        this.title = title;
        this.memo = memo;
    }

    public Qrcode() {

    }

    public Qrcode(String url){
        this.url = url;
    }

    //연관관계 편의 메서드
    public void changeQrcode(Member member){
        this.member = member;
        member.getQrcodes().add(this);
    }

}
