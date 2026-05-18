package com.gestion.educativa.reuniones.reuniones.services;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import javax.crypto.SecretKey;
import com.gestion.educativa.reuniones.reuniones.models.dto.UsuarioValidadoDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IdentidadClientService {

	private static final String JWT_SECRET_DESARROLLO = "dev-secret-change-me-32-bytes-minimum";
	private static final List<Path> ARCHIVOS_ENV_LOCALES = List.of(
			Path.of(".env"),
			Path.of("reuniones", ".env"),
			Path.of("..", "reuniones", ".env"),
			Path.of("identidad", ".env"),
			Path.of("..", "identidad", ".env")
	);

	@Value("${jwt.secreto:}")
	private String secretoJwt;

	private String secretoResuelto;

	public UsuarioValidadoDto validarToken(String token) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(obtenerClaveFirma())
					.build()
					.parseSignedClaims(token)
					.getPayload();

			String runUsuario = claims.getSubject();
			if (runUsuario == null || runUsuario.isBlank()) {
				throw new IllegalArgumentException("Token invalido");
			}

			return new UsuarioValidadoDto(runUsuario, obtenerRolesDesdeClaims(claims));
		} catch (JwtException | IllegalArgumentException ex) {
			throw new IllegalArgumentException("Token invalido");
		}
	}

	private List<String> obtenerRolesDesdeClaims(Claims claims) {
		Object roles = claims.get("roles");
		if (roles instanceof List<?> listaRoles) {
			return listaRoles.stream()
					.map(String::valueOf)
					.toList();
		}
		if (roles instanceof String rolesTexto && !rolesTexto.isBlank()) {
			return Arrays.stream(rolesTexto.split(","))
					.map(String::trim)
					.filter(rol -> !rol.isBlank())
					.toList();
		}
		return List.of();
	}

	private SecretKey obtenerClaveFirma() {
		return Keys.hmacShaKeyFor(obtenerSecretoJwt().getBytes(StandardCharsets.UTF_8));
	}

	private String obtenerSecretoJwt() {
		if (secretoResuelto != null) {
			return secretoResuelto;
		}

		String secreto = normalizarSecreto(secretoJwt);
		if (secreto == null) {
			secreto = normalizarSecreto(System.getenv("JWT_SECRET"));
		}
		if (secreto == null) {
			secreto = buscarSecretoEnArchivosLocales();
		}
		if (secreto == null) {
			secreto = JWT_SECRET_DESARROLLO;
		}

		secretoResuelto = secreto;
		return secretoResuelto;
	}

	private String buscarSecretoEnArchivosLocales() {
		for (Path archivoEnv : ARCHIVOS_ENV_LOCALES) {
			if (!Files.isRegularFile(archivoEnv)) {
				continue;
			}

			try {
				for (String linea : Files.readAllLines(archivoEnv, StandardCharsets.UTF_8)) {
					String lineaLimpia = linea.trim();
					if (lineaLimpia.startsWith("JWT_SECRET=")) {
						return normalizarSecreto(lineaLimpia.substring("JWT_SECRET=".length()));
					}
				}
			} catch (Exception ex) {
				// Si un archivo local no se puede leer, se intenta con el siguiente.
			}
		}
		return null;
	}

	private String normalizarSecreto(String secreto) {
		if (secreto == null || secreto.isBlank()) {
			return null;
		}

		String secretoLimpio = secreto.trim();
		if ((secretoLimpio.startsWith("\"") && secretoLimpio.endsWith("\""))
				|| (secretoLimpio.startsWith("'") && secretoLimpio.endsWith("'"))) {
			secretoLimpio = secretoLimpio.substring(1, secretoLimpio.length() - 1);
		}
		return secretoLimpio.isBlank() ? null : secretoLimpio;
	}
}