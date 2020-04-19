import React from "react";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import Edit from "./Edit";
import Home from "./Home";
import NavBar from "./NavBar";
import Routine from "./Routine";
import RoutineSelect from "./RoutineSelect";
import {continueRoutinePath, editRoutinePath, newRoutinePath} from "../util/RoutineUtils";

class App extends React.Component {
    render() {
        return (
            <BrowserRouter>
                <NavBar/>
                <Switch>
                    <Route path={continueRoutinePath}>
                        <Routine/>
                    </Route>
                    <Route path={editRoutinePath}>
                        <Edit/>
                    </Route>
                    <Route path={newRoutinePath}>
                        <RoutineSelect/>
                    </Route>
                    <Route path="/">
                        <Home/>
                    </Route>
                </Switch>
            </BrowserRouter>
        );
    }
}

export default App;