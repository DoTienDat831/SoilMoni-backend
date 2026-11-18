package com.example.soil_manager.hibernateDao;

// cung cấp các chức năng dùng chung cho tất cả Entity
// chỉ cần viết logic CRUD, phân trang và sắp xếp một lần, sau đó tất cả DAO có thể kế thừa và sử dụng lại.

// định nghĩa những chức năng chung mà một DAO cần cung cấp.

import org.springframework.data.domain.Page; // Đại diện cho một trang dữ liệu
import org.springframework.data.domain.Pageable; // Chứa thông tin trang hiện tại
import org.springframework.data.domain.Sort; // Cấu hình sắp xếp dữ liệu

import java.io.Serializable;
import java.util.List;

public interface IPaginationSortingDao<ID extends Serializable, T> {

    T create(T entity); // Tạo một entity mới

    T update(T entity); // Cập nhật một entity

    void deleteByPk(ID id); // Delete

    T get(ID id); // Tìm entity theo PK

    List<T> findAll(); // Lấy toàn bộ dữ liệu của Entity

    List<T> findAll(Sort sort); //  Lấy toàn bộ dữ liệu và sắp xếp theo điều kiện.

    Page<T> findAll(Pageable pageable); // Lấy dữ liệu theo từng trang.

    long count(); //  Đếm tổng số bản ghi trong bảng.
}