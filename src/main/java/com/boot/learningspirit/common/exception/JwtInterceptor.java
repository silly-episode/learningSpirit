package com.boot.learningspirit.common.exception;

import com.boot.learningspirit.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/2/20 10:22
 * @FileName: JwtInterceptor
 * @Description:
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        //获取请求头token
        String token = request.getHeader("Authorization");
        if (token == null) {
            return false;
        }
        try {
            jwtUtil.verifyToken(token); //校验token
            return true; //放行请求
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new TokenException("token过期！");
        } catch (MalformedJwtException e) {
            e.printStackTrace();
            throw new TokenException("token格式错误！");
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new TokenException("无效签名！");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new TokenException("非法请求！");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TokenException("token无效！");
        }
    }

}
