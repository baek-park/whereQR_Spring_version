package whereQR.project.domain.email;

import lombok.Getter;
import whereQR.project.utils.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
public class Email extends EntityBase {

    @Column(unique = true)
    private String email;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    LocalDateTime expiresAt;

    public Email(){

    }

    public Email(String email, String code, LocalDateTime expiresAt) {
        this.email = email;
        this.code = code;
        this.expiresAt = expiresAt;
    }
    public void updateAuth(String code, LocalDateTime expiresAt){
        this.code= code;
        this.expiresAt = expiresAt;
    }

    public Boolean isExpiredCode(){
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}
