package com.example.soil_manager.model.iam.dao.impl;

// Class triển khai các phương thức đã khai báo trong IUserDao

import com.example.soil_manager.model.iam.dao.IUserDao;
import com.example.soil_manager.model.iam.entity.*;

import com.example.soil_manager.hibernateDao.HibernatePaginationSortingDao; //	Class DAO cơ sở chứa các chức năng truy vấn dùng chung

import jakarta.persistence.EntityManager; // Công cụ làm việc với Entity thông qua JPA

import org.springframework.stereotype.Repository; // Đánh dấu class là tầng truy cập dữ liệu
import org.springframework.transaction.annotation.Transactional; // Quản lý Transaction

import java.util.List; // Danh sách dữ liệu
import java.util.Map; // Lưu các cặp key-value

@Repository // Đánh dấu class này là một Spring Bean thuộc tầng DAO.
@Transactional
public class UserDaoImpl
        extends HibernatePaginationSortingDao<Long, User> // Class UserDaoImpl kế thừa class HibernatePaginationSortingDao.
        implements IUserDao { // Thông báo rằng UserDaoImpl triển khai interface IUserDao.

    public UserDaoImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }
    //User.class: Cho class cha biết DAO này quản lý Entity User.
    //entityManager: Cung cấp công cụ thao tác database.

    // CREATE Lưu user
    @Override
    public User save(User user) {
        return create(user);
    }

    // FIND BY USERNAME Tìm user bằng username
    @Override
    public User getByUsername(String username) {
        return getByField("username", username);
    }

    // FIND BY EMAIL Tìm user bằng email
    @Override
    public User getByEmail(String email) {
        return getByField("email", email);
    }

    // SEARCH BY USERNAME
    @Override
    public List<User> searchByUsername(String keyword) {

        return findByHql(
                """
                SELECT u FROM User u
                WHERE LOWER(u.username)
                LIKE LOWER(CONCAT('%', :keyword, '%'))
                """,
                Map.of("keyword", keyword)
        );
    }

    // ASSIGN ROLE TO USER
    @Override
    public void assignRole(
            Long userId,
            Long roleId,
            Long organizationId) {

        User user = entityManager.find(User.class, userId);
        Role role = entityManager.find(Role.class, roleId);

        Organization organization =
                entityManager.find(
                        Organization.class, organizationId
                );

        if (user == null || role == null || organization == null) {
            throw new IllegalArgumentException(
                    "User, Role or Organization not found"
            );
        }

        UserRoleId id = new UserRoleId(
                userId, roleId, organizationId
        );

        if (entityManager.find(UserRole.class, id) != null) {
            return;
        }

        UserRole userRole = new UserRole(
                id, user, role, organization
        );

        entityManager.persist(userRole);
    }

    // FIND USERS BY ROLE IN ORGANIZATION
    @Override
    public List<User> getUsersByRole(
            Long roleId,
            Long organizationId) {

        return entityManager.createQuery(
                        """
                        SELECT DISTINCT ur.user
                        FROM UserRole ur
                        WHERE ur.role.id = :roleId
                          AND ur.organization.id = :organizationId
                        """,
                        User.class
                )
                .setParameter("roleId", roleId)
                .setParameter("organizationId", organizationId)
                .getResultList();
    }
}