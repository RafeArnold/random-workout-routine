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
        load();
        start();
    }
};

var load = function () {
    document.onkeypress = function(event) {
        if (event.code === 'Enter' || event.code === 'Space') {
            next();
        }
    };
    var nextBtn = document.getElementById('nextBtn');
    nextBtn.onclick = function () {
        next();
        nextBtn.blur();
    };
    var clearBtn = document.getElementById('clearBtn');
    clearBtn.onclick = function () {
        clear();
        clearBtn.blur();
    };
};

var start = function () {
    if (storageAvailable()) {
        loadStorage();
    } else {
        next();
    }
    display();
};

var next = function () {
    setCount++;
    currentExercise = nextExercise(currentGroupIndex);
    incrementCurrentGroupIndex();
    saveStorage();
    display();
};

var nextExercise = function (currentGroupIndex) {
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

var display = function () {
    displayExercise();
    displaySetCount();
};

var displayExercise = function () {
    document.getElementById('exerciseName').value = currentExercise.name;
    document.getElementById('repCount').value = currentExercise.repCount;
};

var displaySetCount = function () {
    document.getElementById('setCount').innerText = setCount;
};

var clear = function () {
    currentGroupIndex = 0;
    setCount = 0;
    localStorage.clear();
    next();
    display();
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
