import React, {Component} from "react";
import axios from "axios";
import authHeader from "../services/auth-header";

import PlaceUnit from "./placeUnit.component";
import PlaceOrder from "./placeOrder.component";
import AuthService from "../services/auth.service";
import Snackbar from '@mui/material/Snackbar';
import {Alert} from "@mui/material";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import {DataGrid, GridColDef} from "@mui/x-data-grid";


let echarts = require('echarts');

/**
 * controls the view of each game room
 */
export default class play extends Component {
    interval = null;

    /**
     * ctor
     * @param props
     */
    constructor(props) {
        super(props);
        //this.handleSubmit = this.handleSubmit.bind(this);
        this.state = {
            loading: true,
            roomID: "",
            room: {},
            responseData: undefined,
            openSnackBar: false,
            snackBarMessage: "Successfully commit the message",
            snackbarType: "error"
        };
        //this.initComponent = this.initComponent.bind(this);
    }

    /**
     * set defualt data
     * @returns {Promise<void>}
     */
    async componentDidMount() {

        this.interval = setInterval(this.getData, 2000);
        this.getData();


    }

    /**
     * render the page according to the status of this game room
     * @param state
     * @returns {JSX.Element}
     */
    renderSwitch(state) {

        console.log(state);
        switch (state) {
            case "WaitingToStartState":
                return <div>Waiting for other players to join...</div>
            case "WaitingState":
                return <div>Waiting for other players to place orders...</div>
            case "PlacingState":
                return <PlaceUnit {...this.state} handleSnackBarUpdate={this.handleSnackBarUpdate}/>
            case "OrderingState":
                return <PlaceOrder {...this.state} handleSnackBarUpdate={this.handleSnackBarUpdate}/>
            case "EndState":
                console.log("logging end state")
                console.log(this.state.responseData)
                return <div>Game End! {typeof(this.state.responseData)==="undefined"
                    ?"Loading!":
                    "Winner is " +
                    this.state.responseData.idToColor[this.state.responseData.winner].colorName +
                    " Player!"}
                    </div>
            case "LostState":
                return <div>You lost, try to conquest more next time!</div>

        }

    }

    handleSnackBarUpdate = (type, message) => {
        this.setState(
            {
                openSnackBar: true,
                snackBarMessage: message,
                snackbarType: type
            }
        )
    }

    /**
     * render the elements in this page
     * @returns {JSX.Element}
     */
    render() {
        // console.log(this.props.match.params.roomID);
        if (this.state.loading) {
            return <div className="spinner">Loading.....</div>; // add a spinner or something until the posts are loaded
        }
        let columns_chat: GridColDef[] = [{
            field: 'id', headerName: 'Player ID', width: 90
        }, {
            field: 'message', headerName: 'Message', width: 150, editable: false,
        }]
        /**
         * define the elements in this page
         */
        return (

            (this.state.message) ?
                (<div className="alert alert-danger" role="alert">{this.state.message}</div>)
                :
                (
                    <div id="upper" style={{marginTop:"3%"}}>
                        <Box sx={{flexGrow: 1}}>

                            <Grid container spacing={2}>
                                <Grid item xs={5}>
                                    <div ref="main" id="main" style={{width: "1000px", height: "800px"}}/>
                                </Grid>
                                <Grid item xs={6}>
                                    <div ref="input" id="input">{this.renderSwitch(this.state.room.state)}</div>
                                </Grid>
                            </Grid>

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
                                columns={columns_chat}
                                pageSize={9}
                                rowsPerPageOptions={[8]}
                            />
                                </div>

                            <Box component="form" onSubmit={this.handleRegister} noValidate sx={{ mt: 1 }}>
                                <label htmlFor="target">Chat Target:</label>
                                <select name="target" id="target">
                                    <option value="allies">Your Allies</option>
                                    <option value="all">All Players</option>
                                </select>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="username"
                                    label="Message"
                                    name="username"
                                    autoComplete="username"
                                    autoFocus
                                    value={this.state.username}
                                    onChange={this.onChangeUsername}
                                />
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    sx={{ mt: 3, mb: 2 }}
                                >
                                    Send
                                </Button>
                            </Box>

                        </Box>


                        <Snackbar open={this.state.openSnackBar} autoHideDuration={6000}
                                  onClose={this.handleSnackBarClose}>
                            <Alert onClose={this.handleSnackBarClose} severity={this.state.snackbarType}
                                   sx={{width: '100%'}}>
                                {this.state.snackBarMessage}
                            </Alert>
                        </Snackbar>
                    </div>
                )
        );
    }

    handleSnackBarClose = (event, reason) => {
        if (reason === 'clickaway') {
            this.setState({openSnackBar: false})
        }


    };

    initComponent() {


    }




    componentDidUpdate() {
        this.initComponent();
    }


    // componentWillUnmount() {
    //     clearInterval(this.interval);
    // }

