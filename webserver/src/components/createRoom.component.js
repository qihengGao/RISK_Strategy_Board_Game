import React, {Component} from "react";
import axios from "axios";
import authHeader from "../services/auth-header";
import Table from 'react-bootstrap/Table'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

const API_URL = "http://localhost:8080/api/game/";


export default class createRoomComponent extends Component {

    constructor(props) {
        super(props);
        //this.handleClick = this.handleClick.bind(this);

        this.state = {
            rooms: [],
            loading: false
        };
        //this.initComponent = this.initComponent.bind(this);
    }

    handleClick(roomSize) {
        axios
            .post("/api/game/createRoom", {
                roomSize
            },{headers: authHeader()})
    }

    render() {
        console.log(this.state.rooms)
        if (this.state.loading) {
            return <div className="spinner">Loading.....</div>; // add a spinner or something until the posts are loaded
        }
        return (
            <div className="col-md-12">
                <form id="loadconfigform">
                    Room Size: <input id="roomsize" name="roomsize"/>
                    <button
                        className="btn btn-primary btn-block"
                        onClick={()=>this.handleClick(document.getElementById('roomsize').value)}>
                        Create
                    </button>
                </form>
            </div>
        );
    }

}