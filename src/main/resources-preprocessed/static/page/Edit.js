import React from "react";
import {
    editExercisePath,
    editGroupPath,
    editRoutinePath,
    getExerciseNames,
    getGroupNames,
    getRoutineNames
} from "../util/apiUtils";
import {Redirect} from "react-router-dom";

const exerciseTypeName = "exercise";
const groupTypeName = "group";
const routineTypeName = "routine";

class Edit extends React.Component {
    constructor(props) {
        super(props);
        this.state = {exercises: null, groups: null, routines: null, selected: null, redirectTo: null};
        this.mapItemsToCol = this.mapItemsToCol.bind(this);
        this.setSelected = this.setSelected.bind(this);
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
        const selected = this.state.selected;
        return (
            <div className="col">
                <h4 className="text-center">{itemDisplayName}</h4>
                {items ? <ul className="list-group">
                    {items.map(item =>
                        <li key={item.id}
                            className={"list-group-item list-group-item-action" +
                            (selected && selected.id === item.id ? " bg-secondary text-light" : "")}
                            onClick={() => this.setSelected(itemTypeName, item.id)}>{item.name}</li>)}
                    {<li className="list-group-item list-group-item-success list-group-item-action"
                         onClick={() => this.addItem(itemTypeName)}>
                        <i className="oi oi-plus"/></li>}
                </ul> : null}
            </div>
        );
    }

    setSelected(type, id) {
        const selected = this.state.selected;
        let newSelected;
        if (selected && selected.id === id) {
            newSelected = null;
        } else {
            newSelected = {type: type, id: id};
        }
        this.setState({selected: newSelected});
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
        const selected = this.state.selected;
        return (
            <>
                <h1>Select an Item to Edit</h1>
                <div className="row my-4">
                    {this.mapItemsToCol(this.state.exercises, exerciseTypeName, "Exercises")}
                    {this.mapItemsToCol(this.state.groups, groupTypeName, "Groups")}
                    {this.mapItemsToCol(this.state.routines, routineTypeName, "Routines")}
                </div>
                {selected ? <button className="btn btn-dark"
                                    onClick={() => this.redirect(this.getUrl(selected.type, selected.id))}>Edit</button> : null}
            </>
        );
    }
}

export default Edit;