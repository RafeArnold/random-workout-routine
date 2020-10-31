import React from "react";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import Edit from "../page/Edit";
import EditExercise from "../page/EditExercise";
import EditGroup from "../page/EditGroup";
import EditRoutine from "../page/EditRoutine";
import Home from "../page/Home";
import NavBar from "./NavBar";
import Session from "../page/Session";
import RoutineSelect from "../page/RoutineSelect";
import {
    contextPath,
    continueSessionPath,
    editExercisePath,
    editGroupPath,
    editPath,
    editRoutinePath,
    newSessionPath,
    sessionIsActive
} from "../util/apiUtils";

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {sessionIsActive: false};
        this.setSessionIsActive = this.setSessionIsActive.bind(this);
    }

    componentDidMount() {
        sessionIsActive((isActive) => this.setSessionIsActive(isActive));
    }

    setSessionIsActive(isActive) {
        this.setState({sessionIsActive: isActive});
    }

    render() {
        return (
            <BrowserRouter>
                <NavBar sessionIsActive={this.state.sessionIsActive}/>
                <div className="container">
                    <Switch>
                        <Route path={continueSessionPath}>
                            <Session sessionIsActive={this.state.sessionIsActive}
                                     setSessionIsActive={this.setSessionIsActive}/>
                        </Route>
                        <Route path={[editExercisePath + "/:id", editExercisePath]}>
                            <EditExercise/>
                        </Route>
                        <Route path={[editGroupPath + "/:id", editGroupPath]}>
                            <EditGroup/>
                        </Route>
                        <Route path={[editRoutinePath + "/:id", editRoutinePath]}>
                            <EditRoutine/>
                        </Route>
                        <Route path={editPath}>
                            <Edit/>
                        </Route>
                        <Route path={newSessionPath}>
                            <RoutineSelect sessionIsActive={this.state.sessionIsActive}
                                           setSessionIsActive={this.setSessionIsActive}/>
                        </Route>
                        <Route path={contextPath}>
                            <Home/>
                        </Route>
                    </Switch>
                </div>
            </BrowserRouter>
        );
    }
}

export default App;