const mainButton = document.querySelector(".btn");
const qrButton = document.querySelector(".qr");
const fileInput = document.querySelector("input[type=file]")
const inputArea = document.querySelector(".input");

inputArea.addEventListener("keydown", function(event) {
    if (event.key === "Enter" || event.keyCode === 13) {
      mainButton.click();
    }
});
mainButton.addEventListener("click", getPrenotazione);
qrButton.addEventListener("click", () => {
    fileInput.click();
});

async function getPrenotazione(){
    const input = document.querySelector(".input");
    id = input.value;
    fetchData(id); 
}

async function fetchData(id){
    const options = {
        method: "GET"
    }
    try{
        const responsePrenotazione = await fetch("http://localhost:8080/prenotazione/"  + id, options);
        if(responsePrenotazione.ok){
            const resObjPrenotazione = await responsePrenotazione.json();
            const responseProiezione = await fetch("http://localhost:8080/proiezione/" + resObjPrenotazione.proiezioneID);
            const resObjProiezione = await responseProiezione.json();
            const responseFilm = await fetch("http://localhost:8080/film/" + resObjProiezione["film id"]);
            const resObjFilm = await responseFilm.json();
            displayPrenotazione(resObjPrenotazione, resObjProiezione, resObjFilm);
        }else{
            throw new Error('Fetch failed with status: ' + response.status);
        }
    }catch(error){
        showAvviso("Codice non valido!");
    }
    
    
}

function displayPrenotazione(prenotazione, proiezione, film){
    const section = document.querySelector("section");
    section.innerHTML = null;
    section.classList.remove("section");
    const container = document.createElement("div");
    container.classList.add("prenotazione");

    const leftCol = document.createElement("div");
    leftCol.classList.add("left-col");

    const title = document.createElement("h3");
    title.classList.add("title");
    title.textContent = film.nome;
    leftCol.appendChild(title);

    const idPrenotazione = document.createElement("p");
    idPrenotazione.classList.add("idP");
    idPrenotazione.textContent = "id: " + prenotazione.id;
    leftCol.appendChild(idPrenotazione);

    const durata = document.createElement("p");
    durata.classList.add("durata");
    durata.textContent = "Durata: " + film.durata;
    leftCol.appendChild(durata);

    const data = document.createElement("p");
    data.classList.add("data");
    data.textContent = proiezione.orario[2] + "/" + proiezione.orario[1] + "/" + proiezione.orario[0] + " " + proiezione.orario[3] + ":" + proiezione.orario[4];
    leftCol.appendChild(data);

    const sala = document.createElement("p");
    sala.classList.add("sala");
    sala.textContent = "Sala: " + proiezione.sala;
    leftCol.appendChild(sala);

    const counter = document.createElement("p");
    counter.classList.add("counter");
    counter.textContent = "Numero posti selezionati: " + prenotazione.numeroPosti;
    leftCol.appendChild(counter);

    const listaPosti = document.createElement("p");
    listaPosti.classList.add("listaPosti");
    listaPosti.textContent = "Posti selezionati: " + prenotazione.posti.join(", ");
    leftCol.appendChild(listaPosti);

    const containerBtn = document.createElement("div");
    containerBtn.classList.add("containerBtn");

    const bottoneCanc = document.createElement("button");
    bottoneCanc.textContent = "Cancella prenotazione";
    bottoneCanc.classList.add("btn-mod");
    bottoneCanc.addEventListener("click", () =>{
        cancellaPrenotazione(prenotazione);
    })
    containerBtn.appendChild(bottoneCanc);

    const bottoneModifica = document.createElement("button");
    bottoneModifica.textContent = "Modifica prenotazione";
    bottoneModifica.classList.add("btn-mod");
    bottoneModifica.addEventListener("click", () =>{
        modificaPrenotazione(prenotazione);
    })
    containerBtn.appendChild(bottoneModifica);

    leftCol.appendChild(containerBtn);
    container.appendChild(leftCol);

    const rightCol = document.createElement("div");
    rightCol.classList.add("right-col");
    rightCol.appendChild(creaTabellaPosti(prenotazione.posti));
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
                td.addEventListener("click", () =>{
                    if(td.classList.contains("selected")){
                        incrCounter();
                        aggiungiPosto(td);
                        td.classList.remove("selected");
                        if(!td.classList.contains("occupied")){
                            td.classList.add("occupied");
                        }
                    }else{
                        decrCounter();
                        rimuoviPosto(td);
                        td.classList.add("selected");
                        td.classList.remove("occupied");
                    }
                });
            }else{
                
            }
            td.classList.add("posto");
            tr.appendChild(td);
        }
        table.appendChild(tr);
    }
    return table;
}

