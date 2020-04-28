import React from "react";
import {continueRoutinePath, getRoutineNames, startRoutine} from "../util/RoutineUtils";

const selectId = "routineSelect";

class RoutineSelect extends React.Component {
    constructor(props) {
        super(props);
        this.state = {options: null};
        this.mapNamesToOptions = this.mapNamesToOptions.bind(this);
        this.start = this.start.bind(this);
    }

    componentDidMount() {
        getRoutineNames((routines) => this.setState({options: this.mapNamesToOptions(routines)}));
    }

    mapNamesToOptions(routines) {
        return routines.map((routine) => (
            <option key={routine.id} value={routine.id}>{routine.name}</option>
        ));
    }

    start() {
        const routineId = document.getElementById(selectId).value;
        if (routineId) {
            startRoutine(routineId, () => window.location.href = continueRoutinePath);
        }
    }

    render() {
        const options = this.state.options;
        const selectSize = options ? Math.min(options.length, 5) : 1;
        return (
            <>
                <h1>Select a Routine</h1>
                <div className="row">
                    <div className="col-8 col-md-6 col-lg-5 form-group">
                        <select id={selectId} className="form-control" size={selectSize}>{options}</select>
                    </div>
                </div>
                <button className="btn btn-dark" onClick={this.start}>Start</button>
            </>
        );
    }
}

export default RoutineSelect;