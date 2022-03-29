import React, {Component} from "react";
import {DataGrid, GridColDef} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import axios from "axios";
import authHeader from "../services/auth-header";
import Box from '@mui/material/Box';
import {Stack} from "@mui/material";
import AuthService from "../services/auth.service";

const API_URL = "http://localhost:8080/api/game/";


export default class unitPlace extends Component {
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

    componentDidMount() {
        let allTerritory = [];
        let enemyTerritory = [];
        let ownTerritory = [];
        let unitTypes = [];
        for (const territory of this.props.room.riskMap.continent) {
            allTerritory.push(territory.name);
            if (territory.ownerID === AuthService.getCurrentUser().id) {
                ownTerritory.push(territory.name);
            } else {
                enemyTerritory.push(territory.name);
            }
            for (const unit of territory.units) {
                if (!unitTypes.includes(unit.type)) {
                    unitTypes.push(unit.type);
                }
            }

        }
        this.setState(prevState => ({
            columns: [
                {field: 'id', headerName: 'ID', width: 45},
                {
                    field: 'orderType',
                    headerName: 'Order Type',
                    width: 100,
                    editable: true,
                    type: "singleSelect",
                    valueOptions: ["Move", "Attack"]
                },
                {
                    field: 'source',
                    headerName: 'Source Territory',
                    width: 150,
                    editable: true,
                    type: "singleSelect",
                    valueOptions: ownTerritory
                },
                {
                    field: 'target',
                    headerName: 'Target Territory',
                    width: 150,
                    editable: true,
                    type: "singleSelect",
                    valueOptions: ({row}) => {
                        if (row === undefined) {
                            return [];
                        }
                        if (row.orderType === "Move") {
                            return ownTerritory;
                        }
                        if (row.orderType === "Attack")
                            return enemyTerritory;
                        return [];
                    }
                },
                {
                    field: 'unitType',
                    headerName: 'Unit Type',
                    width: 150,
                    editable: true,
                    type: "singleSelect",
                    valueOptions: unitTypes
                },
                {field: 'unitAmount', headerName: 'Unit amount', width: 150, editable: true, type: 'number',},

            ]
        }))

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
    handleSelect = (selection) => {
        //console.log(selection);
        this.setState({selectedRowsID: selection})
    }

    handleAddRow = () => {
        this.setState(prevState => ({
            rows: [...prevState.rows, {
                id: prevState.idCounter,
                oderType: null,
                source: null,
                target: null,
                unitType: null,
                unitAmount: null
            }],
            idCounter: prevState.idCounter + 1
        }))
    }

    handleDeleteSelectedRows = () => {
        this.setState(prevState => ({

            rows: prevState.rows.filter(el => !prevState.selectedRowsID.includes(el.id))

        }))
    }

    render() {
        //console.log(this.props.room)
        let columns: GridColDef[] = this.state.columns;

        return (
            <div style={{width: '100%'}}>
                <Stack
                    sx={{width: '100%', mb: 1}}
                    direction="row"
                    alignItems="flex-start"
                    columnGap={1}

                >
                    <Button variant="contained" size="small" color="success"onClick={this.handleAddRow}>
                        Add a order
                    </Button><Button variant="contained" color="error" size="small"
                                     onClick={this.handleDeleteSelectedRows}>

                    Delete selected rows
                </Button>
                    <Button variant="contained" size="small" onClick={this.handleCommit}>Commit</Button>
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

        let orders = [];
        for (const row of this.state.rows) {
            orders.push({
                srcTerritory:row.source,
                destTerritory:row.target,
                unitType:row.unitType,
                unitAmount:row.unitAmount,
                playerID: AuthService.getCurrentUser().id,
                orderType:row.orderType
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
                //this.setState({messages: tmpmessage});
                //window.location.reload();
            }, error => {
                //window.location.reload();
                this.setState({messages: error});
            });

    }


    componentDidUpdate() {
        this.initComponent();
    }


}

