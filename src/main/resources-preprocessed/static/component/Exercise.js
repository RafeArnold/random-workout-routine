import React from "react";

class Exercise extends React.Component {
    render() {
        return (
            <div>
                <p>{this.props.name}</p>
                <p>{this.props.repCount}</p>
            </div>
        )
    }
}

export default Exercise;