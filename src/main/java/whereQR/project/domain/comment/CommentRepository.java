package whereQR.project.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByDashboardIdAndParentIsNull(UUID dashboardId);
    List<Comment> findByParentId(UUID parentId);
}