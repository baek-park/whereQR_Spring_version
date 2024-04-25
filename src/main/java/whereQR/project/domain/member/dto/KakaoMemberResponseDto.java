package whereQR.project.domain.member.dto;

import lombok.Data;

@Data
public class KakaoMemberResponseDto {
    Long kakaoId;
    String username;

    public KakaoMemberResponseDto(Long kakaoId, String username){
        this.kakaoId = kakaoId;
        this.username=  username;
    }
}
