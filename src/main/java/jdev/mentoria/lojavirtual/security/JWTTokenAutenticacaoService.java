package jdev.mentoria.lojavirtual.security;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
		
		/* Montagem do token*/
		
		String JWT = Jwts.builder()
				.setSubject(useername)
				.setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
		String token = TOKEN_PREFIX + " " + JWT;
		
		response.addHeader(HEADER_STRING, token);
		
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}

	
	
	
	
	
	
	
	
}
