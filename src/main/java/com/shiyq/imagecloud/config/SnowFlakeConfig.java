package com.shiyq.imagecloud.config;

import com.shiyq.imagecloud.util.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法的配置类
 */
@Configuration
public class SnowFlakeConfig {

        @Value("${snow-flake.datacenterId}")
        private Long datacenterId;

        @Value("${snow-flake.workerId}")
        private Long workerId;

        @Bean
        public SnowFlakeUtil snowflake() {
            return new SnowFlakeUtil(workerId, datacenterId);
        }

}
