import React from "react";
import {NavLink} from "react-router-dom";
import {editRoutinePath, newRoutinePath} from "../util/RoutineUtils";

class NavBar extends React.Component {
    render() {
        return (
            <nav className="navbar navbar-expand navbar-dark bg-dark">
                <div className="navbar-brand">RWR</div>
                <ul className="navbar-nav">
                    <li className="nav-item">
                        <NavLink to="/" className="nav-link" exact>Home</NavLink>
                    </li>
                    <li className="nav-item">
                        <NavLink to={newRoutinePath} className="nav-link">Start a new Routine</NavLink>
                    </li>
                    <li className="nav-item">
                        <NavLink to={editRoutinePath} className="nav-link">Edit a Routine</NavLink>
                    </li>
                </ul>
            </nav>
        );
    }
}

export default NavBar;