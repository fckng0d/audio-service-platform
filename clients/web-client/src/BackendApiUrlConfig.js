const urlDev = process.env.REACT_APP_BACK_API_ORIGIN;

export let apiUrl = urlDev !== undefined ? urlDev : "http://127.0.0.1:8080";
