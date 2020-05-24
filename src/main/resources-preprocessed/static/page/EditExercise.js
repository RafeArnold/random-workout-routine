import React from "react";
import {Redirect, withRouter} from "react-router-dom";
import {deleteExercise, editPath, getExercise, saveExercise} from "../util/RoutineUtils";
import update from "immutability-helper";

class EditExercise extends React.Component {
    constructor(props) {
        super(props);
        this.state = {exercise: null, redirectToEdit: false, deleting: false};
        this.setExercise = this.setExercise.bind(this);
        this.updateName = this.updateName.bind(this);
        this.updateRepCountLowerBound = this.updateRepCountLowerBound.bind(this);
        this.updateRepCountUpperBound = this.updateRepCountUpperBound.bind(this);
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.delete = this.delete.bind(this);
        this.redirect = this.redirect.bind(this);
    }

    componentDidMount() {
        const id = this.props.match.params.id;
        if (id) {
            getExercise(id, this.setExercise);
        } else {
            this.setState({exercise: {name: "", repCountLowerBound: 0, repCountUpperBound: 0}});
        }
    }

    setExercise(exercise) {
        this.setState({exercise: exercise});
    }

    updateName(event) {
        const name = event.target.value;
        this.setState(state => {
            return {exercise: update(state.exercise, {name: {$set: name}})};
        });
    }

    updateRepCountLowerBound(event) {
        const repCountLowerBound = event.target.value;
        this.setState(state => {
            return {exercise: update(state.exercise, {repCountLowerBound: {$set: repCountLowerBound}})};
        });
    }

    updateRepCountUpperBound(event) {
        const repCountUpperBound = event.target.value;
        this.setState(state => {
            return {exercise: update(state.exercise, {repCountUpperBound: {$set: repCountUpperBound}})};
        });
    }

    handleFormSubmit(event) {
        event.preventDefault();
        saveExercise(this.state.exercise, this.redirect);
    }

    delete() {
        deleteExercise(this.state.exercise.id, this.redirect);
    }

    redirect() {
        this.setState({redirectToEdit: true});
    }

    render() {
        if (this.state.redirectToEdit) {
            return <Redirect to={editPath}/>;
        }
        const exercise = this.state.exercise;
        return (
            <>
                <div className="row justify-content-between align-items-center">
                    <div className="col">
                        <h1>Edit Exercise {exercise?.name}</h1>
                    </div>
                    {exercise?.id ?
                        <div className="col-auto">
                            {this.state.deleting ?
                                <>
                                    <span>Are you sure?</span>
                                    <button className="btn btn-danger mx-3" onClick={this.delete}>Yes</button>
                                    <button className="btn btn-dark"
                                            onClick={() => this.setState({deleting: false})}>No
                                    </button>
                                </> :
                                <button className="btn btn-danger"
                                        onClick={() => this.setState({deleting: true})}>Delete</button>
                            }
                        </div> : null}
                </div>
                {exercise ?
                    <form onSubmit={this.handleFormSubmit}>
                        <input name="id" value={exercise.id} readOnly hidden/>
                        <div className="form-group">
                            <label htmlFor="name">Name</label>
                            <input className="form-control" id="name" name="name" value={exercise.name}
                                   onChange={this.updateName}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="repCountLowerBound">Rep Count Lower Bound</label>
                            <input className="form-control" id="repCountLowerBound" name="repCountLowerBound"
                                   value={exercise.repCountLowerBound} onChange={this.updateRepCountLowerBound}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="repCountUpperBound">Rep Count Upper Bound</label>
                            <input className="form-control" id="repCountUpperBound" name="repCountUpperBound"
                                   value={exercise.repCountUpperBound} onChange={this.updateRepCountUpperBound}/>
                        </div>
                        <div className="d-flex justify-content-between">
                            <button type="submit" className="btn btn-dark">Save</button>
                            <button type="button" className="btn btn-outline-dark"
                                    onClick={this.redirect}>Cancel
                            </button>
                        </div>
                    </form>
                    : null}
            </>
        );
    }
}

export default withRouter(EditExercise);