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
    continueRoutinePath,
    editExercisePath,
    editGroupPath,
    editPath,
    editRoutinePath,
    newRoutinePath
} from "../util/RoutineUtils";

class App extends React.Component {
    render() {
        return (
            <BrowserRouter>
                <NavBar/>
                <div className="container">
                    <Switch>
                        <Route path={continueRoutinePath}>
                            <Routine/>
                        </Route>
                        <Route path={editExercisePath + "/:name"}>
                            <EditExercise/>
                        </Route>
                        <Route path={editGroupPath + "/:name"}>
                            <EditGroup/>
                        </Route>
                        <Route path={editRoutinePath + "/:name"}>
                            <EditRoutine/>
                        </Route>
                        <Route path={editPath}>
                            <Edit/>
                        </Route>
                        <Route path={newRoutinePath}>
                            <RoutineSelect/>
                        </Route>
                        <Route path="/">
                            <Home/>
                        </Route>
                    </Switch>
                </div>
            </BrowserRouter>
        );
    }
}

export default App;