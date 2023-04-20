package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

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
import jdev.mentoria.lojavirtual.model.Produto;
import jdev.mentoria.lojavirtual.repository.ProdutoRepository;

@Controller
@RestController /* Essa anotação indica que sera gerada uma API REST retornando um JSON */
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionMentoriaJava {

		if (produto.getEmpresa() ==  null || produto.getEmpresa().getId() <= 0) {
			throw new ExceptionMentoriaJava("A empresa Responsavel deve ser informada");
		}
		
		if (produto.getId() == null) {

			List<Produto> produtos = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());

			if (!produtos.isEmpty()) {
				throw new ExceptionMentoriaJava("Ja existe produto com esse nome: " + produto.getNome());

			}
		}
		
		if (produto.getCategoriaProduto() ==  null || produto.getCategoriaProduto().getId() <= 0) {
			throw new ExceptionMentoriaJava("A Categoria do Produto deve ser informada");
		}
		
		if (produto.getMarcaProduto() ==  null || produto.getMarcaProduto().getId() <= 0) {
			throw new ExceptionMentoriaJava("A Marca do Produto deve ser informada");
		}

		Produto produtoSalvo = produtoRepository.save(produto);

		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}

	@ResponseBody /* Aqui serve para da retorno da API */
	@PostMapping(value = "**/deleteProduto") /* Mapeando(Recebendo) a tela (url) para receber um json */
	/* os dois ** serve para pegar o salvarAcesso de qualquer lugar */
	public ResponseEntity<String> deleteProduto(
			@RequestBody Produto produto) { /* @requestBody Recebe um json e converte para objeto */

		produtoRepository.deleteById(produto.getId());

		return new ResponseEntity<String>("Produto Removido", HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteProdutoPorId/{id}")
	public ResponseEntity<String> deleteProdutoPorId(@PathVariable("id") Long id) {

		produtoRepository.deleteById(id);

		return new ResponseEntity<String>("Produto Removido", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/obterProduto/{id}")
	public ResponseEntity<Produto> obterProduto(@PathVariable("id") Long id) throws ExceptionMentoriaJava {

		Produto produto = produtoRepository.findById(id).orElse(null);

		if (produto == null) {
			throw new ExceptionMentoriaJava("Não encontrou produto com o código: " + id);

		}

		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarProdutoNome/{desc}")
	public ResponseEntity<List<Produto>> buscarProdutoNome(@PathVariable("desc") String desc) {

		List<Produto> produto = produtoRepository.buscarProdutoNome(desc.toUpperCase());

		return new ResponseEntity<List<Produto>>(produto, HttpStatus.OK);
	}

}
