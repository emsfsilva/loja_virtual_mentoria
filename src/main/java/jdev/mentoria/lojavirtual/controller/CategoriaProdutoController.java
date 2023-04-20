package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExceptionMentoriaJava;
import jdev.mentoria.lojavirtual.model.CategoriaProduto;
import jdev.mentoria.lojavirtual.model.dto.CategoriaProdutoDto;
import jdev.mentoria.lojavirtual.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {
	
	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;
	
	
	@ResponseBody
	@GetMapping(value = "/buscarPorDescCategoria/{desc}")
	public ResponseEntity<List<CategoriaProduto>> buscarPorDesc(@PathVariable("desc") String desc) {

		List<CategoriaProduto> categoriaProduto = categoriaProdutoRepository.buscarCategoriaDes(desc.toUpperCase());

		return new ResponseEntity<List<CategoriaProduto>>(categoriaProduto, HttpStatus.OK);
	}
	
	
	@ResponseBody /* Aqui serve para da retorno da API */
	@PostMapping(value = "/deleteCategoria") /* Mapeando(Recebendo) a tela (url) para receber um json */
	/* os dois ** serve para pegar o salvarAcesso de qualquer lugar */
	public ResponseEntity<?> deleteCategoria(
			@RequestBody CategoriaProduto categoriaProduto) { /* @requestBody Recebe um json e converte para objeto */
		
		if(categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent() == false) {
			return new ResponseEntity("Categoria já foi Removida", HttpStatus.OK);
			
		}

		categoriaProdutoRepository.deleteById(categoriaProduto.getId());

		return new ResponseEntity("Categoria Removida", HttpStatus.OK);
	}
	
	

	@ResponseBody
	@PostMapping(value = "**/salvarCategoria")
	public ResponseEntity<CategoriaProdutoDto> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionMentoriaJava {
		
		if(categoriaProduto.getEmpresa() == null || (categoriaProduto.getEmpresa().getId() == null)) {
			throw new ExceptionMentoriaJava("A Empresa deve ser Informada");
			
		}
		
		if (categoriaProduto.getId() == null && categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc())) {
			throw new ExceptionMentoriaJava("Nao pode cadastrar categoria com mesmo nome");
		}
		
		CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);
		
		CategoriaProdutoDto categoriaProdutoDto = new CategoriaProdutoDto();
		categoriaProdutoDto.setId(categoriaSalva.getId());
		categoriaProdutoDto.setNomeDesc(categoriaSalva.getNomeDesc());
		categoriaProdutoDto.setEmpresa(categoriaSalva.getEmpresa().getId().toString());
		
		return new ResponseEntity<CategoriaProdutoDto>(categoriaProdutoDto, HttpStatus.OK);
		
	}

}
