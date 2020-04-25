import React from "react";
import {getCurrentExercise, getNextExercise, stopRoutine} from "../util/RoutineUtils";

class Routine extends React.Component {
    constructor(props) {
        super(props);
        this.state = {exercise: {name: "", repCount: ""}};
        this.getCurrent = this.getCurrent.bind(this);
        this.next = this.next.bind(this);
        this.stop = this.stop.bind(this);
        this.updateExercise = this.updateExercise.bind(this);
    }

    componentDidMount() {
        this.getCurrent();
    }

    getCurrent() {
        getCurrentExercise(this.updateExercise);
    }

    next() {
        getNextExercise(this.updateExercise);
    }

    stop() {
        stopRoutine(() => window.location.href = "/");
    }

    updateExercise(exercise) {
        this.setState({exercise: exercise});
    }

    render() {
        return (
            <>
                <h5 className="my-3">Current Exercise:</h5>
                <h1>{this.state.exercise.repCount + " " + this.state.exercise.name}</h1>
                <div className="row mt-4">
                    <div className="col-auto">
                        <button className="btn btn-dark" onClick={this.next}>Next</button>
                    </div>
                    <div className="col">
                        <button className="btn btn-danger" onClick={this.stop}>Stop</button>
                    </div>
                </div>
            </>
        );
    }
}

export default Routine;