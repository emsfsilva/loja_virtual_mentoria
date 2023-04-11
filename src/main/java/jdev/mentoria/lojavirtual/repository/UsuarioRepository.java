package jdev.mentoria.lojavirtual.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> { //Long é o tipoda variavel ID do usuario
	
	//Metodo para consutlar um usuario
	@Query("select u from Usuario u where u.login = ?1") //u é variavel e "login" é o atributo na classe Usuario
	Usuario findUserByLogin (String login);
	

}
