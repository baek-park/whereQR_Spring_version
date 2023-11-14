package whereQR.project.entity.dto;

import lombok.Data;

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

    public boolean validationPhoneNumber(){
        String PhoneNumber = this.phoneNumber;
        if(PhoneNumber.length() != 11){
            return false;
        }
        for(char c : phoneNumber.toCharArray()){
            if(!Character.isDigit(c)){
                return false;
            }
        }
        String firstThreeDigits = phoneNumber.substring(0, 3);
        if (!(firstThreeDigits.equals("010") || firstThreeDigits.equals("011"))) {
            return false;
        }
        return true;
    }
}
