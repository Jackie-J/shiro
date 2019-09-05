package vip.jackie.shiro.config.web;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationFilter.class);

    /**
     * 当请求的方法没有登陆，被拦截了
     * 表示当访问拒绝时是否已经处理了
     * 如果返回true 表示需要继续处理
     * 如果返回false表示该拦截器实例已经处理了，将直接返回即可
     */
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // if login request, judge by url
        if (isLoginRequest(request, response)) {
            if (log.isTraceEnabled()) {
                log.trace("request url: [{}]", WebUtils.getPathWithinApplication(WebUtils.toHttp(request)));
            }
            return true;
        } else {
            WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

}
