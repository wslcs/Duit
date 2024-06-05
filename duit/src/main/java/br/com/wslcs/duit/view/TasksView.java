package br.com.wslcs.duit.view;

import org.hibernate.query.QueryParameter;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import br.com.wslcs.duit.dto.inputdata.InputTaskRecord;
import br.com.wslcs.duit.dto.viewdata.ViewTaskRecord;
import br.com.wslcs.duit.model.Task;
import br.com.wslcs.duit.repository.TaskRepository;
import br.com.wslcs.duit.repository.UserRepository;
import br.com.wslcs.duit.service.TaskService;
import br.com.wslcs.duit.view.components.TaskDataProvider;
import br.com.wslcs.duit.view.components.TaskFilter;

@Route("")
public class TasksView extends VerticalLayout {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private TaskFilter taskFilter = new TaskFilter();

    private TaskDataProvider dataProvider;

    private ConfigurableFilterDataProvider<ViewTaskRecord, Void, TaskFilter> filterDataProvider;

    @Autowired
    private TaskService taskService;

    public TasksView(TaskRepository taskRepository, UserRepository userRepository) {

        this.taskRepository = taskRepository;
        this.userRepository = null;
        dataProvider = new TaskDataProvider(taskRepository, userRepository);
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

        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Data de criação, Dono ou Status");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> {

            taskFilter.setSearchTerm(e.getValue());
            filterDataProvider.setFilter(taskFilter);
        });

        Grid<ViewTaskRecord> grid = getTasks();

        add(todosList);
        addButton.addClickListener(click -> {

            Task task = taskService.save(new InputTaskRecord(title.getValue(), textArea.getValue()));
            grid.setItems(new ViewTaskRecord(task, userRepository.findById(task.getUserId()).get().getUserName()));
            title.clear();
            textArea.clear();
        });
        VerticalLayout layout = new VerticalLayout(searchField, grid);
        layout.setPadding(false);
        add(layout);
        add(grid);
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
        select.setItems("Pendente", "Concluída");
        select.setValue("Most recent first");

        return select;
    }

    private Grid<ViewTaskRecord> getTasks() {

        // List<Task> tasks = new ArrayList<>();
        Grid<ViewTaskRecord> grid = new Grid<>(ViewTaskRecord.class, false);
        grid.addColumn(ViewTaskRecord::id).setHeader("Código");
        grid.addColumn(ViewTaskRecord::title).setHeader("Titulo");
        grid.addColumn(ViewTaskRecord::status).setHeader("Status");
        grid.addColumn(ViewTaskRecord::ownerName).setHeader("Dono");
        grid.addColumn(ViewTaskRecord::creationDate).setHeader("Data de Criação");
        grid.addItemClickListener(e -> {

            getUI().ifPresent(
                    ui -> ui.navigate(TaskView.class, QueryParameters.of("tid", e.getItem().id().toString())));
        });
        grid.setItems(filterDataProvider);
        return grid;

    }
}
