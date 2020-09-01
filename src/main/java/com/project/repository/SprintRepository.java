package com.project.repository;

import com.project.entities.Sprint;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SprintRepository extends CrudRepository<Sprint, Integer> {

    List<Sprint> findByProject(Integer projectId);
    Sprint findByProject(String title);
    Sprint findByTitle(String title);
}
