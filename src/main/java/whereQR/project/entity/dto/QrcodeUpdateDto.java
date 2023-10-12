package whereQR.project.entity.dto;

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

}


