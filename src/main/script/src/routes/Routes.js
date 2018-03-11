import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Home from '../Home';
import Join from '../Join';
import Game from '../Game';
import About from '../About';
import Contact from '../Contact';

export default () => (
    <Router>
        <div>
            <Route exact path="/" component={Home}/>
            <Route exact path="/join" component={Join}/>
            <Route exact path="/game" component={Game}/>
            <Route exact path="/about" component={About}/>
            <Route exact path="/contact" component={Contact}/>
        </div>
    </Router>
)

