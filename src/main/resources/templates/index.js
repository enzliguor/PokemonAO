"use strict";

const pokeball = document.getElementById('pokeball');
const pokemonContainer = document.getElementById('pokemon-container');
let pokemonTeam;

async function fetchRandomTeam() {
    pokeball.src = 'Opened-pokeball.png';
    document.body.style.backgroundImage = "url('flash.jpg')";
    // Dopo 0,3 secondi, cambia lo sfondo e nasconde la pokeball e invoca l'api
    setTimeout( async function () {
        pokeball.style.display = 'none';
        document.body.style.backgroundImage = "url('pokedex.jpg')"; // Ripristina lo sfondo predefinito

        try {
            const response = await fetch('http://localhost:8080/api/pokemon/random-team');
            const data = await response.json(); // Converte la risposta in formato JSON

            // Conserva la lista di Pokemon in una variabile
            pokemonTeam = data;
            // Stampa i dati della risposta nella console
            console.log(pokemonTeam);
            // Crea le card dei Pokémon nella squadra
            createPokemonCards();
        } catch (error) {
            console.error('Errore durante la fetch:', error);
        }
    }, 300);

}

function createMovesMarkup(moves) {
    return moves.map(move => `
      <ul class="list-inline">
          <div class="d-flex justify-content-between">
              <li class="list-inline-item">
                  <img width="30px" height="30px" src="${move.type.icon}" alt="Alternate Text" />
              </li>
              <li class="list-inline-item">${move.name}</li>
              <li class="list-inline-item">${move.power}</li>
          </div>
      </ul>
    `).join('');
}

function createPokemonCards() {
    // Loop attraverso la squadra di Pokémon
    pokemonTeam.forEach((pokemon, index) => {
        const card = document.createElement('div');
        card.className = 'col-md-4 mb-4';

        card.innerHTML = `
        <!-- Category Card -->
        <div id="pokemon-${index}" class="card gradient-bg" style="border: 10px solid #ffcb05; border-radius: 2em">
          <div style="border-bottom: 5px solid #ffcb05">
            <div class="pl-4">
              <div class="d-flex justify-content-start px-3 pt-2 ad-title">
                <h5 id="species-name" class="font-weight-bold">${pokemon.species.name}</h5>
              </div>
              <div class="d-flex justify-content-start px-3 ad-title">
                <h6 id="HP">${pokemon.currentHp}/${pokemon.maxHp} HP</h6>
              </div>
            </div>
            <img id="icon-type" class="img-fluid icon-type" src="${pokemon.species.type.icon}" alt="Alternate Text" />
          </div>
          <div class="card-image d-flex justify-content-center mt-3">
            <img id="sprites" class="img-fluid grass-gradient-bg p-3" width="75%" style="border: 5px solid #ffcb05" src="${pokemon.species.spriteUrl}" alt="Alternate Text" />
          </div>
          <div class="d-flex justify-content-center mt-2">
            <h5 id="pokemon-name">${pokemon.name}</h5>
          </div>
          <div id="moves" class="card-body mt-2">
            <ul class="list-group">
              <!-- Ciclo for per creare dinamicamente le mosse del Pokémon -->
              ${createMovesMarkup(pokemon.moves)}
            </ul>
          </div>
          <div class="px-2 d-flex justify-content-center pokemon-trainer">
            <p>${pokemon.originalTrainer}</p>
          </div>
        </div>   
      `;

        pokemonContainer.appendChild(card);
    });
}

pokeball.addEventListener('click', fetchRandomTeam);