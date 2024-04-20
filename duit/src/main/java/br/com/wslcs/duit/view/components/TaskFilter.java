package br.com.wslcs.duit.view.components;

import br.com.wslcs.duit.model.Task;

public class TaskFilter {
    

    private String searchTerm;

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public boolean test(Task task) {
        boolean matchesFullName = matches(task.getOwnerName(), searchTerm);
        boolean matchesProfession = matches(task.getStatus(), searchTerm);
        return matchesFullName || matchesProfession;
    }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }
}
