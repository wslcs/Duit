package br.com.wslcs.duit.dto.viewdata;

import java.sql.Timestamp;

import br.com.wslcs.duit.model.Task;

public record ViewTaskRecord(Long id, String title, String description, String status, Timestamp creationDate,
        Long userId) {

    public ViewTaskRecord(Task task) {
        this(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreationDate(),
                task.getUserId());
    }

}
