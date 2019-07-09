package com.dzq.authproviderdemo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 自定义客户端实现, 可以实现自定义权限
 *
 * @Author dzq
 * @Date 2019/7/9 14:29
 **/
@Slf4j
@Service("clientDetailsServiceImpl")
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);

        if (clientDetails != null) {

            // clientDetails.getAdditionalInformation()获取到存储的一些附加信息， 可以用来辅助校验
            Map<String, Object> map = clientDetails.getAdditionalInformation();

            for (String key : map.keySet()) {
                log.info(key + ":" + map.get(key));
            }

            return clientDetails;
        }

        return null;
    }
}
