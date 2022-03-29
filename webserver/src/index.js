import React from 'react';
import ReactDOM from 'react-dom';
import CssBaseline from '@mui/material/CssBaseline';

import App from './App';

import { BrowserRouter } from "react-router-dom";


ReactDOM.render(
    <BrowserRouter>

        <CssBaseline />
        <App />
    </BrowserRouter>,
    document.getElementById('root')
);