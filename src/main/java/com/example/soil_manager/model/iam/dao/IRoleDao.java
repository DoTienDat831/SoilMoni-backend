package com.example.soil_manager.model.iam.dao;

import com.example.soil_manager.hibernateDao.IPaginationSortingDao;
import com.example.soil_manager.model.iam.entity.Role;
import com.example.soil_manager.model.iam.entity.Permission;

import java.util.List;

public interface IRoleDao
        extends IPaginationSortingDao<Long, Role> {

    Role save(Role role);

    Role getByName(String name);

    List<Role> getByUserId(Long userId);

    List<Permission> getPermissions(Long roleId);

    void assignPermission(
            Long roleId,
            Long permissionId
    );

    void removePermission(
            Long roleId,
            Long permissionId
    );

    List<Role> searchByName(String keyword);
}
