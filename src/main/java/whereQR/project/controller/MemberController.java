package whereQR.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Member;
import whereQR.project.entity.Role;
import whereQR.project.entity.dto.member.*;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.jwt.TokenInfo;
import whereQR.project.service.AuthService;
import whereQR.project.service.KakaoAuthService;
import whereQR.project.service.MemberService;
import whereQR.project.utils.MemberUtil;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final KakaoAuthService kakaoAuthService;
    private final AuthService authService;

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
        KakaoMemberInfo memberInfo = kakaoAuthService.getkakaoIdByAccessToken(accessToken);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(memberInfo)
                .build();
    }

    @PostMapping("/kakao/login")
    public ResponseEntity loginUser(@RequestParam Long kakaoId,HttpServletResponse response ){
        Member member = memberService.getMemberByKakaoIdAndRole(kakaoId,Role.USER);
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

    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody KakaoSignupDto signupDto){

        if(!signupDto.validationPhoneNumber()){
            throw new BadRequestException("전화번호가 유효하지 않습니다.",this.getClass().toString());
        }

        if( memberService.existsMemberByKakaoIdAndRole(signupDto.getKakaoId(), Role.USER) == Boolean.TRUE){
            throw new BadRequestException("이미 존재하는 회원입니다.",this.getClass().toString());
        }

        Member member = memberService.signUp(signupDto, Role.USER);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(member.getId())
                .build();
    }

    @PostMapping("/create/admin")
    public ResponseEntity createAdmin(@RequestBody KakaoSignupDto signupDto){

        if(!signupDto.validationPhoneNumber()){
            throw new BadRequestException("전화번호가 유효하지 않습니다.",this.getClass().toString());
        }

        if( memberService.existsMemberByKakaoIdAndRole(signupDto.getKakaoId(), Role.ADMIN) == Boolean.TRUE){
            throw new BadRequestException("이미 존재하는 회원입니다.",this.getClass().toString());
        }

        Member member = memberService.signUp(signupDto, Role.ADMIN);
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
}
