import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import AuthService from "../services/auth.service";
import {Component} from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import Snackbar from "@mui/material/Snackbar";
import {Alert} from "@mui/material";


const theme = createTheme();

/**
 * control the sign up action of users
 */
export default class muiRegister extends Component {
    /**
     * sign up ctor
     * @param props
     */
    constructor(props) {
        super(props);
        this.handleRegister = this.handleRegister.bind(this);
        this.onChangeUsername = this.onChangeUsername.bind(this);
        this.onChangeEmail = this.onChangeEmail.bind(this);
        this.onChangePassword = this.onChangePassword.bind(this);
        this.state = {
            username: "",
            email: "",
            password: "",
            loading: false,
            message: "",
            openSnackBar: false,
            snackBarMessage: "",
            snackbarType: "error"
        };
    }

    /**
     * change username value to input value
     * @param e
     */
    onChangeUsername(e) {
        this.setState({
            username: e.target.value
        });
    }

    /**
     * change email value to input value
     * @param e
     */
    onChangeEmail(e) {
        this.setState({
            email: e.target.value
        });
    }

    /**
     * change password value to input value
     * @param e
     */
    onChangePassword(e) {
        this.setState({
            password: e.target.value
        });
    }

    /**
     * handle snack bar
     * @param type
     * @param message
     */
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
     * handle the action of clicking the register button
     * @param e
     */
    handleRegister(e) {
        e.preventDefault();
        this.setState({
            message: "",
            loading: true
        });
        //this.form.validateAll();
        //if (this.checkBtn.context._errors.length === 0) {
        console.log("sssssss")

        AuthService.register(
            this.state.username,
            this.state.email,
            this.state.password)
            .then( () => {
                this.handleSnackBarUpdate("success", "Successfully Registered");
                this.props.history.push("/login");
                window.location.reload();
            },
            error => {
                this.handleSnackBarUpdate("error", "Username already exist!");
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
            }
        );
    }

    /**
     * render the elements in the page
     * @returns {JSX.Element}
     */
    render() {
        return (
            <ThemeProvider theme={theme}>
                <Container component="main" maxWidth="xs">
                    <CssBaseline />
                    <Box
                        sx={{
                            marginBottom: 8,
                            marginTop: 8,
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                        }}
                    >
                        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                            <LockOutlinedIcon />
                        </Avatar>
                        <Typography component="h1" variant="h5">
                            Sign Up
                        </Typography>
                        <Box component="form" onSubmit={this.handleRegister} noValidate sx={{ mt: 1 }}>
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                id="username"
                                label="User Name"
                                name="username"
                                autoComplete="username"
                                autoFocus
                                value={this.state.username}
                                onChange={this.onChangeUsername}
                            />
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                id="email"
                                label="Email"
                                name="email"
                                autoComplete="email"
                                autoFocus
                                value={this.state.email}
                                onChange={this.onChangeEmail}
                            />
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                name="password"
                                label="Password"
                                type="password"
                                id="password"
                                autoComplete="current-password"
                                value={this.state.pass}
                                onChange={this.onChangePassword}
                            />
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                            >
                                Sign Up
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
                </Container>
            </ThemeProvider>
        );
    }
}
