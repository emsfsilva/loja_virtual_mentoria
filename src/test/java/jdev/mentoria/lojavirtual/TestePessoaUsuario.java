package jdev.mentoria.lojavirtual;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jdev.mentoria.lojavirtual.controller.PessoaController;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualMentoriaApplication.class)
public class TestePessoaUsuario extends TestCase {

	@Autowired
	private PessoaController pessoaController;

	@Test
	public void testCadPessoaFisica() throws ExceptionMentoriaJava {

		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Francisco Silva");
		pessoaJuridica.setEmail("francisco@gmail.com");
		pessoaJuridica.setTelefone("81986854814");
		pessoaJuridica.setInscEstadual("123456789");
		pessoaJuridica.setInscMunicipal("987654321");
		pessoaJuridica.setNomeFantasia("Empresa de Devs");
		pessoaJuridica.setRazaoSocial("159753852654");

		pessoaController.salvarPj(pessoaJuridica);

	}

}
