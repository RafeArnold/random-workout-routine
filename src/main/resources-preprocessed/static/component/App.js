import React from "react";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import Edit from "../page/Edit";
import EditExercise from "../page/EditExercise";
import EditGroup from "../page/EditGroup";
import EditRoutine from "../page/EditRoutine";
import Home from "../page/Home";
import NavBar from "./NavBar";
import Routine from "../page/Routine";
import RoutineSelect from "../page/RoutineSelect";
import {
    contextPath,
    continueRoutinePath,
    editExercisePath,
    editGroupPath,
    editPath,
    editRoutinePath,
    newRoutinePath,
    routineIsActive
} from "../util/RoutineUtils";

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {routineIsActive: false};
        this.setRoutineIsActive = this.setRoutineIsActive.bind(this);
    }

    componentDidMount() {
        routineIsActive((isActive) => this.setRoutineIsActive(isActive));
    }

    setRoutineIsActive(isActive) {
        this.setState({routineIsActive: isActive});
    }

    render() {
        return (
            <BrowserRouter>
                <NavBar routineIsActive={this.state.routineIsActive}/>
                <div className="container">
                    <Switch>
                        <Route path={continueRoutinePath}>
                            <Routine routineIsActive={this.state.routineIsActive}
                                     setRoutineIsActive={this.setRoutineIsActive}/>
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
                        <Route path={newRoutinePath}>
                            <RoutineSelect routineIsActive={this.state.routineIsActive}
                                           setRoutineIsActive={this.setRoutineIsActive}/>
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