package whereQR.project.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whereQR.project.domain.file.File;
import whereQR.project.domain.file.FileService;
import whereQR.project.domain.member.dto.*;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.exception.CustomExceptions.ForbiddenException;
import whereQR.project.jwt.TokenInfo;
import whereQR.project.utils.MemberUtil;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final KakaoAuthService kakaoAuthService;
    private final AuthService authService;
    private final FileService fileService;

    @GetMapping("/kakao/token")
    public ResponseEntity kakaoToken(@RequestParam String code){
        TokenInfo tokenInfo = kakaoAuthService.getKakaoTokenInfoByCode(code);
        log.info("kakao token accessToken -> {}", tokenInfo.getAccessToken());
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(tokenInfo)
                .build();
    }

    @GetMapping("/kakao/me")
    public ResponseEntity kakaoMe(@RequestParam String accessToken){
        KakaoMemberResponseDto memberInfo = kakaoAuthService.getkakaoIdByAccessToken(accessToken);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(memberInfo)
                .build();
    }

    @PostMapping("/login/kakao")
    public ResponseEntity loginUserByKakao(@RequestParam Long kakaoId,HttpServletResponse response ){
        Member member = memberService.getMemberByKakaoIdAndRole(kakaoId,Role.USER);
        TokenInfo tokenInfo = authService.updateToken(member);
        authService.updateRefreshToken(member, tokenInfo.getRefreshToken() );
        authService.refreshTokenToCookie(tokenInfo.getRefreshToken(), response);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(tokenInfo)
                .build();
    }

    @PostMapping("/login/email")
    public ResponseEntity loginUserByEmail(@RequestBody MemberEmailLoginDto loginDto  , HttpServletResponse response ){
        Member member = memberService.getMemberByEmailAndRole(loginDto.getEmail(),Role.USER);
        memberService.validatePassword(loginDto.getPassword(), member.getPasswordHash());

        TokenInfo tokenInfo = authService.updateToken(member);
        authService.updateRefreshToken(member, tokenInfo.getRefreshToken() );
        authService.refreshTokenToCookie(tokenInfo.getRefreshToken(), response);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(tokenInfo)
                .build();
    }

    @PostMapping("/kakao/login/admin")
    public ResponseEntity loginAdmin(@RequestParam Long kakaoId, HttpServletResponse response){

        Member member = memberService.getMemberByKakaoIdAndRole(kakaoId,Role.ADMIN);
        TokenInfo tokenInfo = authService.updateToken(member);
        authService.updateRefreshToken(member, tokenInfo.getRefreshToken() );
        authService.refreshTokenToCookie(tokenInfo.getRefreshToken(), response);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(tokenInfo)
                .build();
    }

    @PostMapping("/create/kakao")
    public ResponseEntity createUserByKakao(@Valid @RequestBody KakaoSignupDto signupDto){

        if(memberService.existsMemberByKakaoIdAndRole(signupDto.getKakaoId(), Role.USER) == Boolean.TRUE){
            throw new BadRequestException("이미 존재하는 회원입니다.",this.getClass().toString());
        }

        Boolean isExistByPhoneNumber = memberService.existsMemberByPhoneNumberAndRole(signupDto.getPhoneNumber(), Role.USER);
        if(isExistByPhoneNumber){
            if(memberService.existsMemberByKakaoIdAndRole(signupDto.getKakaoId(), Role.USER) == Boolean.TRUE){
                throw new BadRequestException("이미 존재하는 회원입니다.",this.getClass().toString());
            }
            Member existMember = memberService.getMemberByPhoneNumberAndRole(signupDto.getPhoneNumber(), Role.USER);
            Member member = memberService.linkKakao(existMember, signupDto.getKakaoId());
            return ResponseEntity.builder()
                    .status(Status.SUCCESS)
                    .data(member.getId())
                    .build();
        }

        Member member = memberService.kakaoSignUp(signupDto, Role.USER);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(member.getId())
                .build();
    }

    @PostMapping("/create/email")
    public ResponseEntity createUserByEmail(@Valid @RequestBody MemberEmailSignupDto signupDto){

        if(memberService.existsMemberByEmailAndRole(signupDto.getEmail(), Role.USER) == Boolean.TRUE){
            throw new BadRequestException("이미 존재하는 회원입니다.",this.getClass().toString());
        }

        // 만약에 전화번호가 이미 존재한다면? 그 전화번호를 가지는 member의 email가 존재하지 않는다면 만들기. 존재한다면 이미 존재한다고 전송
        Boolean isExistByPhoneNumber = memberService.existsMemberByPhoneNumberAndRole(signupDto.getPhoneNumber(), Role.USER);
        if(isExistByPhoneNumber){
            if(memberService.existsMemberByEmailAndRole(signupDto.getEmail(), Role.USER) == Boolean.TRUE){
                throw new BadRequestException("이미 존재하는 회원입니다.",this.getClass().toString());
            }

            Member existMember = memberService.getMemberByPhoneNumberAndRole(signupDto.getPhoneNumber(), Role.USER);
            Member member = memberService.linkEmail(existMember, signupDto.getEmail() );
            return ResponseEntity.builder()
                    .status(Status.SUCCESS)
                    .data(member.getId())
                    .build();
        }


        Member member = memberService.emailSignUp(signupDto, Role.USER);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(member.getId())
                .build();
    }

    @PostMapping("/create/admin/kakao")
    public ResponseEntity createAdmin(@Valid @RequestBody KakaoSignupDto signupDto) {

        Boolean isExistByPhoneNumber = memberService.existsMemberByPhoneNumberAndRole(signupDto.getPhoneNumber(), Role.ADMIN);
        if (isExistByPhoneNumber) {
            if (memberService.existsMemberByKakaoIdAndRole(signupDto.getKakaoId(), Role.ADMIN) == Boolean.TRUE) {
                throw new BadRequestException("이미 존재하는 회원입니다.", this.getClass().toString());
            }
            Member existMember = memberService.getMemberByPhoneNumberAndRole(signupDto.getPhoneNumber(), Role.ADMIN);
            Member member = memberService.linkKakao(existMember, signupDto.getKakaoId());
            return ResponseEntity.builder()
                    .status(Status.SUCCESS)
                    .data(member.getId())
                    .build();
        }

        Member member = memberService.kakaoSignUp(signupDto, Role.ADMIN);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(member.getId())
                .build();
    }

    /**
     * refresh token을 cookie 활용하는걸로 변경
     */
    @PostMapping("/auth/refresh")
    public ResponseEntity refreshToken(HttpServletResponse response, @CookieValue(value = "refresh-token") String refreshToken ){

        Member member = memberService.getMemberByRefreshToken(refreshToken);
        TokenInfo tokenInfo = authService.updateToken(member);
        authService.updateRefreshToken(member, tokenInfo.getRefreshToken());
        authService.refreshTokenToCookie(tokenInfo.getRefreshToken(), response);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(tokenInfo)
                .build();
    }

    /**
     * frontend에서도 localstorage의 access token을 제거
     */
    @PostMapping("/logout")
    public ResponseEntity signOut(HttpServletResponse response){
        UUID memberId = MemberUtil.getMember().getId();
        Member member = memberService.getMemberById(memberId);

        authService.removeRefreshTokenInCookie(response);
        authService.removeRefreshToken(member);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(member.getId())
                .build();
    }

    @GetMapping("/detail")
    public ResponseEntity detail() {
        Member currentMember = MemberUtil.getMember();
        MemberDetailDto memberDetailDto = memberService.getMemberById(currentMember.getId()).toMemberDetailDto();

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(memberDetailDto)
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity me(){
        Member currentMember = MemberUtil.getMember();
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(currentMember.getId())
                .build();
    }

    @PostMapping("/profile")
    public ResponseEntity updateProfile( @RequestParam(value = "profile", required = false) MultipartFile profile){

        Member currentMember = MemberUtil.getMember();

        List<MultipartFile> list = new ArrayList<>();
        list.add(profile);
        List<File> files = fileService.uploadFile(currentMember, "/profile", list);
        
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(memberService.uploadProfile(currentMember, files.get(0).getId()))
                .build();
    }

    @PostMapping("/delete")
    public ResponseEntity deleteMember(@RequestParam UUID id){
        Member currentMember = MemberUtil.getMember();

        if(!currentMember.getId().equals(id)){
            throw new ForbiddenException("삭제 권한이 존재하지 않습니다.", this.getClass().toString());
        }

        memberService.deleteMemberById(currentMember);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(currentMember.getId())
                .build();
    }


}
