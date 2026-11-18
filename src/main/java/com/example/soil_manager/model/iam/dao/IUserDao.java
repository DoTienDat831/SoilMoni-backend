package com.example.soil_manager.model.iam.dao;

import com.example.soil_manager.hibernateDao.IPaginationSortingDao;
import com.example.soil_manager.model.iam.entity.User;

import java.util.List;

public interface IUserDao // khai báo các phương thức mà class triển khai nó cần cung cấp, chỉ khai báo, chưa cần viết logic.
        extends IPaginationSortingDao<Long, User>{ //IUserDao kế thừa một interface khác có tên IPaginationSortingDao.
        //Long: Kiểu dữ liệu của khóa chính.
        //User: Kiểu Entity mà DAO quản lý.

    User save(User user); // Lưu User

    User getByUsername(String username); // Tìm theo username

    User getByEmail(String email); // Tìm theo email

    List<User> searchByUsername(String keyword); // Tìm kiếm một phần username

    void assignRole( // Gán role cho user
            Long userId,
            Long roleId,
            Long organizationId
    );

    List<User> getUsersByRole( // Lấy User theo Role trong Organization
            Long roleId,
            Long organizationId
    );
}
