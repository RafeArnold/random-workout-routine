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

document.onreadystatechange = function () {
    if (document.readyState === 'complete') {
        start();
    }
};

var start = function () {
    next();
    document.onkeypress = next;
    var nextBtn = document.getElementById('nextBtn');
    nextBtn.onclick = function () {
        next();
        nextBtn.blur();
    }
};

var next = function () {
    displaySetCount(setCount++);
    displayExercise(nextExercise(currentGroupIndex));
    incrementCurrentGroupIndex();
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

var displayExercise = function (exercise) {
    document.getElementById('exerciseName').value = exercise.name;
    document.getElementById('repCount').value = exercise.repCount;
};

var incrementCurrentGroupIndex = function () {
    currentGroupIndex++;
    currentGroupIndex %= routine.length;
};

var displaySetCount = function (setCount) {
    document.getElementById('setCount').innerText = setCount;
};
