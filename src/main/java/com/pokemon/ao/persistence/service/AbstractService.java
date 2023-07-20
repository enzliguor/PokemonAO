package com.pokemon.ao.persistence.service;

import com.pokemon.ao.domain.ValueObject;
import com.pokemon.ao.persistence.entity.EntityDB;
import com.pokemon.ao.persistence.marshaller.Marshaller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractService<V extends ValueObject, E extends EntityDB, ID> {

    protected final Marshaller<V, E> marshaller;

    protected final JpaRepository<E, ID> dao;

    protected AbstractService(Marshaller<V, E> marshaller, JpaRepository<E, ID> dao) {
        this.marshaller = marshaller;
        this.dao = dao;
    }

    public E save(V v){
        if(v == null){
            return null;
        }
        E e = this.marshaller.marshall(v);

        return this.dao.save(e);
    }

    public V findById(ID id){
        if(id == null){
            return null;
        }
        Optional<E> e = this.dao.findById(id);
        return e.map(this.marshaller::unmarshall).orElse(null);
    }

    public void delete(ID id){
        if(id == null){
            return;
        }
        this.dao.deleteById(id);
    }

    public List<V> findAll(){
        List<E> e = this.dao.findAll();
        return e.stream().map(this.marshaller::unmarshall).toList();
    }
}
