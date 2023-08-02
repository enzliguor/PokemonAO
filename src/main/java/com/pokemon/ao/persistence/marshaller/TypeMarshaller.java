package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.persistence.entity.Type;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeMarshaller implements Marshaller<TypeVO, Type> {
    @Override
    public Type marshall(TypeVO typeVO) {
        return Type.builder()
                .id(typeVO.getId())
                .name(typeVO.getName())
                .icon(typeVO.getIcon())
                .build();
    }

    @Override
    public TypeVO unmarshall(Type type) {
        return TypeVO.builder()
                .id(type.getId())
                .name(type.getName())
                .icon(type.getIcon())
                .build();
    }
}