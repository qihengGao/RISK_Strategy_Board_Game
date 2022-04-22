import * as React from 'react';
import Navbar from "./components/Navbar.component";
import {Route, Switch, withRouter} from "react-router-dom";
import Home from "./components/home.component";
import Profile from "./components/profile.component";
import MuiLogin from "./components/MuiLogin.component";
import MuiRegister from "./components/MUIRegister.component";
import normalRooms from "./components/allNormalRooms.component";
import competitiveRooms from "./components/allCompetitiveRooms.component"
import joinedRooms from "./components/joinedRooms.component";
import historyRooms from "./components/allHistoryRooms.component";
import play from "./components/play.component";
import record from "./components/history_game.component";
import testSocket from "./components/testSocket";

class App extends React.Component {
    constructor(props) {
        // Required step: always call the parent class' constructor
        super(props);

        // Set the state directly. Use props if necessary.
        this.state = {
            loggedIn: false,
            currentState: "not-panic",

            // Note: think carefully before initializing
            // state based on props!
            someInitialValue: this.props.initialValue
        }
    }

    render() {
        // whatever you like
        return (
            <div>

                <Navbar {...this.props} />
                <div className="container mt-3">
                    <Switch>
                        <Route exact path={["/", "/home"]} component={Home}/>
                        <Route exact path="/login" component={MuiLogin}/>
                        <Route exact path="/register" component={MuiRegister}/>
                        <Route exact path="/profile" component={Profile}/>
                        <Route exact path="/rooms/normal/all" component={normalRooms}/>
                        <Route exact path="/rooms/competitive/all" component={competitiveRooms}/>
                        <Route exact path="/rooms/joined/all" component={joinedRooms}/>
                        <Route exact path="/rooms/record/all" component={historyRooms}/>
                        <Route exact path="/play/:roomID" component={play}/>
                        <Route exact path="/record/:roomID" component={record}/>
                        <Route exact path="/testSocket" component={testSocket}/>

                    </Switch>
                </div>
            </div>
        )
            ;
    }
}

export default withRouter(App);