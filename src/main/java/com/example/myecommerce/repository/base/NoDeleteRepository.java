package com.example.myecommerce.repository.base;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * Por seguridad se genera una base para que Repositorios como
 * Order, Product y User sean repositorios no permitan acciones delete
 * evitando errores nullPointer futuros.
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface NoDeleteRepository<T,ID> extends Repository<T,ID> { //Generamos una base de repositorios para evitar por seguridad la eliminacion de recursos
    List<T> findAll();//Metodo findAll

    Page<T> findAllByPage(Pageable pageable);//Para evitar sobrecarga obtenemos por paginas

    Optional<T> findById(ID id); //Metodo findById

    <S extends T> S save(S entity);//Metodo save
}
