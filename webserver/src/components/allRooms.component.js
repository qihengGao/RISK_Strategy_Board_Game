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
            snackbarType: "success",
            openCreateRoomDialog: false,
            roomSize: null
        };
        //this.initComponent = this.initComponent.bind(this);
    }

    handleClick(roomID) {
        axios
            .post(API_URL + "joinRoom", {

                roomID
            }, {headers: authHeader()})
            .then(() => {
                    //
                    let tmpmessage = this.state.messages;
                    tmpmessage.find(o => o.roomID === roomID).isOK = true;
                    tmpmessage.find(o => o.roomID === roomID).message = "Successfully Joined this room!"
                    this.setState({messages: tmpmessage});
                }, error => {
                    let tmpmessage = this.state.messages;
                    tmpmessage.find(o => o.roomID === roomID).isOK = false;
                    tmpmessage.find(o => o.roomID === roomID).message = error.response.data.message
                    this.setState({messages: tmpmessage});
                }
            );
    }


    render() {
        console.log(this.state.rooms)
        let columns: GridColDef[] = [{
            field: 'id', headerName: 'Room ID', width: 90
        }, {
            field: 'roomSize', headerName: 'Room Size', width: 150, editable: false,
        }, {
            field: 'action',
            headerName: 'Join',
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
                    axios
                        .post(API_URL + "/api/game/joinRoom", {
                            roomID: thisRow.id
                        }, {
                            headers: authHeader()
                        })
                        .then(() => {
                                this.handleSnackBarUpdate("success", "Successfully join room!")

                            }, error => {
                                this.handleSnackBarUpdate("error", "Failed join room\n" + error.messages)
                            }
                        );
                };
                return <Button onClick={onClick} href={"play/" + params.id}>Join</Button>;
            },
        },
        ];

        return (

            <div style={{
                height: 400
                ,
                width: '100%'
            }
            }><Stack
                sx={{width: '100%', mb: 1}}
                direction="row"
                alignItems="flex-start"
                columnGap={1}

            >

                <Button
                    //variant="contained"
                    size="small" onClick={this.handleCreateRoomButton}>Add new room</Button>
            </Stack>
                <DataGrid

                    rows={this.state.rows}
                    columns={columns}
                    // pageSize={5}
                    // rowsPerPageOptions={[5]}
                    // experimentalFeatures={{newEditingApi: true}}
                    onCellEditCommit={this.handleCellCommit}
                />
                <Snackbar open={this.state.openSnackBar} autoHideDuration={6000}
                          onClose={this.handleSnackBarClose}>
                    <Alert onClose={this.handleSnackBarClose} severity={this.state.snackbarType} sx={{width: '100%'}}>
                        {this.state.snackBarMessage}
                    </Alert>
                </Snackbar>


                <Dialog open={this.state.openCreateRoomDialog} onClose={this.handleCreateRoomClose}>
                    <DialogTitle>Create Room</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            To create a new game room, please room size here.
                        </DialogContentText>
                        <TextField
                            autoFocus
                            margin="dense"
                            id="roomSize"
                            label="Room Size"
                            type="number"
                            fullWidth
                            variant="standard"
                            onChange={this.handleRoomSizeChange}
                        />
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleCreateRoomClose}>Cancel</Button>
                        <Button onClick={this.handleCreateRoomSubmit}>Create</Button>
                    </DialogActions>
                </Dialog>
            </div>)
            ;
    }

    handleCreateRoomSubmit = () => {
        this.setState({
            openCreateRoomDialog: false
        })
        axios
            .post(API_URL + "/api/game/createRoom", {
                roomSize: this.state.roomSize,
            }, {headers: authHeader()})
            .then((response) => {
                //console.log(response);
                this.handleSnackBarUpdate("success", "Successfully create the room!\nroomID: " + response.data.roomIDCreated)
                //this.setState({messages: tmpmessage});
                this.props.history.push("/play/"+response.data.roomIDCreated)
                window.location.reload();
            }, error => {
                //window.location.reload();
                this.handleSnackBarUpdate("error", error.message)
                this.setState({messages: error});
            });

    }


    handleCreateRoomButton = ()=>{
        this.setState({
            openCreateRoomDialog: true
        })
    }
    handleCreateRoomClose = () => {
        this.setState({
            openCreateRoomDialog: false
        })
    }

    handleCreateRoomSubmit = () => {
        this.setState({
            openCreateRoomDialog: false
        })
        axios
            .post(API_URL + "/api/game/createRoom", {
                roomSize: this.state.roomSize,
            }, {headers: authHeader()})
            .then((response) => {
                //console.log(response);
                this.handleSnackBarUpdate("success", "Successfully create the room!\nroomID: " + response.data.roomIDCreated)
                //this.setState({messages: tmpmessage});
                this.props.history.push("/play/"+response.data.roomIDCreated)
                window.location.reload();
            }, error => {
                //window.location.reload();
                this.handleSnackBarUpdate("error", error.message)
                this.setState({messages: error});
            });

    }

    handleRoomSizeChange = (e) => {

        this.setState({
            roomSize: e.target.value
        })
    }

    handleSnackBarClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        this.setState({openSnackBar: false})
    };

    handleSnackBarUpdate = (type, message) => {
        this.setState(
            {
                openSnackBar: true,
                snackBarMessage: message,
                snackbarType: type
            }
        )
    }


    componentDidMount() {
        this.getAllRooms();

    }

    getAllRooms=()=>{
        axios
            .get(API_URL + "/api/game/rooms/available", {
                params: {},
                headers: authHeader()
            })
            .then(response => {

                let tmpRows = [];
                for (const room of response.data.rooms) {
                    tmpRows.push({
                        id: room.roomID,
                        roomSize: room.players.length + "/" + room.roomSize
                    })
                }


                this.setState({

                    rows: tmpRows

                })

            });
    }

    componentDidUpdate() {

    }


}