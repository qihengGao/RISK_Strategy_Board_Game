import React, {Component} from "react";
import axios from "axios";
import authHeader from "../services/auth-header";

import PlaceUnit from "./placeUnit.component";

const API_URL = "http://localhost:8080/api/game/";
let echarts = require('echarts');


export default class play extends Component {

    constructor(props) {
        super(props);
        //this.handleSubmit = this.handleSubmit.bind(this);
        this.onChangeRoomID = this.onChangeRoomID.bind(this);
        this.state = {
            loading: true, message: "", roomID: "",room:{}
        };
        //this.initComponent = this.initComponent.bind(this);
    }

    async componentDidMount() {
        let response = await axios
            .get(API_URL + "gameStatus", {
                params: {
                    roomID: this.props.match.params.roomID
                }, headers: authHeader()
            })
            .then(response => {

                    this.setState({
                        message: "", loading: false,room:response.data,roomID:this.props.match.params.roomID
                    });
                    //console.log(this.refs.main)
                    let columnarChart = echarts.init(this.refs.main);
                    let option = {
                        title: {
                            text: 'Basic Graph'
                        }, tooltip: {}, animationDurationUpdate: 1500, animationEasingUpdate: 'quinticInOut', series: [{
                            type: 'graph', layout: 'force', symbolSize: 150,

                            roam: true, label: {
                                show: true
                            }, edgeSymbol: ['arrow', 'none'], edgeSymbolSize: [4, 10], edgeLabel: {
                                fontSize: 40
                            }, data: [], // links: [],
                            links: [], categories: [{
                                0: {
                                    name: 'A'
                                }
                            }, {
                                1: {
                                    name: 'B'
                                }
                            }], force: {
                                repulsion: 100000
                            }, lineStyle: {
                                opacity: 0.9, width: 2, curveness: 0
                            }
                        }]
                    };


                    let obj = response.data;

                    // initialize the echarts instance


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
                            name: territory.name, category: territory.ownerID, label: {
                                // Styles defined in 'rich' can be applied to some fragments
                                // of text by adding some markers to those fragment, like
                                // `{styleName|text content text content}`.
                                // `'\n'` is the newline character.
                                formatter: territory.name + "\n" + label,

                                rich: {}
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
                    <div id="upper">
                        <div ref="main" id="main" style={{width: "600px", height: "400px"}}/>
                        <div ref="input" id="input" style={{width: "600px", height: "400px"}}><PlaceUnit {...this.state}/></div>
                    </div>
                )
        );
    }

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


}