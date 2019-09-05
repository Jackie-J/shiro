package vip.jackie.shiro.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vip.jackie.shiro.bean.request.LoginInfo;

@RestController
public class ShiroController {

    private static final Logger log = LoggerFactory.getLogger(ShiroController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody LoginInfo info, HttpServletRequest request) {
        String username = info.getAccount();
        String password = info.getPassword();
        boolean rememberMe = info.isRememberMe();
        String host = request.getRemoteHost();

        AuthenticationToken token = new UsernamePasswordToken(username, password, rememberMe, host);
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        } catch (UnknownAccountException | IncorrectCredentialsException uaeOrice) {
            uaeOrice.printStackTrace();
            return uaeOrice.getClass().getName();
        } catch (LockedAccountException le) {
            le.printStackTrace();
            return le.getClass().getName();
        } catch (ExcessiveAttemptsException eae) {
            eae.printStackTrace();
            return eae.getClass().getName();
        } catch (ExpiredCredentialsException ee) {
            ee.printStackTrace();
            return ee.getClass().getName();
        } catch (DisabledAccountException dae) {
            dae.printStackTrace();
            return dae.getClass().getName();
        } catch (AuthenticationException ae) {
            ae.printStackTrace();
            return ae.getClass().getName();
        }
        return "success";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.logout();
        } catch (SessionException ise) {
            log.error("logout error", ise);
        }
        return "logout";
    }

    @RequestMapping(value = "/success")
    public String success(HttpServletRequest request) {
        System.out.println("success");
        return "login success";
    }

    @RequestMapping(value = "/unauthorized")
    public String unauthorized(HttpServletRequest request) {
        System.out.println("unauthorized");
        return "unauthorized";
    }

    @RequestMapping(value = "/anon")
    public String anon(HttpServletRequest request) {
        System.out.println("anon");
        return "anon";
    }

    @RequestMapping(value = "/authc")
    public String authc(HttpServletRequest request) {
        System.out.println("authc");
        return "authc";
    }

}
