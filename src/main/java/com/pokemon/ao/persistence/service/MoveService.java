package com.pokemon.ao.persistence.service;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.persistence.dao.MoveDAO;
import com.pokemon.ao.persistence.entity.Move;
import com.pokemon.ao.persistence.marshaller.MoveMarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoveService {
    private final MoveDAO moveDao;

    private final MoveMarshaller moveMarshaller;

    @Autowired
    public MoveService(MoveDAO moveDao , MoveMarshaller moveMarshaller) {
        this.moveDao = moveDao;
        this.moveMarshaller = moveMarshaller;
    }

    public Move getMoveById(Long id){
        return moveDao.findById(id).orElse(null);
    }

    public Move saveMoveVO(MoveVO moveVO){
        return moveDao.save(moveMarshaller.marshall(moveVO));
    }

    public void deleteMove(Long id){
        moveDao.deleteById(id);
    }
}
