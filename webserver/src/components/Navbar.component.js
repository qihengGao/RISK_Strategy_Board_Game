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

/**
 * controls the navigation bar of each page
 */
class Navbar extends React.Component {
    /**
     * ctor
     * @param props
     */
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

    /**
     * controls logout action
     */
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

    // handleOpenNavMenu(event) {
    //     //setAnchorElNav(event.currentTarget);
    //     this.setState({anchorElNav: true});
    // };

    /**
     * open the user menu
     * @param e
     */
    handleOpenUserMenu(e) {
        //setAnchorElUser(event.currentTarget);
        this.setState({anchorElUser: true});

    };

    // handleCloseNavMenu() {
    //     //setAnchorElNav(null);
    //     this.setState({anchorElUser: false});
    // };

    /**
     * close the user menu
     */
    handleCloseUserMenu() {
        //setAnchorElUser(null);
        this.setState({anchorElUser: false});


    };

    /**
     * handle the action of clicking the login button
     */
    handleLoginButtonInNavBar() {
        //setOpenLoginDialog(true);
        this.props.history.push("/login");
        this.setState({openLoginDialog: true})
    }

    /**
     * render the elements in the page
     * @returns {JSX.Element}
     */
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
                            href="/normalRooms"
                            sx={{my: 2, color: 'white', display: 'block'}}
                        >
                            All rooms
                        </Button>
                        <Button
                            key="Competitive rooms"
                            component={Link}
                            href="/competitiveRooms"
                            sx={{my: 2, color: 'white', display: 'block'}}
                        >
                            Competitive rooms
                        </Button>
                        <Button
                            key="joinedRooms"
                            component={Link}
                            href="/joinedRooms"
                            sx={{my: 2, color: 'white', display: 'block'}}
                        >
                            Joined rooms
                        </Button>
                        <Button
                            key="historyRooms"
                            component={Link}
                            href="/historyGames"
                            sx={{my: 2, color: 'white', display: 'block'}}
                        >
                            History Games
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
