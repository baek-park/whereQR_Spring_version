package whereQR.project.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class GetUser {

    public static String getUserName(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username;
    }
}
