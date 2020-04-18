import React from "react";
import {NavLink} from "react-router-dom";

class NavBar extends React.Component {
    render() {
        return (
            <nav>
                <ul>
                    <li>
                        <NavLink to="/">Home</NavLink>
                    </li>
                    <li>
                        <NavLink to="/routine">Start a Routine</NavLink>
                    </li>
                    <li>
                        <NavLink to="/edit">Edit a Routine</NavLink>
                    </li>
                </ul>
            </nav>
        );
    }
}

export default NavBar;