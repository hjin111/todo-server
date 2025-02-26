package com.example.todo_server.web;

import com.example.todo_server.constants.TaskStatus;
import com.example.todo_server.model.Task;
import com.example.todo_server.service.TaskService;
import com.example.todo_server.web.vo.ResultResponse;
import com.example.todo_server.web.vo.TaskRequest;
import com.example.todo_server.web.vo.TaskStatusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j // 로그 사용
@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskContoller {

    private final TaskService taskService;

    /**
     * 새로운 할 일 추가
     * @param req 추가하고자 하는 할 일
     * @return 추가된 할 일
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest req) { // RequestBody 를 사용해서 요청 body 로 TaskRequest 를 받도록 함
        var result = this.taskService.add(req.getTitle(), req.getDescription(), String.valueOf(req.getDueDate())); // Task 인스턴스 반환
        return ResponseEntity.ok(result); // ResponseEntiy 객체를 생성을 해서 이 result 를 http 응답 body 에다 담아 클라이언트에 반환해줌
    }

    /**
     * 특정 마감일에 해당하는 할일 목록을 반환
     * @param dueDate 할 일의 마감일
     * @return 마감일에 해당하는 할일 목록
     */
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(Optional<String> dueDate) { // Optional 타입으로 받기 때문에 값은 있을 수도 있고 없을 수도 있다

        List<Task> result;
        if (dueDate.isPresent()) { // 마감일이 있는 경우
            result = this.taskService.getByDueDate(dueDate.get());
        } else { // 마감일이 없는 경우는 모든 데이터 목록을 가져옴
            result = this.taskService.getAll();
        }

        return ResponseEntity.ok(result);
    }

    /**
     * 특정 ID에 해당하는 할 일을 조회
     * @param id 할 일 ID
     * @return ID에 해당하는 할일 객체
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> fetchOneTask(@PathVariable Long id) {
        var result = this.taskService.getOne(id);
        return ResponseEntity.ok(result);
    }

    /**
     * 특정 상태에 해당하는 할일 목록을 반환
     * @param status 할일 상태
     * @return 상태에 해당하는 할일 목록
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getByStatus(@PathVariable TaskStatus status) {
        var result = this.taskService.getByStatus(status);
        return ResponseEntity.ok(result);
    }

    /**
     * 특정 ID에 해당하는 할 일을 수정
     * @param id 할일 ID
     * @param task 수정할 할일 정보
     * @return 수정 된 할일 객체
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @RequestBody TaskRequest task) {
        var result = this.taskService.update(id,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate());

        return ResponseEntity.ok(result);
    }

    /**
     * 특정 ID에 해당하는 할일의 상태를 수정 ( TASK의 상태를 수정하기 위한 API )
     * @param id 할일 ID
     * @param req 수정할 할일 상태 정보
     * @return 수정된 할일 객체
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id,
                                                 @RequestBody TaskStatusRequest req) {
        var result = this.taskService.updateStatus(id, req.getStatus());
        return ResponseEntity.ok(result);
    }

    /**
     * 특정 ID에 해당하는 할일을 삭제
     * @param id 삭제할 할일 ID
     * @return 삭제 결과를 담은 응답 객체
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse> deleteTask(@PathVariable Long id){
        var result = this.taskService.delete(id);
        return ResponseEntity.ok(new ResultResponse(result));
    }


    @GetMapping("/status")
    public ResponseEntity<TaskStatus[]> getAllStatus(){
        var status = TaskStatus.values(); // 모든 status 들이 나온다
        return ResponseEntity.ok(status);
    }

}
