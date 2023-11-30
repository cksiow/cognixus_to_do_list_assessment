package com.cognixus.todo.model;


import com.cognixus.todo.snowflake.SnowflakeId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "T_USER")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@ToString
public class User implements SnowflakeId<Long> {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;


}
