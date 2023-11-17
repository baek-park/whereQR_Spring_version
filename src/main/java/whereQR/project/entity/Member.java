package whereQR.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import whereQR.project.entity.dto.member.MemberDetailDto;
import whereQR.project.entity.dto.member.MemberDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name="member")
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private UUID id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private String phoneNumber;
    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<Qrcode> qrcodeList = new ArrayList<>();
    private String refreshToken;
    @Column(unique = true, nullable = false)
    private Long kakaoId;

    //생성자
    public Member(){

    }

    public Member(Long kakaoId, String username, String phoneNumber, Role role) {
        this.kakaoId = kakaoId;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }


    public MemberDetails toMemberDetails(){
        return new MemberDetails(this, new SimpleGrantedAuthority(this.role.getName()));
    }

    public MemberDetailDto toMemberDetailDto(){
        return new MemberDetailDto(
                this.username,
                this.phoneNumber,
                this.qrcodeList);
    }

}
