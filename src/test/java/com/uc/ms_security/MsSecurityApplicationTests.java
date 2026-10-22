package com.uc.ms_security;

import com.uc.ms_security.dto.AuthResponseDTO;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "security.jwt.secret=test-only-secret-with-at-least-32-bytes")
class MsSecurityApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private JwtDecoder jwtDecoder;

	@Test
	void contextLoads() {
	}

	@Test
	void generatedTokenCanBeDecodedWithExpectedClaims() {
		User user = new User();
		user.setId(42L);
		user.setEmail("ana@mail.com");

		AuthResponseDTO response = jwtService.generateToken(user);
		Jwt decodedToken = jwtDecoder.decode(response.accessToken());

		assertEquals("Bearer", response.tokenType());
		assertEquals("ms-security", decodedToken.getClaimAsString("iss"));
		assertEquals("42", decodedToken.getSubject());
		assertEquals("ana@mail.com", decodedToken.getClaimAsString("email"));
		assertTrue(response.expiresAt().isAfter(Instant.now()));
	}

	@Test
	void modifiedTokenIsRejected() {
		User user = new User();
		user.setId(42L);
		user.setEmail("ana@mail.com");

		String[] tokenParts = jwtService.generateToken(user)
				.accessToken()
				.split("\\.");
		String replacement = tokenParts[2].startsWith("A") ? "B" : "A";
		String modifiedToken = tokenParts[0] + "."
				+ tokenParts[1] + "."
				+ replacement + tokenParts[2].substring(1);

		assertThrows(
				JwtException.class,
				() -> jwtDecoder.decode(modifiedToken)
		);
	}
}
