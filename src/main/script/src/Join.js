import React from 'react';
import {withRouter} from 'react-router-dom';
import './style/main.css';
import Header from './Header';

export class Join extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: '',
            sessionId: ''
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.componentWillMount = this.componentWillMount.bind(this);

    }

    getCookie(cname) {
        let name = cname + "=";
        let decodedCookie = decodeURIComponent(document.cookie);
        let ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }

    componentWillMount() {
        if (this.props.location.state === undefined || this.props.location.state.sessionId === null) {
            let sessionIdCookie = this.getCookie('sessionId');
            console.log('sess: ' + sessionIdCookie);
            this.setState({
                sessionId: sessionIdCookie
            })
        } else {
            this.setState({
                sessionId: this.props.location.state.sessionId
            });
        }
    }

    handleChange(event) {
        this.setState({name: event.target.value});
    }

    handleSubmit(event) {
        let domain = window.location.origin;
        let url = domain + '/session/join';
        fetch(url, {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: this.state.name
            }),
            credentials: 'same-origin'
        })
            .then(d => d.json());

        this.props.history.push({
            pathname: '/game',
            state: {
                sessionId: this.state.sessionId,
                name: this.state.name
            }
        });
    }

    render() {
        return (
            <div>
                {<Header/>}
                <p className="Margins">Session id: {this.state.sessionId}</p>
                <form className="Margins" onSubmit={this.handleSubmit}>
                    <label>
                        Name:
                        <input className="Name" type="text" value={this.state.name} onChange={this.handleChange}/>
                    </label>
                    <input className="Button" type="submit" value="Submit"/>
                </form>
            </div>
        )
    }
}

export default withRouter(Join);