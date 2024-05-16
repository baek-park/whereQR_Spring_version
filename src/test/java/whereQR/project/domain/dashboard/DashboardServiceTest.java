package whereQR.project.domain.dashboard;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import whereQR.project.domain.favorite.FavoriteRepository;

@SpringBootTest
@ActiveProfiles("test")
public class DashboardServiceTest {

    @Autowired
    private DashboardService dashboardService;

    @MockBean
    private DashboardRepository dashboardRepository;

    @MockBean
    private FavoriteRepository favoriteRepository;

}