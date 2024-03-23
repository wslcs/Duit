package br.com.wslcs.duit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wslcs.duit.model.User;

public interface UserRepository extends JpaRepository<User,Long>{
    
}
