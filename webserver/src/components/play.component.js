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

const API_URL = "http://localhost:8080/api/game/";
let echarts = require('echarts');


function Item(props: { children: ReactNode }) {
    return null;
}

export default class play extends Component {
    interval = null;

    constructor(props) {
        super(props);
        //this.handleSubmit = this.handleSubmit.bind(this);
        this.onChangeRoomID = this.onChangeRoomID.bind(this);
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

    async componentDidMount() {

        this.interval = setInterval(this.getData, 2000);
        this.getData();


    }

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
                return <div>Game End! Winner
                    is {this.state.responseData.idToColor[this.state.responseData.winner].colorName} player!</div>
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

    render() {
        // console.log(this.props.match.params.roomID);
        if (this.state.loading) {
            return <div className="spinner">Loading.....</div>; // add a spinner or something until the posts are loaded
        }


        return (

            (this.state.message) ?
                (<div className="alert alert-danger" role="alert">{this.state.message}</div>)
                :
                (
                    <div id="upper" style={{marginTop:"3%"}}>
                        <Box sx={{flexGrow: 1}}>
                            <Grid container spacing={2}>
                                <Grid item xs={6}>
                                    <div ref="main" id="main" style={{width: "1200px", height: "800px"}}/>
                                </Grid>
                                <Grid item xs={6}>
                                    <div ref="input" id="input">{this.renderSwitch(this.state.room.state)}</div>
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
            return;
        }

        this.setState({openSnackBar: false})
    };

    initComponent() {


    }

    onChangeRoomID(e) {
        this.setState({
            roomID: e.target.value
        });
    }


    componentDidUpdate() {
        this.initComponent();
    }


    componentWillUnmount() {
        clearInterval(this.interval);
    }

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
                    let option = {
                        title: {
                            text: 'You are ' + response.data.idToColor[AuthService.getCurrentUser().id].colorName + ' player'
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
                        for (const unit of territory.units) {

                            label += unit.type + " " + unit.amount + "\n";

                        }


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

                , error => {
                    const resMessage = (error.response && error.response.data && error.response.data.message) || error.message || error.toString();
                    this
                        .setState({
                            loading: false, message: resMessage
                        });
                });
    }
}