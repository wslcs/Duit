package br.com.wslcs.duit.view.components;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

import br.com.wslcs.duit.model.Task;
import br.com.wslcs.duit.repository.TaskRepository;

public class TaskDataProvider extends AbstractBackEndDataProvider<Task, TaskFilter> {

    public final TaskRepository taskRepository;

    public TaskDataProvider(TaskRepository taskRepository) {

        this.taskRepository = taskRepository;

    }
    @Override
    protected int sizeInBackEnd(Query<Task, TaskFilter> query) {
        return (int) fetchFromBackEnd(query).count();
    }

    @Override
    protected Stream<Task> fetchFromBackEnd(Query<Task, TaskFilter> query) {
        // A real app should use a real database or a service
        // to fetch, filter and sort data.
        Stream<Task> stream = taskRepository.findAll().stream();

        // Filtering
        if (query.getFilter().isPresent()) {
            stream = stream.filter(person -> query.getFilter().get().test(person));
        }

        // Sorting
        if (query.getSortOrders().size() > 0) {
            stream = stream.sorted(sortComparator(query.getSortOrders()));
        }

        // Pagination
        return stream.skip(query.getOffset()).limit(query.getLimit());
    }

    private static Comparator<Task> sortComparator(List<QuerySortOrder> sortOrders) {
        return sortOrders.stream().map(sortOrder -> {
            Comparator<Task> comparator = taskFieldComparator(sortOrder.getSorted());

            if (sortOrder.getDirection() == SortDirection.DESCENDING) {
                comparator = comparator.reversed();
            }

            return comparator;
        }).reduce(Comparator::thenComparing).orElse((p1, p2) -> 0);
    }

    private static Comparator<Task> taskFieldComparator(String sorted) {
        if (sorted.equals("name")) {
            return Comparator.comparing(task -> task.getStatus());
        } else if (sorted.equals("profession")) {
            return Comparator.comparing(task -> task.getStatus());
        }
        return (p1, p2) -> 0;
    }

}
