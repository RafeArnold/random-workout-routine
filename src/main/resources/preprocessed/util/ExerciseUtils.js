const getNextExercise = (onSuccess) => {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/exercise/next');
    xhr.onload = () => onSuccess(JSON.parse(xhr.responseText));
    xhr.send();
};

export default getNextExercise;