package br.com.wslcs.duit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.wslcs.duit.model.Task;

public interface TaskRepository extends JpaRepository<Task,Long>{

    
}
