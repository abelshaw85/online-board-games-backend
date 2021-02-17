package com.online.board.games.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	@Value("${origins.url}")
	private String originsUrl;
	
    public class MyHandshakeHandler extends DefaultHandshakeHandler {
        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, 
                                          Map<String, Object> attributes) {
        	final String ATTR_PRINCIPAL = "__principal__";
        	
            final String name;
            if (!attributes.containsKey(ATTR_PRINCIPAL)) {
                name = "placeholder"; //Would like this to be the logged in user, but is currently null
                attributes.put(ATTR_PRINCIPAL, name);
            } else {
                name = (String) attributes.get(ATTR_PRINCIPAL);
            }
            return new Principal() {
                @Override
                public String getName() {
                    return name;
                }
            };
        }
    }
	
	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
        	.setHandshakeHandler(new MyHandshakeHandler())
        	.setAllowedOrigins(originsUrl)
        	.withSockJS();
    }
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    	registry.enableSimpleBroker("/topic");
    	registry.setApplicationDestinationPrefixes("/app");    	
    }
}
