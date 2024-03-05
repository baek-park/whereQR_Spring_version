package whereQR.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.dashboard.DashboardCreateRequest;
import whereQR.project.service.DashboardService;

import java.util.UUID;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID> createDashboard(@RequestBody DashboardCreateRequest request) {
        // 인증 정보를 바탕으로 Member 객체를 조회하는 로직 구현 필요
        Member author = null; // 인증 정보를 바탕으로 Member 객체를 설정해야함

        UUID dashboardId = dashboardService.createDashboard(request, author);
        return ResponseEntity.ok(dashboardId);
    }
}
