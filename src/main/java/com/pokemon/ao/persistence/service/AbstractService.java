package com.pokemon.ao.persistence.service;

import com.pokemon.ao.domain.ValueObject;
import com.pokemon.ao.persistence.entity.EntityDB;
import com.pokemon.ao.persistence.marshaller.Marshaller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public abstract class AbstractService<V extends ValueObject, E extends EntityDB, ID> {

    private final Marshaller<V, E> marshaller;

    private final JpaRepository<E, ID> dao;

    protected AbstractService(Marshaller<V, E> marshaller, JpaRepository<E, ID> dao) {
        this.marshaller = marshaller;
        this.dao = dao;
    }

    protected E save(V v){
        if(v == null){
            return null;
        }
        E e = this.marshaller.marshall(v);

        return this.dao.save(e);
    }

    protected V findById(ID id){
        if(id == null){
            return null;
        }
        Optional<E> e = this.dao.findById(id);
        return e.map(this.marshaller::unmarshall).orElse(null);
    }

    protected void delete(ID id){
        if(id == null){
            return;
        }
        this.dao.deleteById(id);
    }
}
