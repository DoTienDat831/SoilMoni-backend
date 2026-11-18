package com.example.soil_manager.model.iam.dao.impl;

import com.example.soil_manager.model.iam.dao.IRoleDao;
import com.example.soil_manager.model.iam.entity.*;

import com.example.soil_manager.hibernateDao.HibernatePaginationSortingDao; //	Class DAO cơ sở chứa các chức năng truy vấn dùng chung

import jakarta.persistence.EntityManager; // Công cụ làm việc với Entity thông qua JPA

import org.springframework.stereotype.Repository; // Đánh dấu class là tầng truy cập dữ liệu
import org.springframework.transaction.annotation.Transactional; // Quản lý Transaction

import java.util.List; // Danh sách dữ liệu
import java.util.Map; // Lưu các cặp key-value


@Repository
@Transactional
public class RoleDaoImpl
        extends HibernatePaginationSortingDao<Long, Role>
        implements IRoleDao {

    public RoleDaoImpl(EntityManager entityManager) {
        super(Role.class, entityManager);
    }

    // CREATE
    @Override
    public Role save(Role role) {
        return create(role);
    }

    // FIND BY NAME
    @Override
    public Role getByName(String name) {
        return getByField("name", name);
    }

    // FIND ROLES BY USER
    @Override
    public List<Role> getByUserId(Long userId) {

        return entityManager.createQuery(
                        """
                        SELECT DISTINCT ur.role
                        FROM UserRole ur
                        WHERE ur.user.id = :userId
                        """,
                        Role.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    // FIND PERMISSIONS BY ROLE
    @Override
    public List<Permission> getPermissions(Long roleId) {

        return entityManager.createQuery(
                        """
                        SELECT rp.permission
                        FROM RolePermission rp
                        WHERE rp.role.id = :roleId
                        """,
                        Permission.class
                )
                .setParameter("roleId", roleId)
                .getResultList();
    }

    // ASSIGN PERMISSION TO ROLE
    @Override
    public void assignPermission(
            Long roleId,
            Long permissionId) {

        Role role = entityManager.find(Role.class, roleId);

        Permission permission =
                entityManager.find(Permission.class, permissionId);

        if (role == null || permission == null) {
            throw new IllegalArgumentException(
                    "Role or Permission not found"
            );
        }

        RolePermissionId id =
                new RolePermissionId(roleId, permissionId);

        if (entityManager.find(RolePermission.class, id) != null) {
            return;
        }

        RolePermission rolePermission =
                new RolePermission(id, role, permission);

        entityManager.persist(rolePermission);
    }

    // REMOVE PERMISSION FROM ROLE
    @Override
    public void removePermission(
            Long roleId,
            Long permissionId) {

        RolePermissionId id =
                new RolePermissionId(roleId, permissionId);

        RolePermission rolePermission =
                entityManager.find(RolePermission.class, id);

        if (rolePermission != null) {
            entityManager.remove(rolePermission);
        }
    }

    // SEARCH ROLE BY NAME
    @Override
    public List<Role> searchByName(String keyword) {

        return findByHql(
                """
                SELECT r FROM Role r
                WHERE LOWER(r.name)
                LIKE LOWER(CONCAT('%', :keyword, '%'))
                """,
                Map.of("keyword", keyword)
        );
    }
}
