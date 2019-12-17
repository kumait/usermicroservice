package io.primecoders.voctrainer.userservice.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long iid;

    @Column(name = "pid", nullable = false, length = 100, unique = true)
    private String id;

    @CreatedDate
    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @LastModifiedDate
    @Column(name = "last_modified", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    @CreatedBy
    @Column(name = "created_by", nullable = true)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = true)
    private String lastModifiedBy;
}
