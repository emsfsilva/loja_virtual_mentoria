package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

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
import jdev.mentoria.lojavirtual.enums.TipoPessoa;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.model.dto.ConsultaCnpjDto;
import jdev.mentoria.lojavirtual.repository.PessoaFisicaRepository;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.service.PessoaUserService;
import jdev.mentoria.lojavirtual.service.ServiceContagemAcessoApi;
import jdev.mentoria.lojavirtual.util.ValidaCNPJ;
import jdev.mentoria.lojavirtual.util.ValidaCPF;

@RestController /* Essa anotação indica que sera gerada uma API REST retornando um JSON */
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository; // Esta linha vai bucar a query de consulta no banco

	@Autowired
	private PessoaUserService pessoaUserService; // Esta linha vai bucar o metodo salvar
	
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private ServiceContagemAcessoApi contagemAcessoApi;
	
	
	
	
	
	@ResponseBody
	@GetMapping(value = "**/consultaPfNome/{nome}")
	public ResponseEntity<List<PessoaFisica>> consultaPfNome (@PathVariable("nome") String nome) {
		
		List<PessoaFisica> fisicas = pessoaFisicaRepository.pesquisaPorNomePF(nome.trim().toUpperCase());
		
		contagemAcessoApi.atualizarAcessoEndPointPF();
		
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaPfCpf/{cpf}")
	public ResponseEntity<List<PessoaFisica>> consultaPfCpf (@PathVariable("cpf") String cpf) {
		
		List<PessoaFisica> fisicas = pessoaFisicaRepository.pesquisaPorCpfPF(cpf);
		
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaNomePJ/{nome}")
	public ResponseEntity<List<PessoaJuridica>> consultaNomePJ (@PathVariable("nome") String nome) {
		
		List<PessoaJuridica> fisicas = pessoaRepository.pesquisaPorNomePJ(nome.trim().toUpperCase());
		
		return new ResponseEntity<List<PessoaJuridica>>(fisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaCnpjPJ/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> consultaCnpjPJ (@PathVariable("cnpj") String cnpj) {
		
		List<PessoaJuridica> fisicas = pessoaRepository.existeCnpjCadastradoList(cnpj.trim().toUpperCase());
		
		return new ResponseEntity<List<PessoaJuridica>>(fisicas, HttpStatus.OK);
	}
	
	
	
	// CONSULTAR CEP
	//	@ResponseBody
	//	@GetMapping(value = "**/consultarCep/{cep}")
	//	public ResponseEntity<CepDTO> consultaCep (@PathVariable("cep") String cep) {
	//	return new ResponseEntity<CepDTO>(pessoaUserService.consultaCep(cep), HttpStatus.OK);
	//	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaCnpjReceitaWs/{cnpj}")
	public ResponseEntity<ConsultaCnpjDto> consultaCnpjReceitaWs (@PathVariable("cnpj") String cnpj) {
		return new ResponseEntity<ConsultaCnpjDto>(pessoaUserService.consultaCnpjReceitaWs(cnpj), HttpStatus.OK);
		
	}
	
	
	
	

	@ResponseBody
	@PostMapping(path = "**/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica)
			throws ExceptionMentoriaJava {
		
		if (pessoaJuridica == null) {
			throw new ExceptionMentoriaJava("Pessoa Juridica não pode ser NULL");
		}
		
		if (pessoaJuridica.getTipoPessoa() == null) {
			throw new ExceptionMentoriaJava("Informe o tipo Juridica ou Fornecedor da Loja");
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
		if (pessoaFisica.getTipoPessoa() == null) {
			pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
			
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
