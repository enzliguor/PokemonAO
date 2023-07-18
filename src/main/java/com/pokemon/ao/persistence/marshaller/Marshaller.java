package com.pokemon.ao.persistence.marshaller;

import com.pokemon.ao.domain.ValueObject;
import com.pokemon.ao.persistence.entity.EntityDB;

public interface Marshaller<V extends ValueObject, E extends EntityDB> {
    E marshall(V v);
    V unmarshall(E e);
}