package com.cognixus.todo.audit;

public interface Audit {
    void setCreatedBy(Long id);

    Long getCreatedBy();

}
