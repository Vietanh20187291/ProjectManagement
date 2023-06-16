package com.project.repository;

import com.project.entity.Feature;
import com.project.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    @Modifying
    @Query("UPDATE Feature f SET f.enable = :enable WHERE f.id = :featureId")
    int setEnableById(@Param("featureId") Integer featureId, @Param("enable") boolean enable);

    @Query("SELECT f FROM Feature f WHERE f.projectVersion.projectVersionId = :projectVersionId")
    List<Feature> findByProjectVersionId(@Param("projectVersionId") Integer projectVersionId);

    @Query("SELECT t FROM Task t WHERE t.feature.id = :featureId")
    List<Task> findTasksByFeatureId(@Param("featureId") Integer featureId);


}
