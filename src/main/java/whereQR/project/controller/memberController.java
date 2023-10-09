package whereQR.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.dto.MemberDetailDto;
import whereQR.project.entity.dto.MemberSignupDto;
import whereQR.project.entity.dto.MemberLoginDto;
import whereQR.project.entity.dto.TokenInfo;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class memberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public TokenInfo login(@RequestBody MemberLoginDto memberLoginDto){
        TokenInfo tokenInfo = memberService.login(memberLoginDto);
        return tokenInfo;
    }

    @PostMapping("/signup")
    public MemberSignupDto signUp(@RequestBody MemberSignupDto memberSignupDto){

        List<String> roles = memberSignupDto.getRoles();
        String role = roles.get(0);
        if( memberService.existsMemberByUsernameAndRoles(memberSignupDto.getUsername(), role) == Boolean.TRUE){
            throw new BadRequestException("이미 존재하는 회원입니다.",this.getClass().toString());
        }

        return memberService.signUp(memberSignupDto);
    }

    @GetMapping("/detail")
    public MemberDetailDto detail(){
        MemberDetailDto memberDetailDto = memberService.detail();
        return memberDetailDto;
    }
}
