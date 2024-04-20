package br.com.wslcs.duit.view.components;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

import br.com.wslcs.duit.dto.viewdata.ViewTaskRecord;
import br.com.wslcs.duit.model.Task;
import br.com.wslcs.duit.repository.TaskRepository;
import br.com.wslcs.duit.repository.UserRepository;

public class TaskDataProvider extends AbstractBackEndDataProvider<ViewTaskRecord, TaskFilter> {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskDataProvider(TaskRepository taskRepository, UserRepository userRepository) {

        this.taskRepository = taskRepository;
        this.userRepository = userRepository;

    }

    @Override
    protected int sizeInBackEnd(Query<ViewTaskRecord, TaskFilter> query) {
        return (int) fetchFromBackEnd(query).count();
    }

    @Override
    protected Stream<ViewTaskRecord> fetchFromBackEnd(Query<ViewTaskRecord, TaskFilter> query) {
        // A real app should use a real database or a service
        // to fetch, filter and sort data.
        Stream<ViewTaskRecord> stream = taskRepository.findAll().stream()
                .map(t -> new ViewTaskRecord(t, userRepository.findById(t.getUserId()).get().getUserName()));

        // Filtering
        if (query.getFilter().isPresent()) {
            stream = stream.filter(task -> query.getFilter().get().test(task));
        }

        // Sorting
        if (query.getSortOrders().size() > 0) {
            stream = stream.sorted(sortComparator(query.getSortOrders()));
        }

        // Pagination
        return stream.skip(query.getOffset()).limit(query.getLimit());
    }

    private static Comparator<ViewTaskRecord> sortComparator(List<QuerySortOrder> sortOrders) {
        return sortOrders.stream().map(sortOrder -> {
            Comparator<ViewTaskRecord> comparator = taskFieldComparator(sortOrder.getSorted());

            if (sortOrder.getDirection() == SortDirection.DESCENDING) {
                comparator = comparator.reversed();
            }

            return comparator;
        }).reduce(Comparator::thenComparing).orElse((p1, p2) -> 0);
    }

    private static Comparator<ViewTaskRecord> taskFieldComparator(String sorted) {
        if (sorted.equals("userId")) {
            return Comparator.comparing(task -> task.userId());
        } else if (sorted.equals("status")) {
            return Comparator.comparing(task -> task.status());
        }
        return (p1, p2) -> 0;
    }

}
