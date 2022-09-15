package com.foodlab.foodReservation.auth.token;

import com.foodlab.foodReservation.auth.config.UserInfo;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = ((HttpServletRequest)request).getHeader("Authorization");

        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            Claims claims = jwtProvider.getJwtContents(accessToken.substring(7, accessToken.length()));
            UserInfo userInfo = UserInfo.builder()
                    .id((long)(int) claims.get("id"))
                    .email((String) claims.get("email"))
                    .build();

            Authentication auth = getAuthentication(userInfo);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(UserInfo userInfo) {
        return new UsernamePasswordAuthenticationToken(userInfo, "", Arrays.asList(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
    }

}
