package com.online.board.games.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.online.board.games.service.UserService;
import com.online.board.games.util.JwtUtil;

//Thank you JavaBrains
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Get the "Authorization" value of the header, which is our JWT
		final String authorisationHeader = request.getHeader("Authorization");
		
		String username = null;
		String jwt = null;
		
		if (authorisationHeader != null && authorisationHeader.startsWith("Bearer ")) {
			jwt = authorisationHeader.substring(7);
			username = this.jwtUtil.extractUsername(jwt);
		}
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userService.loadUserByUsername(username);
			
			// The below is what would normally happen without this filter, but now we are manually
			// performing this only if the JWT is valid.
			if (this.jwtUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response); //continue the chain of filters	
	}

}
