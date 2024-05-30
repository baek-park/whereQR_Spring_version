package whereQR.project.domain.email;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomEmailRepositoryImpl implements CustomEmailRepository{

    private final JPAQueryFactory queryFactory;
    private QEmail emailAuth = QEmail.email1;

    @Override
    public Optional<Email> findByEmail(String email) {
        return Optional.ofNullable(
                queryFactory.selectFrom(emailAuth)
                .where(emailAuth.email.eq(email))
                .fetchFirst());
    }

    @Override
    public Boolean existByEmail(String email) {
         return queryFactory.select(emailAuth.count())
                .from(emailAuth)
                .where(emailAuth.email.eq(email))
                .fetchFirst() > 0;
    }
}
