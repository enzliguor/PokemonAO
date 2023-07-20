package com.pokemon.ao.persistence.service;

import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.persistence.dao.TypeDAO;
import com.pokemon.ao.persistence.entity.Type;
import com.pokemon.ao.persistence.marshaller.Marshaller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TypeService extends AbstractService<TypeVO, Type, Long>{
    protected TypeService(Marshaller<TypeVO, Type> marshaller, JpaRepository<Type, Long> dao) {
        super(marshaller, dao);
    }

    public TypeVO findByName(String name) {
        return marshaller.unmarshall(((TypeDAO) dao).findByName(name).orElse(null));
    }
}
