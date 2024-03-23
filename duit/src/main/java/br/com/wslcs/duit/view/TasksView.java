package br.com.wslcs.duit.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style.AlignSelf;
import com.vaadin.flow.router.Route;

import br.com.wslcs.duit.dto.inputdata.InputTaskRecord;
import br.com.wslcs.duit.model.Task;
import br.com.wslcs.duit.service.TaskService;

@Route("")
public class TasksView extends VerticalLayout {

    @Autowired
    private TaskService taskService;

    public TasksView() {

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

        add(todosList);
        addButton.addClickListener(click -> {

            grid.setItems(taskService.save(new InputTaskRecord(title.getValue(), textArea.getValue())));
            title.clear();
            textArea.clear();
        });
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
        select.setItems("Pendente", "Finalizada");
        select.setValue("Most recent first");

        return select;
    }

    private Grid<Task> getTasks() {

        List<Task> tasks = new ArrayList<>();
        Grid<Task> grid = new Grid<>(Task.class, false);
        grid.addColumn(Task::getTitle).setHeader("Titulo");
        grid.addColumn(Task::getStatus).setHeader("Status");
        grid.addColumn(Task::getOwnerName).setHeader("Dono");
        grid.setItems(tasks);
        return grid;

    }

}
