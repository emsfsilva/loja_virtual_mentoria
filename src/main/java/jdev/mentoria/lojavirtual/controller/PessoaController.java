package jdev.mentoria.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExceptionMentoriaJava;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.service.PessoaUserService;

@RestController /* Essa anotação indica que sera gerada uma API REST retornando um JSON */
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository; // Esta linha vai bucar a query de consulta no banco

	@Autowired
	private PessoaUserService pessoaUserService; // Esta linha vai bucar o metodo salvar

	@ResponseBody
	@PostMapping(path = "**/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody PessoaJuridica pessoaJuridica)
			throws ExceptionMentoriaJava {

		if (pessoaJuridica == null) {
			throw new ExceptionMentoriaJava("Pessoa Juridica não pode ser NULL");
		}
		if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionMentoriaJava("Ja existe CNPJ cadastrado com o numero: " + pessoaJuridica.getCnpj());
		}

		/*
		 * Esta linha envia tudo para o PessuaUserService e lá tem os metodos de salvar
		 */
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}

}
