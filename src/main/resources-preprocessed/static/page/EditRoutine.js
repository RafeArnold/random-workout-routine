import React from "react";
import {Redirect, withRouter} from "react-router-dom";
import {deleteRoutine, editPath, getRoutine, saveRoutine, searchGroupNames} from "../util/RoutineUtils";
import update from "immutability-helper";

class EditRoutine extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            routine: null,
            groupSearchResults: null,
            groupSearchInputValue: "",
            redirectToEdit: false,
            deleting: false
        };
        this.setRoutine = this.setRoutine.bind(this);
        this.updateName = this.updateName.bind(this);
        this.addGroup = this.addGroup.bind(this);
        this.removeGroup = this.removeGroup.bind(this);
        this.handleSearchInputChange = this.handleSearchInputChange.bind(this);
        this.searchGroups = this.searchGroups.bind(this);
        this.setGroupSearchResults = this.setGroupSearchResults.bind(this);
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.delete = this.delete.bind(this);
        this.redirect = this.redirect.bind(this);
    }

    componentDidMount() {
        const id = this.props.match.params.id;
        if (id) {
            getRoutine(id, this.setRoutine);
        } else {
            this.setState({routine: {name: "", groups: []}});
        }
    }

    componentDidUpdate(prevProps, prevState) {
        if (this.state.groupSearchInputValue !== prevState.groupSearchInputValue ||
            this.state.routine?.groups?.length !== prevState.routine?.groups?.length) {
            this.searchGroups(this.state.groupSearchInputValue);
        }
    }

    setRoutine(routine) {
        this.setState({routine: routine});
    }

    updateName(event) {
        const name = event.target.value;
        this.setState(state => {
            return {routine: update(state.routine, {name: {$set: name}})};
        });
    }

    addGroup(groupId) {
        this.setState(state => {
            if (state.routine?.groups?.filter(group => group.id === groupId).length === 0) {
                const group = state.groupSearchResults.filter(group => group.id === groupId);
                return {routine: update(state.routine, {groups: {$push: group}})};
            }
        });
    }

    removeGroup(groupId) {
        this.setState(state => {
            const groups = state.routine?.groups;
            const filteredGroups = groups?.filter(group => group.id === groupId);
            if (filteredGroups.length !== 0) {
                const groupIndex = groups?.indexOf(filteredGroups[0]);
                return {routine: update(state.routine, {groups: {$splice: [[groupIndex, 1]]}})};
            }
        });
    }

    handleSearchInputChange(event) {
        const searchTerm = event.target.value;
        this.setState({groupSearchInputValue: searchTerm});
    }

    searchGroups(searchTerm) {
        const filter = {
            searchTerm: searchTerm,
            excludedTerms: this.state.routine?.groups?.map(group => group.name)
        };
        searchGroupNames(filter, this.setGroupSearchResults);
    }

    setGroupSearchResults(groups) {
        this.setState({groupSearchResults: groups});
    }

    handleFormSubmit(event) {
        event.preventDefault();
        saveRoutine(this.state.routine, this.redirect);
    }

    delete() {
        deleteRoutine(this.state.routine.id, this.redirect);
    }

    redirect() {
        this.setState({redirectToEdit: true});
    }

    render() {
        if (this.state.redirectToEdit) {
            return <Redirect to={editPath}/>;
        }
        const routine = this.state.routine;
        const groupListItems = routine?.groups?.map(group =>
            <li key={group.id} className="list-group-item d-flex align-items-center justify-content-between">
                <strong>{group.name}</strong>
                <button type="button" onClick={() => this.removeGroup(group.id)} className="btn btn-danger">
                    <i className="oi oi-trash"/>
                </button>
            </li>);
        const groupSearchResults = this.state.groupSearchResults;
        const groupOptions = groupSearchResults?.map(group =>
            <li key={group.id} onClick={() => this.addGroup(group.id)}
                className="list-group-item list-group-item-action">{group.name}</li>);
        return (
            <>
                <div className="row justify-content-between align-items-center">
                    <div className="col">
                        <h1>Edit Routine {routine?.name}</h1>
                    </div>
                    {routine?.id ?
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
                {routine ?
                    <form onSubmit={this.handleFormSubmit}>
                        <input name="id" value={routine.id} readOnly hidden/>
                        <div className="form-group">
                            <label htmlFor="name">Name</label>
                            <input className="form-control" id="name" name="name" value={routine.name}
                                   onChange={this.updateName}/>
                        </div>
                        <div className="row">
                            <div className="col-lg-7 form-group">
                                <label htmlFor="groups">Groups</label>
                                <ul id="groups" className="list-group">
                                    {groupListItems}
                                </ul>
                            </div>
                            <div className="col-lg form-group">
                                <label htmlFor="group-search"><small>Search</small></label>
                                <input className="form-control" id="group-search"
                                       value={this.state.groupSearchInputValue}
                                       onChange={this.handleSearchInputChange}/>
                                <ul className="list-group mt-3">{groupOptions}</ul>
                            </div>
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

export default withRouter(EditRoutine);