package com.project.entity;
import com.project.entity.enums.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "feature")
@Getter
@Setter
@ToString
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id")
    private int id;

    @Column(name = "feature_name", nullable = false)
    private String name;

    @Column(name = "feature_description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_version_id", nullable = false)
    private ProjectVersion projectVersion;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(name = "progress")
    private Integer progress;

    @Column(name = "estimated_end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date estimatedEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;


}

