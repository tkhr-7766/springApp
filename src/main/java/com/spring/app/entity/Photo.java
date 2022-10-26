package com.spring.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Data;

@Entity
@Data
@Table(name = "photos")
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @ManyToOne
    private User user;

    @Column
    private String filename;

    @Column(updatable = false)
    private Date created;

    public Photo() {
        setId(getRandomId());
    }

    @PrePersist
    public void onPrePersist() {
        setCreated(new Date());
    }

    private String getRandomId() {
        return RandomStringUtils.randomAlphanumeric(12);
    }
}
