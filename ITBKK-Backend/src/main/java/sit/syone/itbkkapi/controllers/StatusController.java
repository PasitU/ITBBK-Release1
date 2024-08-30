package sit.syone.itbkkapi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.syone.itbkkapi.primarydatasource.entities.Status;
import sit.syone.itbkkapi.services.StatusService;
import sit.syone.itbkkapi.util.ListMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v3/statuses")
@CrossOrigin(origins = {"http://ip23sy1.sit.kmutt.ac.th:80", "http://localhost:5173" ,"http://intproj23.sit.kmutt.ac.th"})
public class StatusController {
    @Autowired
    StatusService statusService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ListMapper listMapper;

    @GetMapping("")
    public List<Status> getAllStatuses() {
        return statusService.getAllStatuses();
    }

    @GetMapping("/{id}")
    public Status getStatusById(@PathVariable Integer id) {
        return statusService.getStatusById(id);
    }

    @PostMapping("")
    public ResponseEntity<Status> createStatus(@Valid @RequestBody Status status) {
        Status statusTrim = statusService.trimStatus(status);
        Status createdStatus = statusService.createStatus(statusTrim);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
    }

    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable Integer id) {
        statusService.deleteStatus(id);
    }

    @DeleteMapping("/{id}/{replaceId}")
    public void deleteStatus(@PathVariable Integer id, @PathVariable Integer replaceId) {
        statusService.deleteAndReplaceStatus(id, replaceId);
    }

    @PutMapping("/{id}")
    public Status updateStatus(@PathVariable Integer id,@Valid @RequestBody Status status) {
        Status statusTrim = statusService.trimStatus(status);
        return statusService.updateStatus(id, statusTrim);
    }

    @GetMapping("/usage/{id}")
    public Integer checkUsage(@PathVariable Integer id){
        return statusService.checkIsNotInUsed(id);
    }

    @GetMapping("/usage")
    public ResponseEntity<Map<Status, Integer>> checkAllUsage(){
        return ResponseEntity.ok(statusService.getAllStatUsage());
    }
}
