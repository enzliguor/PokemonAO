package com.pokemon.ao.persistence.marshaller;

public interface Marshaller<V, E> {
    E marshall(V v);
    V unmarshall(E e);
}