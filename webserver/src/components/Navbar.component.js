import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import AuthService from "../services/auth.service";
import MuiLogin from "./MuiLogin.component"
import {Dialog} from "@mui/material";

import Link from "@mui/material/Link";

const pages = ['Products', 'Pricing', 'Blog'];

class Navbar extends React.Component {
    constructor(props) {
        super(props);
        this.logOut = this.logOut.bind(this);
        this.handleLoginButtonInNavBar = this.handleLoginButtonInNavBar.bind(this);
        this.handleOpenUserMenu = this.handleOpenUserMenu.bind(this);
        this.handleCloseUserMenu = this.handleCloseUserMenu.bind(this);
        this.state = {
            showModeratorBoard: false,
            showAdminBoard: false,
            currentUser: undefined,
            anchorElNav: false,
            anchorElUser: false,
            openLoginDialog: false

        };
    }

    componentDidMount() {
        const user = AuthService.getCurrentUser();
        if (user) {
            this.setState({
                currentUser: user,
                showModeratorBoard: user.roles.includes("ROLE_MODERATOR"),
                showAdminBoard: user.roles.includes("ROLE_ADMIN"),
            });
        }
        // EventBus.on("logout", () => {
        //     this.logOut();
        // });
    }

    logOut() {
        this.setState({
            showModeratorBoard: false, showAdminBoard: false, currentUser: undefined, anchorElUser: false
        });
        AuthService.logout();
        this.props.history.push("/");
        window.location.reload();

    }

    // componentWillUnmount() {
    //     EventBus.remove("logout");
    // }

    handleOpenNavMenu(event) {
        //setAnchorElNav(event.currentTarget);
        this.setState({anchorElNav: true});
    };

    handleOpenUserMenu(e) {
        //setAnchorElUser(event.currentTarget);
        this.setState({anchorElUser: true});

    };

    handleCloseNavMenu() {
        //setAnchorElNav(null);
        this.setState({anchorElUser: false});
    };

    handleCloseUserMenu() {
        //setAnchorElUser(null);
        this.setState({anchorElUser: false});


    };

    handleLoginButtonInNavBar() {
        //setOpenLoginDialog(true);
        this.props.history.push("/login");
        this.setState({openLoginDialog: true})
    }

    render() {
        console.log(this.props.history);
        const {currentUser, showModeratorBoard, showAdminBoard} = this.state;
        return (<AppBar position="static">
            <Container maxWidth="xl">
                <Toolbar disableGutters>
                    <Typography
                        variant="h6"
                        noWrap
                        component="div"
                        sx={{mr: 2, display: {xs: 'none', md: 'flex'}}}
                    >
                        Risc
                    </Typography>
                    <Box sx={{flexGrow: 1, display: {xs: 'none', md: 'flex'}}}>
                        <Button
                            key="All rooms"
                            component={Link}
                            href="/allRooms"
                            sx={{my: 2, color: 'white', display: 'block'}}
                        >
                            All rooms
                        </Button>
                        <Button
                            key="joinedRooms"
                            component={Link}
                            href="/joinedRooms"
                            sx={{my: 2, color: 'white', display: 'block'}}
                        >
                            Joined rooms
                        </Button>
                    </Box>
                    {currentUser ? (<Box sx={{flexGrow: 0}}>
                        <Tooltip title="Open settings">
                            <IconButton onClick={this.handleOpenUserMenu} sx={{p: 0}}>
                                <Avatar alt="Remy Sharp">{currentUser.username.substring(0, 2)}</Avatar>
                            </IconButton>
                        </Tooltip>
                        <Menu
                            sx={{mt: '45px'}}
                            id="menu-appbar"
                            // // anchorEl={this.state.anchorElUser}
                            anchorOrigin={{
                                vertical: 'top', horizontal: 'right',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top', horizontal: 'right',
                            }}
                            open={this.state.anchorElUser}
                            onClose={this.handleCloseUserMenu}
                        >
                            <MenuItem key="Profile" component={Link} href={"/profile"}>
                                <Typography>
                                    Profile
                                </Typography>
                            </MenuItem>

                            <MenuItem key="Logout">
                                <Typography onClick={this.logOut}>
                                    Logout
                                </Typography>
                            </MenuItem>


                        </Menu>
                    </Box>) : (<Box sx={{flexGrow: 0}}>
                        <Tooltip title="Login">
                            <Button color="inherit" onClick={this.handleLoginButtonInNavBar}>Login</Button>

                        </Tooltip>
                    </Box>)}
                    {/*<Dialog open={this.state.openLoginDialog}>*/}

                    {/*    <MuiLogin {...this.props}/>*/}
                    {/*</Dialog>*/}
                </Toolbar>
            </Container>

        </AppBar>);
    }
}

export default Navbar;
