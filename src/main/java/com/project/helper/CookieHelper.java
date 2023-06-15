package com.project.helper;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieHelper {

    public void addCookieAttributes(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        String jwtToken = null;
        String userId = null;
        String role = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    jwtToken = cookie.getValue();
                } else if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                } else if (cookie.getName().equals("role")) {
                    role = cookie.getValue();
                }
            }
        }

        model.addAttribute("jwtToken", jwtToken);
        model.addAttribute("userId", userId);
        model.addAttribute("role", role);
    }
}
