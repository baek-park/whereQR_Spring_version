package whereQR.project.domain.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import whereQR.project.domain.comment.dto.CommentCreateRequestDto;
import whereQR.project.domain.comment.dto.CommentUpdateRequestDto;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.dashboard.DashboardService;
import whereQR.project.domain.member.Member;
import whereQR.project.utils.MemberUtil;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final DashboardService dashboardService;

    @PostMapping("/create")
    public ResponseEntity createComment(@RequestBody CommentCreateRequestDto request) {
        UUID dashboardId = request.getDashboardId();
        Dashboard dashboard = dashboardService.getDashboardById(dashboardId);

        Member author = MemberUtil.getMember();

        UUID commentId = commentService.createComment(request, dashboard, author);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(commentId)
                .build();
    }

    @PostMapping("/delete")
    public ResponseEntity deleteComment(@RequestBody Map<String, UUID> request) {
        UUID commentId = request.get("commentId");
        commentService.deleteComment(commentId);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .build();
    }

    @PostMapping("/update")
    public ResponseEntity updateComment(@RequestBody CommentUpdateRequestDto request) {
        commentService.updateComment(request.getCommentId(), request.getContent());
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .build();
    }
}