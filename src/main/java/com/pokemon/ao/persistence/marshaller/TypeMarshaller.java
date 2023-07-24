package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.persistence.entity.Type;
import org.springframework.stereotype.Component;

@Component
public class TypeMarshaller implements Marshaller<TypeVO, Type> {
    @Override
    public Type marshall(TypeVO typeVO) {
        Type type = new Type();
        type.setId(typeVO.getId());
        type.setName(typeVO.getName());
        type.setIcon(typeVO.getIcon());
        return type;
    }
    @Override
    public TypeVO unmarshall(Type type) {
        Integer id = type.getId();
        String name = type.getName();
        String icon = type.getIcon();
        return new TypeVO(id, name, icon);
    }
}