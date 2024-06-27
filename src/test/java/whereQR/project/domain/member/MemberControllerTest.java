package whereQR.project.domain.member;

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
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void testLoginUserByKakao() throws Exception {
        // QueryCounter 인스턴스 생성
        QueryCounter queryCounter = new QueryCounter(entityManagerFactory);

        // 카카오 아이디로 로그인 API 호출
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/member/login/kakao")
                        .param("kakaoId", "3162168932"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // 응답 본문 가져오기
        String responseBody = mvcResult.getResponse().getContentAsString();
        System.out.println("Response body: " + responseBody);

        // 쿼리 발생 횟수 출력
        System.out.println("쿼리 발생 횟수: " + queryCounter.getQueryCount());
        System.out.println("쿼리 실행 시간: " + queryCounter.getQueryExecutionTime());

        // 클리어
        queryCounter.clear();
    }



}