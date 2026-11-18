package com.example.soil_manager.hibernateDao;

// chứa code thực tế để thao tác với database.
// sử dụng `EntityManager` để thực hiện CRUD, truy vấn, phân trang và sắp xếp.

import jakarta.persistence.EntityManager; // Quản lý và thao tác với Entity
import jakarta.persistence.TypedQuery; // Truy vấn có kiểu kết quả xác định

import org.springframework.data.domain.Page; // Đại diện cho một trang dữ liệu
import org.springframework.data.domain.PageImpl; // Implementation của page
import org.springframework.data.domain.Pageable; // Chứa thông tin phân trang
import org.springframework.data.domain.Sort; // Chứa điều kiện sắp xếp

import java.io.Serializable; // Ràng buộc kiểu khóa chính
import java.util.List;

public abstract class HibernatePaginationSortingDao< // Khai báo lớp trừu tượng
        ID extends Serializable, T>
        implements IPaginationSortingDao<ID, T> { // Thông báo rằng class này triển khai interface `IPaginationSortingDao`.

    protected final EntityManager entityManager; //  là công cụ trung tâm để làm việc với database thông qua JPA.

    private final Class<T> entityClass;

    // Constructor nhận 2 tham số
    // entityClass: kiểu entity cần quản lý
    // entityManager: công cụ truy cập vào database
    protected HibernatePaginationSortingDao(
            Class<T> entityClass,
            EntityManager entityManager) {

        this.entityClass = entityClass;
        this.entityManager = entityManager;
    }

    protected String getEntityName() {
        return entityClass.getSimpleName();
    } //  Lấy tên đơn giản của Entity.

    // CREATE
    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    // UPDATE
    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    // DELETE
    @Override
    public void deleteByPk(ID id) {
        T entity = get(id);

        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    // GET BY ID
    @Override
    public T get(ID id) {
        return entityManager.find(entityClass, id);
    }

    // FIND ALL
    @Override
    public List<T> findAll() {

        String jpql = "SELECT e FROM "
                + getEntityName() + " e";

        return entityManager
                .createQuery(jpql, entityClass)
                .getResultList();
    }

    // FIND ALL WITH SORTING
    @Override
    public List<T> findAll(Sort sort) {

        String jpql = "SELECT e FROM "
                + getEntityName() + " e";

        if (sort != null && sort.isSorted()) {

            String orderBy = sort.stream()
                    .map(order -> "e."
                            + order.getProperty()
                            + " "
                            + order.getDirection().name())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");

            jpql += " ORDER BY " + orderBy;
        }

        return entityManager
                .createQuery(jpql, entityClass)
                .getResultList();
    }

    // PAGINATION
    @Override
    public Page<T> findAll(Pageable pageable) {

        String jpql = "SELECT e FROM "
                + getEntityName() + " e";

        TypedQuery<T> query =
                entityManager.createQuery(jpql, entityClass);

        if (pageable.getSort().isSorted()) {

            String orderBy = pageable.getSort().stream()
                    .map(order -> "e."
                            + order.getProperty()
                            + " "
                            + order.getDirection().name())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");

            query = entityManager.createQuery(
                    jpql + " ORDER BY " + orderBy,
                    entityClass
            );
        }

        query.setFirstResult(
                Math.toIntExact(pageable.getOffset())
        );

        query.setMaxResults(pageable.getPageSize());

        List<T> content = query.getResultList();

        return new PageImpl<>(
                content,
                pageable,
                count()
        );
    }

    // COUNT
    @Override
    public long count() {

        String jpql = "SELECT COUNT(e) FROM "
                + getEntityName() + " e";

        return entityManager
                .createQuery(jpql, Long.class)
                .getSingleResult();
    }

    // FIND BY HQL
    protected List<T> findByHql(
            String hql,
            java.util.Map<String, Object> parameters) {

        TypedQuery<T> query =
                entityManager.createQuery(hql, entityClass);

        parameters.forEach(query::setParameter);

        return query.getResultList();
    }

    // FIND BY FIELD
    protected T getByField(String field, Object value) {

        String jpql = "SELECT e FROM "
                + getEntityName()
                + " e WHERE e." + field + " = :value";

        return entityManager.createQuery(jpql, entityClass)
                .setParameter("value", value)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}