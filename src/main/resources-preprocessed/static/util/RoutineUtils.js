export function getNextExercise(onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/api/routine/next");
    xhr.onload = () => onSuccess(JSON.parse(xhr.responseText));
    xhr.send();
}