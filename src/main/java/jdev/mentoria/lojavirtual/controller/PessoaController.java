package jdev.mentoria.lojavirtual.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExceptionMentoriaJava;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.service.PessoaUserService;
import jdev.mentoria.lojavirtual.util.ValidaCNPJ;
import jdev.mentoria.lojavirtual.util.ValidaCPF;

@RestController /* Essa anotação indica que sera gerada uma API REST retornando um JSON */
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository; // Esta linha vai bucar a query de consulta no banco

	@Autowired
	private PessoaUserService pessoaUserService; // Esta linha vai bucar o metodo salvar

	@ResponseBody
	@PostMapping(path = "**/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica)
			throws ExceptionMentoriaJava {
		
		

		if (pessoaJuridica == null) {
			throw new ExceptionMentoriaJava("Pessoa Juridica não pode ser NULL");
		}
		if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionMentoriaJava("Ja existe CNPJ cadastrado com o numero: " + pessoaJuridica.getCnpj());
		}
		
		if (pessoaJuridica.getId() == null && pessoaRepository.existeInscEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
			throw new ExceptionMentoriaJava("Ja existe Inscrição Estadual cadastrado com o numero: " + pessoaJuridica.getInscEstadual());
		}
		
		if(!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
			throw new ExceptionMentoriaJava("Cnpj : " + pessoaJuridica.getCnpj() + " está invalido. ");
			
		}

		/*
		 * Esta linha envia tudo para o PessuaUserService e lá tem os metodos de salvar
		 */
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	@ResponseBody
	@PostMapping(path = "**/salvarPf")
	public ResponseEntity<PessoaFisica> salvarPf(@RequestBody PessoaFisica pessoaFisica)
			throws ExceptionMentoriaJava {

		if (pessoaFisica == null) {
			throw new ExceptionMentoriaJava("Pessoa Fisica não pode ser NULL");
		}
		if (pessoaFisica.getId() == null && pessoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new ExceptionMentoriaJava("Ja existe CPF cadastrado com o numero: " + pessoaFisica.getCpf());
		}
		
		if(!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
			throw new ExceptionMentoriaJava("Cpf : " + pessoaFisica.getCpf() + " está invalido. ");
			
		}

		/*
		 * Esta linha envia tudo para o PessuaUserService e lá tem os metodos de salvar
		 */
		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);

		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}

}
