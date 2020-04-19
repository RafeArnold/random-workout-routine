import React from "react";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import Edit from "./Edit";
import Home from "./Home";
import NavBar from "./NavBar";
import Routine from "./Routine";

class App extends React.Component {
    render() {
        return (
            <BrowserRouter>
                <NavBar/>
                <Switch>
                    <Route path="/edit">
                        <Edit/>
                    </Route>
                    <Route path="/routine">
                        <Routine/>
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