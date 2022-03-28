import React, {Component} from "react";
import axios from "axios";
import authHeader from "../services/auth-header";
import Table from 'react-bootstrap/Table'

const API_URL = "http://localhost:8080/api/game/";


export default class allRoomsComponent extends Component {

    constructor(props) {
        super(props);
        //this.handleClick = this.handleClick.bind(this);

        this.state = {
            rooms: [],
            loading: true,
            messages: []
        };
        //this.initComponent = this.initComponent.bind(this);
    }

    handleClick(roomID) {
        axios
            .post(API_URL + "joinRoom", {

                roomID
            },{headers: authHeader()})
            .then(() => {
                //
                let tmpmessage = this.state.messages;
                tmpmessage.find(o => o.roomID === roomID).isOK=true;
                tmpmessage.find(o => o.roomID === roomID).message="Successfully Joined this room!"
                this.setState({messages: tmpmessage});
                }, error => {
                let tmpmessage = this.state.messages;
                tmpmessage.find(o => o.roomID === roomID).isOK=false;
                tmpmessage.find(o => o.roomID === roomID).message=error.response.data.message
                this.setState({messages: tmpmessage});
                }
            );
    }

    render() {
        console.log(this.state.rooms)
        if (this.state.loading) {
            return <div className="spinner">Loading.....</div>; // add a spinner or something until the posts are loaded
        }
        return (
            <div id="table">
                <Table responsive>
                    <thead>
                    <tr>


                        <th>Room ID</th>
                        <th>Room Size</th>
                        <th>Join</th>

                    </tr>
                    </thead>
                    <tbody>
                    {this.state.rooms.map((item, key) => (
                        <tr>
                            <td>{item.roomID}</td>
                            <td>{item.players.length}/{item.roomSize}</td>
                            <td>
                                <button className="btn btn-primary btn-block"
                                        onClick={()=>this.handleClick(item.roomID)}>Join
                                </button>
                                {this.state.messages.find(o => o.roomID === item.roomID).message && (
                                    <div className="form-group">
                                        <div
                                            className={this.state.messages.find(o => o.roomID === item.roomID).isOK ? ("alert alert-success") : ("alert alert-danger")}
                                            role="alert">
                                            {this.state.messages.find(o => o.roomID === item.roomID).message}
                                        </div>
                                    </div>
                                )}
                            </td>




                        </tr>
                    ))}

                    </tbody>
                </Table>
            </div>
        );
    }

    // initComponent() {
    //     axios
    //         .get(API_URL + "rooms/available", {
    //             params: {},
    //             headers: authHeader()
    //         })
    //         .then(response => {
    //             console.log(response.data);
    //             this.setState({rooms:response.data.rooms, loading: true });
    //         });
    //
    // }

    async componentDidMount() {
        let response = await axios
            .get(API_URL + "rooms/available", {
                params: {},
                headers: authHeader()
            })
            .then(response => {
                return response;
            });

        let tmpmessage = [];
        for (const room of response.data.rooms) {
            tmpmessage.push({
                roomID: room.roomID,
                isOK: true,
                message: ""
            })
        }
        this.setState({rooms: response.data.rooms, loading: false, messages: tmpmessage});
    }

    componentDidUpdate() {

    }


}