package vip.jackie.shiro.config.web;

import java.util.Arrays;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import vip.jackie.shiro.config.ShiroAutoConfiguration;
import vip.jackie.shiro.config.credential.CustomHashedCredentialsMatcher;
import vip.jackie.shiro.config.realm.CustomRealm;
import vip.jackie.shiro.config.realm.UserService;

/**
 * @since 1.4.0
 */
@Configuration
@AutoConfigureBefore(ShiroAutoConfiguration.class)
@ConditionalOnProperty(name = "shiro.web.enabled", matchIfMissing = true)
public class ShiroWebAutoConfiguration extends AbstractShiroWebConfiguration {

    @Value("#{ @environment['shiro.sessionValidationScheduler.interval'] ?: 1800000 }")
    protected int sessionValidationInterval;

    @Value("#{ @environment['shiro.sessionManager.globalSessionTimeout'] ?: 1800000 }")
    protected int globalSessionTimeout;

    @Value("#{ @environment['shiro.sessionManager.sessionValidationSchedulerEnabled'] ?: true }")
    protected boolean sessionValidationSchedulerEnabled;

    @Value("#{ @environment['shiro.logoutUrl'] ?: '/logout' }")
    protected String logoutUrl;

    @Value("#{ @environment['shiro.anon.paths'] ?: null }")
    protected String[] paths;

    @Bean
    @Override
    protected AuthenticationStrategy authenticationStrategy() {
        return super.authenticationStrategy();
    }

    @Bean
    @Override
    protected Authenticator authenticator() {
        return super.authenticator();
    }

    @Bean
    @Override
    protected Authorizer authorizer() {
        return super.authorizer();
    }

    @Bean
    @Override
    protected SubjectDAO subjectDAO() {
        return super.subjectDAO();
    }

    @Bean
    @Override
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        return super.sessionStorageEvaluator();
    }

    @Bean
    @Override
    protected SubjectFactory subjectFactory() {
        return super.subjectFactory();
    }

    @Bean
    @Override
    protected SessionFactory sessionFactory() {
        return super.sessionFactory();
    }

    @Bean
    @Override
    protected SessionDAO sessionDAO() {
        return super.sessionDAO();
    }

    @Bean
    @Override
    protected DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
        webSessionManager.setSessionIdCookieEnabled(sessionIdCookieEnabled);
        webSessionManager.setSessionIdUrlRewritingEnabled(sessionIdUrlRewritingEnabled);
        webSessionManager.setSessionIdCookie(sessionCookieTemplate());

        webSessionManager.setSessionFactory(sessionFactory());
        webSessionManager.setSessionDAO(sessionDAO());
        webSessionManager.setDeleteInvalidSessions(sessionManagerDeleteInvalidSessions);

        webSessionManager.setGlobalSessionTimeout(globalSessionTimeout);
        webSessionManager.setDeleteInvalidSessions(sessionManagerDeleteInvalidSessions);
        webSessionManager.setSessionValidationSchedulerEnabled(sessionValidationSchedulerEnabled);
        webSessionManager.setCacheManager(cacheManager());
        return webSessionManager;
    }

    /**
     * 如果有多个Realm实现，则会出现用户登录的时候，无法捕获到原来的异常
     * 捕获到的都是AuthenticationException
     * @param realm
     * @return
     */
    @Bean
    protected SessionsSecurityManager securityManager(Realm realm) {
        return super.securityManager(Arrays.asList(realm));
    }

    @Bean
    @Override
    protected Cookie sessionCookieTemplate() {
        return super.sessionCookieTemplate();
    }

    @Bean
    @Override
    protected RememberMeManager rememberMeManager() {
        return super.rememberMeManager();
    }

    @Bean
    @Override
    protected Cookie rememberMeCookieTemplate() {
        return super.rememberMeCookieTemplate();
    }

    /**
     * 定义需要拦截和不需要拦截的路径
     */
    @Bean
    @Override
    protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
        // 拦截的顺序要安排好，并且不能使用HashMap，否则会打乱顺序
        // 默认以第一个找到的拦截器进行拦截
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        if (paths != null && paths.length > 0) {
            for (String path : paths) {
                chainDefinition.addPathDefinition(path, "anon");
            }
        }
        chainDefinition.addPathDefinition("/**", "authc");
        chainDefinition.addPathDefinition(logoutUrl, "logout");
        return chainDefinition;
    }

    /**
     * 缓存处理，包括session，用户，权限
     * @return
     */
    @Bean
    protected CacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }

    /**
     * 定时任务，用来删除过期的session
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "shiro.sessionManager", name = "sessionValidationSchedulerEnabled", havingValue = "true")
    protected SessionValidationScheduler sessionValidationScheduler() {
        QuartzSessionValidationScheduler sessionValidationScheduler = new QuartzSessionValidationScheduler();
        sessionValidationScheduler.setSessionValidationInterval(sessionValidationInterval);
        DefaultWebSessionManager sessionManager = sessionManager();
        sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
        sessionValidationScheduler.setSessionManager(sessionManager);
        return sessionValidationScheduler;
    }

    /**
     * 登陆时获取用户信息和用户权限
     * @param userService
     * @return
     */
    @Bean
    @Primary
    protected Realm customRealm(UserService userService) {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setUserService(userService);
        customRealm.setCredentialsMatcher(credentialsMatcher());
        customRealm.setCachingEnabled(true);
        customRealm.setAuthenticationCachingEnabled(true);
        customRealm.setAuthenticationCacheName("authenticationCache");
        customRealm.setAuthorizationCachingEnabled(true);
        customRealm.setAuthorizationCacheName("authorizationCache");
        customRealm.setCacheManager(cacheManager());
        return customRealm;
    }

    /**
     * 在登录时对密码进行加密
     * @return
     */
    @Bean
    protected CredentialsMatcher credentialsMatcher() {
        CustomHashedCredentialsMatcher credentialsMatcher = new CustomHashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        credentialsMatcher.setHashIterations(2);
        return credentialsMatcher;
    }

}
