package whereQR.project.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import whereQR.project.domain.member.Member;
import whereQR.project.jwt.MemberDetails;

public class MemberUtil {

    public static Member getMember() {
        MemberDetails memberDetails = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return memberDetails.getMember();
    }
}
