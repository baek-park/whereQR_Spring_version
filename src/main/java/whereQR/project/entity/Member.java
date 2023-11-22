package whereQR.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import whereQR.project.entity.dto.member.MemberDetailDto;
import whereQR.project.entity.dto.member.MemberDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@DynamicUpdate // 업데이트하고자 하는 필드만 업데이트하기 위해서 추가
@Getter
public class Member {

    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
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
    @Column(unique = true)
    private String refreshToken;

    @Column(unique = true, nullable = false)
    private Long kakaoId;

    //생성자
    public Member(){

    }

    public Member(Long kakaoId, String username, String phoneNumber, Role role) {
        this.id = UUID.randomUUID();
        this.kakaoId = kakaoId;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public void updateToken(String refreshToken){
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
