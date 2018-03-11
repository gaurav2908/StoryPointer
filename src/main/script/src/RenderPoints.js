import React from 'react';
import './style/main.css'

export default class RenderPoints extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
        let url = window.location.origin + '/session/update/point/' + event.target.value;
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
                this.props.onPointClick(d);
            })
    }

    render() {
        return (
            <div>
                <button className="Button" value='0.5' onClick={this.handleSubmit}>0.5</button>
                <button className="Button" value='1' onClick={this.handleSubmit}>1</button>
                <button className="Button" value='2' onClick={this.handleSubmit}>2</button>
                <button className="Button" value='3' onClick={this.handleSubmit}>3</button>
                <button className="Button" value='5' onClick={this.handleSubmit}>5</button>
                <button className="Button" value='8' onClick={this.handleSubmit}>8</button>
                <button className="Button" value='13' onClick={this.handleSubmit}>13</button>
            </div>
        )
    }
}