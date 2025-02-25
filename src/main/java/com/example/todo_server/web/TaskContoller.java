package com.example.todo_server.web;

import com.example.todo_server.model.Task;
import com.example.todo_server.service.TaskService;
import com.example.todo_server.web.vo.TaskRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
