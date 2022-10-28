package com.ppm.services;

import com.ppm.domain.Project;
import com.ppm.exceptions.ProjectIdException;
import com.ppm.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (Exception e){
            throw new ProjectIdException("Project ID: " + project.getProjectIdentifier().toUpperCase() + " already exists");
        }

    }

    public Project findProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Project ID: " +projectId + " doesn't exist");
        }
        return project;
    }

    public Iterable<Project> findAllProject(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project ==  null){
            throw new ProjectIdException("Project with the following id doesn't exist: " + projectId);
        }

        projectRepository.delete(project);
    }
}
