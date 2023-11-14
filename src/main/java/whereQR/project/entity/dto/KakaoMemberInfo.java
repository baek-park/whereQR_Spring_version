package whereQR.project.entity.dto;

import lombok.Data;

@Data
public class KakaoMemberInfo {
    Long kakaoId;
    String username;

    public KakaoMemberInfo(){

    }

    public KakaoMemberInfo(Long kakaoId, String username){
        this.kakaoId = kakaoId;
        this.username=  username;
    }
}
