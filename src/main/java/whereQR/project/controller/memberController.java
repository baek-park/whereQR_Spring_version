package whereQR.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.dto.MemberDetailDto;
import whereQR.project.entity.dto.MemberSignupDto;
import whereQR.project.entity.dto.MemberLoginDto;
import whereQR.project.entity.dto.TokenInfo;
import whereQR.project.service.MemberService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class memberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public TokenInfo login(@RequestBody MemberLoginDto memberLoginDto){
        TokenInfo tokenInfo = memberService.login(memberLoginDto);
        return tokenInfo;
    }

    @PostMapping("/signup")
    public MemberSignupDto signUp(@RequestBody MemberSignupDto memberSignupDto){
        return memberService.signUp(memberSignupDto);
    }

    @GetMapping("/detail")
    public MemberDetailDto detail(){
        MemberDetailDto memberDetailDto = memberService.detail();
        return memberDetailDto;
    }
}
