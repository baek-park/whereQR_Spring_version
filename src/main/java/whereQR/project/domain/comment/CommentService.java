package whereQR.project.domain.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.domain.comment.dto.CommentCreateRequestDto;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.member.Member;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public UUID createComment(CommentCreateRequestDto request, Dashboard dashboard, Member author) {
        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parent comment ID."));
        }
        Comment comment = new Comment(request.getContent(), author, dashboard, parent);
        Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }
}