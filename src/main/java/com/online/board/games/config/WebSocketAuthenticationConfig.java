package com.online.board.games.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.online.board.games.service.UserService;
import com.online.board.games.util.JwtUtil;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)	
public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {
	
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserService userService;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
        	
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {            	
            	StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                List<String> tokenList = accessor.getNativeHeader("Authorization");
                String jwt = null;
                if (tokenList == null || tokenList.size() < 1) {
                  return message;
                } else {
                	jwt = tokenList.get(0).substring(7);
                  if (jwt == null) {
                    return message;
                  }
                }
                String username = jwtUtil.extractUsername(jwt); //Decoder class that can retrieve username from token
                
                UserDetails userDetails = userService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
    				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
    						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        			
    				accessor.setUser(authentication);
    			}
                return message;
            }
        });
    }
}