package br.com.wslcs.duit.view;

import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.Style.AlignSelf;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import br.com.wslcs.duit.model.Task;
import br.com.wslcs.duit.repository.TaskRepository;
import br.com.wslcs.duit.service.TaskService;

@Route("task")
public class TaskView extends VerticalLayout implements BeforeEnterObserver {

    private Task task;
    private FormLayout formLayout;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final UI ui;
    private Binder<Task> binder;
    private TextField titleField;
    private TextArea textArea;
    private Select<String> select;

    public TaskView(TaskRepository taskRepository, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;

        setAlignItems(FlexComponent.Alignment.CENTER);

        add(new H1("Editar Task"));
        this.ui = UI.getCurrent();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        System.out.println("before Enter");

        QueryParameters params = event.getLocation().getQueryParameters();
        Optional<String> param = params.getSingleParameter("tid");
        this.task = taskRepository.findById(Long.valueOf(param.get())).get();
        this.ui.access(() -> {
            add(createForm());
        });

    }

    private TextArea createTextArea() {
        TextArea textArea = new TextArea("Descriçao");
        textArea.setWidthFull();
        textArea.setMinHeight("100px");
        textArea.setMaxHeight("150px");
        textArea.setLabel("Descrição");
        textArea.setValue(task.getDescription());
        return textArea;
    }

    private FormLayout createForm() {
        titleField = new TextField("Título");
        titleField.setValue(task.getTitle());
        textArea = createTextArea();
        select = createSelect();

        FormLayout formLayout = new FormLayout();
        formLayout.add(select, titleField, textArea);
        formLayout.setColspan(titleField, 2);
        formLayout.setColspan(textArea, 2);
        formLayout.setWidth(50, Unit.VW);
        formLayout.getStyle().setAlignSelf(AlignSelf.CENTER);
        formLayout.add(createSaveButton(), 1);
        formLayout.add(createCancelButton(), 1);
        this.formLayout = formLayout;
        taskBinder();
        return this.formLayout;

    }

    private Select<String> createSelect() {
        select = new Select<String>();
        select.setLabel("Status");
        select.setItems("PENDENTE", "FINALIZADA");
        select.setValue(task.getStatus());

        return select;
    }

    private Binder<Task> taskBinder() {

        binder = new Binder<>(Task.class);
        binder.bind(titleField, Task::getTitle, Task::setTitle);
        binder.bind(textArea, Task::getDescription, Task::setDescription);
        binder.bind(select, Task::getStatus, Task::setStatus);
        binder.setBean(task);
        return binder;
    }

    private Button createSaveButton() {

        Button saveButton = new Button("Salvar");
        saveButton.addClickListener(e -> {
            taskService.updateTask(task);
            getUI().ifPresent(
                    ui -> ui.navigate(TasksView.class));
        });
        return saveButton;
    }

    private Button createCancelButton() {

        Button cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(e -> {
            getUI().ifPresent(
                    ui -> ui.navigate(TasksView.class));
        });
        return cancelButton;
    }
}
// pesquisar form data bind para alteração dos dados da entidade no formlário.
// criar cancelButton e usar ui.navigate para a home.