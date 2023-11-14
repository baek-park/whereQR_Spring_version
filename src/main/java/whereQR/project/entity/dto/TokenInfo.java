package whereQR.project.entity.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class TokenInfo {
    String accessToken;
    String refreshToken;

    public TokenInfo(){

    }

    public TokenInfo(JsonElement element){
        JsonObject jsonObject = element.getAsJsonObject();
        this.accessToken = jsonObject.get("access_token").toString();
        this.refreshToken = jsonObject.get("refresh_token").toString();
    }
}
