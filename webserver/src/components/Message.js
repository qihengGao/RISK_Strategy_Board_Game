import React from "react";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import Avatar from "@material-ui/core/Avatar";
import {deepOrange} from "@material-ui/core/colors";
import {blue, green, pink, yellow} from "@mui/material/colors";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        messageRow: {
            display: "flex"
        },
        messageRowRight: {
            display: "flex",
            justifyContent: "flex-end"
        },
        messageBlue: {
            position: "relative",
            marginLeft: "20px",
            marginBottom: "10px",
            padding: "10px",
            backgroundColor: "#A8DDFD",
            //width: "60%",
            //height: "50px",
            textAlign: "left",
            font: "400 .9em 'Open Sans', sans-serif",
            border: "1px solid #97C6E3",
            borderRadius: "10px",
            "&:after": {
                content: "''",
                position: "absolute",
                width: "0",
                height: "0",
                borderTop: "15px solid #A8DDFD",
                borderLeft: "15px solid transparent",
                borderRight: "15px solid transparent",
                top: "0",
                left: "-15px"
            },
            "&:before": {
                content: "''",
                position: "absolute",
                width: "0",
                height: "0",
                borderTop: "17px solid #97C6E3",
                borderLeft: "16px solid transparent",
                borderRight: "16px solid transparent",
                top: "-1px",
                left: "-17px"
            }
        },
        messageOrange: {
            position: "relative",
            marginRight: "20px",
            marginBottom: "10px",
            padding: "10px",
            backgroundColor: "#f8e896",
            width: "60%",
            //height: "50px",
            textAlign: "left",
            font: "400 .9em 'Open Sans', sans-serif",
            border: "1px solid #dfd087",
            borderRadius: "10px",
            "&:after": {
                content: "''",
                position: "absolute",
                width: "0",
                height: "0",
                borderTop: "15px solid #f8e896",
                borderLeft: "15px solid transparent",
                borderRight: "15px solid transparent",
                top: "0",
                right: "-15px"
            },
            "&:before": {
                content: "''",
                position: "absolute",
                width: "0",
                height: "0",
                borderTop: "17px solid #dfd087",
                borderLeft: "16px solid transparent",
                borderRight: "16px solid transparent",
                top: "-1px",
                right: "-17px"
            }
        },

        messageContent: {
            padding: 0,
            margin: 0
        },
        messageTimeStampRight: {
            position: "absolute",
            fontSize: ".85em",
            fontWeight: "300",
            marginTop: "10px",
            bottom: "-3px",
            right: "5px"
        },

        orange: {
            color: theme.palette.getContrastText(deepOrange[500]),
            backgroundColor: deepOrange[500],
            width: theme.spacing(4),
            height: theme.spacing(4)
        },
        avatarNothing: {
            color: "transparent",
            backgroundColor: "transparent",
            width: theme.spacing(4),
            height: theme.spacing(4)
        },
        displayName: {
            marginLeft: "20px"
        }
    })
);
const avatarBgColor = (note) => {
    if (note.category === "work") {
        return yellow[700];
    }
    if (note.category === "money") {
        return green[700];
    }
    if (note.category === "todos") {
        return pink[500];
    } else {
        return blue[500];
    }
};
//avatarが左にあるメッセージ（他人）
export const MessageLeft = (props) => {
    const message = props.message ? props.message : "no message";
    const timestamp = props.timestamp ? new Date(Date.parse(props.timestamp)).toLocaleTimeString("en-US", {
        hour: '2-digit',
        minute: '2-digit'
    }) : "";
    const photoURL = props.photoURL ? props.photoURL : "dummy.js";
    const displayName = props.displayName ? props.displayName : "";
    const classes = useStyles();
    console.log(classes)
    return (
        <>
            <div className={classes.messageRow}>
                <Avatar
                    alt={displayName}
                    src={photoURL}
                    style={{
                        backgroundColor: props.color
                    }}
                ></Avatar>
                <div>
                    <div className={classes.displayName}>{displayName}</div>
                    <div className={classes.messageBlue}>
                        <div>
                            <p className={classes.messageContent}>{message}</p>
                        </div>
                        <div className={classes.messageTimeStampRight}>{timestamp}</div>
                    </div>
                </div>
            </div>
        </>
    );
};
//avatarが右にあるメッセージ（自分）
export const MessageRight = (props) => {
    const classes = useStyles();
    const message = props.message ? props.message : "no message";
    const timestamp = props.timestamp ? new Date(Date.parse(props.timestamp)).toLocaleTimeString("en-US", {
        hour: '2-digit',
        minute: '2-digit'
    }) : "";
    return (
        <div className={classes.messageRowRight}>
            <div className={classes.messageOrange}>
                <p className={classes.messageContent}>{message}</p>
                <div className={classes.messageTimeStampRight}>{timestamp}</div>
            </div>
        </div>
    );
};
