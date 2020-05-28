import React from "react";
import {getCurrentExercise, getNextExercise, getSetCount, newRoutinePath, stopRoutine} from "../util/apiUtils";
import {Redirect} from "react-router-dom";

class Routine extends React.Component {
    constructor(props) {
        super(props);
        this.state = {exercise: {name: "", repCount: ""}, setCount: 0};
        this.getCurrent = this.getCurrent.bind(this);
        this.next = this.next.bind(this);
        this.stop = this.stop.bind(this);
        this.updateExercise = this.updateExercise.bind(this);
        this.getCount = this.getCount.bind(this);
        this.updateSetCount = this.updateSetCount.bind(this);
    }

    componentDidMount() {
        if (this.props.routineIsActive) {
            this.getCurrent();
        }
    }

    getCurrent() {
        getCurrentExercise(this.updateExercise);
        this.getCount();
    }

    next() {
        getNextExercise((exercise) => {
            this.updateExercise(exercise);
            this.getCount();
        });
    }

    stop() {
        stopRoutine(() => this.props.setRoutineIsActive(false));
    }

    updateExercise(exercise) {
        this.setState({exercise: exercise});
    }

    getCount() {
        getSetCount(this.updateSetCount);
    }

    updateSetCount(count) {
        this.setState({setCount: count});
    }

    render() {
        if (!this.props.routineIsActive) {
            return <Redirect to={newRoutinePath}/>;
        }
        return (
            <>
                <h5>Current Exercise:</h5>
                <h1>{this.state.exercise.repCount + " " + this.state.exercise.name}</h1>
                <h5>Count: {this.state.setCount}</h5>
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