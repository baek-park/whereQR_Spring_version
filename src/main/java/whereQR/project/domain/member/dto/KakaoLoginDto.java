package whereQR.project.domain.member.dto;

import lombok.Data;

@Data
public class KakaoLoginDto {

    private Long kakaoId;

    public void KakaoLoginDto(KakaoLoginDto loginDto) {
        this.kakaoId= loginDto.kakaoId;
    }
}
