package whereQR.project.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import whereQR.project.domain.chatroom.Chatroom;
import whereQR.project.domain.comment.Comment;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.file.File;
import whereQR.project.domain.message.Message;
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
public class Member {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 30)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "member",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JsonIgnore
    private List<Qrcode> qrcodeList = new ArrayList<>();

    @Column(unique = true)
    private String refreshToken;

    @Column(nullable = true)
    private Long kakaoId;

    @Column(nullable = true, length = 100)
    private String email;

    @Column
    private String passwordHash;

    @OneToOne(mappedBy = "profile",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private File profile = null;

    @OneToMany(mappedBy = "author",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<Dashboard> dashboards = new ArrayList<>();

    @OneToMany(mappedBy = "author",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "starter",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Chatroom> startChatrooms = new ArrayList<>();

    @OneToMany(mappedBy = "participant",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Chatroom> participantChatrooms = new ArrayList<>();

    @OneToMany(mappedBy = "sender",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    private List<Message> sendMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    private List<Message> receiveMessages = new ArrayList<>();

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

    public Member(String email, String username, String phoneNumber, Role role) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public Member(String email, String username, String phoneNumber, String passwordHash, Role role) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String updateToken(String refreshToken){
        this.refreshToken = refreshToken;
        return this.refreshToken;
    }

    public void updateProfile(File file, Member member){
        this.profile = file;
        member.profile = file;
    }

    public void linkKakao(Long kakaoId){
        this.kakaoId = kakaoId;
    }

    public void linkEmail(String email){
        this.email = email;
    }

    public MemberDetails toMemberDetails(){
        return new MemberDetails(this, new SimpleGrantedAuthority(this.role.getName()));
    }

    public boolean equalId(Member currentMember) {
        return this.id.equals(currentMember.id);
    }

    public MemberDetailDto toMemberDetailDto(){
        return new MemberDetailDto(
                this.id,
                this.username,
                this.phoneNumber,
                this.qrcodeList,
                this.profile);
    }

}
