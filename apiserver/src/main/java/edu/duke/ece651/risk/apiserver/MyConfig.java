package edu.duke.ece651.risk.apiserver;

import edu.duke.ece651.risk.apiserver.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@EnableWebSocketMessageBroker
@Order(HIGHEST_PRECEDENCE + 99)
public class MyConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            @Order(HIGHEST_PRECEDENCE + 99)
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    Logger logger = LoggerFactory.getLogger(APIGameHandler.class);

                    String authToken = accessor.getFirstNativeHeader("Authorization");
                    logger.info("webSocket token is {}", authToken);
                    //Authentication user = ... ; // access authentication header(s)
                    //Authentication user = authenticationManager.authenticate();
                    //logger.info("auth: {}",user);
                    accessor.setUser(jwtUtils.getAuthenticationFromToken(authToken));
                }
                return message;
            }
        });
    }
}