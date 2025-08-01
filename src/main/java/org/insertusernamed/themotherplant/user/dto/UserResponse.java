package org.insertusernamed.themotherplant.user.dto;

import org.insertusernamed.themotherplant.user.Role;

public record UserResponse (
		Long id,
		String email,
		String firstName,
		String lastName,
		Role role,
		Long points
) {}