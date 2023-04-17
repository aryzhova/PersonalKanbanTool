package com.ppm.services;

import antlr.StringUtils;
import com.ppm.domain.Backlog;
import com.ppm.domain.Project;
import com.ppm.domain.ProjectTask;
import com.ppm.exceptions.ProjectNotFoundException;
import com.ppm.repositories.BacklogRepository;
import com.ppm.repositories.ProjectRepository;
import com.ppm.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        try{
            //PT to added to a specific project, project not null
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

            //set the backlog to PT
            projectTask.setBacklog(backlog);

            //we want our project sequence to be like this  PROID-1
            Integer backlogSequence = backlog.getPTSequence();

            //update the backlog sequence
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            //add sequence to project task
            projectTask.setProjectSequence(projectIdentifier +"-"+ backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //initial priority when priority is null
            if(projectTask.getPriority() == null){
                projectTask.setPriority(3);
            }
            //initial status when status is null
            if(projectTask.getStatus() == "" || projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }
            return  projectTaskRepository.save(projectTask);

        } catch (Exception e){
            throw new ProjectNotFoundException("Project Not Found");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String id) {
        Project project = projectRepository.findByProjectIdentifier(id);
        if(project == null){
            throw new ProjectNotFoundException("Project with ID: " + id + " does not exist!!");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){
        //make sure we are seraching on the right backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog==null){
            throw new ProjectNotFoundException("Project with ID: " + backlog_id + " does not exist!!");
        }

        ProjectTask projectTask= projectTaskRepository.findProjectTaskByProjectSequence(pt_id);
        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task not found!");
        }

        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task" +pt_id+" does not exist in project "+backlog_id);
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id) ;
        projectTask = updatedTask;
        return  projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

//        Backlog backlog= projectTask.getBacklog();
//        List<ProjectTask> pts= projectTask.getBacklog().getProjectTasks();
//        pts.remove(projectTask);
//        backlogRepository.save(backlog);
        projectTaskRepository.delete(projectTask);
    }

}
