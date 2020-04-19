import React from "react";
import Exercise from "./Exercise";
import {getNextExercise} from "../util/RoutineUtils";

class Routine extends React.Component {
    constructor(props) {
        super(props);
        this.state = {exercise: {name: "", repCount: ""}};
        this.nextExercise = this.nextExercise.bind(this);
        this.updateExercise = this.updateExercise.bind(this);
    }

    componentDidMount() {
        this.nextExercise();
    }

    nextExercise() {
        getNextExercise(this.updateExercise);
    }

    updateExercise(exercise) {
        this.setState({exercise: exercise});
    }

    render() {
        return (
            <>
                <Exercise name={this.state.exercise.name} repCount={this.state.exercise.repCount}/>
                <button className="btn btn-primary" onClick={this.nextExercise}>Next</button>
            </>
        );
    }
}

export default Routine;