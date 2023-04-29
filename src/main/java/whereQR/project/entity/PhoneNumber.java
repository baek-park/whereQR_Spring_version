package whereQR.project.entity;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
public class PhoneNumber {

    private String areaCode;
    private String localNumber;

    public PhoneNumber(){

    }

    public PhoneNumber(String areaCode, String localNumber){
        this.areaCode = areaCode;
        this.localNumber = localNumber;
    }
}
