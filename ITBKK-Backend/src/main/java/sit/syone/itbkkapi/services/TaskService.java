package sit.syone.itbkkapi.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sit.syone.itbkkapi.dtos.task.CreateTaskDTO;
import sit.syone.itbkkapi.dtos.task.UpdateTaskDTO;
import sit.syone.itbkkapi.entities.Status;
import sit.syone.itbkkapi.entities.Task;
import sit.syone.itbkkapi.repositories.StatusRepository;
import sit.syone.itbkkapi.repositories.TaskRepository;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ConstantService constantService;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<Task> getTasksByStatuses(List<String> statuses) {


        if (statuses == null || statuses.isEmpty()) {
            return taskRepository.findAll();
        }
        return taskRepository.findByStatus_NameIn(statuses);
    }


    @Transactional(readOnly = true)
    public Task getTaskById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task " + id + " does not exist !!!"));
    }

    @Transactional
    public Task createTask(CreateTaskDTO task) {
        // Check if title, status is empty
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.");
        }
        if (!statusRepository.existsById(task.getStatusId())) {
            task.setStatusId(1);
        }
        Task newTask = modelMapper.map(task, Task.class);
        newTask.setStatus(statusRepository.findById(task.getStatusId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status " + task.getStatusId() + " does not exist.")));
        Boolean isLimitEnabled = newTask.getStatus().getLimitEnabled();
        if (isLimitEnabled) {
            Integer limit = constantService.getConstValue("GStatLim");
            if (getAllTaskWithStatus(newTask.getStatus()).size() >= limit) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task limit exceeded.");
            }
        }
        try {
            // Save task
            return taskRepository.save(newTask);
        } catch (DataIntegrityViolationException e) {
            // Handle specific constraint violation (e.g., unique constraint)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save task. Ensure data integrity.");
        } catch (Exception e) {
            // Handle any other unexpected errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional
    public Task deleteTask(Integer id) {
        // Check if task exists
        Task existingtask = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " does not exist."));
        // Delete task
        try {
            taskRepository.delete(existingtask);
            return existingtask;
        } catch (Exception e) {
            // Handle any unexpected errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete task.", e);
        }
    }

    @Transactional
    public Task updateTask(Integer id, UpdateTaskDTO task) {
        // Check if task exists
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " does not exist.")); //source for comparison
        Status newStatus = (statusRepository.findById(task.getStatusId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status " + task.getStatusId() + " does not exist.")); //Get the new status to upd
        // Check updated values is valid
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.");
        }
        else if (task.getStatusId() == null) {
            newStatus = (statusRepository.findById(1)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status 1 does not exist.")));
        }
        Boolean isLimitEnabled = newStatus.getLimitEnabled();
        if (isLimitEnabled && !existingTask.getStatus().getId().equals(newStatus.getId())) {
            Integer limit = constantService.getConstValue("GStatLim");
            if (getAllTaskWithStatus(newStatus).size() >= limit) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task limit exceeded.");
            }
        }
        // Update title
        existingTask.setTitle(task.getTitle());
        // Update description
        existingTask.setDescription(task.getDescription());
        // Update assignees
        existingTask.setAssignees(task.getAssignees());
        // Update status
        existingTask.setStatus(newStatus);

        try {
            // Save updated task
            return taskRepository.save(existingTask);
        } catch (Exception e) {
            // Handle any unexpected errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update task.", e);
        }
    }

    @Transactional
    public List<Task> saveAllTasks(List<Task> tasks) {
        tasks.forEach(task -> {
            trimTask(task);
            if (task.getStatus() == null) {
                task.setStatus(statusRepository.findById(1)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status 1 does not exist.")));
            }
            if (task.getStatus().getLimitEnabled()) {
                Integer limit = constantService.getConstValue("GStatLim");
                if (getAllTaskWithStatus(task.getStatus()).size() >= limit) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task limit exceeded.");
                }
            }
        });
        try {
            return taskRepository.saveAll(tasks);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save tasks.", e);
        }
    }

    public List<Task> getAllTaskWithStatus(Status status) {
        return taskRepository.findAllByStatus(status);
    }

    public Task trimTask(Task task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.");
        }
        task.setTitle(task.getTitle().trim());
        task.setDescription(task.getDescription() != null ? task.getDescription().trim() : null);
        task.setAssignees(task.getAssignees() != null ? task.getAssignees().trim() : null);
        return task;
    }

    public CreateTaskDTO trimTask(CreateTaskDTO task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.");
        }
        task.setTitle(task.getTitle().trim());
        task.setDescription(task.getDescription() != null ? task.getDescription().trim() : null);
        task.setAssignees(task.getAssignees() != null ? task.getAssignees().trim() : null);
        return task;
    }

    public UpdateTaskDTO trimTask(UpdateTaskDTO task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.");
        }
        task.setTitle(task.getTitle().trim());
        task.setDescription(task.getDescription() != null ? task.getDescription().trim() : null);
        task.setAssignees(task.getAssignees() != null ? task.getAssignees().trim() : null);
        return task;
    }
}
