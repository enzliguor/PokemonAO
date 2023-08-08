package com.pokemon.ao.controller;

import com.pokemon.ao.config.CustomProperties;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.utility.ExchangePokemonHandler;
import com.pokemon.ao.utility.PokemonUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pokemon")
@CrossOrigin("*")
public class PokemonController {

    private final PokemonUtility pokemonUtility;
    private final ExchangePokemonHandler exchangePokemonService;
    private final int teamSize;

    @Autowired
    private PokemonController(PokemonUtility pokemonUtility, ExchangePokemonHandler exchangePokemonService, CustomProperties customProperties) {
        this.pokemonUtility = pokemonUtility;
        this.exchangePokemonService = exchangePokemonService;
        this.teamSize = customProperties.getTeamSize();

    }

    @GetMapping("/random-team")
    public ResponseEntity<List<PokemonVO>> getRandomTeam() {
        List<PokemonVO> randomTeam = pokemonUtility.getRandomPokemon(teamSize);
        return new ResponseEntity<>(randomTeam, HttpStatus.OK);
    }

    @PostMapping("/exchange/{pokemonID}")
    public ResponseEntity<PokemonVO> exchangePokemon(@PathVariable Integer pokemonID) {
        PokemonVO receivedPokemon = this.exchangePokemonService.exchange(pokemonID);
        if(receivedPokemon == null)  return ResponseEntity.internalServerError().build();

        return ResponseEntity.ok(receivedPokemon);
    }
}