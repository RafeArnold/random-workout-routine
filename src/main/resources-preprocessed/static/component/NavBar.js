import React from "react";
import {continueRoutinePath, editPath, newRoutinePath, routineIsActive} from "../util/RoutineUtils";
import NavBarItem from "./NavBarItem";

class NavBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {routineIsActive: false};
        this.setRoutineIsActive = this.setRoutineIsActive.bind(this);
    }

    componentDidMount() {
        this.setRoutineIsActive();
    }

    setRoutineIsActive() {
        routineIsActive((isActive) => this.setState({routineIsActive: isActive}));
    }

    render() {
        return (
            <nav className="navbar navbar-expand navbar-dark bg-dark">
                <div className="navbar-brand">RWR</div>
                <ul className="navbar-nav">
                    <NavBarItem to="/" exact>Home</NavBarItem>
                    <NavBarItem to={newRoutinePath}>Start a new Routine</NavBarItem>
                    <NavBarItem to={editPath}>Edit a Routine</NavBarItem>
                    {this.state.routineIsActive ?
                        <NavBarItem to={continueRoutinePath}>Continue your Routine</NavBarItem> : null}
                </ul>
            </nav>
        );
    }
}

export default NavBar;