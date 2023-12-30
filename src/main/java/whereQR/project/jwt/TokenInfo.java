package whereQR.project.jwt;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class TokenInfo {
    String accessToken;
    String refreshToken;

    // kakao token
    public TokenInfo(JsonElement element){
        JsonObject jsonObject = element.getAsJsonObject();
        this.accessToken = jsonObject.get("access_token").getAsString();
        this.refreshToken = jsonObject.get("refresh_token").getAsString();
    }

    // member token
    public TokenInfo(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
