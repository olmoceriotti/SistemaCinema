const mcuP = document.querySelector(".mcu");
fetchMCUdata()
    .then(mcuJson => {
        let mcuString = "Prossimo film MCU tra: " + mcuJson["days_until"] + " giorni";
        mcuP.textContent = mcuString;
    })
    .catch(error => {
    console.error('Error:', error);
});

async function fetchMCUdata(){
    const options = {
        method: 'GET'
    };
    const response = await fetch('https://www.whenisthenextmcufilm.com/api', options);
    const json = await response.json();

    return json;
}

async function getExternalInfo(id){
    const options = {
        method: 'GET',
        headers: {
            accept: 'application/json',
            Authorization: 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlMDYzYzM3NjI5NWY3OTE3ZjQyNGI1YzNjZGU2ZWIzMCIsInN1YiI6IjYyNGVlNzc2MDcyOTFjMDA5ZjUxMGFmYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.d5oGI6iil9kB2gq83aZGTL4dfXGc4hJ2Jv940ZScSOk'
        }
    };
    let response = await fetch('https://api.themoviedb.org/3/movie/'+ id +'?language=en-US', options);
    response = await response.json();
    return response;
}

async function generaPaginaInfo(film, externalInfo){
    if(!externalInfo){
        externalInfo = await getExternalInfo(film.externalID);
    }
    const dialog = document.createElement("dialog");
    dialog.classList.add("infoPage");
    
    const leftCol = document.createElement("div");
    leftCol.classList.add("leftCol");

    const title = document.createElement("h3");
    title.textContent = film.nome;
    leftCol.appendChild(title);

    const genere = document.createElement("p");
    genere.textContent = "Genere: " + externalInfo.genres[0].name;
    leftCol.appendChild(genere);

    const durata = document.createElement("p");
    durata.textContent = "Durata: " + film.durata;
    leftCol.appendChild(durata);

    const regista = document.createElement("p");
    regista.textContent = "Regista: " + film.regista;
    leftCol.appendChild(regista);

    const attori = document.createElement("p");
    attori.textContent = "Attori: " + film.attori;
    leftCol.appendChild(attori);

    const button = document.createElement("button");
    button.textContent = "Ok";
    button.addEventListener("click", () => {
        dialog.close();
        document.body.classList.remove("shadow");
        document.body.removeChild(dialog);
    })
    leftCol.appendChild(button);

    dialog.appendChild(leftCol);

    const rightCol = document.createElement("div");
    rightCol.classList.add("rightCol");

    const img = document.createElement("img");
    img.src = "https://image.tmdb.org/t/p/original/" + externalInfo.poster_path;
    rightCol.appendChild(img);

    dialog.appendChild(rightCol);

    

    document.body.appendChild(dialog);
    dialog.showModal();
    document.body.classList.add("shadow");
}