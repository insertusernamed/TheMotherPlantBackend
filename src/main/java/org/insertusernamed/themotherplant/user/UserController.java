package org.insertusernamed.themotherplant.user;

import lombok.RequiredArgsConstructor;
import org.insertusernamed.themotherplant.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<UserResponse> getMyInfo(Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();

		UserResponse response = new UserResponse(
				currentUser.getId(),
				currentUser.getEmail(),
				currentUser.getFirstName(),
				currentUser.getLastName(),
				currentUser.getRole(),
				currentUser.getPoints()
		);

		return ResponseEntity.ok(response);
	}
}
