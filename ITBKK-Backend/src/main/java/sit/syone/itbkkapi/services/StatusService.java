package sit.syone.itbkkapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sit.syone.itbkkapi.primarydatasource.entities.Status;
import sit.syone.itbkkapi.primarydatasource.repositories.StatusRepository;
import sit.syone.itbkkapi.primarydatasource.repositories.TaskRepository;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ConstantService constantService;

    @Transactional(readOnly = true)
    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Status getStatusById(Integer id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status " + id + " does not exist !!!"));
    }

    @Transactional
    public Status createStatus(Status status) {
        // Check if name is empty
        if (status.getName() == null || status.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required.");
        }
        if (statusRepository.existsByName(status.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name (" + status.getName() + ") already exists.");
        }
        try {
            // Save status
            if (status.getLimitEnabled() == null) {
                status.setLimitEnabled(false);
            }
            status.setCustomizable(true);
            return statusRepository.save(status);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save status.", e);
        }
    }

    @Transactional
    public void deleteStatus(Integer id) {
        Status status = statusRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "status " + id + " does not exist !!!"));
        if (!status.getCustomizable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete status " + status.getName() + " !!!");
        }
        if (!taskService.getAllTaskWithStatus(status).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete status " + status.getName() + " because it is in use !!!");
        }
        try {
            statusRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete status.", e);
        }
    }

    @Transactional
    public void deleteAndReplaceStatus(Integer deleteId, Integer replaceId) {
        // Check if status exists
        if (deleteId.equals(replaceId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "destination status for task transfer must be different from current status");
        }
        Status deleteStatus = statusRepository.findById(deleteId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "status " + deleteId + " does not exist !!!"));
        if (!deleteStatus.getCustomizable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete status " + deleteStatus.getName() + " !!!");
        }
        Status replaceStatus = statusRepository.findById(replaceId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "status " + replaceId + " does not exist !!!"));

        // fetch all tasks with deleteStatus

        Integer delStatTaskNum = taskRepository.countByStatus(deleteStatus.getId());
        Integer repStatTaskNum = taskRepository.countByStatus(replaceStatus.getId());
        Integer constLimit = constantService.getConstValue("GStatLim");

        if(replaceStatus.getLimitEnabled()){
            if(delStatTaskNum + repStatTaskNum > constLimit) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot replace status " + deleteStatus.getName() + " with status " + replaceStatus.getName() + " because it will exceed the limit of 10 tasks !!!");
            }
        }
        try {
            // Replace all deleteStatus in tasks with replaceStatus
            taskRepository.transferStatusTasks(deleteStatus.getId(), replaceStatus.getId());
            // after replacing all deleteStatus in tasks, delete deleteStatus
            statusRepository.deleteById(deleteId);
        } catch (Exception e) {
            throw e;
        }


//        List<Task> taskList = taskRepository.findAllByStatus(deleteStatus);
//        List<Task> replaceTaskList = taskRepository.findAllByStatus(replaceStatus);
//        Integer constLimit = constantService.getConstValue("GStatLim");
//        if (replaceStatus.getLimitEnabled()) {
//            if (taskList.size() + replaceTaskList.size() > constLimit) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot replace status " + deleteStatus.getName() + " with status " + replaceStatus.getName() + " because it will exceed the limit of 10 tasks !!!");
//            }
//        }
//        try {
//            // Replace all deleteStatus in tasks with replaceStatus
//            for (Task task : taskList) {
//                task.setStatus(replaceStatus);
//            }
//            taskRepository.saveAll(taskList);
//            // after replacing all deleteStatus in tasks, delete deleteStatus
//            statusRepository.deleteById(deleteId);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete status.", e);
//        }
    }

    @Transactional
    public Status updateStatus(Integer id, Status updateStatus) {
        // Check if status exists
        Status existingStatus = statusRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "status " + id + " does not exist !!!"));
        // set existing status value
        if (!existingStatus.getCustomizable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update status (" + existingStatus.getName() + ") !!!");
        }
        // check if name is empty
        if (updateStatus.getName() == null || updateStatus.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required.");
        }
//        if (statusRepository.existsByName(updateStatus.getName()) && !updateStatus.getId().equals(existingStatus.getId())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name (" + updateStatus.getName() + ") already exists.");
//        }
        if(statusRepository.findAllByName(updateStatus.getName()).stream().anyMatch(status -> !status.getId().equals(updateStatus.getId()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name (" + updateStatus.getName() + ") already exists.");
        }
        // set updated values
        existingStatus.setName(updateStatus.getName());
        existingStatus.setDescription(updateStatus.getDescription());
        existingStatus.setLimitEnabled(updateStatus.getLimitEnabled());
        existingStatus.setCustomizable(updateStatus.getCustomizable());
        try {
            // save updated status
            return statusRepository.save(existingStatus);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update status.", e);
        }
    }

    public Integer checkIsNotInUsed(Integer id) {
        Status status = statusRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "status " + id + " does not exist !!!"));
        return taskService.getAllTaskWithStatus(status).size();
    }

    public Status trimStatus(Status status) {
        if (status.getName() == null || status.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name is required");
        }
        String trimmedName = status.getName().trim();
        String trimmedDescription = status.getDescription() != null ? status.getDescription().trim() : null;
        status.setName(trimmedName);
        status.setDescription(trimmedDescription);
        return status;
    }

    public Map<Status, Integer> getAllStatUsage() {
        Map usageMap = new HashMap();
        List<Status> statuses = getAllStatuses();
        statuses.forEach(status -> {
            usageMap.put(status.getId(), checkIsNotInUsed(status.getId()));
        });
        return usageMap;
    }
}
