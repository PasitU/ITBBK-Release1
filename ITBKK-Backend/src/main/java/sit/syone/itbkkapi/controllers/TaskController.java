package sit.syone.itbkkapi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.syone.itbkkapi.dtos.task.CreateTaskDTO;
import sit.syone.itbkkapi.dtos.task.DetailedTaskDTO;
import sit.syone.itbkkapi.dtos.task.SimpleTaskDTO;
import sit.syone.itbkkapi.dtos.task.UpdateTaskDTO;
import sit.syone.itbkkapi.primarydatasource.entities.Task;
import sit.syone.itbkkapi.services.TaskService;
import sit.syone.itbkkapi.util.ListMapper;

import java.util.List;

@RestController
@RequestMapping("/v3/tasks")
@CrossOrigin(origins = {"http://ip23sy1.sit.kmutt.ac.th:80", "http://localhost:5173", "http://intproj23.sit.kmutt.ac.th"})
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ListMapper listMapper;

    @GetMapping("")
    public ResponseEntity<List<SimpleTaskDTO>> getTasks(@RequestParam(required = false, value = "filterStatuses") List<String> filterStatuses) {
        return ResponseEntity.ok(listMapper.mapList(taskService.getTasksByStatuses(filterStatuses), SimpleTaskDTO.class, modelMapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailedTaskDTO> getTaskById(@PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(taskService.getTaskById(id), DetailedTaskDTO.class));
    }

    @PostMapping("")
    public ResponseEntity<CreateTaskDTO> createTask(@Valid @RequestBody CreateTaskDTO task) {
        CreateTaskDTO taskTrim = taskService.trimTask(task);
        Task createdTask = taskService.createTask(taskTrim);
        CreateTaskDTO taskDTO = modelMapper.map(createdTask, CreateTaskDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleTaskDTO> deleteTask(@PathVariable Integer id) {
        return ResponseEntity.ok(modelMapper.map(taskService.deleteTask(id), SimpleTaskDTO.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateTaskDTO> updatedTask(@PathVariable Integer id,@Valid @RequestBody UpdateTaskDTO task) {
        UpdateTaskDTO taskTrim = taskService.trimTask(task);
        Task updatedTask = taskService.updateTask(id, taskTrim);
        UpdateTaskDTO taskDTO = modelMapper.map(updatedTask, UpdateTaskDTO.class);
        return ResponseEntity.ok(taskDTO);
    }

    @PutMapping("")
    public List<Task> updatedAllTasks(@Valid @RequestBody List<Task> tasks) {
        return taskService.saveAllTasks(tasks);
    }
}
