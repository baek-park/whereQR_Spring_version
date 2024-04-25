package whereQR.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import whereQR.project.entity.Dashboard;
import whereQR.project.entity.dto.dashboard.DashboardPageResponseDto;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.utils.response.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.dashboard.DashboardCreateRequest;
import whereQR.project.entity.dto.dashboard.DashboardUpdateRequest;
import whereQR.project.service.DashboardService;
import whereQR.project.utils.response.Status;
import whereQR.project.utils.MemberUtil;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping("/create")
    public ResponseEntity createDashboard(@RequestBody DashboardCreateRequest request) {
        Member author = MemberUtil.getMember();

        UUID dashboardId = dashboardService.createDashboard(request, author);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(dashboardId)
                .build();
    }

    @GetMapping
    public ResponseEntity getDashboards(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "search", required = false) String search) {

        DashboardPageResponseDto pageResponseDto = dashboardService.getDashboards(offset, limit, search);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(pageResponseDto)
                .build();
    }

    @GetMapping("/detail")
    public ResponseEntity getDashboard(@RequestParam UUID dashboardId) {
        Dashboard dashboard = dashboardService.getDashboardById(dashboardId);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(dashboard.toDashboardResponseDto())
                .build();
    }


    @PostMapping("/update")
    public ResponseEntity updateDashboard(@RequestBody DashboardUpdateRequest request) {

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

    @DeleteMapping("/delete/{dashboardId}")
    public ResponseEntity deleteDashboard(@PathVariable UUID dashboardId) {

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

}
