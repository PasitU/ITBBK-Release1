package sit.syone.itbkkapi.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.syone.itbkkapi.primarydatasource.entities.Status;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    boolean existsByName(String name);
    List<Status> findAllByName(String name);
}
