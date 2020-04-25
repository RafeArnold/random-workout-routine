import React from "react";
import {withRouter} from "react-router-dom";

class EditRoutine extends React.Component {
    render() {
        const name = this.props.match.params.name;
        return (
            <h1>Edit Routine {name}</h1>
        );
    }
}

export default withRouter(EditRoutine);