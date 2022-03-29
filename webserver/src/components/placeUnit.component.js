import React, {Component} from "react";
import {DataGrid, GridColDef} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import AuthService from "../services/auth.service";
import axios from "axios";
import authHeader from "../services/auth-header";

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
        // let response = await axios
        //     .get(API_URL + "gameStatus", {
        //         params: {
        //             roomID: this.props.match.params.roomID
        //         }, headers: authHeader()
        //     })
        //     .then(response => {
        //             this.setState({
        //                 message: "", loading: false,room:response.data
        //             });
        //             console.log(this.refs.main)
        //             let columnarChart = echarts.init(this.refs.main);
        //
        //
        //
        //             let obj = response.data;
        //
        //             // initialize the echarts instance
        //
        //
        //             // Display the chart using the configuration items and data just specified.
        //             console.log(obj);
        //
        //             option["series"][0]["data"].push(...addAllTerritory(obj["riskMap"]["continent"]));
        //             option["series"][0]["links"].push(...addAllLink(obj["riskMap"]["continent"]));
        //
        //             columnarChart.setOption(option);
        //             console.log(option["series"][0]["data"]);
        //
        //             function addNewTerritory(territory) {
        //
        //                 let label = "";
        //                 for (const unit of territory.units) {
        //
        //                     label += unit.type + " " + unit.amount + "\n";
        //
        //                 }
        //
        //
        //                 return {
        //                     name: territory.name, category: territory.ownerID, label: {
        //                         // Styles defined in 'rich' can be applied to some fragments
        //                         // of text by adding some markers to those fragment, like
        //                         // `{styleName|text content text content}`.
        //                         // `'\n'` is the newline character.
        //                         formatter: territory.name + "\n" + label,
        //
        //                         rich: {}
        //                     }
        //                 };
        //             }
        //
        //             function addAllTerritory(continent) {
        //                 let res = [];
        //                 for (const territory of continent) {
        //                     res.push(addNewTerritory(territory));
        //                 }
        //                 return res;
        //             }
        //
        //
        //             function addNewLink(territory) {
        //                 let res = [];
        //                 for (const neighbor of territory.neighbors) {
        //
        //                     res.push({
        //                         source: territory.name, target: neighbor, symbolSize: [5, 20], label: {
        //                             show: false
        //                         }, lineStyle: {
        //                             width: 5, curveness: 0.2
        //                         }
        //
        //
        //                     });
        //
        //                 }
        //
        //
        //                 return res;
        //             }
        //
        //             function addAllLink(continent) {
        //                 let res = [];
        //                 let isRedundent = false;
        //                 for (const territory of continent) {
        //                     let tmpRes = addNewLink(territory);
        //                     for (const tmplink of tmpRes) {
        //                         isRedundent = false;
        //                         for (const link of res) {
        //                             if (tmplink.source === link.target && tmplink.target === link.source) {
        //                                 isRedundent = true;
        //                                 break;
        //                             }
        //                         }
        //                         if (!isRedundent) {
        //                             res.push(tmplink);
        //                         }
        //                     }
        //
        //                 }
        //                 return res;
        //             }
        //
        //
        //         }
        //
        //         , error => {
        //             const resMessage = (error.response && error.response.data && error.response.data.message) || error.message || error.toString();
        //             this
        //                 .setState({
        //                     loading: false, message: resMessage
        //                 });
        //         });
        //


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
            .post( "/api/game/place/unit", {
                roomID: this.state.roomID, unitPlaceOrders
            }, {headers: authHeader()})
            .then((response) => {
                console.log(response);
                //this.setState({messages: tmpmessage});
                window.location.reload();
            }, error => {
                window.location.reload();
                this.setState({messages: error});
            });

    }


    componentDidUpdate() {
        this.initComponent();
    }


}