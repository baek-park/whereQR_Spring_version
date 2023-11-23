package whereQR.project.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.Serializable;
import java.util.Collections;


public class CustomAuthenticationToken extends AbstractAuthenticationToken implements Serializable {

    private static final long serialVersionUID = 1L; // 직렬화, 역직렬화 과정에서의 일관성 보장

    private final MemberDetails principal;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param principal the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public CustomAuthenticationToken(MemberDetails principal, WebAuthenticationDetails details) {
        super(Collections.singleton(principal.getRole()));
        this.principal = principal;
        setAuthenticated(true);
        setDetails(details);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}