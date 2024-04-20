package br.com.wslcs.duit.view.components;

import br.com.wslcs.duit.dto.viewdata.ViewTaskRecord;
import br.com.wslcs.duit.model.Task;

public class TaskFilter {
    

    private String searchTerm;

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public boolean test(ViewTaskRecord task) {
        boolean matchesFullName = matches(task.ownerName(), searchTerm);
        boolean matchesStatus = matches(task.status(), searchTerm);
        boolean matchesCreateDate = matches(task.creationDate(), searchTerm);
        return matchesFullName || matchesStatus || matchesCreateDate;
    }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }
}
