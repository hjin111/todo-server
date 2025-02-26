package com.example.todo_server.service;

import com.example.todo_server.constants.TaskStatus;
import com.example.todo_server.persist.TaskRepository;
import com.example.todo_server.persist.entity.TaskEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    // Mock 어노테이션을 이용을 하면 Mock 객체를 생성 할 수 있다
    // Mock 객체를 생성을 하게 되면 해당 객체를 이용해서 실제 객체를 대체 하고 Mock 객체의 동작을 검증할 수 있다
    // 이렇게 생성된 Mock 객체는 실제 객체와 비슷한 동작을 수행 하지만 실제 객체와 다르게 미리 정의 된 동작을 수행 하고
    // 이렇게 테스트 코드를 구현 하면 해당 클래스나 라이브러리와 같은 의존성을 완전히 제거 할 수 있게 된다
    @Mock
    private TaskRepository taskRepository;

    // InjectMocks 어노테이션을 써 주게 되면 해당 클래스의 인스턴스 생성을 하면서 Mock 객체를 포함해서 모든 의존성을 여기에 자동으로 주입을 해 주게 됨
    @InjectMocks
    private TaskService taskService;

    // 단위 테스트는 독립적이어야 한다는 말과 같이 해당 클래스의 의존성을 완전히 제거하고 해당 클래스의 메서드가 예상대로 동작하는지를 검증을 해야 함
    // 그래서 Mock 객체를 써줌으로써 해당 클래스가 가진 의존성을 완전히 제거 해 주고 내가 검증하고자 하는 기능 그 자체만 테스트 해서 해당 기능이 예상대로 동작하는지를 검증할 수 있다
    @Test
    @DisplayName("할일 추가 기능 테스트")
    void add() {

        var title = "test";
        var description = "test description";
        var dueDate = LocalDate.now();

        // 우리가 구현 할 이 테스트 코드의 taskRepository 는 실제 DB에 연결해서 사용하는 것이 아닌 Mock 객체로 선언을 해줬음
        // 그렇기 때문에 이 taskRepository 의 save 메서드가 호출 되면은 어떻게 동작하게 될 지를 우리가 정의해줘야 함
        when(taskRepository.save(any(TaskEntity.class))) // taskRepository 의 save 가 발생을 하게 된다면은 어떤 것을 반환해 주도록 하겠다
                .thenAnswer(invocation -> {
                    var e = (TaskEntity)invocation.getArgument(0);
                    e.setId(1L);
                    e.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                    e.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    return e;
                });

        var actual = taskService.add(title, description, dueDate);

        verify(taskRepository, times(1)).save(any());

        assertEquals(1L, actual.getId());
        assertEquals(title, actual.getTitle());
        assertEquals(description, actual.getDescription());
        assertEquals(dueDate.toString(), actual.getDueDate());
        assertEquals(TaskStatus.TODO, actual.getStatus());
        assertNotNull(actual.getCreatedAt());
        assertNotNull(actual.getUpdatedAt());

    }


}