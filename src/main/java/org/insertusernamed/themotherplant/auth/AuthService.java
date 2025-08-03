package org.insertusernamed.themotherplant.auth;

import lombok.RequiredArgsConstructor;
import org.insertusernamed.themotherplant.auth.dto.AuthResponse;
import org.insertusernamed.themotherplant.auth.dto.LoginRequest;
import org.insertusernamed.themotherplant.security.JwtService;
import org.insertusernamed.themotherplant.user.User;
import org.insertusernamed.themotherplant.user.UserRepository;
import org.insertusernamed.themotherplant.user.UserService;
import org.insertusernamed.themotherplant.user.dto.CreateUserRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final UserService userService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthResponse register(CreateUserRequest request) {
		userService.createUser(request);
		return login(new LoginRequest(request.email(), request.password()));
	}

	public AuthResponse login(LoginRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.email(),
						request.password()
				)
		);

		User user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new IllegalStateException("User not found"));

		String jwtToken = jwtService.generateToken(user);
		return new AuthResponse(jwtToken);
	}
}
