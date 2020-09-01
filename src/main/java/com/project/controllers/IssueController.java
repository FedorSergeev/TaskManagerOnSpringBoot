package com.project.controllers;

import com.project.entities.issue.Issue;
import com.project.entities.issue.IssuePriority;
import com.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IssueController {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @PostMapping("project/{id}/issueListInProject/titleFilter")
    public String titleFilter(@PathVariable("id") Integer projectId, @RequestParam String title, Model model) {
        List<Issue> issuesTitle = new ArrayList<>();
        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();

        if (title.isEmpty()) {
            model.addAttribute("issues", issueRepository.findByBacklog(backlogId));
        }

        if (!title.isEmpty()) {
            for (Issue issue : issueRepository.findByBacklog(backlogId)) {
                if (issue.getTitle().equals(title)) {
                    issuesTitle.add(issue);
                }
            }
            model.addAttribute("issues", issuesTitle);
        }

        model
                .addAttribute("projectID",projectRepository.findById(projectId).get().getId())
                .addAttribute("title",projectRepository.findById(projectId).get().getTitle())
                .addAttribute("description", projectRepository.findById(projectId).get().getDescription())
                .addAttribute("supervisorID", projectRepository.findById(projectId).get().getSupervisor())
                .addAttribute("subdivision", projectRepository.findById(projectId).get().getSubdivision())
                .addAttribute("adminID", projectRepository.findById(projectId).get().getAdmin());

        model.addAttribute("backlogTitle", backlogRepository.findByProjectId(projectId).getTitle());
        model.addAttribute("users", userRepository.findAll());

        return "redirect:/project/{id}/issueListInProject";
    }

    @PostMapping("project/{id}/issueListInProject/priorityFilter")
    public String priorityFilter(@PathVariable("id") Integer projectId,
                                 @RequestParam IssuePriority issuePriority,
                                 Model model) {
        List<Issue> issuesPriority = new ArrayList<>();
        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();

        if (issuePriority == null) {
            model.addAttribute("issues", issueRepository.findByBacklog(backlogId));
        }

        if (issuePriority != null) {
            for (Issue issue : issueRepository.findByBacklog(backlogId)) {
                if (issue.getIssuePriority()==issuePriority) {
                    issuesPriority.add(issue);
                }
            }
            model.addAttribute("issues", issuesPriority);
        }


        model
                .addAttribute("projectID",projectRepository.findById(projectId).get().getId())
                .addAttribute("title",projectRepository.findById(projectId).get().getTitle())
                .addAttribute("description", projectRepository.findById(projectId).get().getDescription())
                .addAttribute("supervisorID", projectRepository.findById(projectId).get().getSupervisor())
                .addAttribute("subdivision", projectRepository.findById(projectId).get().getSubdivision())
                .addAttribute("adminID", projectRepository.findById(projectId).get().getAdmin());

        model.addAttribute("backlogTitle", backlogRepository.findByProjectId(projectId).getTitle());
        model.addAttribute("users", userRepository.findAll());

        return "issueListInProject";
    }

    @PostMapping("project/{id}/issueListInProject/dateFilter")
    public String dateFilter(@PathVariable("id") Integer projectId,
                             @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             Model model) {

        List<Issue> issuesData = new ArrayList<>();
        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();

        if (date == null) {
            model.addAttribute("issues", issueRepository.findByBacklog(backlogId));
        }

        if(date != null) {
            for (Issue issue : issueRepository.findByBacklog(backlogId)) {
                if (issue.getDataCreate().equals(date)) {
                    issuesData.add(issue);
                }
            }
            model.addAttribute("issues", issuesData);
        }

        model
                .addAttribute("projectID",projectRepository.findById(projectId).get().getId())
                .addAttribute("title",projectRepository.findById(projectId).get().getTitle())
                .addAttribute("description", projectRepository.findById(projectId).get().getDescription())
                .addAttribute("supervisorID", projectRepository.findById(projectId).get().getSupervisor())
                .addAttribute("subdivision", projectRepository.findById(projectId).get().getSubdivision())
                .addAttribute("adminID", projectRepository.findById(projectId).get().getAdmin());

        model.addAttribute("backlogTitle", backlogRepository.findByProjectId(projectId).getTitle());
        model.addAttribute("users", userRepository.findAll());

        return "redirect:/project/{id}/issueListInProject";
    }

    @PostMapping("project/{id}/issueListInProject/executorFilter")
    public String executorFilter(@PathVariable("id") Integer projectId,
                                 @RequestParam String executor, Model model) {

        List<Issue> issuesExecutor = new ArrayList<>();
        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();

        if(!executor.isEmpty()) {
            for (Issue issue : issueRepository.findByBacklog(backlogId)) {
                if (Integer.parseInt(executor) == issue.getExecutor()) {
                    issuesExecutor.add(issue);
                }
            }
            model.addAttribute("issues", issuesExecutor);
        } else {
            model.addAttribute("issues", issueRepository.findByBacklog(backlogId));
        }

        model
                .addAttribute("projectID",projectRepository.findById(projectId).get().getId())
                .addAttribute("title",projectRepository.findById(projectId).get().getTitle())
                .addAttribute("description", projectRepository.findById(projectId).get().getDescription())
                .addAttribute("supervisorID", projectRepository.findById(projectId).get().getSupervisor())
                .addAttribute("subdivision", projectRepository.findById(projectId).get().getSubdivision())
                .addAttribute("adminID", projectRepository.findById(projectId).get().getAdmin());

        model.addAttribute("backlogTitle", backlogRepository.findByProjectId(projectId).getTitle());
        model.addAttribute("users", userRepository.findAll());

        return "redirect:/project/{id}/issueListInProject";
    }

    @PostMapping("project/{id}/issueListInProject/reporterFilter")
    public String reporterFilter(@PathVariable("id") Integer projectId, @RequestParam String reporter, Model model) {

        List<Issue> issuesReporter = new ArrayList<>();
        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();

        if(!reporter.isEmpty()) {
            for (Issue issue : issueRepository.findByBacklog(backlogId)) {
                if (Integer.parseInt(reporter) == issue.getReporter()) {
                    issuesReporter.add(issue);
                }
            }
            model.addAttribute("issues", issuesReporter);
        } else {
            model.addAttribute("issues", issueRepository.findByBacklog(backlogId));
        }

        model
                .addAttribute("projectID",projectRepository.findById(projectId).get().getId())
                .addAttribute("title",projectRepository.findById(projectId).get().getTitle())
                .addAttribute("description", projectRepository.findById(projectId).get().getDescription())
                .addAttribute("supervisorID", projectRepository.findById(projectId).get().getSupervisor())
                .addAttribute("subdivision", projectRepository.findById(projectId).get().getSubdivision())
                .addAttribute("adminID", projectRepository.findById(projectId).get().getAdmin());

        model.addAttribute("backlogTitle", backlogRepository.findByProjectId(projectId).getTitle());
        model.addAttribute("users", userRepository.findAll());

        return "redirect:/project/{id}/issueListInProject";
    }
    @PostMapping("/project/{id}/issueListInProject/moveToSprint/issue/{issue.id}")
    public String moveIssueToSprint(@PathVariable("id") Integer projectId,
                                    @PathVariable("issue.id") Integer issueId,
                                    @RequestParam String parentIssue,
                                    @RequestParam String sprintInProject,
                                    Model model) {

        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();

        Issue issue = issueRepository.findById(issueId).get();
        issue.setParentIssue(issueRepository.findByTitle(parentIssue).getId());
        issue.setSprint(sprintRepository.findByTitle(sprintInProject).getId());
        issue.setBacklog(null);
        issueRepository.save(issue);

        model.addAttribute("issues", issueRepository.findByBacklog(backlogId));

        model
                .addAttribute("projectID",projectRepository.findById(projectId).get().getId())
                .addAttribute("title",projectRepository.findById(projectId).get().getTitle())
                .addAttribute("description", projectRepository.findById(projectId).get().getDescription())
                .addAttribute("supervisorID", projectRepository.findById(projectId).get().getSupervisor())
                .addAttribute("subdivision", projectRepository.findById(projectId).get().getSubdivision())
                .addAttribute("adminID", projectRepository.findById(projectId).get().getAdmin());

        model.addAttribute("backlogTitle", backlogRepository.findByProjectId(projectId).getTitle());
        model.addAttribute("users", userRepository.findAll());

        return "redirect:/project/{id}/issueListInProject";
    }
}
