package whereQR.project.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.file.File;
import whereQR.project.domain.qrcode.Qrcode;
import whereQR.project.domain.member.dto.MemberDetailDto;
import whereQR.project.jwt.MemberDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@DynamicUpdate // 업데이트하고자 하는 필드만 업데이트하기 위해서 추가
@Getter
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE id=?") // soft delete
@Where(clause = "deleted=false")
public class Member {

    @Id
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

    @Column(nullable = false)
    private Long kakaoId;

    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    @OneToOne(mappedBy = "profile",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private File profile = null;

    @OneToMany(mappedBy = "author",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Dashboard> dashboards = new ArrayList<>();

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



    public String updateToken(String refreshToken){
        this.refreshToken = refreshToken;
        return this.refreshToken;
    }

    public MemberDetails toMemberDetails(){
        return new MemberDetails(this, new SimpleGrantedAuthority(this.role.getName()));
    }

    public boolean equalId(Member currentMember) {
        return this.id.equals(currentMember.id);
    }

    public MemberDetailDto toMemberDetailDto(){
        return new MemberDetailDto(
                this.username,
                this.phoneNumber,
                this.qrcodeList,
                this.profile);
    }

}
