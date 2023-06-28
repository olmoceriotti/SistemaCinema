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