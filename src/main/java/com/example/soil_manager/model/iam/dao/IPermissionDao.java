package com.example.soil_manager.model.iam.dao;

import com.example.soil_manager.hibernateDao.IPaginationSortingDao;
import com.example.soil_manager.model.iam.entity.Permission;

import java.util.List;

public interface IPermissionDao
        extends IPaginationSortingDao<Long, Permission> {

}
