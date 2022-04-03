import React, {Component} from "react";
import axios from "axios";
import authHeader from "../services/auth-header";
import Table from 'react-bootstrap/Table'
import {DataGrid, GridApi, GridCellValue, GridColDef} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import Snackbar from "@mui/material/Snackbar";
import {Alert} from "@mui/material";

const API_URL = "http://localhost:8080";


export default class joinedRoomsComponent extends Component {

    constructor(props) {
        super(props);
        //this.handleClick = this.handleClick.bind(this);

        this.state = {
            rooms: [],
            loading: true,
            messages: [],
            rows: [],
            openSnackBar: false,
            snackBarMessage: "Successfully commit the message",
            snackbarType: "success"
        };
        //this.initComponent = this.initComponent.bind(this);
    }

    render() {
        console.log(this.state.rooms)
        let columns: GridColDef[] = [{
            field: 'id', headerName: 'Room ID', width: 90
        }, {
            field: 'roomSize', headerName: 'Room Size', width: 150, editable: false,
        }, {
            field: 'action',
            headerName: 'Return',
            sortable: false,
            renderCell: (params) => {
                const onClick = (e) => {
                    e.stopPropagation(); // don't select this row after clicking

                    const api: GridApi = params.api;
                    const thisRow: Record<string, GridCellValue> = {};

                    api
                        .getAllColumns()
                        .filter((c) => c.field !== '__check__' && !!c)
                        .forEach(
                            (c) => (thisRow[c.field] = params.getValue(params.id, c.field)),
                        );
                };

                return <Button href={"play/"+params.id} >Return</Button>;
            },
        },
        ];

        return (

            <div style={{
                height: 600,
                marginLeft: 50,
                marginRight: 50,
                marginTop: 40,
                marginBottom: 40,
            }
            }>
                <DataGrid

                    rows={this.state.rows}
                    columns={columns}
                    pageSize={9}
                    rowsPerPageOptions={[8]}
                    // experimentalFeatures={{newEditingApi: true}}
                    onCellEditCommit={this.handleCellCommit}
                />
                <Snackbar open={this.state.openSnackBar} autoHideDuration={6000}
                          onClose={this.handleSnackBarClose}>
                    <Alert onClose={this.handleSnackBarClose} severity={this.state.snackbarType} sx={{width: '100%'}}>
                        {this.state.snackBarMessage}
                    </Alert>
                </Snackbar>
            </div>)
            ;
    }
    handleSnackBarClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        this.setState({openSnackBar: false})
    };

    handleSnackBarUpdate = (type,message) => {
        this.setState(
            {
                openSnackBar:true,
                snackBarMessage: message,
                snackbarType: type
            }
        )
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

    componentDidMount() {
        axios
            .get("/api/game/rooms/joined", {
                params: {},
                headers: authHeader()
            })
            .then(response => {

                let tmpRows = [];
                for(const room of response.data.rooms){
                    tmpRows.push({
                        id:room.roomID,
                        roomSize:room.currentState
                    })
                }


                this.setState( {

                    rows: tmpRows

                })

            });

    }


}