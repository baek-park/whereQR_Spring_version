package whereQR.project.domain.member.dto;

import lombok.Data;
import whereQR.project.domain.file.File;
import whereQR.project.domain.file.dto.FileResponseDto;
import whereQR.project.domain.qrcode.Qrcode;
import whereQR.project.domain.qrcode.dto.QrcodeResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberDetailDto {

    private String username;
    private String phoneNumber;
    private List<QrcodeResponseDto> qrcodes;
    private FileResponseDto fileResponseDto;

    public MemberDetailDto(String username, String phoneNumber, List<Qrcode> qrcodes, File file){
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.qrcodes = qrcodes.stream().map(it -> it.toQrcodeResponseDto()).collect(Collectors.toList());
        this.fileResponseDto = file == null? null : file.toFileResponseDto();
    }
}
