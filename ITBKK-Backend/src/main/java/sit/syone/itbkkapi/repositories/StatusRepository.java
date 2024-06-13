package sit.syone.itbkkapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.syone.itbkkapi.entities.Status;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    boolean existsByName(String name);
    List<Status> findAllByName(String name);
}
