async function loadFilms(){
    const options = {
        method: 'GET'
    };
    let response = await fetch('http://localhost:8080/film/', options);
    let container = document.querySelector(".film-container");
   
    obj = await response.json();
    i = 0;
    obj.forEach(async element => {
        el = await createFilmCard(element);
        container.append(el);
    });
}


async function createFilmCard(film) {
  const filmCard = document.createElement("div");
  filmCard.classList.add("film-card");

  const h6 = document.createElement("h6");
  h6.textContent = film.nome;
  filmCard.appendChild(h6);

  const img = document.createElement("img");
  
  const externalInfo =  await getExternalInfo(film.externalID);
  const url = "https://image.tmdb.org/t/p/original/" + externalInfo.poster_path;
  img.src = url;
  filmCard.appendChild(img);

  const button = document.createElement("button");
  button.textContent = "More info";
  button.addEventListener("click", () =>{
    generaPaginaInfo(film, externalInfo);
  });
  button.classList.add("more-info");
  filmCard.appendChild(button);

  return filmCard;
}