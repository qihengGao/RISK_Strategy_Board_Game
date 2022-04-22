import React, {Component} from "react";
import axios from "axios";
import authHeader from "../services/auth-header";
import PlaceOrder from "./placeOrder.component";
import Snackbar from '@mui/material/Snackbar';
import {Alert, FormControl, FormHelperText, InputLabel, Select} from "@mui/material";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import {GridColDef} from "@mui/x-data-grid";

import TestSocket from "./testSocket";
import MenuItem from "@mui/material/MenuItem";


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
            room: {},
            responseData: {},
            openSnackBar: false,
            snackBarMessage: "Successfully commit the message",
            snackbarType: "error",
            roomID: undefined,
            roundNumber: 0,
            playerID: undefined,
            roundList: [],
            historyMode:true


        };
        //this.initComponent = this.initComponent.bind(this);
    }

    /**
     * set defualt data
     * @returns {Promise<void>}
     */
    async componentDidMount() {
        this.setState({
            roomID: this.props.match.params.roomID
        })
        await this.getData();

        // axios
        //     .get("/api/historyGame/gameStatus", {
        //         params: {
        //             roomID: this.props.match.params.roomID,
        //             roundNumber: 0
        //         }, headers: authHeader()
        //     })
        //     .then(response => {
        //
        //             this.setState({
        //                 responseData: response.data.rooms[0],
        //                 loading: false,
        //                 playerID: response.data.rooms[0].players[0],
        //                 roundNumber: 0
        //             })
        //             console.log(this.state.responseData.players)
        //
        //
        //         }
        //     );
    }


    handleRoundNumberChange = (e) => {
        this.setState({
            roundNumber: e.target.value
        }, () => {
            this.getData()
        })


    }


    handlePlayerIDChange = (e) => {
        this.setState({
            playerID: e.target.value
        }, () => {
            console.log("OnplayerIDChange" + this.state.playerID);
            this.renderMap(this.state.playerID)
        })

        // this.getData()

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
                return <div>PlacingState, Please check the graph to see the placement information.</div>
            case "OrderingState":
                return <PlaceOrder key={this.state.playerID} {...this.state} handleSnackBarUpdate={this.handleSnackBarUpdate}/>
            case "EndState":
                console.log("logging end state")
                console.log(this.state.responseData)
                return <div>Game End!</div>
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
                    <div id="upper" style={{marginTop: "3%"}}>
                        <FormControl sx={{m: 1, minWidth: 1200}}>
                            <InputLabel id="demo-simple-select-helper-label">RoundNumber</InputLabel>
                            <Select
                                labelId="demo-simple-select-helper-label"
                                id="demo-simple-select-helper"
                                value={this.state.roundNumber}
                                label="Age"
                                onChange={this.handleRoundNumberChange}
                            >
                                {this.state.roundList.map((index) =>
                                    <MenuItem key={index} value={index}>{index}</MenuItem>
                                )}

                            </Select>
                            <FormHelperText>Round Number</FormHelperText>

                        </FormControl>
                        <FormControl sx={{m: 1, minWidth: 240}}>
                            <InputLabel id="roundNumber-label">PlayersID</InputLabel>
                            <Select
                                labelId="roundNumber-label"
                                value={this.state.playerID}
                                onChange={this.handlePlayerIDChange}
                                displayEmpty
                                inputProps={{'aria-label': 'Without label'}}
                            >
                                {this.state.responseData.players.map((index) =>
                                    <MenuItem key={index} value={index}>{index}</MenuItem>
                                )}
                            </Select>
                            <FormHelperText>Players ID</FormHelperText>

                        </FormControl>

                        <Box sx={{flexGrow: 1}}>


                            <Grid container spacing={2}>
                                <Grid item xs={5}>
                                    <div ref="main" id="main" style={{width: "1000px", height: "800px"}}/>

                                    <Grid item xs={6}>
                                        <div ref="input"
                                             id="input">{this.renderSwitch(this.state.responseData.currentState)}</div>
                                    </Grid>
                                </Grid>
                            </Grid>
                            <Grid container spacing={2}>
                                <Grid item xs={5}>
                                    <TestSocket {...this.state}/>
                                </Grid>

                                <Grid item xs={6}>

                                </Grid>
                            </Grid>

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


    }
    ;

    initComponent() {


    }


    componentDidUpdate() {
        this.initComponent();
    }


    // componentWillUnmount()
    //     clearInterval(this.interval);
    // }

    /**
     * read data from server for this game room
     */
    getData = async () => {
        await axios
            .get("/api/historyGame/gameStatus", {
                params: {
                    roomID: this.props.match.params.roomID,
                    roundNumber: this.state.roundNumber
                }, headers: authHeader()
            })
            .then(response => {
                    const _ = require("lodash");
                    this.setState(prevState => {
                        return {
                            responseData: response.data.rooms[0],
                            loading: false,
                            playerID: prevState.playerID ? prevState.playerID : response.data.rooms[0].players[0],
                            room: response.data.rooms[0],
                            roundList: _.range(parseInt(this.props.match.params.roundNumber) + 1)
                        }
                    }, () => {
                        console.log("currentState: " + this.state.responseData);
                        this.renderMap(this.state.playerID)
                    })


                }
            );
    }

    renderMap = (playerID) => {
        // this.setState({
        //     message: "", loading: false, room: response.data, roomID: this.props.match.params.roomID
        // });
        console.log("insiderendeMap" + playerID)
        let columnarChart = echarts.init(this.refs.main);
        let allianceColors = [];
        for (const allianceID of this.state.responseData.riskMap.owners[playerID].alliance) {
            allianceColors.push(this.state.responseData.idToColor[allianceID].colorName)
        }
        console.log("allianceColors: ", allianceColors)
        let option = {
            title: {
                //Todo: add round number
                text: 'You are ' + this.state.responseData.idToColor[playerID].colorName + ' player\n' +
                    'Alliances: ' + allianceColors + '\n' +
                    'Current Tech Level: ' + this.state.responseData.riskMap.owners[playerID].currTechlevel + "\n" +
                    'Current Food Resource: ' + this.state.responseData.riskMap.owners[playerID].ownedFoodResource + "\n" +
                    'Current Tech Resource: ' + this.state.responseData.riskMap.owners[playerID].ownedTechResource
            },
            tooltip: {},
            animationDurationUpdate: 1500,
            animationEasingUpdate: 'quinticInOut',
            series: [{
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


        let obj = this.state.responseData;
        console.log("obj:")
        console.log(obj)
        // initialize the echarts instance
        //
        // if (_.isEqual(obj, this.state.responseData))
        //     return;


        // this.setState({responseData: obj})
        // Display the chart using the configuration items and data just specified.


        option["series"][0]["data"].push(...addAllTerritory(obj["riskMap"]["continent"]));
        option["series"][0]["links"].push(...addAllLink(obj["riskMap"]["continent"]));
        console.log(option)
        columnarChart.setOption(option);


        function addNewTerritory(territory) {

            let label = "";
            label += "Size:" + territory.size + "\n";
            for (const unit of territory.units) {
                if (unit.amount > 0) {
                    label += obj.idToColor[unit.ownerId].colorName + "'s" + unit.amount + " " + unit.type + " level " + unit.level + "\n";
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
                    color: obj.idToColor[territory.ownerID].colorName
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
}