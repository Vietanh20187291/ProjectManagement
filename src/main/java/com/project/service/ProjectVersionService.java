package com.project.service;

import com.project.entity.Feature;
import com.project.entity.FeatureDTO;
import com.project.entity.ProjectVersion;
import com.project.entity.Task;
import com.project.repository.FeatureRepository;
import com.project.repository.ProjectVersionRepository;
import com.project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectVersionService {
    @Autowired
    private ProjectVersionRepository projectVersionRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private TaskRepository taskRepository;


    public ProjectVersionService(ProjectVersionRepository projectVersionRepository) {
        this.projectVersionRepository = projectVersionRepository;
    }

    public void disableProjectVersion(Integer projectVersionId) {
        ProjectVersion projectVersion = projectVersionRepository.findById(projectVersionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid projectVersionId: " + projectVersionId));

        projectVersion.setEnable(false);
        projectVersionRepository.save(projectVersion);
    }

    public List<ProjectVersion> getProjectVersionsByProjectId(Integer projectId) {
        return projectVersionRepository.getProjectVersionByProjectId(projectId);
    }

    public ProjectVersion getProjectVersionById(Integer projectVersionId) {
        return projectVersionRepository.findById(projectVersionId).orElse(null);
    }

    public List<FeatureDTO> findByProjectVersionId(Integer projectVersionId) {
        List<Feature> features = featureRepository.findByProjectVersionId(projectVersionId);
        List<FeatureDTO> featureDTOs = new ArrayList<>();

        for (Feature feature : features) {
            List<Task> tasks = taskRepository.findTasksByFeatureId(feature.getId());
            FeatureDTO featureDTO = new FeatureDTO(feature, tasks);
            featureDTOs.add(featureDTO);
        }

        return featureDTOs;
    }
}
