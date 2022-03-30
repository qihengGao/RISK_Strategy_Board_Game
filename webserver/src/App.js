import * as React from 'react';
import Navbar from "./components/Navbar.component";
import {Switch, Route, Link, withRouter} from "react-router-dom";
import Home from "./components/home.component";
import Profile from "./components/profile.component";
import MuiLogin from "./components/MuiLogin.component";
import MuiRegister from "./components/MUIRegister.component";
import allRooms from "./components/allRooms.component";
import joinedRooms from "./components/joinedRooms.component";
import play from "./components/play.component";
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
        console.log(this.props);
        console.log("123");
        return (
            <div>
            <Navbar {...this.props} />
        <div className="container mt-3">
            <Switch>
                <Route exact path={["/", "/home"]} component={Home}/>
                <Route exact path="/login" component={MuiLogin}/>
                <Route exact path="/register" component={MuiRegister}/>
                <Route exact path="/profile" component={Profile}/>
                <Route exact path="/allRooms" component={allRooms}/>
                <Route exact path="/joinedRooms" component={joinedRooms}/>
                <Route exact path="/play/:roomID" component={play}/>

            </Switch>
        </div>
            </div>
    )
        ;
    }
}
export default withRouter(App);