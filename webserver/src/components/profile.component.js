import React, { Component } from "react";
import AuthService from "../services/auth.service";

/**
 * controls the profile to show to the user
 */
export default class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: AuthService.getCurrentUser()
        };
    }

    /**
     * render the elements in this page
     * @returns {JSX.Element}
     */
    render() {
        const { currentUser } = this.state;
        return (
            <div style={{
                height: 550,
                marginLeft: 50,
                marginRight: 50,
                marginTop: 40,
                marginBottom: 40,
            }}
                 className="container">
                <header className="jumbotron">
                    <h2>
                        <strong>{currentUser.username}</strong> Profile
                    </h2>
                </header>
                <p>
                    <strong>Token:</strong>{" "}
                    {currentUser.accessToken}
                </p>
                <p>
                    <strong>Elo:</strong>{" "}
                    {currentUser.elo}
                </p>
                <p>
                    <strong>Id:</strong>{" "}
                    {currentUser.id}
                </p>
                <p>
                    <strong>Email:</strong>{" "}
                    {currentUser.email}
                </p>
                <strong>Authorities:</strong>
                <ul>
                    {currentUser.roles &&
                        currentUser.roles.map((role, index) => <li key={index}>{role}</li>)}
                </ul>
            </div>
        );
    }
}