package br.com.wslcs.duit.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style.AlignSelf;
import com.vaadin.flow.router.Route;
import br.com.wslcs.duit.dto.inputdata.InputTaskRecord;
import br.com.wslcs.duit.model.Task;
import br.com.wslcs.duit.repository.TaskRepository;
import br.com.wslcs.duit.service.TaskService;
import br.com.wslcs.duit.view.components.TaskDataProvider;
import br.com.wslcs.duit.view.components.TaskFilter;

@Route("")
public class TasksView extends VerticalLayout {

    private final TaskRepository taskRepository;

    private TaskFilter personFilter = new TaskFilter();

    private TaskDataProvider dataProvider;

    private ConfigurableFilterDataProvider<Task, Void, TaskFilter> filterDataProvider;

    @Autowired
    private TaskService taskService;

    public TasksView(TaskRepository taskRepository) {

        this.taskRepository = taskRepository;
        dataProvider = new TaskDataProvider(taskRepository);
        filterDataProvider = dataProvider
                .withConfigurableFilter();

        VerticalLayout todosList = new VerticalLayout();
        Button addButton = new Button("Adicionar");
        addButton.addClickShortcut(Key.ENTER);

        add(
                new H1("Du!t"),
                new HorizontalLayout(new H2("Nova Tarefa")));

        // Form

        TextField title = new TextField("Título");
        TextArea textArea = createTextArea();

        FormLayout formLayout = new FormLayout();
        formLayout.add(title, textArea);
        formLayout.setColspan(title, 2);
        formLayout.setColspan(textArea, 2);
        formLayout.setWidth(50, Unit.VW);

        add(formLayout);
        add(addButton);

        formLayout.getStyle().setAlignSelf(AlignSelf.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        Grid<Task> grid = getTasks();
        GridDataView<Task> gridDataView = grid.setItems(filterDataProvider);

        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> gridDataView.refreshAll());



        addButton.addClickListener(click -> {

            grid.setItems(taskService.save(new InputTaskRecord(title.getValue(), textArea.getValue())));
            title.clear();
            textArea.clear();

        });
        VerticalLayout layout = new VerticalLayout(searchField, grid);
        layout.setPadding(false);

        grid.getListDataView().addFilter(task -> {
            String searchTerm = searchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesStatus = matchesTerm(task.getStatus(),
                    searchTerm);
            boolean matchesCreationDate = matchesTerm(task.getTitle(), searchTerm);

            return matchesStatus || matchesCreationDate;
        });

        add(todosList);
        add(layout);
        add(grid);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private TextArea createTextArea() {
        TextArea textArea = new TextArea("Descriçao");
        textArea.setWidthFull();
        textArea.setMinHeight("100px");
        textArea.setMaxHeight("150px");
        textArea.setLabel("Description");
        return textArea;
    }

    private Select<String> createSelect() {
        Select<String> select = new Select<>();
        select.setLabel("Status");
        select.setItems("Pendente", "Finalizada");
        select.setValue("Most recent first");

        return select;
    }

    private Grid<Task> getTasks() {

        // List<Task> tasks = new ArrayList<>();
        Grid<Task> grid = new Grid<>(Task.class, false);
        grid.addColumn(Task::getTitle).setHeader("Titulo");
        grid.addColumn(Task::getStatus).setHeader("Status");
        grid.addColumn(Task::getOwnerName).setHeader("Dono");
        return grid;

    }

}
