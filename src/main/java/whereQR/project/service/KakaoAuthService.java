package whereQR.project.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import whereQR.project.entity.dto.member.KakaoMemberInfo;
import whereQR.project.jwt.TokenInfo;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.properties.KakaoLoginProperties;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoLoginProperties kakaoLoginProperties ;


    /**
     * code를 통해서 tokenInfo를 반환
     */

    public TokenInfo getKakaoTokenInfoByCode(String code){

        try{
            URL requestUrl = new URL(kakaoLoginProperties.getTokenRequestUrl());
            HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder builder = getUrlBuilder(code, kakaoLoginProperties);
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
            URL requestUrl = new URL(kakaoLoginProperties.getUserDataRequestUrl());
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
    private static StringBuilder getUrlBuilder(String code, KakaoLoginProperties kakaoLoginProperties) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("grant_type="+ kakaoLoginProperties.getGrantType());
        builder.append("&client_id="+ kakaoLoginProperties.getClientId());
        builder.append("&redirect_uri="+ kakaoLoginProperties.getRedirectUrl());
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
}
