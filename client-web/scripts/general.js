function generaPaginaInfo(film, externalInfo){
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