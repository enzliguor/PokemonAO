package com.pokemon.ao.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MoveSlot {
    SLOT_1(1),
    SLOT_2(2),
    SLOT_3(3),
    SLOT_4(4);

    private final int slotNumber;

    MoveSlot(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public MoveSlot of(int index){
        return Arrays.stream(MoveSlot.values())
                .filter(moveSlot -> moveSlot.slotNumber == index)
                .findFirst()
                .orElse(null);
    }
}
