package com.example.project_management.dto;

import java.util.List;

public class TaskAssignDTO {
    private List<Long> userIds;
    
    public TaskAssignDTO() {}
    
    public TaskAssignDTO(List<Long> userIds) {
        this.userIds = userIds;
    }
    
    public List<Long> getUserIds() {
        return userIds;
    }
    
    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
