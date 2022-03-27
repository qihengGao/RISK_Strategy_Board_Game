import React, {Component} from "react";
import AuthService from "../services/auth.service";
import Input from "react-validation/build/input";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import axios from "axios";
import authHeader from "../services/auth-header";
const API_URL = "http://localhost:8080/api/game/";
let echarts = require('echarts');



export default class Graph extends Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.state = {
            username: "",
            password: "",
            loading: false,
            message: "",
            roomID:0
        };
        //this.initComponent = this.initComponent.bind(this);
    }

    render() {

        return (
            <div id="upper">
                <div style={{marginBottom: '25px'}}>
                    <Form
                        onSubmit={this.handleSubmit}
                        ref={c => {
                            this.form = c;
                        }}
                    >
                        <div className="form-group" >
                            <label htmlFor="roomID">RoomID</label>
                            <Input
                                type="text"
                                className="form-control"
                                name="roomID"
                                onChange={this.onChangeRoomID}

                            />
                        </div>
                        <div className="form-group">
                            <button
                                className="btn btn-primary btn-block"
                                disabled={this.state.loading}
                            >
                                {this.state.loading && (
                                    <span className="spinner-border spinner-border-sm"></span>
                                )}
                                <span>Generate Graph</span>
                            </button>
                        </div>
                        {this.state.message && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    {this.state.message}
                                </div>
                            </div>
                        )}
                        <CheckButton
                            style={{ display: "none" }}
                            ref={c => {
                                this.checkBtn = c;
                            }}
                        />
                    </Form>
                </div>
                <div ref="main" id="main" style={{width: "600px", height: "400px"}}/>
            </div>
        );
    }

    initComponent() {


    }
    onChangeRoomID(e) {
        this.setState({
            roomID: e.target.value
        });
    }
    componentDidMount() {
        this.initComponent()
    }

    componentDidUpdate() {
        this.initComponent();
    }

    handleSubmit(e){
        e.preventDefault();
        this.setState({
            message: "",
            loading: true
        });
        axios
            .get(API_URL + "gameStatus", {
                params: {
                    roomID: this.state.roomID
                },
                headers:  authHeader()
            })
            .then(response => {
                    this.setState({
                        message: "",
                        loading: false
                    });
                    console.log(this.refs.main)
                    let columnarChart = echarts.init(this.refs.main);
                    let option = {
                        title: {
                            text: 'Basic Graph'
                        },
                        tooltip: {},
                        animationDurationUpdate: 1500,
                        animationEasingUpdate: 'quinticInOut',
                        series: [
                            {
                                type: 'graph',
                                layout: 'force',
                                symbolSize: 150,

                                roam: true,
                                label: {
                                    show: true
                                },
                                edgeSymbol: ['arrow', 'none'],
                                edgeSymbolSize: [4, 10],
                                edgeLabel: {
                                    fontSize: 40
                                },
                                data: [],
                                // links: [],
                                links: [],
                                categories: [
                                    {
                                        0: {
                                            name: 'A'
                                        }
                                    },
                                    {
                                        1: {
                                            name: 'B'
                                        }
                                    }
                                ],
                                force: {
                                    repulsion: 100000
                                },
                                lineStyle: {
                                    opacity: 0.9,
                                    width: 2,
                                    curveness: 0
                                }
                            }
                        ]
                    };


                    let obj = response.data;

                    // initialize the echarts instance


                    // Display the chart using the configuration items and data just specified.
                    console.log(obj);
                    option["series"][0]["data"].push.apply(option["series"][0]["data"], addAllTerritory(obj["riskMap"]["continent"]))
                    option["series"][0]["links"].push.apply(option["series"][0]["links"], addAllLink(obj["riskMap"]["continent"]))




                    columnarChart.setOption(option);
                    console.log(option["series"][0]["data"]);

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
                                source: territory.name,
                                target: neighbor,
                                symbolSize: [5, 20],
                                label: {
                                    show: false
                                },
                                lineStyle: {
                                    width: 5,
                                    curveness: 0.2
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
            },
                error => {
                    const resMessage =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();
                    this.setState({
                        loading: false,
                        message: resMessage
                    });
                });
    }


}