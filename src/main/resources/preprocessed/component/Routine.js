import React from 'react';
import Exercise from './Exercise';
import getNextExercise from "../util/RoutineUtils";

class Routine extends React.Component {
    constructor(props) {
        super(props);
        this.state = {name: '', repCount: ''};
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
        this.setState({name: exercise.name, repCount: exercise.repCount});
    }

    render() {
        return (
            <>
                <Exercise name={this.state.name} repCount={this.state.repCount}/>
                <button className='btn btn-primary' onClick={this.nextExercise}>Next</button>
            </>
        );
    }
}

export default Routine;