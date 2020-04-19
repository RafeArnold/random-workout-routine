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
        getRoutineNames((names) => this.setState({options: this.mapNamesToOptions(names)}));
    }

    mapNamesToOptions(routineNames) {
        return routineNames.map((name) => (
            <option key={name} value={name}>{name}</option>
        ));
    }

    start() {
        const routineName = document.getElementById(selectId).value;
        if (routineName) {
            startRoutine(routineName, () => window.location.href = continueRoutinePath);
        }
    }

    render() {
        const options = this.state.options;
        const selectSize = options ? Math.min(options.length, 5) : 1;
        return (
            <>
                <h1 className="my-3">Select a Routine</h1>
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