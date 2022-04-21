import React, {Component} from "react";
import axios from "axios";
import authHeader from "../services/auth-header";
import {DataGrid, GridApi, GridCellValue, GridColDef} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import Snackbar from "@mui/material/Snackbar";
import {
    Alert,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Stack,
    TextField
} from "@mui/material";

const API_URL = "http://localhost:8080";


export default class allRoomsComponent extends Component {

    /**
     * ctor for joined rooms
     * @param props
     */
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

    /**
     * render the information in this page+
     * @returns {JSX.Element}
     */
    render() {
        console.log("Rows: ")
        console.log(this.state.rows);
        let columns: GridColDef[] = [{
            field: 'id', headerName: '', width: 90
        },{
            field: 'roomID', headerName: 'Room ID', width: 90
        }, {
            field: 'roundNumber', headerName: 'Round Number', width: 150, editable: false,
        }, {
            field: 'action',
            headerName: '',
            sortable: false,
            /**
             * render the cells in the list of rooms in joined rooms
             * @param params
             * @returns {JSX.Element}
             */
            renderCell: (params) => {
                // const onClick = (e) => {
                //     e.stopPropagation(); // don't select this row after clicking
                //
                //     const api: GridApi = params.api;
                //     const thisRow: Record<string, GridCellValue> = {};
                //
                //     api
                //         .getAllColumns()
                //         .filter((c) => c.field !== '__check__' && !!c)
                //         .forEach(
                //             (c) => (thisRow[c.field] = params.getValue(params.id, c.field)),
                //         );
                // };
                console.log("params:")
                console.log(params)

                return <Button href={"record/"+params.row.roomID + "/"+params.row.roundNumber} >View</Button>;
            },
        },
        ];

        /**
         * define all the elements in the page
         */
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
            </div>)
            ;
    }

    /**
     * get data
     */
    componentDidMount() {
        axios
            .get("/api/historyGame/rooms/available", {
                params: {},
                headers: authHeader()
            })
            .then(response => {
                console.log(response.data.rooms);
                let tmpRows = [];
                var idCounter = 0;
                for(const room of response.data.rooms){
                    tmpRows.push({
                        id:idCounter++,
                        roomID:room.roomID,
                        roundNumber:room.roundNumber
                    })
                }

                console.log("tmpRows")
                console.log(tmpRows);
                this.setState( {

                    rows: tmpRows

                })
                console.log(this.state.rows);

            });

    }


}