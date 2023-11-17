package whereQR.project.entity.dto.member;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import whereQR.project.entity.Member;

import java.io.Serializable;

@Data
public class MemberDetails implements Serializable {

    private Member member;
    private SimpleGrantedAuthority role;

    public MemberDetails(Member member, SimpleGrantedAuthority role){
            this.member = member;
            this.role = role;
    }

    private static final long serialVersionUID = 2L; // 일관성 보장

}
