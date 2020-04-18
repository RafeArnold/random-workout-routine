import React from "react";
import Edit from "./Edit";
import Routine from "./Routine";
import {BrowserRouter, Route, Switch} from "react-router-dom";

class App extends React.Component {
    render() {
        return (
            <BrowserRouter>
                <Switch>
                    <Route path='/edit'>
                        <Edit/>
                    </Route>
                    <Route path='/routine'>
                        <Routine/>
                    </Route>
                </Switch>
            </BrowserRouter>
        );
    }
}

export default App;