package br.com.wslcs.duit.dto.viewdata;

import br.com.wslcs.duit.model.Task;

public record ViewTaskRecord(Long id, String title, String description, String status, String creationDate,
        Long userId, String ownerName) {

    public ViewTaskRecord(Task task, String ownerName) {
        this(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getCreationDate(),
                task.getUserId(), ownerName);
    }

}
