package com.cognixus.todo.model;


import com.cognixus.todo.snowflake.SnowflakeId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
public class Todo implements SnowflakeId<Long> {
    @Id
    private Long id;

    //task name
    @Column(nullable = false)
    private String name;

    
    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Integer publishYear;

    //avoid this field get set when create/update
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Long borrowBy;


}