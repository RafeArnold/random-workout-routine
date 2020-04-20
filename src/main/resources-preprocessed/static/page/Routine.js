import React from "react";
import {getCurrentExercise, getNextExercise} from "../util/RoutineUtils";

class Routine extends React.Component {
    constructor(props) {
        super(props);
        this.state = {exercise: {name: "", repCount: ""}};
        this.getCurrent = this.getCurrent.bind(this);
        this.next = this.next.bind(this);
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

    updateExercise(exercise) {
        this.setState({exercise: exercise});
    }

    render() {
        return (
            <>
                <h5 className="my-3">Current Exercise:</h5>
                <h1>{this.state.exercise.repCount + " " + this.state.exercise.name}</h1>
                <button className="mt-3 btn btn-dark" onClick={this.next}>Next</button>
            </>
        );
    }
}

export default Routine;