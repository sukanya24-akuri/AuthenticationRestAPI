package com.project.authify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
    @Table(name = "Users")
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public class UserEntity
    {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String userId;
        private String name;
        @Column(unique = true)
        private String email;
        private String password;
        private String verifyOtp;
        private Boolean isAccountverified;
        private Long verifyOtpExpireAt;
        private String resetOtp;
        private Long resetOtpExpireAt;

        @CreationTimestamp
        @Column(updatable = false)
        private Timestamp createAt;
        @UpdateTimestamp
        private Timestamp updatedAt;

    }
