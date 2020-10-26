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
    request(apiExercisePath + "/" + id, (responseText) => onSuccess(JSON.parse(responseText)), "GET");
}

export function getGroup(id, onSuccess) {
    request(apiGroupPath + "/" + id, (responseText) => onSuccess(JSON.parse(responseText)), "GET");
}

export function getRoutine(id, onSuccess) {
    request(apiRoutinePath + "/" + id, (responseText) => onSuccess(JSON.parse(responseText)), "GET");
}

export function getExerciseNames(onSuccess) {
    request(apiExercisePath + "/names", (responseText) => onSuccess(JSON.parse(responseText)), "GET");
}

export function getGroupNames(onSuccess) {
    request(apiGroupPath + "/names", (responseText) => onSuccess(JSON.parse(responseText)), "GET");
}

export function getRoutineNames(onSuccess) {
    request(apiRoutinePath + "/names", (responseText) => onSuccess(JSON.parse(responseText)), "GET");
}

export function routineIsActive(onSuccess) {
    request(apiRoutinePath + "/in-progress", (responseText) => onSuccess(responseText === "true"), "GET");
}

export function startRoutine(routineId, onSuccess) {
    request(apiRoutinePath + "/start/" + routineId, onSuccess, "POST");
}

export function getNextExercise(onSuccess) {
    request(apiRoutinePath + "/next", (responseText) => onSuccess(JSON.parse(responseText)), "POST");
}

export function getCurrentExercise(onSuccess) {
    request(apiRoutinePath + "/current", (responseText) => onSuccess(JSON.parse(responseText)), "POST");
}

export function getSetCount(onSuccess) {
    request(apiRoutinePath + "/set-count", (responseText) => onSuccess(parseInt(responseText)), "GET");
}

export function stopRoutine(onSuccess) {
    request(apiRoutinePath + "/stop", onSuccess, "POST");
}

export function searchExerciseNames(filter, onSuccess) {
    request(apiExercisePath + "/search", (responseText) => onSuccess(JSON.parse(responseText)), "POST", filter);
}

export function searchGroupNames(filter, onSuccess) {
    request(apiGroupPath + "/search", (responseText) => onSuccess(JSON.parse(responseText)), "POST", filter);
}

export function searchRoutineNames(filter, onSuccess) {
    request(apiRoutinePath + "/search", (responseText) => onSuccess(JSON.parse(responseText)), "POST", filter);
}

export function saveExercise(exercise, onSuccess) {
    request(apiExercisePath + "/save", onSuccess, "POST", exercise);
}

export function saveGroup(group, onSuccess) {
    request(apiGroupPath + "/save", onSuccess, "POST", group);
}

export function saveRoutine(routine, onSuccess) {
    request(apiRoutinePath + "/save", onSuccess, "POST", routine);
}

export function deleteExercise(id, onSuccess) {
    request(apiExercisePath + "/delete/" + id, onSuccess, "DELETE");
}

export function deleteGroup(id, onSuccess) {
    request(apiGroupPath + "/delete/" + id, onSuccess, "DELETE");
}

export function deleteRoutine(id, onSuccess) {
    request(apiRoutinePath + "/delete/" + id, onSuccess, "DELETE");
}

function request(url, onSuccess, method, body) {
    const xhr = new XMLHttpRequest();
    xhr.open(method, window.location.origin + url);
    xhr.onload = () => onSuccess(xhr.responseText);
    if (body) {
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhr.send(JSON.stringify(body));
    } else {
        xhr.send();
    }
}