package whereQR.project.repository.favorite;

import whereQR.project.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface FavoriteRepository extends JpaRepository<Favorite, UUID>{
}
