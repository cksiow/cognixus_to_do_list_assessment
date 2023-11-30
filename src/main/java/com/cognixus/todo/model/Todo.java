package com.cognixus.todo.model;


import com.cognixus.todo.audit.Audit;
import com.cognixus.todo.snowflake.SnowflakeId;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "T_TODO")

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@ToString
public class Todo implements SnowflakeId<Long>, Audit {
    @Id
    private Long id;

    //task name
    @Column(nullable = false)
    private String name;


    //description
    @Column(nullable = false)
    private String description;

    //is completed
    @Column(nullable = false)
    @Builder.Default
    private Boolean completed = false;

    @Column(nullable = false)
    private Long createdBy;


}