function incrCounter(){
    const counter = document.querySelector(".counter");
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
    const counter = document.querySelector(".counter");
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

async function modificaPrenotazione(prenotazione){
    const listaPosti = document.querySelector(".listaPosti");
    if(listaPosti != "Nessun posto selezionato" ){
        const postiRimanenti = listaPosti.textContent.replace("Posti selezionati: ", "").split(", ");
        if(postiRimanenti == "Nessun posto selezionato"){
            cancellaPrenotazione(prenotazione);
        }else{
            const posti = prenotazione.posti.filter((element) => !postiRimanenti.includes(element));
        
            options = {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(posti)
            }
            try{
                await fetch("http://localhost:8080/prenotazione/" + prenotazione.id, options);
                fetchData(prenotazione.id);
            }catch(error){
                showAvviso("Qualcosa è andato storto!");
            }
        
        }   
    }
}

async function cancellaPrenotazione(prenotazione){
    const finestra = document.createElement("dialog");
    finestra.classList.add("dialog");

    const text = document.createElement("p");
    text.textContent = "Cancellare la prenotazione?";
    finestra.appendChild(text);

    const div = document.createElement("div");

    const buttonSi = document.createElement("button");
    buttonSi.textContent = "Conferma";
    buttonSi.addEventListener("click", async () =>{
        options = {
            method: "DELETE"
        }
        fetch("http://localhost:8080/prenotazione/" + prenotazione.id, options);
        finestra.close();
        body.classList.remove("shadow");
        body.removeChild(finestra);
        const section = document.querySelector("section");
        section.innerHTML = null;
        section.classList.add("inputform");

        const h3 = document.createElement("h3");
        h3.textContent = "Prenotazione cancellata con successo!"
        section.appendChild(h3);

        const a = document.createElement("a");
        a.href = "../index.html"
        const button = document.createElement("button");
        button.textContent = "Ok";
        a.appendChild(button);
        section.appendChild(a);
    });
    div.appendChild(buttonSi);

    const buttonNo = document.createElement("button");
    buttonNo.textContent = "Annulla";
    buttonNo.addEventListener("click", () =>{
        finestra.close();
        body.classList.remove("shadow");
        body.removeChild(finestra);
    });
    div.appendChild(buttonNo);

    finestra.appendChild(div);

    const body =document.querySelector("body");
    body.appendChild(finestra);
    finestra.showModal();
    body.classList.add("shadow");
}

function showAvviso(text){
    const body = document.querySelector("body");
    const dialog = document.createElement("dialog");

    const p = document.createElement("p");
    p.textContent = text;
    dialog.appendChild(p);

    const div = document.createElement("div");

    const button = document.createElement("button");
    button.textContent = "Ok";
    button.addEventListener("click", () =>{
        dialog.close();
        body.classList.remove("shadow");
        body.removeChild(dialog);
    });

    div.appendChild(button);
    dialog.appendChild(div);

    dialog.classList.add("dialog");
    body.appendChild(dialog);
    
    body.classList.add("shadow");

    dialog.showModal();
}

function fetchQR(userFile, formData) {
    fetch("https://api.qrserver.com/v1/read-qr-code/", {
        method: 'POST', body: formData
    }).then(res => res.json()).then(result => {
        result = result[0].symbol[0].data;
        const input = document.querySelector("body > div > section > div > input");
        if(result){
            showAvviso("QR Code letto correttamente!");
            input.value = result;

        }else{
            showAvviso("Impossibile leggere QR Code");
        }
    }).catch(() => {
        showAvviso("Impossibile leggere QR Code");
    });
}

fileInput.addEventListener("change", async event => {
    const userFile = event.target.files[0];
    if(!userFile) return;
    const formData = new FormData();
    formData.append('file', userFile);
    fetchQR(userFile, formData);
});