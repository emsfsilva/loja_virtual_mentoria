package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> { //Long é o tipoda variavel ID do usuario
	
	//Metodo para consutlar um usuario
	@Query(value = "select u from Usuario u where u.login = ?1") //u é variavel e "login" é o atributo na classe Usuario
	Usuario findUserByLogin (String login);
	
	@Query(value="select u from Usuario u where u.dataAtualSenha <= current_date - 90")
	List<Usuario> usuarioSenhaVencida();
	

	@Query(value = "select u from Usuario u where u.pessoa.id = ?1 or u.login = ?2")
	Usuario findUserByPessoa(Long id, String email);

	@Query(value = "select constraint_name from information_schema.constraint_column_usage\r\n"
			+ "where table_name = 'usuarios_acesso' and column_name = 'acesso_id' and constraint_name <> 'unique_acesso_user'; ", nativeQuery = true)
	String consultaConstraintAcesso();

	@Transactional
	@Modifying
	@Query(value = "insert into usuarios_acesso(usuario_id, acesso_id) values (?1, (select id from acesso where descricao = 'ROLE_USER'))", nativeQuery = true)
	void insereAcessoUser(Long idUser);
	
	@Transactional
	@Modifying
	@Query(value = "insert into usuarios_acesso(usuario_id, acesso_id) values (?1, (select id from acesso where descricao = ?2 limit 1))", nativeQuery = true)
	void insereAcessoUserPj(Long idUser, String acesso);
	

}
