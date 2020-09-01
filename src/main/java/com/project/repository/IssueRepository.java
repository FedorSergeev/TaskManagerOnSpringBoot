package com.project.repository;

import com.project.entities.issue.Issue;
import com.project.entities.issue.IssuePriority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IssueRepository extends CrudRepository<Issue, Integer> {

    List<Issue> findByBacklog(Integer backlogId);
    List<Issue> findBySprint(Integer sprintId);
    Issue findByTitle(String title);
    List<Issue> findByDataCreate(Date dateCreate);
    List<Issue> findByReporter(Integer reporterId);
    List<Issue> findByIssuePriority(IssuePriority issuePriority);
    List<Issue> findByExecutor(Integer executorId);
    Optional<Issue> findById(Integer issueId);
    List<Issue> deleteByBacklog(Integer issueId);
}
