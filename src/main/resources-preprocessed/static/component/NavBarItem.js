import React from "react";
import {NavLink} from "react-router-dom";

class NavBarItem extends React.Component {
    render() {
        return (
            <li className="nav-item">
                <NavLink to={this.props.to} className="nav-link" exact={this.props.exact}>{this.props.children}</NavLink>
            </li>
        );
    }
}

export default NavBarItem;