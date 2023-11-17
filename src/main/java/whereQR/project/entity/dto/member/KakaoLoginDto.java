package whereQR.project.entity.dto.member;

import lombok.Data;

@Data
public class KakaoLoginDto {

    private Long kakaoId;

    public void KakaoLoginDto(KakaoLoginDto loginDto) {
        this.kakaoId= loginDto.kakaoId;
    }
}
