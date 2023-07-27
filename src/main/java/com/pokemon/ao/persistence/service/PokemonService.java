package com.pokemon.ao.persistence.service;

import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.MoveSlot;
import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.persistence.dao.PokemonDAO;
import com.pokemon.ao.persistence.entity.Pokemon;
import com.pokemon.ao.persistence.marshaller.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class PokemonService extends AbstractService<PokemonVO, Pokemon, Integer> {

    private final CustomProperties customProperties;
    private final MoveService moveService;
    @Autowired
    protected PokemonService(Marshaller<PokemonVO, Pokemon> marshaller, JpaRepository<Pokemon, Integer> dao, CustomProperties customProperties, MoveService moveService) {
        super(marshaller, dao);
        this.customProperties = customProperties;
        this.moveService = moveService;
    }

    public List<Integer> findAllIds(){
        PokemonDAO pokemonDAO = (PokemonDAO) dao;
        return pokemonDAO.findAllIds();
    }

    @Override
    public PokemonVO save(PokemonVO pokemonVO) {
        Map<MoveSlot, MoveVO> moves = pokemonVO.getMoves();
        if(moves == null || moves.isEmpty()){
            MoveVO defaultMove = this.moveService.findById(this.customProperties.getDefaultMoveID());
            pokemonVO.setMoves(new EnumMap<>(MoveSlot.class));
            pokemonVO.getMoves().put(MoveSlot.SLOT_1, defaultMove);
        }
        return super.save(pokemonVO);
    }
}
