package whereQR.project.domain.dashboard;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import whereQR.project.config.QueryCounter;

import javax.persistence.EntityManagerFactory;

@SpringBootTest
@AutoConfigureMockMvc
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Test
    public void testGetDashboards() throws Exception {
        // QueryCounter 인스턴스 생성
        QueryCounter queryCounter = new QueryCounter(entityManagerFactory);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/dashboard")
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // 쿼리 발생 횟수 출력
        System.out.println("쿼리 발생 횟수: " + queryCounter.getQueryCount());

        // 응답 본문 가져오기
        String responseBody = mvcResult.getResponse().getContentAsString();
        System.out.println(responseBody);

        // 응답 본문을 JSON으로 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);

        // 데이터의 개수 확인
        if (responseJson.has("data")) {
            int dataSize = responseJson.get("data").size();
            System.out.println("반환된 데이터의 개수: " + dataSize);
        }

        // 클리어
        queryCounter.clear();
    }

}