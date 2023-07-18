package com.pokemon.ao.persistence.service;

import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.persistence.dao.TypeDAO;
import com.pokemon.ao.persistence.entity.Type;
import com.pokemon.ao.persistence.marshaller.TypeMarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypeService {
    private final TypeDAO typeDao;

    private final TypeMarshaller typeMarshaller;
    @Autowired
    public TypeService(TypeDAO typeDao , TypeMarshaller typeMarshaller) {
        this.typeDao = typeDao;
        this.typeMarshaller = typeMarshaller;
    }

    public Type getTypeById(Long id){
        return typeDao.findById(id).orElse(null);
    }

    public Type saveTypeVO(TypeVO typeVO){
        return typeDao.save(typeMarshaller.marshall(typeVO));
    }

    public void deleteType(Long id){
        typeDao.deleteById(id);
    }
}
