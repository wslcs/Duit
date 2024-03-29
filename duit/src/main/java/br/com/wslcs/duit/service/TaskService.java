package br.com.wslcs.duit.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wslcs.duit.dto.inputdata.InputTaskRecord;
import br.com.wslcs.duit.model.Task;
import br.com.wslcs.duit.model.User;
import br.com.wslcs.duit.repository.TaskRepository;
import br.com.wslcs.duit.repository.UserRepository;
import jakarta.validation.Valid;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task save(@Valid InputTaskRecord inputTaskRecord) {

        // return new ViewTaskRecord(taskRepository.save(task));
        return taskRepository.save(new Task(inputTaskRecord));
    }


    public Optional<User> getUserName(Long userId){
        return userRepository.findById(userId);
    }

}
