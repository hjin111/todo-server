package com.example.todo_server.web.vo;

import com.example.todo_server.constants.TaskStatus;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TaskStatusRequest {
    private TaskStatus status;
}