    /**
     * read data from server for this game room
     */
    getData = () => {
        axios
            .get("/api/game/gameStatus", {
                params: {
                    roomID: this.props.match.params.roomID
                }, headers: authHeader()
            })
            .then(response => {

                    this.setState({
                        message: "", loading: false, room: response.data, roomID: this.props.match.params.roomID
                    });
                    //console.log(this.refs.main)
                    let columnarChart = echarts.init(this.refs.main);
                    let allianceColors = [];
                    for (const allianceID of response.data.riskMap.owners[AuthService.getCurrentUser().id].alliance){
                        allianceColors.push(response.data.idToColor[allianceID].colorName)
                    }
                    console.log("allianceColors: ", allianceColors)
                    let option = {
                        title: {
                            //Todo: add round number
                            text: 'You are ' + response.data.idToColor[AuthService.getCurrentUser().id].colorName + ' player\n' +
                                'Alliances: ' + allianceColors + '\n' +
                                'Current Tech Level: ' + response.data.riskMap.owners[AuthService.getCurrentUser().id].currTechlevel + "\n" +
                                'Current Food Resource: ' + response.data.riskMap.owners[AuthService.getCurrentUser().id].ownedFoodResource + "\n" +
                                'Current Tech Resource: ' + response.data.riskMap.owners[AuthService.getCurrentUser().id].ownedTechResource
                        }, tooltip: {}, animationDurationUpdate: 1500, animationEasingUpdate: 'quinticInOut', series: [{
                            type: 'graph',
                            layout: 'force',
                            symbolSize: 80,

                            roam: true, label: {
                                show: true
                            }, edgeSymbol: ['arrow', 'none'], edgeSymbolSize: [4, 10], edgeLabel: {
                                fontSize: 40
                            }, data: [], // links: [],
                            links: [],
                            // categories: [{
                            //     0: {
                            //         name: 'A'
                            //     }
                            // }, {
                            //     1: {
                            //         name: 'B'
                            //     }
                            // }],
                            force: {
                                repulsion: 2500,
                                //layoutAnimation : false
                            }, lineStyle: {
                                opacity: 0.9, width: 2, curveness: 0
                            }
                        }]
                    };


                    let obj = response.data;
                    console.log("obj:")
                    console.log(obj)
                    // initialize the echarts instance
                    const _ = require("lodash");
                    if (_.isEqual(obj, this.state.responseData))
                        return;


                    this.setState({responseData: obj})
                    // Display the chart using the configuration items and data just specified.


                    option["series"][0]["data"].push(...addAllTerritory(obj["riskMap"]["continent"]));
                    option["series"][0]["links"].push(...addAllLink(obj["riskMap"]["continent"]));

                    columnarChart.setOption(option);


                    function addNewTerritory(territory) {

                        let label = "";
                        label += "Size:"+territory.size + "\n";
                        for (const unit of territory.units) {
                            if (unit.amount >0){
                                label += response.data.idToColor[unit.ownerId].colorName + "'s" + unit.amount + " " + unit.type + " level " + unit.level+"\n";
                            }
                        }

                        label += "Food Production:" + territory.foodProduction + "\n";
                        label += "Tech Production:" + territory.techProduction + "\n";


                        return {
                            name: territory.name,
                            category: territory.ownerID,
                            label: {
                                // Styles defined in 'rich' can be applied to some fragments
                                // of text by adding some markers to those fragment, like
                                // `{styleName|text content text content}`.
                                // `'\n'` is the newline character.
                                formatter: territory.name + "\n" + label,

                                rich: {}
                            },
                            itemStyle: {
                                //Todo: change to fancier colors
                                // (either in Color or someway to add rgb value to current color)
                                color: response.data.idToColor[territory.ownerID].colorName
                            }
                        };
                    }

                    function addAllTerritory(continent) {
                        let res = [];
                        for (const territory of continent) {
                            res.push(addNewTerritory(territory));
                        }
                        return res;
                    }


                    function addNewLink(territory) {
                        let res = [];
                        for (const neighbor of territory.neighbors) {

                            res.push({
                                source: territory.name, target: neighbor, symbolSize: [5, 20], label: {
                                    show: false
                                }, lineStyle: {
                                    width: 5, curveness: 0.2
                                }


                            });

                        }


                        return res;
                    }

                    function addAllLink(continent) {
                        let res = [];
                        let isRedundent = false;
                        for (const territory of continent) {
                            let tmpRes = addNewLink(territory);
                            for (const tmplink of tmpRes) {
                                isRedundent = false;
                                for (const link of res) {
                                    if (tmplink.source === link.target && tmplink.target === link.source) {
                                        isRedundent = true;
                                        break;
                                    }
                                }
                                if (!isRedundent) {
                                    res.push(tmplink);
                                }
                            }

                        }
                        return res;
                    }


                }
);
    }
}