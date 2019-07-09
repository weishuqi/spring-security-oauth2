package com.dzq.authproviderdemo.configuration;

import com.dzq.authproviderdemo.properties.AuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AuthProperties.class})
public class AutoEnablePropertiesConfig {
}
