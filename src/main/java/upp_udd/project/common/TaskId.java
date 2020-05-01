package upp_udd.project.common;

public enum TaskId {

    SELECT_REVIEWERS_AND_EDITORS("sid-8456BD8B-23D8-4FC4-BEF6-37E615B4D6D7"),
    REVIEWER_APPROVAL("sid-88DD0656-4A78-484E-AAE9-F03F22F104E0"),
    MAGAZINE_APPROVAL("sid-2DEB90B3-15C3-4300-ACC9-4994068F1A82");

    private String taskId;

    TaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}
