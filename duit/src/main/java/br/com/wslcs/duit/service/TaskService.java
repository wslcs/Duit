package br.com.wslcs.duit.service;

import org.springframework.stereotype.Service;

import br.com.wslcs.duit.dto.inputdata.InputTaskRecord;
import br.com.wslcs.duit.model.Task;
import br.com.wslcs.duit.repository.TaskRepository;
import br.com.wslcs.duit.repository.UserRepository;
import jakarta.validation.Valid;

@Service
public class TaskService {

    
    private final TaskRepository taskRepository;

    
    private final UserRepository userRepository;

    

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


    public Task save(@Valid InputTaskRecord inputTaskRecord) {

        // return new ViewTaskRecord(taskRepository.save(task));
        return taskRepository.save(new Task(inputTaskRecord));
    }


    public String getUserName(Long userId){
        return userRepository.findById(userId).get().getUserName();
    }
}
