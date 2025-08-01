package org.insertusernamed.themotherplant.user.dto;

public record CreateUserRequest(
		String firstName,
		String lastName,
		String email,
		String password
) {}