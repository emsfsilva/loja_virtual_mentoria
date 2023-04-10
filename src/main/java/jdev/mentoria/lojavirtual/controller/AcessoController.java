package jdev.mentoria.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import jdev.mentoria.lojavirtual.service.AcessoService;

@Controller
@RestController /* Essa anotação indica que sera gerada uma API REST retornando um JSON */
public class AcessoController {

	@Autowired
	private AcessoRepository acessoRepository;
	
	@Autowired
	private AcessoService acessoService;

	@ResponseBody /* Aqui serve para da retorno da API */
	@PostMapping(value = "/salvarAcesso") /* Mapeando(Recebendo) a tela (url) para receber um json */
	/* os dois ** serve para pegar o salvarAcesso de qualquer lugar */
	public ResponseEntity<Acesso> salvarAcesso(
			@RequestBody Acesso acesso) { /* @requestBody Recebe um json e converte para objeto */

		Acesso acessoSalvo = acessoService.save(acesso);

		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody /* Aqui serve para da retorno da API */
	@PostMapping(value = "/deleteAcesso") /* Mapeando(Recebendo) a tela (url) para receber um json */
	/* os dois ** serve para pegar o salvarAcesso de qualquer lugar */
	public ResponseEntity<?> deleteAcesso(
			@RequestBody Acesso acesso) { /* @requestBody Recebe um json e converte para objeto */

		acessoRepository.deleteById(acesso.getId());

		return new ResponseEntity("Acesso Removido", HttpStatus.OK);
	}

}
