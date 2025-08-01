package org.insertusernamed.themotherplant.user;

import org.insertusernamed.themotherplant.user.dto.CreateUserRequest;
import org.insertusernamed.themotherplant.user.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserResponse createUser(CreateUserRequest request) {
		if (userRepository.findByEmail(request.email()).isPresent()) {
			throw new IllegalStateException("User with email " + request.email() + " already exists.");
		}

		User newUser = User.builder()
				.firstName(request.firstName())
				.lastName(request.lastName())
				.email(request.email())
				.password(passwordEncoder.encode(request.password()))
				.role(Role.USER)
				.build();

		User savedUser = userRepository.save(newUser);

		return new UserResponse(
				savedUser.getId(),
				savedUser.getFirstName(),
				savedUser.getLastName(),
				savedUser.getEmail(),
				savedUser.getRole(),
				savedUser.getPoints()
		);
	}
}
