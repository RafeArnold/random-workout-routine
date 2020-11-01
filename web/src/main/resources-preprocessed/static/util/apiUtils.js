export const contextPath = document.getElementById("contextPath").innerText;
export const apiExercisePath = contextPath + "api/exercise";
export const apiGroupPath = contextPath + "api/group";
export const apiRoutinePath = contextPath + "api/routine";
export const apiSessionPath = contextPath + "api/session";
export const editPath = contextPath + "edit";
export const editExercisePath = contextPath + "edit/exercise";
export const editGroupPath = contextPath + "edit/group";
export const editRoutinePath = contextPath + "edit/routine";
export const newSessionPath = contextPath + "new-session";
export const continueSessionPath = contextPath + "session";

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

export function sessionIsActive(onSuccess) {
    request(apiSessionPath + "/in-progress", (responseText) => onSuccess(responseText === "true"), "GET");
}

export function startSession(routineId, onSuccess) {
    request(apiSessionPath + "/start/" + routineId, onSuccess, "POST");
}

export function getNextExercise(onSuccess) {
    request(apiSessionPath + "/next", (responseText) => onSuccess(JSON.parse(responseText)), "POST");
}

export function getCurrentExercise(onSuccess) {
    request(apiSessionPath + "/current", (responseText) => onSuccess(JSON.parse(responseText)), "GET");
}

export function getSetCount(onSuccess) {
    request(apiSessionPath + "/set-count", (responseText) => onSuccess(parseInt(responseText)), "GET");
}

export function stopSession(onSuccess) {
    request(apiSessionPath + "/stop", onSuccess, "POST");
}

export function searchExerciseNames(searchTerm, excludedTerms, onSuccess) {
    request(apiExercisePath + "/search" + getSearchQueryString(searchTerm, excludedTerms),
        (responseText) => onSuccess(JSON.parse(responseText)), "GET");
}

export function searchGroupNames(searchTerm, excludedTerms, onSuccess) {
    request(apiGroupPath + "/search" + getSearchQueryString(searchTerm, excludedTerms),
        (responseText) => onSuccess(JSON.parse(responseText)), "GET");
}

export function searchRoutineNames(searchTerm, excludedTerms, onSuccess) {
    request(apiRoutinePath + "/search" + getSearchQueryString(searchTerm, excludedTerms),
        (responseText) => onSuccess(JSON.parse(responseText)), "GET");
}

export function getSearchQueryString(searchTerm, excludedTerms) {
    let queryParameters = new Map();
    if (searchTerm) queryParameters.set("term", searchTerm);
    if (excludedTerms) queryParameters.set("exclude", excludedTerms);
    return getQueryString(queryParameters);
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

function getQueryString(queryParameterMap) {
    if (queryParameterMap && queryParameterMap.size > 0) {
        return "?" + Array.from(queryParameterMap)
            .map(([key, value]) => key + "=" + value)
            .join("&");
    }
    return "";
}