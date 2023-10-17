package com.example.test.repository.search;

import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;

@Repository
public abstract class Querydsl5RepositorySupport {

    private final PathBuilder<?> builder;
    private @Nullable EntityManager entityManager;
    private @Nullable Querydsl querydsl;
    private JPAQueryFactory queryFactory;

    public Querydsl5RepositorySupport(Class<?> domainClass) {

        Assert.notNull(domainClass, "Domain class must not be null");
        this.builder = new PathBuilderFactory().create(domainClass);
    }


    @Autowired
    public void setEntityManager(EntityManager entityManager) {

        Assert.notNull(entityManager, "EntityManager must not be null");
        this.querydsl = new Querydsl(entityManager, builder);
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null");
        Assert.notNull(querydsl, "Querydsl must not be null");
        Assert.notNull(queryFactory, "QueryFactory must not be null!");
    }

    @Nullable
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected JPQLQuery<Object> from(EntityPath<?>... paths) {
        return getRequiredQuerydsl().createQuery(paths);
    }

    protected <T> JPQLQuery<T> from(EntityPath<T> path) {
        return getRequiredQuerydsl().createQuery(path).select(path);
    }

    protected DeleteClause<JPADeleteClause> delete(EntityPath<?> path) {
        return new JPADeleteClause(getRequiredEntityManager(), path);
    }

    protected UpdateClause<JPAUpdateClause> update(EntityPath<?> path) {
        return new JPAUpdateClause(getRequiredEntityManager(), path);
    }

    @SuppressWarnings("unchecked")
    protected <T> PathBuilder<T> getBuilder() {
        return (PathBuilder<T>) builder;
    }


    @Nullable
    protected Querydsl getQuerydsl() {
        return this.querydsl;
    }

    private Querydsl getRequiredQuerydsl() {

        if (querydsl == null) {
            throw new IllegalStateException("Querydsl is null");
        }

        return querydsl;
    }
    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }
    private EntityManager getRequiredEntityManager() {

        if (entityManager == null) {
            throw new IllegalStateException("EntityManager is null");
        }

        return entityManager;
    }

    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return getQueryFactory().select(expr);
    }

    protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
        return getQueryFactory().selectFrom(from);
    }

    protected <T> Page<T> applyPagination(Pageable pageable, Function<JPAQueryFactory, JPAQuery> contentQuery) {
        JPAQuery jpaQuery = contentQuery.apply(getQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable, jpaQuery).fetch();
        return PageableExecutionUtils.getPage(content, pageable, jpaQuery::fetchCount);
    }

    protected <T> JPQLQuery<T> JPQLPagination(Pageable pageable,Function<JPAQueryFactory, JPAQuery> contentQuery) {
        JPQLQuery jpqQuery = contentQuery.apply(getQueryFactory());
        getQuerydsl().applyPagination(pageable,jpqQuery);
        return jpqQuery;
    }

    protected <T> Page<T> applyPagination(Pageable pageable,
                                          Function<JPAQueryFactory, JPAQuery> contentQuery, Function<JPAQueryFactory,
            JPAQuery> countQuery) {
        JPAQuery jpaContentQuery = contentQuery.apply(getQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable, jpaContentQuery).fetch();
        JPAQuery countResult = countQuery.apply(getQueryFactory());
        return PageableExecutionUtils.getPage(content, pageable, countResult::fetchCount);
    }
}
