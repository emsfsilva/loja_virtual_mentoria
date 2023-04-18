package jdev.mentoria.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.model.dto.ConsultaCnpjDto;
import jdev.mentoria.lojavirtual.repository.PessoaFisicaRepository;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica juridica) {

		//juridica = pessoaRepository.save(juridica);
		
		for (int i = 0; i< juridica.getEnderecos().size(); i++) {
			juridica.getEnderecos().get(i).setPessoa(juridica);
			juridica.getEnderecos().get(i).setEmpresa(juridica);
		}
		
		juridica = pessoaRepository.save(juridica);

		Usuario usuarioPj = usuarioRepository.findUserByPessoa(juridica.getId(), juridica.getEmail());

		if (usuarioPj == null) {

			String constraint = usuarioRepository.consultaConstraintAcesso();

			if (constraint != null) {
				jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;");
			}
			usuarioPj = new Usuario();
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresa(juridica);
			usuarioPj.setPessoa(juridica);
			usuarioPj.setLogin(juridica.getEmail());

			String senha = "" + Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);

			usuarioPj.setSenha(senhaCript);

			usuarioPj = usuarioRepository.save(usuarioPj);

			usuarioRepository.insereAcessoUser(usuarioPj.getId());
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");
			
			StringBuilder messagemHtml = new StringBuilder();
			
			messagemHtml.append("<b>Segue abaixo seus dados de acesso </b>");
			messagemHtml.append("<b>Login: </b>" +juridica.getEmail()+"<br/>");
			messagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
			messagemHtml.append("Obrigado");
			
			try {
				serviceSendEmail.enviarEmailHtml("Acesso gerado para a loja virutal", messagemHtml.toString(), juridica.getEmail());
			}catch (Exception e) {
				e.printStackTrace();
			}

		}
		return juridica;

	}
	
	
	
	
	
	
	
	
	

	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {
		
		//juridica = pessoaRepository.save(juridica);
		
				for (int i = 0; i< pessoaFisica.getEnderecos().size(); i++) {
					pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
					//pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
				}
				
				pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);

				Usuario usuarioPf = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

				if (usuarioPf == null) {

					String constraint = usuarioRepository.consultaConstraintAcesso();

					if (constraint != null) {
						jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;");
					}
					usuarioPf = new Usuario();
					usuarioPf.setDataAtualSenha(Calendar.getInstance().getTime());
					usuarioPf.setEmpresa(pessoaFisica.getEmpresa());
					usuarioPf.setPessoa(pessoaFisica);
					usuarioPf.setLogin(pessoaFisica.getEmail());

					String senha = "" + Calendar.getInstance().getTimeInMillis();
					String senhaCript = new BCryptPasswordEncoder().encode(senha);

					usuarioPf.setSenha(senhaCript);

					usuarioPf = usuarioRepository.save(usuarioPf);

					usuarioRepository.insereAcessoUser(usuarioPf.getId());
					
					StringBuilder messagemHtml = new StringBuilder();
					
					messagemHtml.append("<b>Segue abaixo seus dados de acesso </b>");
					messagemHtml.append("<b>Login: </b>" +pessoaFisica.getEmail()+"<br/>");
					messagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
					messagemHtml.append("Obrigado");
					
					try {
						serviceSendEmail.enviarEmailHtml("Acesso gerado para a loja virutal", messagemHtml.toString(), pessoaFisica.getEmail());
					}catch (Exception e) {
						e.printStackTrace();
					}

				}
				return pessoaFisica;
	}
	
	//public CepDTO consultaCep(String cep) {
	//	return new RestTemplate().getForEntity("http://viacep.com.br/ws" + cep + "/json/", CepDTO.class).getBody();
	//}
	
	public ConsultaCnpjDto consultaCnpjReceitaWs(String cnpj) {
		return new RestTemplate().getForEntity("https://receitaws.com.br/v1/cnpj/" + cnpj, ConsultaCnpjDto.class).getBody();
	}	

	
	
}
