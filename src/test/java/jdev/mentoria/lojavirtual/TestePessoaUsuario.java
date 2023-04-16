package jdev.mentoria.lojavirtual;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jdev.mentoria.lojavirtual.controller.PessoaController;
import jdev.mentoria.lojavirtual.enums.TipoEndereco;
import jdev.mentoria.lojavirtual.model.Endereco;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualMentoriaApplication.class)
public class TestePessoaUsuario extends TestCase {

	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@Test
	public void testCadPessoaJuridica() throws ExceptionMentoriaJava {

		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Francisco Silva");
		pessoaJuridica.setEmail("ems.fsilva.pe@gmail.com");
		pessoaJuridica.setTelefone("81986854814");
		pessoaJuridica.setInscEstadual("123456789");
		pessoaJuridica.setInscMunicipal("987654321");
		pessoaJuridica.setNomeFantasia("Empresa de Devs");
		pessoaJuridica.setRazaoSocial("159753852654");
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Jd Dias");
		endereco1.setCep("98653221");
		endereco1.setComplemento("Casa Cinza");
		endereco1.setEmpresa(pessoaJuridica);
		endereco1.setNumero("389");
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setRuaLogra("Av Jao Sexto");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("PR");
		endereco1.setCidade("Recife");
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Jd Maracana");
		endereco2.setCep("45789644");
		endereco2.setComplemento("Andar 4");
		endereco2.setEmpresa(pessoaJuridica);
		endereco2.setNumero("555");
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setRuaLogra("Av Maringá");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("PR");
		endereco2.setCidade("Recife");
		
		pessoaJuridica.getEnderecos().add(endereco1);
		pessoaJuridica.getEnderecos().add(endereco2);
		
		pessoaJuridica = pessoaController.salvarPj(pessoaJuridica).getBody();
		
		assertEquals(true, pessoaJuridica.getId() > 0);
		
		for (Endereco endereco : pessoaJuridica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaJuridica.getEnderecos().size());
		

	}
	
	
	
	
	
	
	@Test
	public void testCadPessoaFisica() throws ExceptionMentoriaJava {
		
		PessoaJuridica pessoaJuridica = pessoaRepository.existeCnpjCadastrado("1681653236408");

		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setCpf("082.866.674-17");
		pessoaFisica.setNome("Francisco Silva");
		pessoaFisica.setEmail("ems.fsilva.pe@gmail.com");
		pessoaFisica.setTelefone("81986854814");
		pessoaFisica.setEmpresa(pessoaJuridica);
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Jd Dias");
		endereco1.setCep("98653221");
		endereco1.setComplemento("Casa Cinza");
		endereco1.setNumero("389");
		endereco1.setPessoa(pessoaFisica);
		endereco1.setRuaLogra("Av Jao Sexto");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("PR");
		endereco1.setCidade("Recife");
		endereco1.setEmpresa(pessoaJuridica);
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Jd Maracana");
		endereco2.setCep("45789644");
		endereco2.setComplemento("Andar 4");
		endereco2.setNumero("555");
		endereco2.setPessoa(pessoaFisica);
		endereco2.setRuaLogra("Av Maringá");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("PR");
		endereco2.setCidade("Recife");
		endereco2.setEmpresa(pessoaJuridica);
		
		pessoaFisica.getEnderecos().add(endereco1);
		pessoaFisica.getEnderecos().add(endereco2);
		
		pessoaFisica = pessoaController.salvarPf(pessoaFisica).getBody();
		
		assertEquals(true, pessoaFisica.getId() > 0);
		
		for (Endereco endereco : pessoaFisica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaFisica.getEnderecos().size());
		

	}
	
	
	
	

}
