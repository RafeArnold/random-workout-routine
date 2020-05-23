export const contextPath = document.getElementById("contextPath").innerText;
export const apiExercisePath = contextPath + "api/exercise";
export const apiGroupPath = contextPath + "api/group";
export const apiRoutinePath = contextPath + "api/routine";
export const editPath = contextPath + "edit";
export const editExercisePath = contextPath + "edit/exercise";
export const editGroupPath = contextPath + "edit/group";
export const editRoutinePath = contextPath + "edit/routine";
export const newRoutinePath = contextPath + "new-routine";
export const continueRoutinePath = contextPath + "routine";

export function getExercise(id, onSuccess) {
    get(apiExercisePath + "/" + id, (responseText) => onSuccess(JSON.parse(responseText)));
}

export function getGroup(id, onSuccess) {
    get(apiGroupPath + "/" + id, (responseText) => onSuccess(JSON.parse(responseText)));
}

export function getRoutine(id, onSuccess) {
    get(apiRoutinePath + "/" + id, (responseText) => onSuccess(JSON.parse(responseText)));
}

export function getExerciseNames(onSuccess) {
    get(apiExercisePath + "/names", (responseText) => onSuccess(JSON.parse(responseText)));
}

export function getGroupNames(onSuccess) {
    get(apiGroupPath + "/names", (responseText) => onSuccess(JSON.parse(responseText)));
}

export function getRoutineNames(onSuccess) {
    get(apiRoutinePath + "/names", (responseText) => onSuccess(JSON.parse(responseText)));
}

export function routineIsActive(onSuccess) {
    get(apiRoutinePath + "/in-progress", (responseText) => onSuccess(responseText === "true"));
}

export function startRoutine(routineId, onSuccess) {
    post(apiRoutinePath + "/start/" + routineId, onSuccess);
}

export function getNextExercise(onSuccess) {
    post(apiRoutinePath + "/next", (responseText) => onSuccess(JSON.parse(responseText)));
}

export function getCurrentExercise(onSuccess) {
    post(apiRoutinePath + "/current", (responseText) => onSuccess(JSON.parse(responseText)));
}

export function stopRoutine(onSuccess) {
    post(apiRoutinePath + "/stop", onSuccess);
}

export function searchExerciseNames(filter, onSuccess) {
    post(apiExercisePath + "/search", (responseText) => onSuccess(JSON.parse(responseText)), filter);
}

export function searchGroupNames(filter, onSuccess) {
    post(apiGroupPath + "/search", (responseText) => onSuccess(JSON.parse(responseText)), filter);
}

export function saveExercise(exercise, onSuccess) {
    post(apiExercisePath + "/save", onSuccess, exercise);
}

export function saveGroup(group, onSuccess) {
    post(apiGroupPath + "/save", onSuccess, group);
}

export function saveRoutine(routine, onSuccess) {
    post(apiRoutinePath + "/save", onSuccess, routine);
}

function get(url, onSuccess) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", window.location.origin + url);
    xhr.onload = () => onSuccess(xhr.responseText);
    xhr.send();
}

function post(url, onSuccess, body) {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", window.location.origin + url);
    xhr.onload = () => onSuccess(xhr.responseText);
    if (body) {
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhr.send(JSON.stringify(body));
    } else {
        xhr.send();
    }
}