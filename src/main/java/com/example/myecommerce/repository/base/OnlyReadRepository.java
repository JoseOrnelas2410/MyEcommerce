package com.example.myecommerce.repository.base;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Por seguridad se genera una base para que Repositorios como
 * OrderStatus, PaymentStatus y ProductType sean repositorios de solo lectura
 * evitando acciones como editar algun status dentro de la tabla
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface OnlyReadRepository<T,ID> extends Repository<T,ID> {
    Optional<T> findByID(ID id);

    List<T> findAll();
}
