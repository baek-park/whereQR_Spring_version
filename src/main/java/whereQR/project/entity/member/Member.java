package whereQR.project.entity.member;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import whereQR.project.entity.Address;
import whereQR.project.entity.PhoneNumber;
import whereQR.project.entity.qrcode.Qrcode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name="member")
public class Member implements UserDetails {

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
    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
