package com.example.todo_server.service;

import com.example.todo_server.constants.TaskStatus;
import com.example.todo_server.model.Task;
import com.example.todo_server.persist.TaskRepository;
import com.example.todo_server.persist.entity.TaskEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // 요청 받은 데이터를 DB에 저장하고 DB에 저장 한 결과를 반환해주는 역할을 하는 메서드 구현
    public Task add(String title, String description, LocalDate dueDate) {
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

    // 데이터 모든 목록을 조회하는 메서드
    public List<Task> getAll() {
        return this.taskRepository.findAll().stream() // stream 을 이용해서 TaskEntity 객체를 Task 객체로 매핑해주겠음
                .map(this::entityToObject) // map 에서 TaskEntity 객체를 Task 객체로 변환을 할 건데 entityToObject 메서드 호출 해주기
                .collect(Collectors.toList()); // 마지막으로 collect 메서드를 이용해서 Task 로 변환 된 객체를 List 형태로 반환을 해주면 됨
    }

    // 마감일에 해당하는 할 일 목록을 조회하는 메서드
    public List<Task> getByDueDate(String dueDate) {
        return this.taskRepository.findAllByDueDate(Date.valueOf(dueDate)).stream()
                .map(this::entityToObject)
                .collect(Collectors.toList());
    }

    // status 에 해당하는 할 일 목록을 조회 하는 메서드
    public List<Task> getByStatus(TaskStatus status) {
        return this.taskRepository.findAllByStatus(status).stream()
                .map(this::entityToObject)
                .collect(Collectors.toList());
    }

    // 특정 id에 해당하는 할 일 데이터를 조회 하는 메서드
    public Task getOne(Long id) {
        var entity = this.getById(id);
        return this.entityToObject(entity);
    }

    // 특정 id에 해당하는 Task 전부 수정 해 주는 메서드
    public Task update(Long id, String title, String description, LocalDate dueDate) {
        var exists = this.getById(id);

        // input 으로 들어 온 title의 값이 null 이거나 빈 문자열이면 기존에 있는 title 유지해주고
        // 만약 title 이 존재 하는 경우에는 해당 값으로 title 필드 update
        exists.setTitle(Strings.isEmpty(title) ? exists.getTitle() : title);

        exists.setDescription(Strings.isEmpty(description) ? exists.getDescription() : description);
        exists.setDueDate(Objects.isNull(dueDate) ?
                exists.getDueDate() : Date.valueOf(dueDate)); // 만약 null 이라면 기존에 데이터를 유지해주고 아니라면 Date.valueOf로 타입을 바꿔서 필드를 update 해준다

        var updated = this.taskRepository.save(exists);
        return this.entityToObject(updated); // entityToObject 이 메서드로 Task 객체로 매핑한 후에 반환을 해주도록 한다
    }

    // 특정 id에 해당하는 Task 상태를 업데이트 해 주는 메서드
    public Task updateStatus(Long id, TaskStatus status) {

        var entity = this.getById(id);
        entity.setStatus(status);
        var saved = this.taskRepository.save(entity);
        return this.entityToObject(saved);

    }

    // delete 메서드
    public boolean delete(Long id) {

        try{
            this.taskRepository.deleteById(id);
        }catch (Exception e){
            log.error("an error occurred while deleting [{}]", e.toString());
            return false;
        }

        return true;

    }


    private TaskEntity getById(Long id) {
     return this.taskRepository.findById(id) // Optional 타입을 반환을 함.
                                             // 해당 id의 데이터가 db에 존재하지 않는 경우가 있을 수 있기 때문에 null이 될수 있다
             .orElseThrow(() -> new IllegalArgumentException(String.format("not exists task id [%d]",id))); // 값이 없는 경우
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
