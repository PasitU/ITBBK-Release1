package sit.syone.itbkkapi.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sit.syone.itbkkapi.primarydatasource.entities.Status;
import sit.syone.itbkkapi.primarydatasource.entities.Task;

import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByStatus(Status status);

    List<Task> findByStatus_NameIn(Collection<String> statusNames);

    @Query(value = "select count(*) from tasks where taskStatus = :id ", nativeQuery = true)
    Integer countByStatus(Integer id);
//    List<Task> findByCreatedOnBetween(ZonedDateTime startDate, ZonedDateTime endDate);

    @Modifying
    @Query(value = "update tasks set taskStatus = :newStat where taskStatus = :delStat", nativeQuery = true)
    void transferStatusTasks(Integer delStat, Integer newStat);
}
