import React from "react";
import {withRouter} from "react-router-dom";
import {editPath, getGroup, saveGroup, searchExerciseNames} from "../util/RoutineUtils";
import update from "immutability-helper";

class EditGroup extends React.Component {
    constructor(props) {
        super(props);
        this.state = {group: null, exerciseSearchResults: null, exerciseSearchInputValue: ""};
        this.setGroup = this.setGroup.bind(this);
        this.updateName = this.updateName.bind(this);
        this.addExercise = this.addExercise.bind(this);
        this.removeExercise = this.removeExercise.bind(this);
        this.handleSearchInputChange = this.handleSearchInputChange.bind(this);
        this.searchExercise = this.searchExercise.bind(this);
        this.setExerciseSearchResults = this.setExerciseSearchResults.bind(this);
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
    }

    componentDidMount() {
        const id = this.props.match.params.id;
        getGroup(id, this.setGroup);
    }

    componentDidUpdate(prevProps, prevState) {
        if (this.state.exerciseSearchInputValue !== prevState.exerciseSearchInputValue ||
            this.state.group?.exerciseOptions?.length !== prevState.group?.exerciseOptions?.length) {
            this.searchExercise(this.state.exerciseSearchInputValue);
        }
    }

    setGroup(group) {
        this.setState({group: group});
    }

    updateName(event) {
        const name = event.target.value;
        this.setState(state => {
            return {group: update(state.group, {name: {$set: name}})};
        });
    }

    addExercise(exerciseId) {
        this.setState(state => {
            if (state.group?.exerciseOptions?.filter(exercise => exercise.id === exerciseId).length === 0) {
                const exercise = state.exerciseSearchResults.filter(exercise => exercise.id === exerciseId);
                return {group: update(state.group, {exerciseOptions: {$push: exercise}})};
            }
        });
    }

    removeExercise(exerciseId) {
        this.setState(state => {
            const exercises = state.group?.exerciseOptions;
            const filteredExercise = exercises?.filter(exercise => exercise.id === exerciseId);
            if (filteredExercise.length !== 0) {
                const exerciseIndex = exercises?.indexOf(filteredExercise[0]);
                return {group: update(state.group, {exerciseOptions: {$splice: [[exerciseIndex, 1]]}})};
            }
        });
    }

    handleSearchInputChange(event) {
        const searchTerm = event.target.value;
        this.setState({exerciseSearchInputValue: searchTerm});
    }

    searchExercise(searchTerm) {
        const filter = {
            searchTerm: searchTerm,
            excludedTerms: this.state.group?.exerciseOptions?.map(exercise => exercise.name)
        };
        searchExerciseNames(filter, this.setExerciseSearchResults);
    }

    setExerciseSearchResults(exercises) {
        this.setState({exerciseSearchResults: exercises});
    }

    handleFormSubmit(event) {
        event.preventDefault();
        saveGroup(this.state.group, () => window.location.href = editPath);
    }

    render() {
        const group = this.state.group;
        const exerciseListItems = group?.exerciseOptions?.map(exercise =>
            <li key={exercise.id} className="list-group-item d-flex align-items-center justify-content-between">
                <strong>{exercise.name}</strong>
                <button type="button" onClick={() => this.removeExercise(exercise.id)} className="btn btn-danger">
                    <i className="oi oi-trash"/>
                </button>
            </li>);
        const exerciseSearchResults = this.state.exerciseSearchResults;
        const exerciseOptions = exerciseSearchResults?.map(exercise =>
            <li key={exercise.id} onClick={() => this.addExercise(exercise.id)}
                className="list-group-item list-group-item-action">{exercise.name}</li>);
        return (
            <>
                <h1>Edit Group {group?.name}</h1>
                {group ?
                    <form onSubmit={this.handleFormSubmit}>
                        <input name="id" value={group.id} readOnly hidden/>
                        <div className="form-group">
                            <label htmlFor="name">Name</label>
                            <input className="form-control" id="name" name="name" value={group.name}
                                   onChange={this.updateName}/>
                        </div>
                        <div className="row">
                            <div className="col-lg-7 form-group">
                                <label htmlFor="groups">Exercises</label>
                                <ul id="groups" className="list-group">
                                    {exerciseListItems}
                                </ul>
                            </div>
                            <div className="col-lg form-group">
                                <label htmlFor="group-search"><small>Search</small></label>
                                <input className="form-control" id="group-search"
                                       value={this.state.exerciseSearchInputValue}
                                       onChange={this.handleSearchInputChange}/>
                                <ul className="list-group mt-3">{exerciseOptions}</ul>
                            </div>
                        </div>
                        <div className="d-flex justify-content-between">
                            <button type="submit" className="btn btn-dark">Save</button>
                            <a href={editPath} type="button" className="btn btn-danger">Cancel</a>
                        </div>
                    </form>
                    : null}
            </>
        );
    }
}

export default withRouter(EditGroup);