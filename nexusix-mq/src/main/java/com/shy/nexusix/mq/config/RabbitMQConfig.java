package com.shy.nexusix.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * RabbitMQ 配置类
 * </p>
 * <p>
 * 负责声明队列、交换机、绑定关系，以及配置 RabbitTemplate 的消息转换器。
 * 采用 Direct 交换机实现精确路由，配合死信队列处理消费失败场景。
 * </p>
 *
 * @author shy
 * @since 2026-05-15
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 业务队列名称
     */
    public static final String NEXUSIX_QUEUE = "nexusix.queue";

    /**
     * 延迟队列名称
     */
    public static final String NEXUSIX_DELAY_QUEUE = "nexusix.delay.queue";

    /**
     * 死信队列名称
     */
    public static final String NEXUSIX_DEAD_LETTER_QUEUE = "nexusix.dead.letter.queue";

    /**
     * Direct 交换机名称
     */
    public static final String NEXUSIX_DIRECT_EXCHANGE = "nexusix.direct.exchange";

    /**
     * Topic 交换机名称
     */
    public static final String NEXUSIX_TOPIC_EXCHANGE = "nexusix.topic.exchange";

    /**
     * 业务队列路由键
     */
    public static final String NEXUSIX_ROUTING_KEY = "nexusix.routing.key";

    /**
     * 延迟队列路由键
     */
    public static final String NEXUSIX_DELAY_ROUTING_KEY = "nexusix.delay.routing.key";

    /**
     * 死信队列路由键
     */
    public static final String NEXUSIX_DEAD_LETTER_ROUTING_KEY = "nexusix.dead.letter.routing.key";

    /**
     * <p>
     * 消息转换器
     * </p>
     * <p>
     * 使用 Jackson2JsonMessageConverter 将 Java 对象序列化为 JSON 格式的消息体，
     * 消费者端反序列化时自动还原为原始对象类型。
     * </p>
     *
     * @return JSON 消息转换器实例
     * @author shy
     * @since 2026-05-15
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
//
//    /**
//     * <p>
//     * RabbitTemplate 实例
//     * </p>
//     * <p>
//     * 配置消息转换器、开启强制路由（mandatory=true），以及设置发布确认回调。
//     * 强制路由确保当消息无法路由到任何队列时触发回调。
//     * </p>
//     *
//     * @param connectionFactory 连接工厂
//     * @return 配置好的 RabbitTemplate 实例
//     * @author shy
//     * @since 2026-05-15
//     */
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(messageConverter());
//
//        // 开启强制路由：当消息无法路由到队列时触发 ReturnCallback
//        rabbitTemplate.setMandatory(true);
//
//        // 发布确认回调（Publisher Confirm）
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//            if (!ack) {
//                System.err.println("消息发布确认失败: " + cause);
//            }
//        });
//
//        // 消息未路由回调（Return Callback）
//        rabbitTemplate.setReturnsCallback(returned -> {
//            System.err.println("消息未路由到队列: " + returned.getMessage() + ", 路由键: " + returned.getRoutingKey());
//        });
//
//        return rabbitTemplate;
//    }

    /**
     * <p>
     * 死信队列声明
     * </p>
     * <p>
     * 独立的死信队列，用于接收被业务队列拒绝的消息。
     * </p>
     *
     * @return 死信队列
     * @author shy
     * @since 2026-05-15
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(NEXUSIX_DEAD_LETTER_QUEUE).build();
    }

    /**
     * <p>
     * 业务队列声明
     * </p>
     * <p>
     * 持久化队列，配置死信交换机和死信路由键，当消息被拒绝且 requeue=false 时自动转发到死信队列。
     * </p>
     *
     * @return 业务队列
     * @author shy
     * @since 2026-05-15
     */
    @Bean
    public Queue nexusixQueue() {
        return QueueBuilder.durable(NEXUSIX_QUEUE)
                .withArgument("x-dead-letter-exchange", NEXUSIX_TOPIC_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", NEXUSIX_DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    /**
     * <p>
     * Direct 交换机声明
     * </p>
     * <p>
     * 持久化 Direct 交换机，用于精确路由的消息分发。
     * </p>
     *
     * @return Direct 交换机
     * @author shy
     * @since 2026-05-15
     */
    @Bean
    public DirectExchange nexusixDirectExchange() {
        return ExchangeBuilder.directExchange(NEXUSIX_DIRECT_EXCHANGE).durable(true).build();
    }

    /**
     * <p>
     * Topic 交换机声明
     * </p>
     * <p>
     * 持久化 Topic 交换机，用于模式匹配路由的消息分发，支持死信队列路由。
     * </p>
     *
     * @return Topic 交换机
     * @author shy
     * @since 2026-05-15
     */
    @Bean
    public TopicExchange nexusixTopicExchange() {
        return ExchangeBuilder.topicExchange(NEXUSIX_TOPIC_EXCHANGE).durable(true).build();
    }

    /**
     * <p>
     * 业务队列绑定到 Direct 交换机
     * </p>
     *
     * @return 绑定关系
     * @author shy
     * @since 2026-05-15
     */
    @Bean
    public Binding nexusixQueueBinding() {
        return BindingBuilder.bind(nexusixQueue()).to(nexusixDirectExchange()).with(NEXUSIX_ROUTING_KEY);
    }

    /**
     * <p>
     * 死信队列绑定到 Topic 交换机
     * </p>
     *
     * @return 绑定关系
     * @author shy
     * @since 2026-05-15
     */
    @Bean
    public Binding deadLetterQueueBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(nexusixTopicExchange()).with(NEXUSIX_DEAD_LETTER_ROUTING_KEY);
    }
}