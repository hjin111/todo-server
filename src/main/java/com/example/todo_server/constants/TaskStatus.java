package com.example.todo_server.constants;

public enum TaskStatus {

    TODO, // 아직 진행이 되지 않은 상태
    IN_PROGRESS, // 현재 진행 중이지만 아직 완료 X
    ON_HOLD, // 잠깐 HOLDING
    COMPLETED, // 할 일 다 끝낸 상태
    CANCELLED // TASK 취소 상태
}
