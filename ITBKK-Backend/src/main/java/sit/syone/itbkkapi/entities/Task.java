package sit.syone.itbkkapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskID")
    private Integer id;
    @Column(name = "taskTitle")

    private String title;
    @Column(name = "taskDescription")

    private String description;
    @Column(name = "taskAssignees")

    private String assignees;
    @ManyToOne
    @JoinColumn(name = "taskStatus", referencedColumnName = "statusID")
    private Status status;
    @Column(name = "createdOn", insertable = false, updatable = false)
    private ZonedDateTime createdOn;
    @Column(name = "updatedOn", insertable = false, updatable = false)
    private ZonedDateTime updatedOn;
}
