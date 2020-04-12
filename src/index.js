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
    }
};

var load = function () {
    loadListeners();
    if (storageAvailable()) {
        loadStorage();
        displayExercise();
    } else {
        resetForm();
        displayForm();
    }
};

var loadListeners = function () {
    document.onkeypress = function (event) {
        if (event.code === 'Enter') {
            event.preventDefault();
            next();
        }
    };
    document.getElementById('startBtn').onclick = start;
    document.getElementById('nextBtn').onclick = next;
    document.getElementById('resetBtn').onclick = reset;
    document.getElementById('addGroup').onclick = addGroup;
    document.getElementById('resetFormBtn').onclick = reset;
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
    document.getElementById(formId).style.display = 'none';
    document.getElementById('exerciseDisplay').style.display = null;
    document.getElementById('exerciseName').value = currentExercise.name;
    document.getElementById('repCount').value = currentExercise.repCount;
    document.getElementById('setCount').innerText = setCount;
};

var displayForm = function () {
    document.getElementById('exerciseDisplay').style.display = 'none';
    document.getElementById(formId).style.display = null;
};

var reset = function () {
    currentGroupIndex = 0;
    setCount = 0;
    localStorage.clear();
    resetForm();
    displayForm();
};

var storageRoutineName = 'routine';
var storageGroupIndexName = 'groupIndex';
var storageSetCountName = 'setCount';
var storageExerciseName = 'exercise';

var storageAvailable = function () {
    return Boolean(localStorage.getItem(storageExerciseName));
};

var loadStorage = function () {
    var storageRoutine = localStorage.getItem(storageRoutineName);
    if (storageRoutine) {
        routine = JSON.parse(storageRoutine);
    }
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
    localStorage.setItem(storageRoutineName, JSON.stringify(routine));
    localStorage.setItem(storageGroupIndexName, currentGroupIndex);
    localStorage.setItem(storageSetCountName, setCount);
    localStorage.setItem(storageExerciseName, JSON.stringify(currentExercise));
};

var formId = 'optionForm';
var groupInputClassName = 'groupInput';
var optionInputClassName = 'exerciseOptionInput';
var deleteGroupClassName = 'deleteGroup';
var deleteOptionClassName = 'deleteOption';
var addOptionClassName = 'addOption';
var optionsAfterClassName = 'optionsAfter';
var exerciseNameInputClassName = 'exerciseNameInput';
var repCountLowerInputClassName = 'repCountLowerBound';
var repCountUpperInputClassName = 'repCountUpperBound';

var start = function () {
    routine = parseForm();
    saveStorage();
    next();
};

var parseForm = function () {
    var routine = [];
    var groups = getGroups();
    for (var group of groups) {
        routine.push(parseGroup(group));
    }
    return routine;
};

var parseGroup = function (groupNode) {
    var group = [];
    var options = getOptions(groupNode);
    for (var option of options) {
        group.push(parseOption(option));
    }
    return group;
};

var parseOption = function (optionNode) {
    var name = optionNode.querySelector('input.' + exerciseNameInputClassName).value;
    var repCountLowerBound = parseInt(optionNode.querySelector('input.' + repCountLowerInputClassName).value);
    var repCountUpperBound = parseInt(optionNode.querySelector('input.' + repCountUpperInputClassName).value);
    return {name: name, repCountLowerBound: repCountLowerBound, repCountUpperBound: repCountUpperBound};
};

var resetForm = function () {
    var groups = getGroups();
    for (var i = groups.length - 1; i >= 0; i--) {
        groups[i].remove();
    }
    addGroup();
};

var addGroup = function () {
    var groups = getGroups();
    var bottomGroup = groups[groups.length - 1];
    var formNode = document.getElementById(formId);
    if (bottomGroup) {
        formNode.insertBefore(getGroupNode(), bottomGroup.nextSibling);
    } else {
        formNode.prepend(getGroupNode());
    }
};

var addOption = function (groupNode) {
    var options = getOptions(groupNode);
    var bottomOption = options[options.length - 1];
    if (bottomOption) {
        groupNode.insertBefore(getOptionNode(), bottomOption.nextSibling);
    } else {
        groupNode.insertBefore(getOptionNode(), groupNode.querySelector('.' + optionsAfterClassName).nextSibling);
    }
};

var getOptions = function (groupNode) {
    return groupNode.querySelectorAll('div.' + optionInputClassName);
};

var getGroups = function () {
    return document.querySelectorAll('#' + formId + ' div.' + groupInputClassName);
};

var getGroupNode = function () {
    var div = getNode(groupInputClassName, groupHtml, deleteGroupClassName);
    div.append(getOptionNode());
    var addOptionBtn = div.querySelector('button.' + addOptionClassName);
    addOptionBtn.onclick = function () {
        addOption(div);
    };
    return div;
};

var getOptionNode = function () {
    return getNode(optionInputClassName, exerciseOptionHtml, deleteOptionClassName);
};

var getNode = function (inputClassName, html, deleteClassName) {
    var div = document.createElement('div');
    div.className = inputClassName;
    div.innerHTML = html;
    var deleteBtn = div.querySelector('button.' + deleteClassName);
    deleteBtn.onclick = function () {
        div.remove();
    };
    return div;
};

var exerciseOptionHtml =
    '<p><label>exercise: <input class="' + exerciseNameInputClassName + '" type="text"/></label> ' +
    '<label>lower bound: <input class="' + repCountLowerInputClassName + '" type="number" min="1"/></label> ' +
    '<label>upper bound: <input class="' + repCountUpperInputClassName + '" type="number" min="1"/></label> ' +
    '<button type="button" class="' + deleteOptionClassName + '">delete</button></p>';

var groupHtml =
    '<h1 class="' + optionsAfterClassName + '">group</h1>' +
    '<button type="button" class="' + deleteGroupClassName + '">delete group</button> ' +
    '<button type="button" class="' + addOptionClassName + '">add exercise</button>';
