package whereQR.project.entity.dto;

import lombok.Data;

@Data
public class MemberLoginDto {

    private String username;
    private String password;

    public void MemberLoginDto(String username, String password){
        this.username = username;
        this.password = password;
    }

}
