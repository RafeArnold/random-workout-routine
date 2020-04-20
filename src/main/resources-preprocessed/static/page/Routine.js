import React from "react";
import Exercise from "../component/Exercise";
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
                <Exercise name={this.state.exercise.name} repCount={this.state.exercise.repCount}/>
                <button className="btn btn-primary" onClick={this.next}>Next</button>
            </>
        );
    }
}

export default Routine;