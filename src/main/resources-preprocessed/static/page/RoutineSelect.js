import React from "react";
import {continueRoutinePath, getRoutineNames, startRoutine} from "../util/RoutineUtils";

class RoutineSelect extends React.Component {
    constructor(props) {
        super(props);
        this.state = {routines: null, selectedRoutineId: ""};
        this.start = this.start.bind(this);
    }

    componentDidMount() {
        getRoutineNames((routines) => this.setState({
            routines: routines,
            selectedRoutineId: routines?.length > 0 ? routines[0].id : ""
        }));
    }

    handleSelectChange(event) {
        const routineId = event.target.value;
        this.setState({selectedRoutineId: routineId});
    }

    start() {
        const routineId = this.state.selectedRoutineId;
        if (routineId) {
            startRoutine(routineId, () => window.location.href = continueRoutinePath);
        }
    }

    render() {
        const options = this.state.routines?.map((routine) => (
            <option key={routine.id} value={routine.id}>{routine.name}</option>
        ));
        const selectSize = options ? Math.min(options.length, 5) : 1;
        return (
            <>
                <h1>Select a Routine</h1>
                <div className="row">
                    <div className="col-8 col-md-6 col-lg-5 form-group">
                        <select className="form-control" size={selectSize} value={this.state.selectedRoutineId}
                                onChange={this.handleSelectChange}>{options}</select>
                    </div>
                </div>
                <button className="btn btn-dark" onClick={this.start}>Start</button>
            </>
        );
    }
}

export default RoutineSelect;