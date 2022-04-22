import * as React from "react";
import {Component} from "react";
import Container from "@mui/material/Container";
import authHeader from "../services/auth-header";
import SockJS from "sockjs-client"
import * as Stomp from 'stompjs'
import AuthService from "../services/auth.service";
import {MessageLeft, MessageRight} from "./Message";
import {Alert, CircularProgress, Paper, Snackbar} from "@mui/material";
import Grid from "@mui/material/Grid";
import TextField from "@material-ui/core/TextField";
import Button from "@mui/material/Button";
import SendIcon from '@mui/icons-material/Send';

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
            socket_client: null,
            topic: "public",
            currentUser: AuthService.getCurrentUser(),
            socket_message: "",
            socket_isConnect: false,
            snackbar_loading_open: true,
            snackbar_severity: "info",
            snackbar_message: "Connecting chat server",
            snackbar_success_open: false,
            socket_history_messages: []
        };
    }


    /**
     * handle the action of
     */
    componentDidMount() {

        let client = Stomp.over(new SockJS("http://"+window.location.hostname+":8080/chat", null, {
            transports: ['xhr-streaming'],
            headers: {'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9...'}
        }));
        client.connect(authHeader(), function (frame) {

            console.log("connect Stomp")
            client.subscribe('/topic/' + this.props.roomID + '/user/' + AuthService.getCurrentUser().id, function (message) {
                console.log(JSON.parse(message.body))
                this.setState(prevState => ({

                    socket_history_messages: [...prevState.socket_history_messages, JSON.parse(message.body)]

                }))
                console.log(this.state.socket_history_messages)
                React.createElement(
                    "MessageLeft", {message: message.body.message, timestamp: "123631"}
                )
            }.bind(this));
            this.setState({
                socket_client: client,
                socket_isConnect: true,
                snackbar_success_open: true,
                snackbar_loading_open: false

            });
        }.bind(this));
    }


    /**
     * handle the action of
     */
    handleSendMessage = () => {

        this.state.socket_client.send("/app/chat/" + this.props.roomID, {}, JSON.stringify({
            from: this.state.currentUser.username,
            text: this.state.socket_message
        }));
        this.setState({
            socket_message: ''
        })
    }


    handleCloseLoadingSnackbar = () => {

        this.setState({

            snackbar_loading_open: false
        })
    }
    handleSocketMessageChange = (e) => {

        this.setState({

            socket_message: e.target.value
        })
    }

    handleCloseSuccessSnackbar = () => {

        this.setState({
            snackbar_success_open: false

        })
    }

    /**
     * render the page (define the elements in the page)
     * @returns {JSX.Element}
     */
    render() {

        return (

            <Container maxWidth="xs">


                <div>
                    <Paper>

                        {
                            this.state.socket_history_messages.map((item) => (
                                    item.from === this.state.currentUser.id ?
                                        <MessageRight message={item.message} timestamp={item.timestamp}/>
                                        :
                                        <MessageLeft color={this.props.responseData.idToColor[item.from].colorName?this.props.responseData.idToColor[item.from].colorName:"#000000"} message={item.message} timestamp={item.timestamp}
                                                     displayName={this.props.responseData.idToColor[item.from].colorName + ' Player'}
                                                     timestamp={item.timestamp}/>
                                )
                            )
                        }


                    </Paper>
                    <form noValidate autoComplete="off">
                        <TextField
                            label="Message"
                            value={this.state.socket_message}
                            onChange={this.handleSocketMessageChange}
                        />
                        <Button variant="contained" onClick={this.handleSendMessage}>
                            <SendIcon/>
                        </Button>
                    </form>

                </div>


                <Snackbar open={this.state.snackbar_loading_open} autoHideDuration={3000}
                          onClose={this.handleCloseLoadingSnackbar}>
                    <Alert onClose={this.handleCloseLoadingSnackbar} severity="info" sx={{width: '100%'}}>
                        <Grid container direction="row" alignItems="center">
                            <Grid item>
                                <div>Connecting to chat server</div>
                            </Grid>
                            <Grid item>
                                <CircularProgress/>
                            </Grid>
                        </Grid>
                    </Alert>

                </Snackbar>
                <Snackbar open={this.state.snackbar_success_open} autoHideDuration={2000}
                          onClose={this.handleCloseSuccessSnackbar}>
                    <Alert onClose={this.handleCloseSuccessSnackbar} severity="success" sx={{width: '100%'}}>
                        <Grid container direction="row" alignItems="center">
                            <Grid item>
                                <div>Successfully connect to chat server</div>
                            </Grid>
                        </Grid>
                    </Alert>

                </Snackbar>
            </Container>

        );
    }
}