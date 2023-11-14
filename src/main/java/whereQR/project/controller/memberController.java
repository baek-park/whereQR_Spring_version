package whereQR.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Member;
import whereQR.project.entity.Role;
import whereQR.project.entity.dto.*;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.service.MemberService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class memberController {

    private final MemberService memberService;

    /**
     * kakao auth code를 통해 kakao token info를 반환
     */
    @GetMapping("/kakao/token")
    public TokenInfo kakaoToken(@RequestParam String code){
        return memberService.getTokenInfoByCode(code);
    }

    @GetMapping("/kakao/me")
    public KakaoMemberInfo me(@RequestParam String accessToken){
        log.info("accessToken -> {}", accessToken);
        return memberService.getkakaoIdByAccessToken(accessToken);
    }
    /**
     * tokenInfo를 사용해서 login 성공시 UUID 반환
     * Todo : refresh token 저장 및 cookie setting
     */

    @PostMapping("/kakao/login")
    public UUID login(@RequestBody KakaoLoginDto loginDto){
        //refresh token 저장
        return memberService.login(loginDto.getKakaoId(), loginDto.getRefreshToken()).getId();
    }

    /**
     * Todo : createMember로 변경 -> role을 user로 설정
     */
    @PostMapping("/create")
    public UUID createMember(@RequestBody KakaoSignupDto signupDto){

        //validation
        if(!signupDto.validationPhoneNumber()){
            throw new BadRequestException("전화번호가 유효하지 않습니다.",this.getClass().toString());
        }

        if( memberService.existsMemberByKakaoIdAndRole(signupDto.getKakaoId(), Role.USER) == Boolean.TRUE){
            throw new BadRequestException("이미 존재하는 회원입니다.",this.getClass().toString());
        }

        return memberService.signUp(signupDto, Role.USER).getId();
    }

    /**
     * createAdmin
     */
    @PostMapping("/admin/create")
    public UUID createAdmin(@RequestBody KakaoSignupDto signupDto){

        //validation
        if(!signupDto.validationPhoneNumber()){
            throw new BadRequestException("전화번호가 유효하지 않습니다.",this.getClass().toString());
        }

        if( memberService.existsMemberByKakaoIdAndRole(signupDto.getKakaoId(), Role.USER) == Boolean.TRUE){
            throw new BadRequestException("이미 존재하는 회원입니다.",this.getClass().toString());
        }

        return memberService.signUp(signupDto, Role.USER).getId();
    }


    /**
     * Todo : filter chain의 context user 활용
     */
//    @GetMapping("/detail")
//    public MemberDetailDto detail(){
//        //Todo :[exception] 본인 계정일때만 가능하도록함-> fileter chain을 이용할 예정
//        //return memberService.getMemberById(id).toMemberDetailDto();
//
//    }
}
