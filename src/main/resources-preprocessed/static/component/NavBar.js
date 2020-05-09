import React from "react";
import {continueRoutinePath, editPath, newRoutinePath} from "../util/RoutineUtils";
import NavBarItem from "./NavBarItem";

class NavBar extends React.Component {
    render() {
        return (
            <nav className="navbar navbar-expand navbar-dark bg-dark mb-3">
                <div className="navbar-brand">RWR</div>
                <ul className="navbar-nav">
                    <NavBarItem to="/" exact>Home</NavBarItem>
                    <NavBarItem to={newRoutinePath}>Start a new Routine</NavBarItem>
                    <NavBarItem to={editPath}>Edit a Routine</NavBarItem>
                    {this.props.routineIsActive ?
                        <NavBarItem to={continueRoutinePath}>Continue your Routine</NavBarItem> : null}
                </ul>
            </nav>
        );
    }
}

export default NavBar;