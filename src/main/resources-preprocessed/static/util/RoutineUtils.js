export const apiRoutinePath = "/api/routine";
export const editRoutinePath = "/edit";
export const newRoutinePath = "/new-routine";
export const continueRoutinePath = "/routine";

export function routineIsActive(onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080" + apiRoutinePath + "/in-progress");
    xhr.onload = () => onSuccess(xhr.responseText === "true");
    xhr.send();
}

export function getNextExercise(onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080" + apiRoutinePath + "/next");
    xhr.onload = () => onSuccess(JSON.parse(xhr.responseText));
    xhr.send();
}