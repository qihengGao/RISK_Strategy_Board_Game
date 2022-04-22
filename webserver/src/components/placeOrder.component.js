import React, {Component} from "react";
import {DataGrid, GridColDef} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import axios from "axios";
import authHeader from "../services/auth-header";
import Box from '@mui/material/Box';
import {Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Stack} from "@mui/material";
import AuthService from "../services/auth.service";


/**
 * handle the acitons of placing orders
 */
class unitPlace extends Component {
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
            idCounter: 1,
            columns: [],
            selectedRowsID: []
        };
    }

    /**
     * set the default values or choosable value for the grid cell data
     */
    componentDidMount() {
        let orderChoices = ["Move", "Attack", "Upgrade Unit", "Upgrade Tech Level"];
        // console.log("ROOMSIZE:", Object.keys(this.props.room.idToColor).length)
        if (Object.keys(this.props.room.idToColor).length > 2) {
            orderChoices.push("Form Alliance");
        }

        let enemyTerritory = [];
        let ownTerritory = [];
        let myUnitTypes = [];
        let ownAndAllyTerr = [];
        for (const territory of this.props.room.riskMap.continent) {
            if (territory.ownerID === AuthService.getCurrentUser().id) {
                ownTerritory.push(territory.name);
                ownAndAllyTerr.push(territory.name);
                for (let i = 0; i < 7; i++) {
                    let unitInfo = "Soldier" + " level " + i;
                    if (!myUnitTypes.includes(unitInfo)) {
                        myUnitTypes.push(unitInfo);
                    }
                }
            } else {
                if (this.props.room.riskMap.owners[AuthService.getCurrentUser().id].alliance.includes(territory.ownerID)) {
                    ownAndAllyTerr.push(territory.name);
                }
                enemyTerritory.push(territory.name);
            }
        }
        let otherPlayers = [];
        for (var id in this.props.room.idToColor) {
            if (id != AuthService.getCurrentUser().id) {
                console.log("Other player", id);
                otherPlayers.push(this.props.room.idToColor[id].colorName)
            }
        }

        this.setState(prevState => ({
            columns: [
                {field: 'id', headerName: 'ID', width: 30},
                {
                    field: 'orderType',
                    headerName: 'Order Type',
                    width: 100,
                    editable: true,
                    type: "singleSelect",
                    valueOptions: orderChoices
                },
                {
                    field: 'source',
                    headerName: 'Source Territory',
                    width: 125,
                    editable: true,
                    type: "singleSelect",
                    //valueOptions: ownTerritory
                    valueOptions: ({row}) => {
                        if (row === undefined ||
                            row.orderType === "Upgrade Tech Level" ||
                            row.orderType === "Form Alliance") {
                            return [];
                        } else if (row.orderType === "Attack") {
                            return ownTerritory;
                        }
                        return ownAndAllyTerr;
                    }
                },
                {
                    field: 'target',
                    headerName: 'Target Territory',
                    width: 120,
                    editable: true,
                    type: "singleSelect",
                    valueOptions: ({row}) => {
                        if (row === undefined ||
                            row.orderType === "Upgrade Unit" ||
                            row.orderType === "Upgrade Tech Level" ||
                            row.orderType === "Form Alliance") {
                            return [];
                        }
                        if (row.orderType === "Move") {
                            return ownAndAllyTerr;
                        }
                        if (row.orderType === "Attack")
                            return enemyTerritory;
                        return [];
                    }
                },
                {
                    field: 'unitType',
                    headerName: 'Unit Type',
                    width: 80,
                    editable: true,
                    type: "singleSelect",
                    valueOptions: ({row}) => {
                        if (row === undefined ||
                            row.orderType === "Upgrade Tech Level" ||
                            row.orderType === "Form Alliance") {
                            return [];
                        }
                        return myUnitTypes;
                    }
                },
                {field: 'unitAmount', headerName: 'Unit amount', width: 110, editable: true, type: 'number'},
                {
                    field: 'upLevel',
                    headerName: 'Upgrade To',
                    width: 110,
                    editable: true,
                    type: "singleSelect",
                    valueOptions: ({row}) => {
                        if (row.orderType === "Upgrade Unit") {
                            return [1, 2, 3, 4, 5, 6];
                        }
                        return [];
                    }
                },
                {
                    field: 'allianceWith',
                    headerName: 'Target Player',
                    width: 110,
                    editable: true,
                    type: "singleSelect",
                    valueOptions: ({row}) => {
                        if (row.orderType === "Form Alliance") {
                            return otherPlayers;
                        }
                        return [];
                    }
                }
            ]
        }))
        let idcount = -1;
        if (this.props.historyMode) {
            console.log("historyMode");
            console.log(this.props.responseData.temporaryOrders);
            let selfOrders = this.props.responseData.temporaryOrders.filter((order) => {
                console.log(order.playerID)
                return order.playerID === this.props.playerID
            }).map((order)=>{
                idcount+=1
                return {...order,id:idcount,source:order.srcTerritory,target:order.destTerritory }
            })

            console.log(selfOrders);
            console.log(this.props.playerID);
            this.setState({
                rows: selfOrders
            })
        }
    }

    /**
     * handle the commit of one cell
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
     * handle the select of some row
     * @param selection
     */
    handleSelect = (selection) => {
        //console.log(selection);
        this.setState({selectedRowsID: selection})
    }

    /**
     * handle add one row
     */
    handleAddRow = () => {
        this.setState(prevState => ({
            rows: [...prevState.rows, {
                id: prevState.idCounter,
                oderType: null,
                source: null,
                target: null,
                unitType: null,
                unitAmount: null,
                toLevel: null,
                allianceWith: null
            }],
            idCounter: prevState.idCounter + 1
        }))
    }

    /**
     * handle delete some selected row
     */
    handleDeleteSelectedRows = () => {
        this.setState(prevState => ({

            rows: prevState.rows.filter(el => !prevState.selectedRowsID.includes(el.id))

        }))
    }

    /**
     * render all the elements in the page
     * @returns {JSX.Element}
     */
    render() {
        //console.log(this.props.room)
        let columns: GridColDef[] = this.state.columns;

        return (
            <div style={{
                height: 400,
                width: '100%'
            }}>
                <Stack
                    sx={{width: '100%', mb: 1}}
                    direction="row"
                    alignItems="flex-start"
                    columnGap={1}

                ><Button variant="contained" size="small" color="success" onClick={this.handleAddRow}>
                    Add a order
                </Button>
                    <Button variant="contained" color="error" size="small"
                            onClick={this.handleDeleteSelectedRows}>

                        Delete selected rows
                    </Button>

                </Stack>
                <Box sx={{height: 400, bgcolor: 'background.paper'}}>
                    <DataGrid
                        checkboxSelection
                        hideFooter
                        rows={this.state.rows}
                        onSelectionModelChange={this.handleSelect}
                        onCellEditCommit={this.handleRowCommit}
                        columns={columns}/>
                </Box>

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
            </div>
        );
    }

    handleRowCommit = (row) => {
        console.log(row);
        this.setState(prevState => ({

            rows: prevState.rows.map(el => el.id === row.id ? {...el, [row.field]: row.value} : el)

        }))

    }

    initComponent() {


    }

    handleCommit(e) {
        const ColorToId = new Map();
        for (var id in this.props.room.idToColor) {
            ColorToId.set(this.props.room.idToColor[id].colorName, id)
        }
        console.log("ColorToID:", ColorToId);

        let orders = [];
        console.log("logging", this.props.room);
        for (const row of this.state.rows) {
            console.log("row alliance", row.allianceWith);
            orders.push({
                srcTerritory: row.source,
                destTerritory: row.target,
                unitType: row.unitType,
                unitAmount: row.unitAmount,
                playerID: AuthService.getCurrentUser().id,
                orderType: row.orderType,
                toLevel: row.upLevel,
                allianceID: ColorToId.get(row.allianceWith)
            })
        }
        console.log(orders);
        axios
            .post("/api/game/place/order", {
                roomID: this.state.roomID,
                orders
            }, {headers: authHeader()})
            .then((response) => {
                    console.log(response);
                    this.props.handleSnackBarUpdate("success", response.data.prompt)
                    this.setState({
                        rows: []
                    })
                    this.setState({openConfirmDialog: false})
                    //this.setState({messages: tmpmessage});
                    //window.location.reload();
                }
            ).catch((error) => {
            console.log(error);
            this.props.handleSnackBarUpdate("error", error.response.data.prompt);
            this.setState({messages: error});
            console.log(error.response);
        });

    }

    handleConfirmDialogOpen = () => {
        this.setState({openConfirmDialog: true})
    }
    handleConfirmDialogClose = () => {
        this.setState({openConfirmDialog: false})
    }

    componentDidUpdate() {
        this.initComponent();
    }


}


export default unitPlace;

