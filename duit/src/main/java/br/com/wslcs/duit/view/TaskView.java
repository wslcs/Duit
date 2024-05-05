package br.com.wslcs.duit.view;

import java.util.Optional;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style.AlignSelf;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import br.com.wslcs.duit.model.Task;
import br.com.wslcs.duit.repository.TaskRepository;

@Route("task")
public class TaskView extends VerticalLayout implements BeforeEnterObserver {

    private Task task;
    private final TaskRepository taskRepository;

    public TaskView(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

        setAlignItems(FlexComponent.Alignment.CENTER);

        add(new H1("Task Titulo"));
        add(createForm());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        System.out.println("before Enter");

        QueryParameters params = event.getLocation().getQueryParameters();
        Optional<String> param = params.getSingleParameter("tid");
        this.task = taskRepository.findById(Long.valueOf(param.get())).get();
        System.out.println(task);
    }

    private TextArea createTextArea() {
        TextArea textArea = new TextArea("Descriçao");
        textArea.setWidthFull();
        textArea.setMinHeight("100px");
        textArea.setMaxHeight("150px");
        textArea.setLabel("Description");
        return textArea;
    }

    private FormLayout createForm() {
        TextField title = new TextField(task.getTitle());
        TextArea textArea = createTextArea();

        FormLayout formLayout = new FormLayout();
        formLayout.add(title, textArea);
        formLayout.setColspan(title, 2);
        formLayout.setColspan(textArea, 2);
        formLayout.setWidth(50, Unit.VW);
        formLayout.getStyle().setAlignSelf(AlignSelf.CENTER);
        return formLayout;

    }

}
//criar o atributo do form na tela, da UI e depois usar o metodo access da UI para modificar a UI dentro do beforeEnter.