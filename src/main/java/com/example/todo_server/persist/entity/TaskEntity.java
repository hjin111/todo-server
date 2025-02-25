package com.example.todo_server.persist.entity;

import com.example.todo_server.constants.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;  // 기존의 java.security.Timestamp 대신 이걸 사용하세요.
import java.util.Date;

@Getter
@Setter
@ToString
@Builder // 인스턴스 초기화를 편하게 하기 위해서 Builder 어노테이션 추가
@DynamicInsert // createdAt 컬럼을 자동으로 데이터가 생성되게 관리해주는 어노테이션
@DynamicUpdate
@Entity(name = "TASK")
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(value = EnumType.STRING) // Entity 에는 Stirng 으로 저장될 수 있도록 Enumerated 어노테이션 사용
    private TaskStatus status;

    private Date dueDate;

    @CreationTimestamp
    @Column(insertable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(insertable = false, updatable = false)
    private Timestamp updatedAt;

}
