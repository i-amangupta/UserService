package com.user.service.UserService.entity;

import com.user.service.UserService.model.Rating;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @UuidGenerator
    private String userId;
    private String userName;
    private String userEmail;
    @Transient
    private List<Rating> ratings = new ArrayList<>();
}
