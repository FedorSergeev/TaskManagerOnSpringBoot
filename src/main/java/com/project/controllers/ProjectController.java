package com.project.controllers;

import com.project.entities.Backlog;
import com.project.entities.Project;
import com.project.entities.Sprint;
import com.project.entities.User;
import com.project.entities.issue.Issue;
import com.project.entities.issue.IssuePriority;
import com.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @GetMapping("createProject")
    public String getUsers(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "createProject";
    }

    @GetMapping(value = "createProject", params = "tpl")
    public String backToCreateProject() {
        return "createProject";
    }

    @GetMapping("addUser")
    public String addUser() {
        return "/addUser";
    }

    @GetMapping("/projectsList")
    public String backToProjectsList(Model model) {
        Iterable<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);

        return "projectsList";
    }

    @GetMapping("/project/{id}/projectInfo")
    public String getProjectById(@PathVariable("id") Integer projectId,
                                 Model model) {

        model
                .addAttribute("projectID",projectRepository.findById(projectId).get().getId())
                .addAttribute("title",projectRepository.findById(projectId).get().getTitle())
                .addAttribute("description", projectRepository.findById(projectId).get().getDescription())
                .addAttribute("supervisorID", projectRepository.findById(projectId).get().getSupervisor())
                .addAttribute("subdivision", projectRepository.findById(projectId).get().getSubdivision())
                .addAttribute("adminID", projectRepository.findById(projectId).get().getAdmin());

        model.addAttribute("backlogTitle", backlogRepository.findByProjectId(projectId).getTitle());

        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();

        List<Issue> issues = issueRepository.findByBacklog(backlogId);
        List<Sprint> sprints = sprintRepository.findByProject(projectId);
        List<Issue.WorkFlowIssue> workFlowsIssue= new ArrayList<>();
        workFlowsIssue.add(Issue.WorkFlowIssue.OPEN_ISSUE);
        workFlowsIssue.add(Issue.WorkFlowIssue.INPROGRESS_ISSUE);
        workFlowsIssue.add(Issue.WorkFlowIssue.REVIEW_ISSUE);
        workFlowsIssue.add(Issue.WorkFlowIssue.TEST_ISSUE);
        workFlowsIssue.add(Issue.WorkFlowIssue.RESOLVED_ISSUE);
        workFlowsIssue.add(Issue.WorkFlowIssue.REOPENED_ISSUE);
        workFlowsIssue.add(Issue.WorkFlowIssue.CLOSE_ISSUE);

        model.addAttribute("sprints", sprints);
        model.addAttribute("issues", issues);
        model.addAttribute("workflows", workFlowsIssue);

        return "projectInfo";
    }

    @PostMapping("createProject")
    public String createProject(@RequestParam String title,
                                @RequestParam String description,
                                @RequestParam String subdivision,
                                @RequestParam String supervisor,
                                @RequestParam String admin,
                                Model model) {

        Integer supervisorId = Integer.parseInt(supervisor);
        Integer adminId = Integer.parseInt(admin);
        Project project = new Project(title, description, subdivision,supervisorId,adminId);
        projectRepository.save(project);

        Integer projectId = project.getId();

        Backlog backlog = new Backlog("Backlog", projectId);
        backlogRepository.save(backlog);

        model
                .addAttribute("projectID",projectId)
                .addAttribute("title",title)
                .addAttribute("description", description)
                .addAttribute("supervisorID", supervisorId)
                .addAttribute("subdivision", subdivision)
                .addAttribute("adminID", adminId);

        model.addAttribute("backlogTitle", backlog.getTitle());

        return "redirect:/projectsList";
    }

    @PostMapping("project/{id}/projectInfo")
    public String createIssue (@PathVariable("id") Integer projectId,
                               @RequestParam String title,
                               @RequestParam String description,
                               @RequestParam String executor,
                               @RequestParam String reporter,
                               @RequestParam IssuePriority issuePriority,
                               @RequestParam String issueType,
                               Model model
                               ) {

        Integer executorId = Integer.parseInt(executor);
        Integer reporterId = Integer.parseInt(reporter);

        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();
        Issue issue = new Issue(title, description, issueType, issuePriority, backlogId, executorId, reporterId);
        issueRepository.save(issue);

        List<Issue> issues = issueRepository.findByBacklog(backlogId);
        List<Sprint> sprints = sprintRepository.findByProject(projectId);
        model.addAttribute("issues", issues);

        model
                .addAttribute("projectID",projectRepository.findById(projectId).get().getId())
                .addAttribute("title",projectRepository.findById(projectId).get().getTitle())
                .addAttribute("description", projectRepository.findById(projectId).get().getDescription())
                .addAttribute("supervisorID", projectRepository.findById(projectId).get().getSupervisor())
                .addAttribute("subdivision", projectRepository.findById(projectId).get().getSubdivision())
                .addAttribute("adminID", projectRepository.findById(projectId).get().getAdmin());

        model.addAttribute("backlogTitle", backlogRepository.findByProjectId(projectId).getTitle());
        model.addAttribute("sprints", sprints);

        return "redirect:/project/{id}/projectInfo";
    }

    @GetMapping("project/{id}/issues")
    public String getIssues(@PathVariable("id") Integer projectId, Model model) {

        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();

        List<Issue> issues = issueRepository.findByBacklog(backlogId);

        model.addAttribute("issues", issues);
        return "projectInfo";
    }

    @GetMapping("/project/{id}/issueListInProject")
    public String goToIssueList(@PathVariable("id") Integer projectId,
                                Model model) {
        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();
        List<Sprint> sprints = sprintRepository.findByProject(projectId);

        model
                .addAttribute("projectID",projectRepository.findById(projectId).get().getId())
                .addAttribute("title",projectRepository.findById(projectId).get().getTitle())
                .addAttribute("description", projectRepository.findById(projectId).get().getDescription())
                .addAttribute("supervisorID", projectRepository.findById(projectId).get().getSupervisor())
                .addAttribute("subdivision", projectRepository.findById(projectId).get().getSubdivision())
                .addAttribute("adminID", projectRepository.findById(projectId).get().getAdmin());

        model.addAttribute("backlogTitle", backlogRepository.findByProjectId(projectId).getTitle());

        model.addAttribute("issues", issueRepository.findByBacklog(backlogId));
        model.addAttribute("parentIssue", issueRepository.findByBacklog(backlogId));
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("sprints", sprints);
        model.addAttribute("sprintInProject", sprints);
        return "issueListInProject";
    }

    @PostMapping("project/{id}/projectInfo/createSprint")
    public String createSprint(@PathVariable("id") Integer projectId,
                                @RequestParam String title,
                                @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                Model model) {

        Integer project = projectRepository.findById(projectId).get().getId();
        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();

        Sprint sprint = new Sprint(project, title, startDate ,endDate);
        sprintRepository.save(sprint);

        List<Sprint> sprints = sprintRepository.findByProject(projectId);

        model
                .addAttribute("projectID",projectRepository.findById(projectId).get().getId())
                .addAttribute("title",projectRepository.findById(projectId).get().getTitle())
                .addAttribute("description", projectRepository.findById(projectId).get().getDescription())
                .addAttribute("supervisorID", projectRepository.findById(projectId).get().getSupervisor())
                .addAttribute("subdivision", projectRepository.findById(projectId).get().getSubdivision())
                .addAttribute("adminID", projectRepository.findById(projectId).get().getAdmin());

        model.addAttribute("backlogTitle", backlogRepository.findByProjectId(projectId).getTitle());

        model.addAttribute("sprints", sprints);
        model.addAttribute("issues", issueRepository.findByBacklog(backlogId));

        return "redirect:/project/{id}/projectInfo";
    }

    @PostMapping("project/{id}/projectInfo/changeWorkFlow/issue/{issue.id}")
    public String changeWorkflow (Model model,
                                  @PathVariable("id") Integer projectId,
                                  @PathVariable("issue.id") Integer issueId,
                                  @RequestParam Issue.WorkFlowIssue workflow
                                  ) {

        Integer backlogId = backlogRepository.findByProjectId(projectId).getId();

        Issue issue = issueRepository.findById(issueId).get();
        issue.setWorkFlowIssue(workflow);
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

        return "redirect:/project/{id}/projectInfo";
    }
}