package com.dzq.authproviderdemo.configuration;

import com.dzq.authproviderdemo.exception.AuthExceptionTranslator;
import com.dzq.authproviderdemo.properties.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * @Author dzq
 * @Date  2019/7/8 17:20
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthProperties authProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier(value = "clientDetailsServiceImpl")
    private ClientDetailsService clientDetailsService;

    /**
     * client模式，没有用户的概念，直接与认证服务器交互，用配置中的客户端信息去申请accessToken，
     * 客户端有自己的client_id,client_secret对应于用户的username,password，而客户端也拥有自己的authorities，
     * 当采取client模式认证时，对应的权限也就是客户端自己的authorities。
     *
     * password模式，自己本身有一套用户体系，在认证时需要带上自己的用户名和密码，以及客户端的client_id,client_secret。
     * 此时，accessToken所包含的权限是用户本身的权限，而不是客户端的权限。
     * @Author dzq
     * @Date  2019/7/8 17:32
     **/
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        // 从配置文件读取和写死用户
        /*clients.inMemory().withClient(authProperties.getClientId())
                .resourceIds("test")
                .authorizedGrantTypes("client_credentials", "refresh_token") // client认证
                .scopes("select")
                .authorities("client")
                .secret(passwordEncoder.encode(authProperties.getSecret()))
                .and()
                .withClient("client_2")
                .resourceIds("test")
                .authorizedGrantTypes("password", "refresh_token") // password认证
                .scopes("select")
                .authorities("client")
                .secret(passwordEncoder.encode("123456"));*/

        // 从数据库读取client
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 配置数据库查询客户端服务
     * @Author dzq
     * @Date  2019/7/9 14:59
     **/
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    /**
     * token存储
     * @Author dzq
     * @Date  2019/7/9 14:41
     **/
    @Bean
    public TokenStore tokenStore() {
        // 使用redis存储
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
     *
     * approvalStore()
     *      1.基于内存存储
     *      2.基于token实现，将token存储在oauth_access_token表中，一般，只有使用TokenStoreUserApprovalHandler策略才会使用这种存储方式
     *      3.基于scope实现，将scope授权情况存储在oauth_approvals表中,一般只有使用ApprovalStoreUserApprovalHandler策略才使用这种存储方式
     * @Author dzq
     * @Date  2019/7/9 14:41
     **/
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore()) // 将token信息存入redis
                .authenticationManager(authenticationManager)
                // 设置自定义异常解释类 异常时可以自定义返回的内容
                .exceptionTranslator(new AuthExceptionTranslator());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // 允许表单验证
        security.allowFormAuthenticationForClients();
    }
}
