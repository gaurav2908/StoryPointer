import React from 'react';
import RenderPlayers from './RenderPlayers';
import RenderPoints from './RenderPoints';
import RenderStats from './RenderStats';
import './style/main.css';
import AutosizeInput from 'react-input-autosize';
import Header from './Header';

export default class Game extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            players: '',
            desc: '',
            showVotes: false
        };
        this.handleChangeDesc = this.handleChangeDesc.bind(this);
        this.handleSubmitDesc = this.handleSubmitDesc.bind(this);
        this.handleClearVotes = this.handleClearVotes.bind(this);
        this.handleClearDesc = this.handleClearDesc.bind(this);
        this.handleShowVotes = this.handleShowVotes.bind(this);
        this.handlePointClick = this.handlePointClick.bind(this);
        this.componentDidMount = this.componentDidMount.bind(this);
        this.checkUpdate = this.checkUpdate.bind(this);
        this.loadPlayers = this.loadPlayers.bind(this);
        this.sleep = this.sleep.bind(this);
    }

    componentDidMount() {
        this.loadPlayers()
    }

    loadPlayers() {
        let url = window.location.origin + '/session/players';
        fetch(url, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'same-origin'
        })
            .then(d => d.json())
            .then(d => {
                this.setState({
                    players: d
                });
                this.checkUpdate();
            })
    }

    sleep(time) {
        return new Promise((resolve) => {
            return setTimeout(resolve, time);
        });
    }

    getCookie(cname) {
        let name = cname + "=";
        let decodedCookie = decodeURIComponent(document.cookie).trim();
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

    checkUpdate() {
        let url = window.location.origin + '/session/update-game';
        fetch(url, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                desc: this.state.desc,
                showVotes: this.state.showVotes,
                players: this.state.players
            }),
            credentials: 'same-origin'
        })
            .then(d => {
                if (d.status !== 200) {
                    this.sleep(5000).then(this.checkUpdate);
                } else {
                    d.json().then(data => {
                        this.setState({
                            desc: data.desc,
                            showVotes: data.showVotes,
                            players: data.players

                        }, this.checkUpdate);
                    });
                }
            });
    }


    handleSubmitDesc(event) {
        let url = window.location.origin + '/session/update/desc';
        event.preventDefault();
        fetch(url, {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                desc: this.state.desc
            }),
            credentials: 'same-origin'
        });
    }

    handleClearVotes() {
        let url = window.location.origin + '/session/clear-points';
        fetch(url, {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            credentials: 'same-origin'
        })
            .then(d => d.json())
            .then(d => {
                this.setState({
                    players: d,
                    showVotes: false
                })
            })
    }

    handleClearDesc() {
        let url = window.location.origin + '/session/clear-desc';
        fetch(url, {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            credentials: 'same-origin'
        })
            .then(d => d.json())
            .then(d => {
                this.setState({
                    desc: ''
                })
            })
    }

    handleShowVotes() {
        let url = window.location.origin + '/session/update-showvotes';
        fetch(url, {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                showVotes: true
            }),
            credentials: 'same-origin'
        })
            .then(d => {
                d.json().then(data => {
                    this.setState({
                        desc: data.desc,
                        showVotes: data.showVotes,
                        players: data.players

                    });
                });
            });
    }

    handlePointClick(players) {
        this.setState({
            players: players
        })
    }

    handleChangeDesc(event) {
        this.setState({desc: event.target.value});
    }

    render() {
        let renderPlayers = null;
        if (this.state.players.length > 0) {
            renderPlayers = <RenderPlayers players={this.state.players}/>
        }
        let displayVotes = null;
        if (this.state.showVotes && this.state.players.length > 0) {
            displayVotes = <RenderStats players={this.state.players}/>
        }
        return (
            <div>
                {<Header/>}
                <div className="Margins">
                    <div className="Left">session id: {this.props.location.state.sessionId}</div>
                    <div>
                        <button className="Button Right" onClick={this.loadPlayers}>Reconnect</button>
                    </div>
                </div>
                <div className="Margins">Name: {this.props.location.state.name}</div>
                <div className="Margins">
                    Description:
                    <AutosizeInput
                        style={{marginLeft: 10}}
                        inputStyle={{fontSize: 15}}
                        minWidth={500}
                        autoFocus
                        value={this.state.desc}
                        onChange={this.handleChangeDesc}
                    />
                    {
                        <button className="Button" onClick={this.handleSubmitDesc}>Submit</button>
                    }
                </div>
                {
                    <div className="BottomMargin">
                        <button className="Button" onClick={this.handleShowVotes}>Show votes</button>
                        <button className="Button" onClick={this.handleClearVotes}>Clear votes</button>
                        <button className="Button" onClick={this.handleClearDesc}>Clear description</button>
                    </div>
                }
                <div className="BottomMargin">
                    <RenderPoints onPointClick={this.handlePointClick}/>
                </div>
                <div className="BottomMargin">
                    <div style={{width: 500, float: 'left'}}> {renderPlayers} </div>
                    <div style={{marginLeft: 520, width: 200}}> {displayVotes} </div>
                </div>
            </div>
        )
    }
}

