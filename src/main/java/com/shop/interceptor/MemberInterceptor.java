package com.shop.interceptor;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemberInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(MemberInterceptor.class);

    @Autowired
    private MemberRepository memberRepository;

    @Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
        try {
            if(modelAndView != null) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Object principal = authentication.getPrincipal();

                String email = null;

                if (principal instanceof User) {
                    email = ((User) principal).getUsername();
                } else if (principal instanceof String) {
                    email = (String) principal;
                }

                if (email != null && !email.equals("anonymousUser")) {
                    Member member = memberRepository.findByEmail(email);

                    if (member != null) {
                        ModelMap modelMap = modelAndView.getModelMap();
                        modelMap.addAttribute("memberName", member.getName());
                        modelMap.addAttribute("memberPoint", member.getPoint());
                    }
                }
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
	}

}
