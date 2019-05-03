import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './style/main.css'
import {Link} from 'react-router-dom';

export default class Header extends React.Component {
    render() {
        return (
            <div style={{paddingBottom: 15}}>
                <nav className="navbar navbar-expand-lg navbar-light nav-background" role="navigation">
                    {/*<a className="navbar-brand" href="#">Story Pointer</a>*/}
                    <Link className="navbar-brand" to="/">Story Pointer</Link>
                    <button className="navbar-toggler" type="button" data-toggle="collapse"  data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation" data-tar>
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarNav">
                        <ul className="navbar-nav mr-auto">
                            <li className="nav-item">
                                <Link className="nav-link" to="/">Home</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/about">About</Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/contact">Contact</Link>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
        )
    }
}