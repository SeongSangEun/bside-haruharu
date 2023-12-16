package com.bigbang.haruharu.domain.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.bigbang.haruharu.domain.entity.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@DynamicUpdate
@Entity
@Getter
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private String imageUrl;
    private String nickname;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String providerId;

    private Integer dailyCount;
    
    public User(){}

    @Builder
    public User(String name, String email, String password, Role role, Provider provider, String providerId, String imageUrl, Integer dailyCount){
        this.email = email;
        this.password = password;
        this.name = name;
        this.provider = provider;
        this.role = role;
        this.dailyCount = dailyCount;
    }

    public void updateName(String name){
        this.name = name;
    }

    public void updateImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
    public void resetDailyCount() {this.dailyCount = 5;}
    public void minusDailyCount() {this.dailyCount --;}
    public boolean canCreateArticle() {
        return this.dailyCount > 1
                ? true
                : false;
    }
    public void updateNickname(String toUpdateNickname){
        this.nickname = toUpdateNickname;
    }
}
