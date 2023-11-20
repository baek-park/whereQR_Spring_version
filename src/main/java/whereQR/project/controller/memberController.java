package whereQR.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Member;
import whereQR.project.entity.Role;
import whereQR.project.entity.dto.member.*;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.service.AuthService;
import whereQR.project.service.KakaoAuthService;
import whereQR.project.service.MemberService;
import whereQR.project.utils.MemberUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class memberController {

    private final MemberService memberService;
    private final KakaoAuthService kakaoAuthService;
    private final AuthService authService;

    @GetMapping("/kakao/token")
    public ResponseEntity<TokenInfo> kakaoToken(@RequestParam String code){
        return ResponseEntity.ok(kakaoAuthService.getKakaoTokenInfoByCode(code));
    }

    @GetMapping("/kakao/me")
    public ResponseEntity<KakaoMemberInfo> kakaoMe(@RequestParam String accessToken){
        log.info("accessToken -> {}", accessToken);
        return ResponseEntity.ok(kakaoAuthService.getkakaoIdByAccessToken(accessToken));
    }

    @PostMapping("/kakao/login")
    public ResponseEntity<TokenInfo> login(@RequestBody KakaoLoginDto loginDto, HttpServletResponse response){
        Member member = memberService.getMemberByKakaoId(loginDto.getKakaoId());
        TokenInfo tokenInfo = authService.updateToken(member);
        authService.accessTokenToCookie(tokenInfo.getAccessToken(), response);

        return ResponseEntity.ok(tokenInfo);
    }

    @PostMapping("/create/member")
    public ResponseEntity<UUID> createMember(@RequestBody KakaoSignupDto signupDto){

        if(!signupDto.validationPhoneNumber()){
            throw new BadRequestException("전화번호가 유효하지 않습니다.",this.getClass().toString());
        }

        if( memberService.existsMemberByKakaoIdAndRole(signupDto.getKakaoId(), Role.USER) == Boolean.TRUE){
            throw new BadRequestException("이미 존재하는 회원입니다.",this.getClass().toString());
        }

        return ResponseEntity.ok(memberService.signUp(signupDto, Role.USER).getId());
    }

    @PostMapping("/create/admin")
    public ResponseEntity<UUID> createAdmin(@RequestBody KakaoSignupDto signupDto){

        if(!signupDto.validationPhoneNumber()){
            throw new BadRequestException("전화번호가 유효하지 않습니다.",this.getClass().toString());
        }

        if( memberService.existsMemberByKakaoIdAndRole(signupDto.getKakaoId(), Role.USER) == Boolean.TRUE){
            throw new BadRequestException("이미 존재하는 회원입니다.",this.getClass().toString());
        }

        return ResponseEntity.ok(memberService.signUp(signupDto, Role.USER).getId());
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<TokenInfo> refreshToken(@RequestParam String refreshToken, HttpServletResponse response){
        Member currentMember = MemberUtil.getMember();

        // 일치하지 않다면 -> exception
        if(!currentMember.getRefreshToken().equals(refreshToken)){
            throw new BadRequestException("유효하지 않은 token입니다.", this.getClass().toString());
        }

        // 일치한다면 -> updateToken(member)을 진행
        TokenInfo tokenInfo =  authService.updateToken(currentMember);
        authService.accessTokenToCookie(tokenInfo.getAccessToken(), response);

       return ResponseEntity.ok(tokenInfo);
    }

    @PostMapping
    public ResponseEntity<UUID> signOut(HttpServletResponse response){
        Member currentMember = MemberUtil.getMember();
        authService.removeAccessTokenInCookie(response);
        authService.removeRefreshToken(currentMember);

        return ResponseEntity.ok(currentMember.getId());
    }

    @GetMapping("/detail")
    public MemberDetailDto detail() {
        return MemberUtil.getMember().toMemberDetailDto();
    }
}
