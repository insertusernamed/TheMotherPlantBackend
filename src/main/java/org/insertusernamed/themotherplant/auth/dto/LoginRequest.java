package org.insertusernamed.themotherplant.auth.dto;

public record LoginRequest (
		String email,
		String password
) {}