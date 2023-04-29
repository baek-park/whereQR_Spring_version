package whereQR.project.entity.member;

import com.sun.istack.NotNull;
import lombok.Getter;
import whereQR.project.entity.Address;
import whereQR.project.entity.PhoneNumber;
import whereQR.project.entity.qrcode.Qrcode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="member")
public class Member {

    //pk
    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;
    // others
    @NotNull @Column(unique = true)
    private String username;
    @NotNull @Column(unique = true)
    private String email;
    private int age;

    // address
    @Embedded
    private Address address;

    //phonenumber
    @Embedded
    private PhoneNumber phoneNumber;

    // member : qrcodes -> 1 : 다 -> qrcode가 연관관계 주인
    @OneToMany(mappedBy = "member")
    private List<Qrcode> qrcodes = new ArrayList<>();

    //생성자
    public Member(){

    }

    public Member(String username, String email, int age){
        this.username = username;
        this.email = email;
        this.age = age;
    }

    public void updateMember(String username, int age, Address address, PhoneNumber phoneNumber){
        this.username = username;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

}
