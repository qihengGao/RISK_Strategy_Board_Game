package edu.duke.ece651.risk.apiserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .simpMessageDestMatchers("/chat/**").permitAll()
//                .anyMessage().permitAll();

        messages
                .simpTypeMatchers(
                        SimpMessageType.CONNECT,
                        SimpMessageType.MESSAGE,
                        SimpMessageType.SUBSCRIBE).authenticated()
                .simpTypeMatchers(
                        SimpMessageType.UNSUBSCRIBE,
                        SimpMessageType.DISCONNECT).permitAll()
                //.nullDestMatcher().authenticated()
                .anyMessage().denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }


}
