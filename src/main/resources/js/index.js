"use strict";

const pokeball = document.getElementById('pokeball');
const pokemonContainer = document.getElementById('pokemon-container');
let pokemonTeam;
const tradeButton = document.getElementById('trade-section');

async function fetchRandomTeam() {
    pokeball.src = '../static/images/index/Opened-pokeball.png';
    document.body.style.backgroundImage = "url('../static/images/index/flash.jpg')";
    // Dopo 0,3 secondi, cambia lo sfondo e nasconde la pokeball e invoca l'api
    setTimeout( async function () {
        pokeball.style.display = 'none';
        document.body.style.backgroundImage = "url('../static/images/index/pokedex.jpg')"; // Ripristina lo sfondo predefinito

        try {
            const response = await fetch('http://localhost:8080/api/pokemon/random-team');
            const data = await response.json(); // Converte la risposta in formato JSON

            // Conserva la lista di Pokemon in una variabile
            pokemonTeam = data;
            // Stampa i dati della risposta nella console
            console.log(pokemonTeam);
            // Crea le card dei Pokémon nella squadra
            createPokemonCards();
            tradeButton.setAttribute("style", "display: inline-block")
        } catch (error) {
            console.error('Errore durante la fetch:', error);
        }
    }, 300);

}

function createMovesMarkup(moves) {
    return moves.map(move => `
      <ul class="list-inline px-4 d-flex justify-content-between">
              <li class="list-inline-item">
                  <img width="30px" height="30px" src="${move.type.icon}" alt="Alternate Text" />
              </li>
              <li class="list-inline-item move-font-size">${move.name}</li>
              <li class="list-inline-item move-font-size">${move.power}</li>
      </ul>
    `).join('');
}

function createPokemonCards() {
    // Loop attraverso la squadra di Pokémon
    pokemonTeam.forEach((pokemon, index) => {
        const card = document.createElement('div');
        const typeClass = getTypeClass(pokemon.species.type.name); // Funzione per ottenere la classe CSS del tipo
        card.className = 'col-md-4 mb-4';

        card.innerHTML = `
        <!-- Category Card -->
        <div id="pokemon-${index}" class="card gradient-bg" style="border: 10px solid #ffcb05; border-radius: 2em">
          <div style="border-bottom: 5px solid #ffcb05">
            <div class="px-5 pt-2 ad-titl">
                <h5 id="species-name" class="font-weight-bold">${pokemon.species.name.toUpperCase()}</h5>
                <h6 id="HP">${pokemon.currentHp}/${pokemon.maxHp} HP</h6>
            </div>
            <img id="icon-type" class="icon-type" src="${pokemon.species.type.icon}" alt="Alternate Text" />
          </div>
          <div class="d-flex justify-content-center mt-3">
            <img id="sprites" class="${typeClass}" width="75%" style="border: 5px solid #ffcb05" src="${pokemon.species.spriteUrl}" alt="Alternate Text" />
          </div>
          <div class="d-flex justify-content-center mt-2">
            <h5 id="pokemon-name">${pokemon.name}</h5>
          </div>
          <div id="moves" class="card-body" style="height: 150px">
            <ul class="list-group">
              <!-- Ciclo for per creare dinamicamente le mosse del Pokémon -->
              ${createMovesMarkup(pokemon.moves)}
            </ul>
          </div>
          <div class="d-flex justify-content-center pokemon-trainer">
            <p>${pokemon.originalTrainer}</p>
          </div>
        </div>   
      `;

        pokemonContainer.appendChild(card);
    });
}

// Funzione per ottenere la classe CSS in base al nome del tipo del Pokémon
function getTypeClass(typeName) {
    switch (typeName) {
        case 'bug':
            return 'bug-gradient-bg';
        case 'poison':
            return 'poison-gradient-bg';
        case 'steel':
            return 'steel-gradient-bg';
        case 'fire':
            return 'fire-gradient-bg';
        case 'water':
            return 'water-gradient-bg';
        case 'electric':
            return 'electric-gradient-bg';
        case 'grass':
            return 'grass-gradient-bg';
        case 'psychic':
            return 'psychic-gradient-bg';
        case 'normal':
            return 'normal-gradient-bg';
        case 'ice':
            return 'ice-gradient-bg';
        case 'ghost':
            return 'ghost-gradient-bg';
        case 'flying':
            return 'flying-gradient-bg';
        case 'fighting':
            return 'fighting-gradient-bg';
        case 'fairy':
            return 'fairy-gradient-bg';
        case 'dragon':
            return 'dragon-gradient-bg';
        case 'dark':
            return 'dark-gradient-bg';
        case 'rock':
            return 'rock-gradient-bg';
        case 'ground':
            return 'ground-gradient-bg';
        // Aggiungi altri casi per gli altri tipi
        default:
            return 'gradient-bg';
    }
}

pokeball.addEventListener('click', fetchRandomTeam);