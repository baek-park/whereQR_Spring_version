package whereQR.project.entity.dto.qrcode;

import lombok.Data;
import whereQR.project.entity.*;

import javax.persistence.Embedded;

@Data
public class QrcodeUpdateDto {

    private String title;
    private String memo;

    private String phoneNumber;

    public QrcodeUpdateDto(){

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


