package jdev.mentoria.lojavirtual.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import jdev.mentoria.lojavirtual.ApplicationContextLoad;
import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;

/*Cria a autenticação e retorna a autenticação*/

@Service
@Component
public class JWTTokenAutenticacaoService {

	/* Tempo de expiração do token = 11 dias */
	private static final long EXPIRATION_TIME = 959990000;

	/* Chave de senha para juntar com JWT */
	private static final String SECRET = "ss/-*-*sds565sd-s/d-s*dsds";

	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	public void addAuthentication(HttpServletResponse response, String useername) throws Exception {

		/* Montagem do token */

		String JWT = Jwts.builder().setSubject(useername)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();

		String token = TOKEN_PREFIX + " " + JWT;

		response.addHeader(HEADER_STRING, token);

		liberacaoCors(response);

		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}

	/*
	 * Metodo que retorna o usuario validado com o token ou caso nao seja valido
	 * retorna null
	 */
	public Authentication getAuthetication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String token = request.getHeader(HEADER_STRING);

		try {

			if (token != null) {

				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

				String user = Jwts.parser().
						setSigningKey(SECRET).
						parseClaimsJws(tokenLimpo)
						.getBody().getSubject();

				if (user != null) {
					Usuario usuario = ApplicationContextLoad.
							getApplicationContext().
							getBean(UsuarioRepository.class)
							.findUserByLogin(user);

					if (usuario != null) {
						return new UsernamePasswordAuthenticationToken(
								usuario.getLogin(),
								usuario.getSenha(),
								usuario.getAuthorities());

					}
				}
			}

		} catch (SignatureException e) {
			response.getWriter().write("Token está invalido");

		} catch (ExpiredJwtException e) {
			response.getWriter().write("Token está expirado. Efetue login Novamente");

		}
		finally {
			liberacaoCors(response);
		}

		liberacaoCors(response);
		return null;
	}

	private void liberacaoCors(HttpServletResponse response) {

		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}

		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}

		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
	}

}
