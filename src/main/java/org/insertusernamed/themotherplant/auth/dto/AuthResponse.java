package org.insertusernamed.themotherplant.auth.dto;

import org.insertusernamed.themotherplant.user.Role;

public record AuthResponse (
		String token,
		Long id,
		String firstName,
		String lastName,
		String email,
		Long points,
		Role role
) {}