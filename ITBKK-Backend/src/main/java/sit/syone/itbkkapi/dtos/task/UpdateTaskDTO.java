package sit.syone.itbkkapi.dtos.task;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import sit.syone.itbkkapi.entities.Status;
import sit.syone.itbkkapi.validations.annotations.StatusExists;

@Data
public class UpdateTaskDTO {
    @NotNull(message = "Task title is required")
    @NotEmpty(message = "Task title must not be empty")
    @Size(max = 100, message = "size must be between 0 and 100")
    private String title;
    @Size(max = 500, message = "size must be between 0 and 500")
    private String description;
    @Size(max = 30, message = "size must be between 0 and 30")
    private String assignees;
    @Transient
    @StatusExists
    private Integer statusId;
}
