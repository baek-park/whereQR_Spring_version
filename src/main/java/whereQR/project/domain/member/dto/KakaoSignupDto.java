package whereQR.project.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class KakaoSignupDto {

    private Long kakaoId;
    private String username;

    private String phoneNumber;

    public void KakaoSignupDto(KakaoSignupDto signupDto){
        this.kakaoId = signupDto.kakaoId;
        this.username = signupDto.username;
        this.phoneNumber = signupDto.phoneNumber;
    }

}
