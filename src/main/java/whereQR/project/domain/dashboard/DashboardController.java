package whereQR.project.domain.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import whereQR.project.domain.comment.CommentService;
import whereQR.project.domain.comment.dto.CommentInfoDto;
import whereQR.project.domain.dashboard.dto.*;
import whereQR.project.domain.member.Member;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.jwt.MemberDetails;
import whereQR.project.utils.response.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whereQR.project.utils.response.Status;
import whereQR.project.utils.MemberUtil;
import whereQR.project.domain.favorite.FavoriteService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final FavoriteService favoriteService;
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity createDashboard(@RequestBody DashboardCreateRequestDto request) {
        Member author = MemberUtil.getMember();

        UUID dashboardId = dashboardService.createDashboard(request, author);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(dashboardId)
                .build();
    }

    @PostMapping("/update")
    public ResponseEntity updateDashboard(@RequestBody DashboardUpdateRequestDto request) {

        Member currentMember = MemberUtil.getMember();
        Dashboard dashboard = dashboardService.getDashboardById(request.getDashboardId());

        if(!dashboard.isAuthor(currentMember)){
            throw new BadRequestException("접근 권한이 존재하지 않습니다.", this.getClass().toString());
        }

        UUID dashboardId = dashboardService.updateDashboard(request);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(dashboardId)
                .build();
    }

    @GetMapping
    public ResponseEntity getDashboards(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "lostedDistrict", required = false) String lostedDistrict,
            @RequestParam(value = "lostedType", required = false) String lostedType,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        DashboardSearchCriteria criteria = new DashboardSearchCriteria(search, lostedDistrict, lostedType, startDate, endDate);
        DashboardPageResponseDto pageResponseDto = dashboardService.getDashboards(offset, limit, criteria);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(pageResponseDto)
                .build();
    }
    @GetMapping("/detail")
    public ResponseEntity getDashboard(@RequestParam UUID dashboardId) {
        Dashboard dashboard = dashboardService.getDashboardById(dashboardId);

        Member currentMember = null;
        try {
            currentMember = MemberUtil.getMember();
        } catch (Exception e) {
            ;
        }

        boolean isFavorite = false;
        if (currentMember != null) {
            UUID favoriteId = favoriteService.getFavoriteId(dashboardId, currentMember);
            isFavorite = favoriteId != null;
        }

        long favoriteCount = favoriteService.getFavoriteCountByDashboardId(dashboardId).getCount();

        List<CommentInfoDto> comments = commentService.getCommentsByDashboardIdAndMember(dashboardId, currentMember);

        DashboardDetailResponseDto responseDto = dashboard.toDashboardDetailResponseDto(isFavorite, favoriteCount, comments);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(responseDto)
                .build();
    }

    @PostMapping("/delete")
    public ResponseEntity deleteDashboard(@RequestBody DashboardDeleteRequestDto request) {
        UUID dashboardId = request.getDashboardId();

        Member currentMember = MemberUtil.getMember();
        Dashboard dashboard = dashboardService.getDashboardById(dashboardId);

        if(!dashboard.isAuthor(currentMember)){
            throw new BadRequestException("접근 권한이 존재하지 않습니다.", this.getClass().toString());
        }

        dashboardService.deleteDashboard(dashboardId);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(dashboardId)
                .build();
    }

    @GetMapping("/my")
    public ResponseEntity myDashboards(  @RequestParam(value = "offset", defaultValue = "0") int offset,
                                         @RequestParam(value = "limit", defaultValue = "10") int limit){
        Member currentMember = MemberUtil.getMember();

        DashboardPageResponseDto pageResponseDto = dashboardService.getDashboardsByMemberId(offset, limit, currentMember.getId());
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(pageResponseDto)
                .build();
    }

    @GetMapping("/my/favorite")
    public ResponseEntity myDashboardsByFavorite( @RequestParam(value = "offset", defaultValue = "0") int offset,
                                                  @RequestParam(value = "limit", defaultValue = "10") int limit){

        Member member = MemberUtil.getMember();

        DashboardPageResponseDto dashboards = dashboardService.getFavoriteDashboardsByByMember(offset, limit, member);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(dashboards)
                .build();

    }

}
