import React, {Component} from "react";
import {DataGrid, GridColDef} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import AuthService from "../services/auth.service";
import axios from "axios";
import authHeader from "../services/auth-header";

const API_URL = "http://localhost:8080";



export default class unitPlace extends Component {
    constructor(props) {
        super(props);

        this.handleCommit = this.handleCommit.bind(this);
        this.state = {
            loading: true,
            message: "",
            roomID: this.props.roomID,
            rows: [], //this.initComponent = this.initComponent.bind(this);
        };
    }

    componentDidMount() {
        let idCounter = 0;
        let tmpRows = [];
        for (const territory of this.props.room.riskMap.continent) {
            if (territory.ownerID === AuthService.getCurrentUser().id) tmpRows.push({
                id: idCounter++, territoryName: territory.name, unitAmount: 0
            })

        }
        this.setState({rows: tmpRows});


    }

    handleCellCommit = (id) => {


        let newRow = {
            id: id.id, territoryName: id.row.territoryName, unitAmount: id.value
        }
        console.log(newRow)
        this.setState(prevState => ({

            rows: prevState.rows.map(el => el.id === id.id ? {...el, unitAmount: id.value} : el)

        }))

    }

    render() {
        console.log(this.props.room)
        let columns: GridColDef[] = [{field: 'id', headerName: 'ID', width: 90}, {
            field: 'territoryName', headerName: 'Territory name', width: 150, editable: false,
        }, {
            field: 'unitAmount', headerName: 'Unit amount', width: 150, editable: true, type: 'number',
        },];

        return (

            <div style={{height: 400, width: '100%'}}>
                <DataGrid

                    rows={this.state.rows}
                    columns={columns}
                    // pageSize={5}
                    // rowsPerPageOptions={[5]}
                    // experimentalFeatures={{newEditingApi: true}}
                    onCellEditCommit={this.handleCellCommit}
                />
                <Button variant="contained" onClick={this.handleCommit}>Commit</Button>
            </div>);
    }

    initComponent() {


    }

    handleCommit(e) {
        console.log(this.state.rows)
        let unitPlaceOrders = {};
        for (const row of this.state.rows) {
            unitPlaceOrders[row.territoryName] = row.unitAmount;
        }
        console.log(unitPlaceOrders);
        axios
            .post( API_URL + "/api/game/place/unit", {
                roomID: this.state.roomID, unitPlaceOrders
            }, {headers: authHeader()})
            .then((response) => {
                console.log(response);
                //this.setState({messages: tmpmessage});
                this.props.handleSnackBarUpdate("success","Successfully commit the orders!")
            }, error => {
                this.props.handleSnackBarUpdate("error",error.message)
                this.setState({messages: error});
            });

    }


    componentDidUpdate() {
        this.initComponent();
    }


}