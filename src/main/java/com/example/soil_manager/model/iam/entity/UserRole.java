package com.example.soil_manager.model.iam.entity;

// Xây dựng chức năng gán vai trò (Role) cho người dùng (User) trong từng tổ chức (Organization)

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_roles", schema = "iam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @EmbeddedId // Sử dụng một class khác để định nghĩa khóa chính tổng hợp của Entity này.
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY) // Nhiều UserRole có thể thuộc về 1 User. LAZY Chỉ tải dữ liệu liên quan khi thực sự truy cập
    @MapsId("userId") // Giá trị khóa ngoại user_id đồng thời cũng là một phần của khóa chính tổng hợp UserRoleId.
    @JoinColumn(name = "user_id") // Chỉ định cột khóa ngoại dùng để liên kết với bảng users.
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("organizationId")
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
