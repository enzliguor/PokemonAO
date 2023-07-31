"use strict";

const pokeball = document.getElementById('pokeball');
const pokemonContainer = document.getElementById('pokemon-container');
let pokemonTeam;
const tradeButton = document.getElementById('trade-section');
let exchangedPokemon;

function hyphenToTitleCase(inputString) {
    const words = inputString.split("-");
    const titleCaseWords = words.map((word) => {
        return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
    });
    return titleCaseWords.join(" ");
}

async function fetchRandomTeam() {
    pokeball.src = '../static/images/index/Opened-pokeball.png';
    document.body.style.backgroundImage = "url('../static/images/index/flash.jpg')";
    // Dopo 0,3 secondi, cambia lo sfondo e nasconde la pokeball e invoca l'api
    setTimeout( async function () {
        pokeball.style.display = 'none';
        document.body.style.backgroundImage = "url('../static/images/index/pokedex.jpg')"; // Ripristina lo sfondo predefinito

        try {
            const response = await fetch('http://localhost:8080/api/pokemon/random-team');
            pokemonTeam = await response.json();
            updatePokemonTeamView();
            tradeButton.setAttribute("style", "display: inline-block")
        } catch (error) {
            console.error('Errore durante la fetch:', error);
        }
    }, 300);

}

function createMovesMarkup(moves) {
    return moves.map(move => `
      <ul class="list-inline px-4 d-flex justify-content-between mt-2">
              <li class="list-inline-item">
                  <img width="30px" height="30px" src="${move.type.icon}" alt="Alternate Text" />
              </li>
              <li class="list-inline-item move-font-size">${hyphenToTitleCase(move.name)}</li>
              <li class="list-inline-item move-font-size">${move.power}</li>
      </ul>
    `).join('');
}

function getRandomIndexFromPokemonTeam() {
    const randomizedNumber = Math.random();
    return  Math.floor( randomizedNumber * (pokemonTeam.length - 1));
}

function generatePokemonCard(pokemon, index) {
    const card = document.createElement('div');
    card.className = 'col-lg-4 col-md-4 col-sm-6 mb-4';

    card.innerHTML = `
        <!-- Category Card -->
        <div id="pokemon-${index}" class="card gradient-bg" style="border: 10px solid #ffcb05; border-radius: 2em; font-family: 'SF distant Galaxy',sans-serif">
          <div style="border-bottom: 5px solid #ffcb05 ">
            <div class="px-5 pt-2 ad-titl">
                <h5 id="species-name" class="font-weight-bold">${pokemon.species.name.toUpperCase()}</h5>
                <h6 id="HP">${pokemon.currentHp}/${pokemon.maxHp} HP</h6>
            </div>
            <img id="icon-type" class="icon-type" src="${pokemon.species.type.icon}" alt="Alternate Text" />
          </div>
          <div class="d-flex justify-content-center mt-3">
            <img id="sprites" class="${pokemon.species.type.name}-gradient-bg" width="75%" style="border: 5px solid #ffcb05" src="${pokemon.species.spriteUrl}" alt="Alternate Text" />
          </div>
          <div class="d-flex justify-content-center mt-2">
            <h5 id="pokemon-name">${pokemon.name}</h5>
          </div>
          <div id="moves" class="card-body" style="height: 200px">
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
}

function updatePokemonTeamView(){
    while (pokemonContainer.firstChild) {
        pokemonContainer.removeChild(pokemonContainer.firstChild);
    }
    pokemonTeam.forEach((pokemon, index) => {
        generatePokemonCard(pokemon, index)
    });
}

async function exchangePokemon() {
    const randomizedIndex = getRandomIndexFromPokemonTeam();
    const selectedPokemon = pokemonTeam.at(randomizedIndex);
    const modalError = document.getElementById('div');
    const response = await fetch('http://localhost:8080/api/pokemon/exchange/' + selectedPokemon.id, {method: "POST"});
    if (response.ok) {
        exchangedPokemon = await response.json();
        pokemonTeam = pokemonTeam.map((pokemon) =>
            pokemon.id === selectedPokemon.id ? exchangedPokemon : pokemon
        );
        const cardToRemove = document.getElementById("pokemon-" + randomizedIndex);
        pokemonContainer.removeChild(cardToRemove);
        updatePokemonTeamView();
    } else {
        console.log("Errore nello scambio del Pokemon, lancio modale errore");
        pokemonContainer.appendChild(modalError);
        $('#errorMessageModal').modal('show');
    }
}

pokeball.addEventListener('click', fetchRandomTeam);
tradeButton.addEventListener('click', exchangePokemon);