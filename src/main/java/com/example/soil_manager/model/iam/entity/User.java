package com.example.soil_manager.model.iam.entity;

import jakarta.persistence.*; // Thư viện chứa các annotation và interface @Entity @Table @Id @Column ...
import lombok.*; // giúp giảm lượng code Java phải viết thủ công, đặc biệt là các phương thức Getter, Setter, Constructor...


@Entity // Đánh dấu 1 class là entity
@Table(name="users", schema = "iam") // Chỉ định tên bảng
@Getter @Setter
@NoArgsConstructor //
@AllArgsConstructor
@Builder

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Yêu cầu database tự động sinh giá trị ID khi thêm người dùng mới.
    private Long id; // Id

    @Column(nullable = false, unique = true, length = 100)
    private String username; // Username NOT NULL, UNIQUE

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // Password NOT NULL

    @Column(name = "full_name")
    private String fullName;

    @Column
    private Boolean enable = true;

}
