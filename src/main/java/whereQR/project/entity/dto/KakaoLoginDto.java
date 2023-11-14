package whereQR.project.entity.dto;

import lombok.Data;

@Data
public class KakaoLoginDto {

    private Long kakaoId;
    private String refreshToken;

    public void KakaoLoginDto(KakaoLoginDto loginDto) {
        this.kakaoId= loginDto.kakaoId;
        this.refreshToken = loginDto.refreshToken;
    }
}
