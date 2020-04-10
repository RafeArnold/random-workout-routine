var routine = [
    [
        {name: 'sit ups', repCountLowerBound: 8, repCountUpperBound: 25},
        {name: 'press ups', repCountLowerBound: 5, repCountUpperBound: 20},
        {name: 'tricep dips', repCountLowerBound: 10, repCountUpperBound: 25}
    ],
    [
        {name: 'rope climbs', repCountLowerBound: 1, repCountUpperBound: 5},
        {name: 'monkey bars', repCountLowerBound: 1, repCountUpperBound: 4},
        {name: 'bicep curls', repCountLowerBound: 8, repCountUpperBound: 26}
    ]
];

var currentGroupIndex = 0;
var setCount = 0;
var currentExercise;

document.onreadystatechange = function () {
    if (document.readyState === 'complete') {
        loadListeners();
        if (storageAvailable()) {
            loadStorage();
            displayExercise();
        } else {
            displayStartBtn();
        }
    }
};

var loadListeners = function () {
    document.onkeypress = function (event) {
        if (event.code === 'Enter') {
            event.preventDefault();
            next();
        }
    };
    document.getElementById('startBtn').onclick = next;
    document.getElementById('nextBtn').onclick = next;
    document.getElementById('resetBtn').onclick = reset;
};

var next = function () {
    setCount++;
    currentExercise = nextExercise();
    incrementCurrentGroupIndex();
    saveStorage();
    displayExercise();
};

var nextExercise = function () {
    var currentGroup = routine[currentGroupIndex];
    var exerciseOption = getRandomExerciseOption(currentGroup);
    return getExerciseFromOption(exerciseOption);
};

var getRandomExerciseOption = function (group) {
    var exerciseIndex = Math.floor(Math.random() * group.length);
    return group[exerciseIndex];
};

var getExerciseFromOption = function (option) {
    var max = option.repCountUpperBound;
    var min = option.repCountLowerBound;
    var repCount = Math.floor(Math.random() * (max - min)) + min;
    return {name: option.name, repCount: repCount};
};

var incrementCurrentGroupIndex = function () {
    currentGroupIndex++;
    currentGroupIndex %= routine.length;
};

var displayExercise = function () {
    document.getElementById('startBtn').style.display = 'none';
    document.getElementById('exerciseDisplay').style.display = null;
    document.getElementById('exerciseName').value = currentExercise.name;
    document.getElementById('repCount').value = currentExercise.repCount;
    document.getElementById('setCount').innerText = setCount;
};

var displayStartBtn = function () {
    document.getElementById('exerciseDisplay').style.display = 'none';
    document.getElementById('startBtn').style.display = null;
};

var reset = function () {
    currentGroupIndex = 0;
    setCount = 0;
    localStorage.clear();
    displayStartBtn();
};

var storageGroupIndexName = 'groupIndex';
var storageSetCountName = 'setCount';
var storageExerciseName = 'exercise';

var storageAvailable = function () {
    return Boolean(localStorage.getItem(storageExerciseName));
};

var loadStorage = function () {
    var storageGroupIndex = localStorage.getItem(storageGroupIndexName);
    if (storageGroupIndex) {
        currentGroupIndex = parseInt(storageGroupIndex);
    }
    var storageSetCount = localStorage.getItem(storageSetCountName);
    if (storageSetCount) {
        setCount = parseInt(storageSetCount);
    }
    var storageExercise = localStorage.getItem(storageExerciseName);
    if (storageExercise) {
        currentExercise = JSON.parse(storageExercise);
    }
};

var saveStorage = function () {
    localStorage.setItem(storageGroupIndexName, currentGroupIndex);
    localStorage.setItem(storageSetCountName, setCount);
    localStorage.setItem(storageExerciseName, JSON.stringify(currentExercise));
};
