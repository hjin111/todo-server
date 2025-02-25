package com.example.todo_server.persist;

import com.example.todo_server.constants.TaskStatus;
import com.example.todo_server.persist.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findAllByDueDate(Date dueDate);

    // Status 기준
    List<TaskEntity> findAllByStatus(TaskStatus status);
}
