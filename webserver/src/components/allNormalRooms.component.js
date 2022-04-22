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


export default class allNormalRooms extends Component {

    /**
     * constructor to build this component
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
            snackbarType: "success",
            openCreateRoomDialog: false,
            roomSize: null,
            roomCompetitive: false
        };
        //this.initComponent = this.initComponent.bind(this);
    }

    /**
     * render the page of all rooms
     * @returns {JSX.Element}
     */
    render() {
        console.log(this.state.rooms)
        let columns: GridColDef[] = [{
            field: 'id', headerName: 'Room ID', width: 150
        }, {
            field: 'roomSize', headerName: 'Room Size', width: 150, editable: false,
        }, {
            field: 'roomState', headerName: 'Room Status', width: 150, editable: false,
        }, {
            field: 'roomAvgElo', headerName: 'Average Rank', width: 150, editable: false,
        }, {
            field: 'action',
            headerName: '',
            sortable: false,
            /**
             * render each cell in the list of rooms
             * @param params
             * @returns {JSX.Element}
             */
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
                        .post("/api/game/joinRoom", {
                            roomID: thisRow.id
                        }, {
                            headers: authHeader()
                        });
                        // .then(() => {
                        //         this.handleSnackBarUpdate("success", "Successfully join room!")
                        //
                        //     }, error => {
                        //         this.handleSnackBarUpdate("error", "Failed join room\n" + error.messages)
                        //     }
                        // );
                };
                return <Button onClick={onClick} href={"play/" + params.id}>Join</Button>;
            },
        },
        ];

        /**
         * define the elements in the page
         */
        return (

            <div>
                <div style={{
                    height: 550,
                    marginLeft: 50,
                    marginRight: 50,
                    marginTop: 40,
                    marginBottom: 40,
                }
                }><Stack
                    sx={{width: '100%', mb: 1}}
                    direction="row"
                    alignItems="flex-start"
                    columnGap={1}

                >

                    <Button
                        variant="contained"
                        size="small" onClick={this.handleCreateRoomButton}>Add new room</Button>
                </Stack>
                    <DataGrid

                        rows={this.state.rows}
                        columns={columns}
                        pageSize={10}
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
                                To create a new game room, specify room size here.
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
                            <Button variant="contained" onClick={this.handleCreateRoomSubmit}>Create</Button>
                            <Button onClick={this.handleCreateRoomClose}>Cancel</Button>
                        </DialogActions>
                    </Dialog>
                </div>
            </div>
        )
    }

    /**
     * handle the action of clicking create room
     */
    handleCreateRoomButton = ()=>{
        this.setState({
            openCreateRoomDialog: true
        })
    }
    /**
     * handle the action of clicking close create room window
     */
    handleCreateRoomClose = () => {
        this.setState({
            openCreateRoomDialog: false
        })
    }

    /**
     * handle the action when submit a create room request
     */
    handleCreateRoomSubmit = () => {
        this.setState({
            openCreateRoomDialog: false
        })
        axios
            .post("/api/game/createRoom", {
                roomSize: this.state.roomSize,
                competitive: this.state.roomCompetitive
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

    /**
     * handle choose of changing room size
     * @param e
     */
    handleRoomSizeChange = (e) => {

        this.setState({
            roomSize: e.target.value
        })
    }

    /**
     * snack bar handlers
     * @param event
     * @param reason
     */
    handleSnackBarClose = (event,reason) => {
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

    /**
     * get all rooms data
     */
    getAllRooms=()=>{
        axios
            .get("/api/game/rooms/available", {
                params: {
                    competitive:false
                },
                headers: authHeader()
            })
            .then(response => {

                let tmpRows = [];
                for (const room of response.data.rooms) {
                    console.log("room.averageElo")
                    console.log(room.averageElo)
                    tmpRows.push({
                        id: room.roomID,
                        roomSize: room.players.length + "/" + room.roomSize,
                        roomState: "Waiting To Start",
                        roomAvgElo: room.averageElo
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