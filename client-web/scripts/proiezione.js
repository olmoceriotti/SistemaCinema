async function loadPrenotazioni(){
    const options = {
        method: 'GET'
    };
    let response = await fetch('http://localhost:8080/proiezione/', options);
    let container = document.querySelector(".prenotazione-grid");
   
    obj = await response.json();

    obj.forEach(async element => {
        el = await createPrenotazione(element);
        container.append(el);
    });
}

async function createPrenotazione(obj){
    const prenotazione = document.createElement("tr");
    prenotazione.classList.add("prenotazione-container");

    let tr = document.createElement("tr");
    const h6 = document.createElement("h6");
    h6.textContent = obj.nome;
    tr.appendChild(h6);
    prenotazione.appendChild(tr);

    const img = document.createElement("img");
    const url = await getPhotoPath(film.externalID);
    img.src = "https://image.tmdb.org/t/p/original/" + url;
    filmCard.appendChild(img);

    const button = document.createElement("button");
    button.textContent = "More info";
    button.classList.add("more-info");
    filmCard.appendChild(button);

    return filmCard;
}