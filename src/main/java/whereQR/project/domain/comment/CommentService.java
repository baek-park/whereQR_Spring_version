package whereQR.project.domain.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.domain.comment.dto.CommentCreateRequestDto;
import whereQR.project.domain.comment.dto.CommentInfoDto;
import whereQR.project.domain.comment.dto.CommentResponseDto;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.member.Member;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.utils.MemberUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public UUID createComment(CommentCreateRequestDto request, Dashboard dashboard, Member author) {
        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 부모 댓글 ID입니다."));
        }

        if (parent != null && parent.getParent() != null) {
            throw new IllegalArgumentException("댓글의 깊이는 2를 초과할 수 없습니다.");
        }

        Comment comment = new Comment(request.getContent(), author, dashboard, parent);
        Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }
    public List<CommentInfoDto> getCommentsByDashboardId(UUID dashboardId) {
        List<Comment> comments = commentRepository.findByDashboardIdAndParentIsNull(dashboardId);
        return comments.stream()
                .map(comment -> {
                    CommentInfoDto commentInfoDto = new CommentInfoDto(
                            comment.getId(),
                            comment.getContent(),
                            comment.getAuthor().getUsername(),
                            comment.getCreatedAt(),
                            comment.getStatus()
                    );
                    List<CommentResponseDto> childComments = getCommentsByParentId(comment.getId());
                    commentInfoDto.setChildComments(childComments);
                    return commentInfoDto;
                })
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> getCommentsByParentId(UUID parentId) {
        List<Comment> comments = commentRepository.findByParentId(parentId);
        return comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getAuthor().getUsername(),
                        comment.getCreatedAt(),
                        comment.getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 댓글 ID입니다."));
        Member currentMember = MemberUtil.getMember();
        if(!comment.isAuthor(currentMember)){
            throw new BadRequestException("접근 권한이 존재하지 않습니다.", this.getClass().toString());
        }
        comment.delete();
    }

    @Transactional
    public void updateComment(UUID commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 댓글 ID입니다."));
        Member currentMember = MemberUtil.getMember();
        if(!comment.isAuthor(currentMember)){
            throw new BadRequestException("접근 권한이 존재하지 않습니다.", this.getClass().toString());
        }
        comment.update(content);
    }
}