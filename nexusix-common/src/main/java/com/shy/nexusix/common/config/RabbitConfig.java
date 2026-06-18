package com.shy.nexusix.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>RabbitMQ配置类 - 租户消息队列与JSON序列化</p>
 *
 * @author shy
 */
@Configuration
public class RabbitConfig {

    /**
     * 声明租户队列（持久化）
     *
     * @return 租户队列
     */
    @Bean
    public Queue queue() {
        return new Queue("nexusix.tenant.queue", true);
    }

    /**
     * 声明租户直连交换机（持久化、非自动删除）
     *
     * @return 租户直连交换机
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("nexusix.tenant.exchange", true, false);
    }

    /**
     * 将租户队列绑定到交换机，使用租户路由键
     *
     * @return 队列与交换机的绑定关系
     */
    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with("nexusix.tenant.routing.key");
    }

    /**
     * 配置JSON消息转换器，使用Jackson序列化消息体
     *
     * @return Jackson2Json消息转换器
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
