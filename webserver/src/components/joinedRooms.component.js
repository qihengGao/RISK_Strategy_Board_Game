import React, {Component} from "react";
import axios from "axios";
import authHeader from "../services/auth-header";
import Table from 'react-bootstrap/Table'

const API_URL = "http://localhost:8080/api/game/";


export default class joinedRoomsComponent extends Component {

    constructor(props) {
        super(props);
        //this.handleClick = this.handleClick.bind(this);

        this.state = {
            rooms: [],
            loading: true
        };
        //this.initComponent = this.initComponent.bind(this);
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
                        <th>Game State</th>
                        <th>Return</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.rooms.map((item, key) => (
                        <tr>
                            <td>{item.roomID}</td>
                            <td>{item.currentState}</td>
                            <td>
                                <button className="btn btn-primary btn-block"
                                        onClick={()=>this.handleClick(item.roomID)}>Return
                                </button>
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
            .get(API_URL + "rooms/joined", {
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