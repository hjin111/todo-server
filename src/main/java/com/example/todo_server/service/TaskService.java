package com.example.todo_server.service;

import com.example.todo_server.constants.TaskStatus;
import com.example.todo_server.model.Task;
import com.example.todo_server.persist.TaskRepository;
import com.example.todo_server.persist.entity.TaskEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // 요청 받은 데이터를 DB에 저장하고 DB에 저장 한 결과를 반환해주는 역할을 하는 메서드 구현
    public Task add(String title, String description, String dueDate) {
        // title, description, dueDate 를 input 으로 받아서 데이터 베이스에 저장하는 역할을 담당 저장 된 결과는 Task 인스턴스로 반환을 함
        var e = TaskEntity.builder()
                .title(title)
                .description(description)
                .dueDate(Date.valueOf(dueDate)) // String 타입의 날짜를 Date 타입으로 변환
                .status(TaskStatus.TODO)
                .build();

        var saved = this.taskRepository.save(e); // TaskEntity 객체를 데이터베이스에 저장을 해 주고
        return entityToObject(saved); // 저장 된 TaskEntity 객체를 다시 DTO 형태 클래스로 변환을 해서 반환을 해주도록 함
    }

    private Task entityToObject(TaskEntity e) {
        return Task.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .status(e.getStatus())
                .dueDate(e.getDueDate().toString())
                // createdAt을 Timestamp에서 LocalDateTime으로 변환
                .createdAt(e.getCreatedAt().toLocalDateTime())
                // updatedAt도 Timestamp에서 LocalDateTime으로 변환
                .updatedAt(e.getUpdatedAt().toLocalDateTime())
                .build();
    }

}
