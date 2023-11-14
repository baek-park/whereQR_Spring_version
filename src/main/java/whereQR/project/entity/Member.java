package whereQR.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.Query;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import whereQR.project.entity.dto.MemberDetailDto;
import whereQR.project.entity.dto.MemberSignupDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public MemberDetailDto toMemberDetailDto(){
            return new MemberDetailDto(
                    this.username,
                    this.phoneNumber,
                    this.qrcodeList);
    }
//
//    public MemberSignupDto toMemberSignupDto(){
//        return new MemberSignupDto(
//                this.username,
//                this.phoneNumber,
//                this.role
//        );
//    }
}
