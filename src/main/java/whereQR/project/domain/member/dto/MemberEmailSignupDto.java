package whereQR.project.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class MemberEmailSignupDto {

    @NotEmpty(message = "이름 입력은 필수 입니다.")
    @Size(max = 10, message = "이름은 10자리를 넘어선 안 됩니다.")
    private String username;

    @NotEmpty(message = "이메일 입력은 필수 입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Pattern(regexp = "regexp=^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "비밀번호는 8글자~20자, 소문자 1개, 숫자 1개, 특수문자 1개 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Size(max = 20, message = "비밀번호는 최대 20자까지 입니다.")
    private String password;

    private String phoneNumber;
}
