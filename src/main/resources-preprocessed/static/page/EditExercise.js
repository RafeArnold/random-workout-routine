import React from "react";
import {withRouter} from "react-router-dom";

class EditExercise extends React.Component {
    render() {
        const name = this.props.match.params.name;
        return (
            <h1>Edit Exercise {name}</h1>
        );
    }
}

export default withRouter(EditExercise);