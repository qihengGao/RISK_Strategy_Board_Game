import React, {Component} from "react";
import {DataGrid, GridColDef} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import AuthService from "../services/auth.service";
import axios from "axios";
import authHeader from "../services/auth-header";
import {Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@mui/material";

const API_URL = "http://localhost:8080/api/game/";


/**
 * handle the action of placing units from user
 */
export default class unitPlace extends Component {
    /**
     * ctor
     * @param props
     */
    constructor(props) {
        super(props);

        this.handleCommit = this.handleCommit.bind(this);
        this.state = {
            loading: true,
            message: "",
            roomID: this.props.roomID,
            rows: [], //this.initComponent = this.initComponent.bind(this);
            openConfirmDialog:false
        };
    }

    /**
     * add default values for fields
     */
    componentDidMount() {
        let idCounter = 0;
        let tmpRows = [];
        console.log(this.props.room)
        for (const territory of this.props.room.riskMap.continent) {
            if (territory.ownerID === AuthService.getCurrentUser().id) tmpRows.push({
                id: idCounter++, territoryName: territory.name, unitAmount: null
            })

        }
        this.setState({rows: tmpRows});


    }

    /**
     * handle the commit of some cell
     * @param id
     */
    handleCellCommit = (id) => {


        let newRow = {
            id: id.id, territoryName: id.row.territoryName, unitAmount: id.value
        }
        console.log(newRow)
        this.setState(prevState => ({

            rows: prevState.rows.map(el => el.id === id.id ? {...el, unitAmount: id.value} : el)

        }))

    }

    /**
     * render the elements in this page
     * @returns {JSX.Element}
     */
    render() {
        console.log(this.props.room)
        let columns: GridColDef[] = [{field: 'id', headerName: 'ID', width: 90}, {
            field: 'territoryName', headerName: 'Territory name', width: 150, editable: false,
        }, {
            field: 'unitAmount', headerName: 'Unit amount', width: 150, editable: true, type: 'number',
        },];

        return (

            <div style={{height: 400, width: '100%'}}>
                <p>You have 30 units to put.</p>
                <DataGrid

                    rows={this.state.rows}
                    columns={columns}
                    // pageSize={5}
                    // rowsPerPageOptions={[5]}
                    // experimentalFeatures={{newEditingApi: true}}
                    onCellEditCommit={this.handleCellCommit}
                />
                <Button variant="contained" onClick={this.handleConfirmDialogOpen}>Commit</Button>
                <Dialog

                    open={this.state.openConfirmDialog}

                    aria-labelledby="responsive-dialog-title"
                >
                    <DialogTitle id="responsive-dialog-title">
                        {"Confirm Placement?"}
                    </DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                           Are you sure?
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button autoFocus onClick={this.handleCommit}>
                            Commit
                        </Button>
                        <Button onClick={this.handleConfirmDialogClose} autoFocus>
                            Cancel
                        </Button>
                    </DialogActions>
                </Dialog>

            </div>);
    }



    initComponent() {


    }

    /**
     * open the confirm dialog ask for confirmation from user
     */
    handleConfirmDialogOpen =()=>{
        this.setState({openConfirmDialog:true})
    }
    /**
     * close the confirm dialog
     */
    handleConfirmDialogClose =()=>{
        this.setState({openConfirmDialog:false})
    }

    /**
     * handle the commit of all placing unit orders
     * @param e
     */
    handleCommit(e) {
        console.log(this.ref)
        console.log(this.state.rows)
        let unitPlaceOrders = {};
        for (const row of this.state.rows) {
            if (row.unitAmount == null){
                unitPlaceOrders[row.territoryName] = 0;
            } else{
                unitPlaceOrders[row.territoryName] = row.unitAmount;
            }
        }
        console.log(unitPlaceOrders);
        axios
            .post("/api/game/place/unit", {
                roomID: this.state.roomID, unitPlaceOrders
            }, {headers: authHeader()})
            .then((response) => {
                console.log(response);
                //this.setState({messages: tmpmessage});
                this.props.handleSnackBarUpdate("success", response.data.prompt)
            }).catch((error) => {
            console.log(error.response);
            this.props.handleSnackBarUpdate("error", error.response.data.prompt);
            this.setState({messages: error});
        });

    }


    componentDidUpdate() {
        this.initComponent();
    }


}