import React from "react";
import {
    editExercisePath,
    editGroupPath,
    editRoutinePath,
    getExerciseNames,
    getGroupNames,
    getRoutineNames
} from "../util/RoutineUtils";

const exerciseTypeName = "exercise";
const groupTypeName = "group";
const routineTypeName = "routine";

class Edit extends React.Component {
    constructor(props) {
        super(props);
        this.state = {exercises: null, groups: null, routines: null, selected: null};
        this.mapItemsToRows = this.mapItemsToRows.bind(this);
        this.tableRow = this.tableRow.bind(this);
        this.setSelected = this.setSelected.bind(this);
        this.getUrl = this.getUrl.bind(this);
    }

    componentDidMount() {
        getExerciseNames((exercises) => this.setState({exercises: exercises}));
        getGroupNames((groups) => this.setState({groups: groups}));
        getRoutineNames((routines) => this.setState({routines: routines}));
    }

    mapItemsToRows() {
        const exercises = this.state.exercises;
        const groups = this.state.groups;
        const routines = this.state.routines;
        if (exercises && groups && routines) {
            const rowCount = Math.max(exercises.length, groups.length, routines.length);
            const rows = [];
            for (let i = 0; i < rowCount; i++) {
                const exercise = i < exercises.length ? exercises[i] : null;
                const group = i < groups.length ? groups[i] : null;
                const routine = i < routines.length ? routines[i] : null;
                rows.push(
                    <tr key={exercise?.id + "|" + group?.id + "|" + routine?.id}>
                        {this.tableRow(exerciseTypeName, exercise)}
                        {this.tableRow(groupTypeName, group)}
                        {this.tableRow(routineTypeName, routine)}
                    </tr>
                );
            }
            return rows;
        }
    }

    tableRow(type, item) {
        if (item) {
            const selected = this.state.selected;
            const id = item.id;
            const name = item.name;
            return <td
                className={selected && selected.id === id ? "bg-secondary text-light" : ""}
                onClick={() => this.setSelected(type, id)}>{name}</td>;
        }
    }

    setSelected(type, id) {
        const selected = this.state.selected;
        let newSelected;
        if (selected && selected.id === id) {
            newSelected = null;
        } else {
            newSelected = {type: type, id: id};
        }
        this.setState({selected: newSelected});
    }

    getUrl(type, id) {
        let url;
        switch (type) {
            case exerciseTypeName:
                url = editExercisePath;
                break;
            case groupTypeName:
                url = editGroupPath;
                break;
            case routineTypeName:
                url = editRoutinePath;
        }
        url += "/" + id;
        return url;
    }

    render() {
        const selected = this.state.selected;
        return (
            <>
                <h1>Select an Item to Edit</h1>
                <table className="table table-bordered table-cell-hover">
                    <thead className="thead-dark">
                        <tr>
                            <th>Exercise</th>
                            <th>Group</th>
                            <th>Routine</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.mapItemsToRows()}
                    </tbody>
                </table>
                {selected ? <a className="btn btn-dark" href={this.getUrl(selected.type, selected.id)}>Edit</a> : null}
            </>
        );
    }
}

export default Edit;