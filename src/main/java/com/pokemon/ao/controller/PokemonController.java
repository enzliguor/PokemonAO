package com.pokemon.ao.controller;

import com.pokemon.ao.config.PropertyManager;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.utility.PokemonUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {

    private final PokemonUtility pokemonUtility;
    private final int teamSize;

    @Autowired
    public PokemonController(PokemonUtility pokemonUtility, PropertyManager propertyManager) {
        this.pokemonUtility = pokemonUtility;
        this.teamSize = propertyManager.getTeamSize();

    }

    @GetMapping("/random-team")
    public ResponseEntity<List<PokemonVO>> getRandomTeam() {
        List<PokemonVO> randomTeam = pokemonUtility.getRandomPokemons(teamSize);
        return new ResponseEntity<>(randomTeam, HttpStatus.OK);
    }
}