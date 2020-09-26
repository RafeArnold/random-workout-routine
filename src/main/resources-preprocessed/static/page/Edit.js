import React from "react";
import {
    editExercisePath,
    editGroupPath,
    editRoutinePath,
    getExerciseNames,
    getGroupNames,
    getRoutineNames,
    searchExerciseNames,
    searchGroupNames,
    searchRoutineNames
} from "../util/apiUtils";
import {Redirect} from "react-router-dom";

const exerciseTypeName = "exercise";
const groupTypeName = "group";
const routineTypeName = "routine";

class Edit extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            exercises: null,
            groups: null,
            routines: null,
            exerciseSearchInputValue: "",
            groupSearchInputValue: "",
            routineSearchInputValue: "",
            redirectTo: null
        };
        this.mapItemsToCol = this.mapItemsToCol.bind(this);
        this.handleSearchInputChange = this.handleSearchInputChange.bind(this);
        this.getUrl = this.getUrl.bind(this);
        this.addItem = this.addItem.bind(this);
        this.redirect = this.redirect.bind(this);
    }

    componentDidMount() {
        getExerciseNames((exercises) => this.setState({exercises: exercises}));
        getGroupNames((groups) => this.setState({groups: groups}));
        getRoutineNames((routines) => this.setState({routines: routines}));
    }

    mapItemsToCol(items, itemTypeName, itemDisplayName) {
        return (
            <div className="col">
                <h4 className="text-center">{itemDisplayName}</h4>
                <div>
                    <input className="form-control my-3" id={itemTypeName + "-search"}
                           placeholder="Search"
                           value={this.state[itemTypeName + "SearchInputValue"]}
                           onChange={(event) => this.handleSearchInputChange(event, itemTypeName)}/>
                </div>
                {items ? <ul className="list-group">
                    {<li className="list-group-item list-group-item-success list-group-item-action"
                         onClick={() => this.addItem(itemTypeName)} style={{cursor: "pointer"}}>
                        <i className="oi oi-plus"/></li>}
                    {items.map(item =>
                        <li key={item.id}
                            className={"list-group-item list-group-item-action"}
                            style={{cursor: "pointer"}}
                            onClick={() => this.redirect(this.getUrl(itemTypeName, item.id))}>{item.name}</li>)}
                </ul> : null}
            </div>
        );
    }

    handleSearchInputChange(event, type) {
        const searchTerm = event.target.value
        switch (type) {
            case exerciseTypeName:
                this.setState({exerciseSearchInputValue: searchTerm})
                searchExerciseNames({searchTerm: searchTerm}, (exercises) => this.setState({exercises: exercises}))
                break;
            case groupTypeName:
                this.setState({groupSearchInputValue: searchTerm})
                searchGroupNames({searchTerm: searchTerm}, (groups) => this.setState({groups: groups}))
                break;
            case routineTypeName:
                this.setState({routineSearchInputValue: searchTerm})
                searchRoutineNames({searchTerm: searchTerm}, (routines) => this.setState({routines: routines}))
        }
    }

    getUrl(type, id) {
        let url;
        switch (type) {
            case exerciseTypeName:
                url = editExercisePath;
                break;
            case groupTypeName:
                url = editGroupPath;
                break;
            case routineTypeName:
                url = editRoutinePath;
        }
        url += "/" + id;
        return url;
    }

    addItem(itemTypeName) {
        switch (itemTypeName) {
            case exerciseTypeName:
                this.redirect(editExercisePath);
                break;
            case groupTypeName:
                this.redirect(editGroupPath);
                break;
            case routineTypeName:
                this.redirect(editRoutinePath);
        }
    }

    redirect(to) {
        this.setState({redirectTo: to});
    }

    render() {
        const redirectTo = this.state.redirectTo;
        if (redirectTo) {
            return <Redirect to={redirectTo}/>;
        }
        return (
            <>
                <h1>Select an Item to Edit</h1>
                <div className="row my-4">
                    {this.mapItemsToCol(this.state.exercises, exerciseTypeName, "Exercises")}
                    {this.mapItemsToCol(this.state.groups, groupTypeName, "Groups")}
                    {this.mapItemsToCol(this.state.routines, routineTypeName, "Routines")}
                </div>
            </>
        );
    }
}

export default Edit;