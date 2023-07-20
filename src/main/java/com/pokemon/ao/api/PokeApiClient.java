package com.pokemon.ao.api;

import com.pokemon.ao.domain.MoveVO;
import com.pokemon.ao.domain.PokemonVO;
import com.pokemon.ao.domain.TypeVO;
import com.pokemon.ao.persistence.service.PokemonService;
import com.pokemon.ao.persistence.service.TypeService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Component
public class PokeApiClient implements ApplicationRunner {
    private final TypeService typeService;
    private final PokemonService pokemonService;

    @Autowired
    public PokeApiClient(TypeService typeService, PokemonService pokemonService) {
        this.typeService = typeService;
        this.pokemonService = pokemonService;
    }

    @Override
    public void run(ApplicationArguments args) {
        JSONArray pokemonArray = getPokemonFromPokeApi(50);
        if (pokemonArray != null) {
            savePokemonFromApiToDatabase(pokemonArray);
        }
    }

    public JSONArray getPokemonFromPokeApi(Integer limit){
        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon?limit=" + limit);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader (conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getJSONArray("results"); //Array dei pokemon all'interno della risposta
            } else {
                System.out.println("Errore nella richiesta: " + conn.getResponseCode());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }

    public void savePokemonFromApiToDatabase(JSONArray pokemonList) {
        for(int i = 0; i < pokemonList.length (); i++) {
            JSONObject pokemon = pokemonList.getJSONObject(i);
            this.savePokemon(pokemon);
        }
    }

    protected void savePokemon(JSONObject pokemon) {

        if (pokemon != null) {
            Long id = pokemon.getLong("id");
            int baseExperience = pokemon.getInt("base_experience");
            String name = pokemon.getString("name");
            //TODO popolare la tabella Types per ripristinare il recupero del type dei pokemon
            /*String typeName = pokemon.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name");
            TypeVO type = typeService.findByName(typeName);*/
            Set<MoveVO> moves = getRandomMoves(pokemon.getJSONArray("moves"), 4);
            String frontSpriteUrl = pokemon.getJSONObject("sprites")
                    .getJSONObject("other")
                    .getJSONObject("home")
                    .getString("front_default");
            PokemonVO pokemonVO = new PokemonVO(id, name, frontSpriteUrl, baseExperience, baseExperience, null, moves, null);
            pokemonService.save(pokemonVO);
        }
    }

    public Set<MoveVO> getRandomMoves(JSONArray allMoves, int count) {
        Set<MoveVO> randomMoves = new HashSet<>();
        int totalMoves = allMoves.length();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(totalMoves);
            JSONObject moveObject = allMoves.getJSONObject(randomIndex).getJSONObject("move");
            Long moveId = moveObject.getLong("id");
            String moveName = moveObject.getString("name");
            int movePower = moveObject.getInt("power");
            //TODO popolare la tabella Types per ripristinare il recupero del type delle moves
            /*String moveTypeName = moveObject.getJSONObject("type").getString("name");
            TypeVO moveType = typeService.findByName(moveTypeName);*/
            MoveVO move = new MoveVO(moveId, moveName, null, movePower);
            randomMoves.add(move);
        }
        return randomMoves;
    }
}
