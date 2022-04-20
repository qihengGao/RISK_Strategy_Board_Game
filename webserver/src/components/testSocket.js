import * as React from "react";
import {Component} from "react";
import Container from "@mui/material/Container";
import SockJsClient from 'react-stomp';
import authHeader from "../services/auth-header";
import SockJS from "sockjs-client"
import * as Stomp from 'stompjs'
import AuthService from "../services/auth.service";


export default class testSocket extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: "",
            password: "",
            loading: false,
            message: "",
            openSnackBar: false,
            snackBarMessage: "",
            snackbarType: "error",
            socket_client : null,
            topic:"public",
            currentUser: AuthService.getCurrentUser(),
            socket_message:"demo"
        };
    }


    /**
     * handle the action of
     */
    handleConnectSocket = () => {

        let client = Stomp.over(new SockJS('http://localhost:8080/chat'));
        client.connect({}, function (frame) {
            //setConnected(true);
            client.subscribe('/topic/messages', function (message) {
                //showMessage(JSON.parse(message.body));
            });
            this.setState({
                socket_client:client
            });
        }.bind(this));
    }


    /**
     * handle the action of
     */
    handleSendMessage = () => {

        this.state.socket_client.send("/app/chat/" + this.state.topic, {}, JSON.stringify({
            from: this.state.currentUser.username,
            text: this.state.socket_message
        }));
    }


    /**
     * render the page (define the elements in the page)
     * @returns {JSX.Element}
     */
    render() {
        return (

            <Container component="main" maxWidth="xs">

                {/*<SockJsClient url='http://localhost:8080/chat' topics={['/topic/messages']} headers={authHeader()}*/}
                {/*              onMessage={(msg) => {*/}
                {/*                  console.log(msg);*/}
                {/*              }}*/}
                {/*              ref={(client) => {*/}
                {/*                  this.clientRef = client*/}
                {/*              }}*/}
                {/*              onConnected={(client) => {*/}
                {/*                  console.log("onConnected");*/}
                {/*                  // Subscribe to the Public Topic*/}
                {/*                  client.subscribe("/topic/public", this.onMessageReceived);*/}

                {/*                  // Tell your username to the server*/}
                {/*                  client.send(*/}
                {/*                      "/chat/addUser/1",*/}
                {/*                      {},*/}
                {/*                      JSON.stringify({sender: "Ali", type: "JOIN"})*/}
                {/*                  );*/}
                {/*              }}/>*/}
                <div id="main-content" className="container">
                    <div className="row">
                        <div className="col-md-12 space-bottom10">

                                <div className="form-group">
                                    <label htmlFor="from">Name?</label>
                                    <input type="text" id="from" className="form-control"
                                           placeholder="enter your name..."/>
                                </div>
                                <button

                                        onClick={() => this.handleConnectSocket()}>Connect
                                </button>
                                <button id="disconnect"
                                        className="btn btn-default"
                                        type="submit"
                                        disabled="disabled">Disconnect
                                </button>

                        </div>
                    </div>
                    <div className="row space-bottom10">

                            <div className="col-md-2">
                                <select name="topic"
                                        id="topic"
                                        className="form-control">
                                    <option>Lifestyle</option>
                                    <option>Travel</option>
                                    <option>Career</option>
                                </select>
                            </div>
                            <div className="col-md-6">
                                <input type="text"
                                       id="text"
                                       className="form-control"
                                       placeholder="enter message ..."/>
                            </div>
                            <div className="col-md-4">
                                <button onClick={() => this.handleSendMessage()}>Send
                                </button>
                            </div>

                    </div>
                    <div className="row">
                        <div className="col-md-12">
                            <table id="conversation" className="table table-striped">
                                <thead>
                                <tr>
                                    <th width="10%">From</th>
                                    <th width="15%">Topic</th>
                                    <th width="60%">Message</th>
                                    <th width="10%">Time</th>
                                </tr>
                                </thead>
                                <tbody id="messages">
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </Container>

        );
    }
}