import React from "react";
import {withRouter} from "react-router-dom";

class EditGroup extends React.Component {
    render() {
        const name = this.props.match.params.name;
        return (
            <h1>Edit Group {name}</h1>
        );
    }
}

export default withRouter(EditGroup);