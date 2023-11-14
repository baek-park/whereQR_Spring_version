package whereQR.project.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.Role;
import whereQR.project.entity.dto.*;
import whereQR.project.entity.Member;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import whereQR.project.jwt.JwtTokenProvider;
import whereQR.project.repository.MemberRepository;
import whereQR.project.utils.GetUser;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private static String KakaoAuthRequestUrl = "https://kauth.kakao.com/oauth/token";
    private static String KakaoUserDataRequestUrl = "https://kapi.kakao.com/v2/user/me";
    private final MemberRepository memberRepository;

    public Boolean existsMemberByUsernameAndRole( String username, Role role ){
        return memberRepository.existsMemberByUsernameAndRole(username, role);
    }

    public Boolean existsMemberByKakaoIdAndRole( Long kakaoId, Role role ){
        return memberRepository.existsMemberByKakaoIdAndRole(kakaoId, role);
    }

    public Member getMemberByUsername(String username){
        return memberRepository.findMemberByUsername(username).orElseThrow(() -> new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }

    public Member getMemberById(UUID id){
        System.out.println(id);
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }


    public Member getMemberByKakaoId(Long kakaoId){
        return memberRepository.findMemberByKakaoId(kakaoId).orElseThrow(() -> new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }

    /**
     * code를 통해서 tokenInfo를 반환
     */

    public TokenInfo getTokenInfoByCode(String code){

        try{
            URL requestUrl = new URL(KakaoAuthRequestUrl);
            HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder builder = getUrlBuilder(code);
            writer.write(builder.toString());
            writer.flush();

            //예외처리
            int responseCode = conn.getResponseCode();
            if(responseCode != 200){
                log.error("response code is not 200");
                throw new BadRequestException("kakao api 요청이 유효하지 않습니다.", this.getClass().toString());
                // throw exception
            }

            // reponse 처리
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = getBufferedReaderResult(reader);
            reader.close();
            writer.close();

            log.info("token response => {}", result);

            //Gson library
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            return new TokenInfo(element);

        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("kakao api 요청이 유효하지 않습니다.", this.getClass().toString());
        }
    }

    public KakaoMemberInfo getkakaoIdByAccessToken(String accessToken){

        try{
            URL requestUrl = new URL(KakaoUserDataRequestUrl);
            HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestMethod("POST");

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            //예외처리
            int responseCode = conn.getResponseCode();
            if(responseCode != 200){
                // throw exception
                log.error("here");
                throw new BadRequestException("kakao token이 유효하지 않습니다.", this.getClass().toString());
            }

            // reponse 처리
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = getBufferedReaderResult(reader);
            reader.close();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonObject jsonObject = element.getAsJsonObject();
            Long kakaoId = jsonObject.get("id").getAsLong();
            JsonObject properties = jsonObject.getAsJsonObject("properties");
            String username = properties.get("nickname").getAsString();

            return new KakaoMemberInfo(kakaoId, username);

        }catch (IOException e){
            e.printStackTrace();
            throw new BadRequestException("kakao token이 유효하지 않습니다.", this.getClass().toString());
        }

    }

    // Todo : properties로 읽기
    private static StringBuilder getUrlBuilder(String code) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("grant_type=authorization_code");
        builder.append("&client_id=271b6b6b673acb0d6daca27769150dbc");
        builder.append("&redirect_uri=http://localhost:3000/login"); // 일단임의로 설정
        builder.append("&code=" + code);
        return builder;
    }

    private static String getBufferedReaderResult(BufferedReader reader) throws IOException {
        String line = "";
        String result = "";
        while((line = reader.readLine()) != null){
            result += line;
        }
        return result;
    }

    @Transactional
    public Member login(Long kakaoId, String refreshToken){
        Member member = getMemberByKakaoId(kakaoId);
        member.setRefreshToken(refreshToken);
        return member;
    }

    @Transactional
    public Member signUp(KakaoSignupDto signupDto, Role role){
        Member member = new Member(signupDto.getKakaoId(),signupDto.getUsername(),signupDto.getPhoneNumber(), role);
        memberRepository.save(member);
        return member;
    }

}
