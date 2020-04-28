export const apiExercisePath = "/api/exercise";
export const apiGroupPath = "/api/group";
export const apiRoutinePath = "/api/routine";
export const editPath = "/edit";
export const editExercisePath = "/edit/exercise";
export const editGroupPath = "/edit/group";
export const editRoutinePath = "/edit/routine";
export const newRoutinePath = "/new-routine";
export const continueRoutinePath = "/routine";

export function getExercise(id, onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080" + apiExercisePath + "/" + id);
    xhr.onload = () => onSuccess(JSON.parse(xhr.responseText));
    xhr.send();
}

export function getGroup(id, onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080" + apiGroupPath + "/" + id);
    xhr.onload = () => onSuccess(JSON.parse(xhr.responseText));
    xhr.send();
}

export function getRoutine(id, onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080" + apiRoutinePath + "/" + id);
    xhr.onload = () => onSuccess(JSON.parse(xhr.responseText));
    xhr.send();
}

export function getExerciseNames(onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080" + apiExercisePath + "/names");
    xhr.onload = () => onSuccess(JSON.parse(xhr.responseText));
    xhr.send();
}

export function getGroupNames(onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080" + apiGroupPath + "/names");
    xhr.onload = () => onSuccess(JSON.parse(xhr.responseText));
    xhr.send();
}

export function getRoutineNames(onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080" + apiRoutinePath + "/names");
    xhr.onload = () => onSuccess(JSON.parse(xhr.responseText));
    xhr.send();
}

export function routineIsActive(onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080" + apiRoutinePath + "/in-progress");
    xhr.onload = () => onSuccess(xhr.responseText === "true");
    xhr.send();
}

export function startRoutine(routineId, onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080" + apiRoutinePath + "/start/" + routineId);
    xhr.onload = onSuccess;
    xhr.send();
}

export function getNextExercise(onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080" + apiRoutinePath + "/next");
    xhr.onload = () => onSuccess(JSON.parse(xhr.responseText));
    xhr.send();
}

export function getCurrentExercise(onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080" + apiRoutinePath + "/current");
    xhr.onload = () => onSuccess(JSON.parse(xhr.responseText));
    xhr.send();
}

export function stopRoutine(onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080" + apiRoutinePath + "/stop");
    xhr.onload = onSuccess;
    xhr.send();
}