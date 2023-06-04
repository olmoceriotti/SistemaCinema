const response = await fetch("/film")

if(!response.ok){
    //loading Failure
}else{
    filmList = response.json();
    filmList.forEach(film => {
        filmCard  = createFilmCard();
        filmSection = document.querySelector(".film-container");
        filmSection.appendChild(filmCard);
    });
    
    
}

function createFilmCard(film){
    filmCard = document.createElement("div");
    filmCard.classList.add("film-card");
    h6 = document.createElement("h6");
    h6.textContent = film.title;
    img = document.createElement("img");
    img.src = film.img;
    button = document.createElement("button");
    button.textContent = "More info";
    button.classList.add("more-info");
    button.addEventListener("click", (event) => {
        //change page and generate film info
    });
    filmCard.appendChild(h6);
    filmCard.appendChild(img);
    filmCard.appendChild(button);
}