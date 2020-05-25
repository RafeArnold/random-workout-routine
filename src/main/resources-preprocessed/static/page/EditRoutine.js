import React from "react";
import {withRouter} from "react-router-dom";
import EditParentItem from "./EditParentItem";
import {deleteRoutine, getRoutine, saveRoutine, searchGroupNames} from "../util/apiUtils";

class EditRoutine extends EditParentItem {
    constructor(props) {
        super(props);
        this.itemDisplayName = "Routine";
        this.getItem = getRoutine;
        this.saveItem = saveRoutine;
        this.deleteItem = deleteRoutine;
        this.childItemPropertyName = "groups";
        this.emptyItem = {name: "", [this.childItemPropertyName]: []};
        this.searchChildItemNames = searchGroupNames;
        this.childItemDisplayNamePlural = "Groups";
    }
}

export default withRouter(EditRoutine);