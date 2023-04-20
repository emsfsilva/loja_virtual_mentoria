package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExceptionMentoriaJava;
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

	@ResponseBody
	@PostMapping(value = "**/salvarAcesso")
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws ExceptionMentoriaJava {

		if (acesso.getId() == null) {

			List<Acesso> acessos = acessoRepository.buscarAcessoDesc(acesso.getDescricao().toUpperCase());

			if (!acessos.isEmpty()) {
				throw new ExceptionMentoriaJava("Ja existe acesso com a descrição: " + acesso.getDescricao());

			}
		}

		Acesso acessoSalvo = acessoService.save(acesso);

		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}

	@ResponseBody /* Aqui serve para da retorno da API */
	@PostMapping(value = "**/deleteAcesso") /* Mapeando(Recebendo) a tela (url) para receber um json */
	/* os dois ** serve para pegar o salvarAcesso de qualquer lugar */
	public ResponseEntity<?> deleteAcesso(
			@RequestBody Acesso acesso) { /* @requestBody Recebe um json e converte para objeto */

		acessoRepository.deleteById(acesso.getId());

		return new ResponseEntity<Object>("Acesso Removido", HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteAcessoPorId/{id}")
	public ResponseEntity<?> deleteAcessoPorId(@PathVariable("id") Long id) {

		acessoRepository.deleteById(id);

		return new ResponseEntity<Object>("Acesso Removido", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/obterAcesso/{id}")
	public ResponseEntity<Acesso> obterAcesso(@PathVariable("id") Long id) throws ExceptionMentoriaJava {

		Acesso acesso = acessoRepository.findById(id).orElse(null);

		if (acesso == null) {
			throw new ExceptionMentoriaJava("Não encontrou acesso com o código: " + id);

		}

		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarPorDesc/{desc}")
	public ResponseEntity<List<Acesso>> buscarPorDesc(@PathVariable("desc") String desc) {

		List<Acesso> acesso = acessoRepository.buscarAcessoDesc(desc.toUpperCase());

		return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);
	}

}
