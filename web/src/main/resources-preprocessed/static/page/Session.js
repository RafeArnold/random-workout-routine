import React from "react";
import {getCurrentExercise, getNextExercise, getSetCount, newSessionPath, stopSession} from "../util/apiUtils";
import {Redirect} from "react-router-dom";

class Session extends React.Component {
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
        if (this.props.sessionIsActive) {
            this.getCurrent();
        }
    }

    getCurrent() {
        getCurrentExercise(this.updateExercise);
    }

    next() {
        getNextExercise(this.updateExercise);
    }

    stop() {
        stopSession(() => this.props.setSessionIsActive(false));
    }

    updateExercise(exercise) {
        this.setState({exercise: exercise});
        this.getCount();
    }

    getCount() {
        getSetCount(this.updateSetCount);
    }

    updateSetCount(count) {
        this.setState({setCount: count});
    }

    render() {
        if (!this.props.sessionIsActive) {
            return <Redirect to={newSessionPath}/>;
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

export default Session;