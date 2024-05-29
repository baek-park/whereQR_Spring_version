package whereQR.project.domain.email;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomEmailRepository {

    public Optional<Email> findByEmail(String email);

    public Boolean existByEmail(String email);

}
