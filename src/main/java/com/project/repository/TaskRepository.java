package com.project.repository;
import com.project.entity.Task;
import com.project.entity.User;
import com.project.entity.UserProjectVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Override
    @Query("SELECT t FROM Task t WHERE t.enable = true")
    List<Task> findAll();

    @Override
    @Query("SELECT t FROM Task t WHERE t.enable = true AND t.taskId = :id")
    Optional<Task> findById(@Param("id") Integer id);

    @Query("SELECT t FROM Task t WHERE t.feature.id = :featureId AND t.enable = true")
    List<Task> findTasksByFeatureId(@Param("featureId") int featureId);

    @Modifying
    @Query("UPDATE Task t SET t.enable = :enable WHERE t.taskId = :taskId")
    int setEnableById(@Param("taskId") Integer taskId, @Param("enable") boolean enable);

    @Query("SELECT DISTINCT upv.user FROM UserProjectVersion upv WHERE upv.projectVersion IN (SELECT t.feature.projectVersion FROM Task t WHERE t.taskId = :taskId)")
    List<User> findAttendeesByTaskId(@Param("taskId") Integer taskId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true")
    int getTotalTask();

    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.assignedTo.userId = :employeeId")
    Integer countByAssignedTo(@Param("employeeId") int employeeId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.assignedTo.userId = :employeeId AND t.status = 'COMPLETED'")
    Integer countCompletedTasks(@Param("employeeId") int employeeId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.enable = true AND t.assignedTo.userId = :employeeId AND ((t.status = 'COMPLETED' AND t.endDate > t.estimatedEndDate) OR (t.status = 'PROCESSING' AND t.estimatedEndDate < :currentDate))")
    Integer countOverdueTasks(@Param("employeeId") int employeeId, @Param("currentDate") Date currentDate);

    @Query("SELECT AVG(CASE WHEN t.status = 'COMPLETED' THEN DATEDIFF(t.endDate, t.estimatedEndDate) ELSE DATEDIFF(:currentDate, t.estimatedEndDate) END) FROM Task t WHERE t.enable = true AND t.assignedTo.userId = :employeeId AND ((t.status = 'COMPLETED' AND t.endDate > t.estimatedEndDate) OR (t.status = 'PROCESSING' AND t.estimatedEndDate < :currentDate))")
    Double calculateAverageDaysOverdue(@Param("employeeId") int employeeId, @Param("currentDate") Date currentDate);



}

