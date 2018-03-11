import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './style/main.css'
import Header from './Header'

export default class Home extends React.Component {

    constructor(props) {
        super(props);
        this.state = {sessionId: ''};
        this.handleChange = this.handleChange.bind(this);
        this.handleStartSession = this.handleStartSession.bind(this);
        this.handleJoin = this.handleJoin.bind(this);
    }

    handleChange(event) {
        this.setState({sessionId: event.target.value});
    }

    handleJoin(event) {
        let domain = window.location.origin;
        let url = domain + '/session/verify/' + this.state.sessionId;
        event.preventDefault();

        fetch(url, {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            credentials: 'same-origin'
        }).then(d => {
            console.log(d.status);
            if (d.status === 200) {
                this.props.history.push({
                    pathname: '/join',
                    state: {sessionId: this.state.sessionId}
                });
            }
        });
    }

    handleStartSession() {
        let domain = window.location.origin;
        let url = domain + '/session/get';
        console.log(url);
        fetch(url, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'same-origin'
        })
            .then(d => d.json())
            .then(d => {
                this.props.history.push({
                    pathname: '/join',
                    state: {sessionId: d.sessionId, host: true}
                });
            });
    }

    render() {
        return (
            <div>
                {<Header/>}
                <p>
                    <h2 className="Margins">Start a session</h2>
                    <button className="Button" onClick={this.handleStartSession}>Start
                    </button>
                </p>

                <p>
                    <form onSubmit={this.handleJoin}>
                        <label>
                            <h2 className="Margins">Join a session</h2>
                            <input className="Margins" type="text" value={this.state.sessionId} onChange={this.handleChange}/>
                        </label>
                        <input className="Button" type="submit" value="Join"/>
                    </form>
                </p>
            </div>
        );
    }
}