import React from "react";
import {withRouter} from "react-router-dom";
import EditParentItem from "./EditParentItem";
import {deleteGroup, getGroup, saveGroup, searchExerciseNames} from "../util/RoutineUtils";

class EditGroup extends EditParentItem {
    constructor(props) {
        super(props);
        this.itemDisplayName = "Group";
        this.getItem = getGroup;
        this.saveItem = saveGroup;
        this.deleteItem = deleteGroup;
        this.childItemPropertyName = "exerciseOptions";
        this.emptyItem = {name: "", [this.childItemPropertyName]: []};
        this.searchChildItemNames = searchExerciseNames;
        this.childItemDisplayNamePlural = "Exercises";
    }
}

export default withRouter(EditGroup);