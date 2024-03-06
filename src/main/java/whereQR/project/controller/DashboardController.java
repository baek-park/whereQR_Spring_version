package whereQR.project.controller;

import lombok.RequiredArgsConstructor;
import whereQR.project.utils.response.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.dashboard.DashboardCreateRequest;
import whereQR.project.entity.dto.dashboard.DashboardUpdateRequest;
import whereQR.project.service.DashboardService;
import whereQR.project.utils.response.Status;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping("/create")
    public ResponseEntity createDashboard(@RequestBody DashboardCreateRequest request) {
        // 인증 정보를 바탕으로 Member 객체를 조회하는 로직 구현 필요
        Member author = null; // 인증 정보를 바탕으로 Member 객체를 설정해야함

        UUID dashboardId = dashboardService.createDashboard(request, author);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(dashboardId)
                .build();
    }
    @PostMapping("/update")
    public ResponseEntity updateDashboard(@RequestBody DashboardUpdateRequest request) {
        UUID dashboardId = dashboardService.updateDashboard(request);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(dashboardId)
                .build();
    }

    @DeleteMapping("/delete/{dashboardId}")
    public ResponseEntity deleteDashboard(@PathVariable UUID dashboardId) {
        dashboardService.deleteDashboard(dashboardId);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(dashboardId)
                .build();
    }

}
