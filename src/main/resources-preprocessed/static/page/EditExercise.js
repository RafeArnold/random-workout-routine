import React from "react";
import {withRouter} from "react-router-dom";
import EditItem from "./EditItem";
import {deleteExercise, getExercise, saveExercise} from "../util/RoutineUtils";
import update from "immutability-helper";

class EditExercise extends EditItem {
    constructor(props) {
        super(props);
        this.itemDisplayName = "Exercise";
        this.getItem = getExercise;
        this.saveItem = saveExercise;
        this.deleteItem = deleteExercise;
        this.emptyItem = {name: "", repCountLowerBound: 0, repCountUpperBound: 0};
        this.updateRepCountLowerBound = this.updateRepCountLowerBound.bind(this);
        this.updateRepCountUpperBound = this.updateRepCountUpperBound.bind(this);
    }

    updateRepCountLowerBound(event) {
        const repCountLowerBound = event.target.value;
        this.setState(state => {
            return {item: update(state.item, {repCountLowerBound: {$set: repCountLowerBound}})};
        });
    }

    updateRepCountUpperBound(event) {
        const repCountUpperBound = event.target.value;
        this.setState(state => {
            return {item: update(state.item, {repCountUpperBound: {$set: repCountUpperBound}})};
        });
    }

    extraInputs() {
        const item = this.state.item;
        return (
            <>
                <div className="form-group">
                    <label htmlFor="repCountLowerBound">Rep Count Lower Bound</label>
                    <input className="form-control" id="repCountLowerBound" name="repCountLowerBound"
                           value={item.repCountLowerBound} onChange={this.updateRepCountLowerBound}/>
                </div>
                <div className="form-group">
                    <label htmlFor="repCountUpperBound">Rep Count Upper Bound</label>
                    <input className="form-control" id="repCountUpperBound" name="repCountUpperBound"
                           value={item.repCountUpperBound} onChange={this.updateRepCountUpperBound}/>
                </div>
            </>
        );
    }
}

export default withRouter(EditExercise);