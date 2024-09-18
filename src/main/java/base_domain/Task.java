package base_domain;

import java.time.LocalDateTime;
import java.util.List;

public class Task extends Labeled{
    private String materialId;
    private String materialDescription;
    private int unclearedQuantity;
    private TaskType taskType;
    private List<Operation> craftPath;
    private LocalDateTime dueDate;

    public Task(String id, String materialId, String materialDescription, int unclearedQuantity, TaskType taskType, List<Operation> craftPath,LocalDateTime dueDate) {
        super(id);
        this.materialId = materialId;
        this.materialDescription = materialDescription;
        this.unclearedQuantity = unclearedQuantity;
        this.taskType = taskType;
        this.craftPath = craftPath;
        this.dueDate = dueDate;
    }

    public Task() {
        super(null);
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public int getUnclearedQuantity() {
        return unclearedQuantity;
    }

    public void setUnclearedQuantity(int unclearedQuantity) {
        this.unclearedQuantity = unclearedQuantity;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public List<Operation> getCraftPath() {
        return craftPath;
    }

    public void setCraftPath(List<Operation> craftPath) {
        this.craftPath = craftPath;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
