package whereQR.project.entity.dto.member;

import lombok.Data;

@Data
public class KakaoMemberInfo {
    Long kakaoId;
    String username;

    public KakaoMemberInfo(Long kakaoId, String username){
        this.kakaoId = kakaoId;
        this.username=  username;
    }
}
