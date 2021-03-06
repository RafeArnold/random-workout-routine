import React from "react";
import {contextPath, continueSessionPath, editPath, newSessionPath} from "../util/apiUtils";
import NavBarItem from "./NavBarItem";

class NavBar extends React.Component {
    render() {
        return (
            <nav className="navbar navbar-expand navbar-dark bg-dark mb-3">
                <div className="navbar-brand">RWR</div>
                <ul className="navbar-nav">
                    <NavBarItem to={contextPath} exact>Home</NavBarItem>
                    {!this.props.sessionIsActive ?
                        <NavBarItem to={newSessionPath}>Start</NavBarItem> :
                        <NavBarItem to={continueSessionPath}>Continue</NavBarItem>}
                    <NavBarItem to={editPath}>Edit</NavBarItem>
                </ul>
            </nav>
        );
    }
}

export default NavBar;