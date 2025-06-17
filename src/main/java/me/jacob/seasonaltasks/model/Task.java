package me.jacob.seasonaltasks.model;

public class Task {
    private final String id;
    private final String name;
    private final String type;
    private final long target;
    private final String rewardType;
    private final String rewardValue;
    private final String message;

    public Task(String id, String name, String type, long target, String rewardType, String rewardValue, String message) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.target = target;
        this.rewardType = rewardType;
        this.rewardValue = rewardValue;
        this.message = message;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public long getTarget() { return target; }
    public String getRewardType() { return rewardType; }
    public String getRewardValue() { return rewardValue; }
    public String getMessage() { return message; }
}
