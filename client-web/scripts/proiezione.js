async function loadProiezioni(){
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
        let el = await createProiezione(element);
        return el;
    });

    const elements = await Promise.all(promises);
    elements.forEach((el) => {
        container.append(el);
    });
}

async function createProiezione(obj){
    const prenotazione = document.createElement("div");
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
    console.log(obj["totale posti occupati"]);
    posti.classList.add("posti");
    prenotazione.appendChild(posti);

    const divButton = document.createElement("div");
    const button = document.createElement("button");
    button.textContent = "Prenota";
    button.addEventListener("click", (event) => {
        effettuaPrenotazione(obj, film);
    });
    divButton.appendChild(button);
    prenotazione.appendChild(divButton);

    return prenotazione;
}

function effettuaPrenotazione(obj, film){
    const section = document.querySelector("section");
    section.innerHTML = null;

    const container = document.createElement("div");
    container.classList.add("prenotazione");

    const leftCol = document.createElement("div");
    leftCol.classList.add("left-col");

    const title = document.createElement("h3");
    title.classList.add("title");
    title.textContent = film.nome;
    leftCol.appendChild(title);

    const  durata = document.createElement("p");
    durata.classList.add("durata");
    durata.textContent = "Durata: " + film.durata;
    leftCol.appendChild(durata);

    const data = document.createElement("p");
    data.classList.add("durata");
    data.textContent = obj.orario[2] + "/" + obj.orario[1] + "/" + obj.orario[0] + " " + obj.orario[3] + ":" + obj.orario[4];
    leftCol.appendChild(data);

    const sala = document.createElement("p");
    sala.classList.add("durata");
    sala.textContent = "Sala: " + obj.sala;
    leftCol.appendChild(sala);

    const form = document.createElement("form");

    const counterPosti = document.createElement("p");
    counterPosti.classList.add("counterPosti");
    counterPosti.textContent = "Numero posti selezionati: 0"
    form.appendChild(counterPosti);

    const listaPosti = document.createElement("p");
    listaPosti.classList.add("listaPosti");
    listaPosti.textContent = "Nessun posto selezionato";
    form.appendChild(listaPosti);

    const submitButton = document.createElement("button");
    submitButton.textContent = "Prenota";
    submitButton.addEventListener("click", (event) => {
        event.preventDefault();
        onSubmit(obj.id);
    })

    form.appendChild(submitButton);
    leftCol.appendChild(form);
    container.appendChild(leftCol);

    const rightCol = document.createElement("div");
    rightCol.classList.add("right-col");
    rightCol.appendChild(creaTabellaPosti(obj["posti prenotati"]));
    container.appendChild(rightCol);

    section.appendChild(container);
}

function creaTabellaPosti(posti){
    const table = document.createElement("table");
    for(let i = 0; i < 15; i++){
        let tr = document.createElement("tr");
        let rowCode = String.fromCharCode(65 + i);
        for(let j = 0; j < 10; j++){
            let td = document.createElement("td");
            td.setAttribute("codice", rowCode + j);
            if(posti.includes(rowCode + j)){
                td.classList.add("occupied");
            }else{
                td.addEventListener("click", () =>{
                    if(td.classList.contains("selected")){
                        decrCounter();
                        rimuoviPosto(td);
                        td.classList.remove("selected");
                    }else{
                        if(incrCounter()){
                            aggiungiPosto(td);
                            td.classList.add("selected");
                        }else{
                            //TODO rimuovere sto schifo
                            alert("Massimo posti raggiunto!");
                        }
                        
                    }
                });
            }
            td.classList.add("posto");
            tr.appendChild(td);
        }
        table.appendChild(tr);
    }
    return table;
}

function incrCounter(){
    const counter = document.querySelector(".counterPosti");
    let s = counter.textContent;
    s = s.replace("Numero posti selezionati: ", "");
    s = parseInt(s);
    if(s < 5){
        s++;
        counter.textContent = "Numero posti selezionati: " + s;
        return s;
    }
    return null;
    
}

function decrCounter(){
    const counter = document.querySelector(".counterPosti");
    let s = counter.textContent;
    s = s.replace("Numero posti selezionati: ", "");
    s = parseInt(s);
    s--;
    counter.textContent = "Numero posti selezionati: " + s;
}

function aggiungiPosto(td){
    const listaPosti = document.querySelector(".listaPosti");
    if(listaPosti.textContent == "Nessun posto selezionato"){
        listaPosti.textContent = "Posti selezionati: " + td.getAttribute("codice");
    }else{
        listaPosti.textContent += ", " + td.getAttribute("codice");
    }
    
}

function rimuoviPosto(td){
    const listaPosti = document.querySelector(".listaPosti");
    let s = listaPosti.textContent
    let posto = td.getAttribute("codice");
    if(s.includes(posto)){
        if(s == "Posti selezionati: " + td.getAttribute("codice")){
            listaPosti.textContent = "Nessun posto selezionato";
        }else{
            if(s.includes(posto + ', ')){
                listaPosti.textContent = s.replace(posto + ", ", "");
            }else{
                listaPosti.textContent = s.replace(", " + posto, "");
            }
        } 
    }
}

async function onSubmit(id){
    const data = {
        proiezioneID: id,
        numeroPosti: document.querySelector(".counterPosti").textContent.replace("Numero posti selezionati: ", ""),
        posti: document.querySelector(".listaPosti").textContent.replace("Posti selezionati: ", "").split(", ")
    }
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }

    if(data.numeroPosti != "0"){
        const response = await fetch("http://localhost:8080/prenotazione/", options);
        const json = await response.json();
        inviaConfermaPrenotazione(json);
    }

}

function inviaConfermaPrenotazione(json){
    const section = document.querySelector("section");
    section.innerHTML = null;
    
    const conferma = document.createElement("div");
    conferma.classList.add("conferma");

    const titolo = document.createElement("h1");
    titolo.textContent = "Prenotazione confermata!"
    conferma.appendChild(titolo);

    const id = document.createElement("p");
    id.textContent = "L'ID della tua prenotazione è " + json;
    conferma.appendChild(id);

    const qr = document.createElement("img");
    qr.src = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + json;
    conferma.appendChild(qr);
    section.appendChild(conferma);
}