package whereQR.project.jwt;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import whereQR.project.entity.Member;

import java.io.Serializable;

@Data
public class MemberDetails implements Serializable {

    private static final long serialVersionUID = 2L; // 직렬화, 역직렬화 과정에서의 일관성 보장
    private Member member;
    private SimpleGrantedAuthority role;

    public MemberDetails(Member member, SimpleGrantedAuthority role){
            this.member = member;
            this.role = role;
    }

}
