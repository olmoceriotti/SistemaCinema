async function loadPrenotazioni(){
    const options = {
        method: 'GET'
    };
    let response = await fetch('http://localhost:8080/proiezione/', options);
    let container = document.querySelector(".table");
   
    obj = await response.json();

    obj.sort((a, b) => {
        const dateA = new Date(a.orario[0], a.orario[1] - 1, a.orario[2], a.orario[3], a.orario[4]);
        const dateB = new Date(b.orario[0], b.orario[1] - 1, b.orario[2], b.orario[3], b.orario[4]);
        return dateA - dateB;
    });

    const promises = obj.map(async (element) => {
        let el = await createPrenotazione(element);
        return el;
    });

  const elements = await Promise.all(promises);
    elements.forEach((el) => {
        container.append(el);
  });
}

async function createPrenotazione(obj){
    const prenotazione = document.createElement("tr");
    prenotazione.classList.add("table-row");

    let div = document.createElement("div");
    let resFilm = await fetch('http://localhost:8080/film/'  + obj["film id"]);
    let film = await resFilm.json();
    div.textContent = film.nome;
    div.classList.add("film");
    prenotazione.appendChild(div);

    const divData = document.createElement("div");
    divData.textContent = obj.orario[2] + "/" + obj.orario[1] + "/" + obj.orario[0];
    divData.classList.add("data");
    prenotazione.appendChild(divData);

    const divOre = document.createElement("div");
    divOre.textContent = obj.orario[3] + ":"  + obj.orario[4];
    divOre.classList.add("ora");
    prenotazione.appendChild(divOre);

    const posti = document.createElement("div");
    posti.textContent = 150 - parseInt(obj["totale posti occupati"]);
    posti.classList.add("posti");
    prenotazione.appendChild(posti);

    const divButton = document.createElement("div");
    const button = document.createElement("button");
    button.textContent = "Prenota";
    divButton.appendChild(button);
    prenotazione.appendChild(divButton);

    return prenotazione;
}