import React from "react";
import {getExerciseNames, getGroupNames, getRoutineNames} from "../util/RoutineUtils";

class Edit extends React.Component {
    constructor(props) {
        super(props);
        this.state = {exerciseNames: null, groupNames: null, routineNames: null, selected: null};
        this.mapNamesToRows = this.mapNamesToRows.bind(this);
        this.tableRow = this.tableRow.bind(this);
        this.setSelected = this.setSelected.bind(this);
        this.edit = this.edit.bind(this);
    }

    componentDidMount() {
        getExerciseNames((names) => this.setState({exerciseNames: names}));
        getGroupNames((names) => this.setState({groupNames: names}));
        getRoutineNames((names) => this.setState({routineNames: names}));
    }

    mapNamesToRows() {
        const exerciseNames = this.state.exerciseNames;
        const groupNames = this.state.groupNames;
        const routineNames = this.state.routineNames;
        if (exerciseNames && groupNames && routineNames) {
            const rowCount = Math.max(exerciseNames.length, groupNames.length, routineNames.length);
            const rows = [];
            for (let i = 0; i < rowCount; i++) {
                const exerciseName = i < exerciseNames.length ? exerciseNames[i] : null;
                const groupName = i < groupNames.length ? groupNames[i] : null;
                const routineName = i < routineNames.length ? routineNames[i] : null;
                rows.push(
                    <tr key={exerciseName + "|" + groupName + "|" + routineName}>
                        {this.tableRow(0, i, exerciseName)}
                        {this.tableRow(1, i, groupName)}
                        {this.tableRow(2, i, routineName)}
                    </tr>
                );
            }
            return rows;
        }
    }

    tableRow(x, y, name) {
        const selected = this.state.selected;
        return <td
            className={selected && selected.x === x && selected.y === y ? "bg-secondary text-light" : ""}
            onClick={() => this.setSelected(x, y)}>{name}</td>;
    }

    setSelected(x, y) {
        let newSelected = this.state.selected;
        if (newSelected && newSelected.x === x && newSelected.y === y) {
            newSelected = null;
        } else {
            let namesList;
            switch (x) {
                case 0:
                    namesList = this.state.exerciseNames;
                    break;
                case 1:
                    namesList = this.state.groupNames;
                    break;
                case 2:
                    namesList = this.state.routineNames;
            }
            if (namesList && namesList.length > y) {
                newSelected = {x: x, y: y};
            }
        }
        this.setState({selected: newSelected});
    }

    edit() {
        console.log("edit");
    }

    render() {
        return (
            <>
                <h1 className="my-3">Select an Item to Edit</h1>
                <table className="table table-cell-hover">
                    <thead className="thead-dark">
                        <tr>
                            <th>Exercise</th>
                            <th>Group</th>
                            <th>Routine</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.mapNamesToRows()}
                    </tbody>
                </table>
                <button className="btn btn-dark" disabled={!this.state.selected} onClick={this.edit}>Edit</button>
            </>
        );
    }
}

export default Edit;