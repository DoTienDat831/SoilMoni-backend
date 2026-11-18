package com.example.soil_manager.model.iam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable // đánh dấu class có thể được nhúng vào một Entity khác.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserRoleId implements Serializable { // cho phép object có thể được tuần tự hóa (Serialization).

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "organization_id")
    private Long organizationId;
}