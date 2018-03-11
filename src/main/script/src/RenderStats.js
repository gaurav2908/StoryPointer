import React from 'react';
import {SortedMap} from 'collections/sorted-map';
import './style/main.css'

export default class RenderStats extends React.Component {

    render() {
        let sum = 0;
        let votesMap = new SortedMap();
        for (let i = 0, len = this.props.players.length; i < len; i++) {
            let point = this.props.players[i].point;
            if (point === null || point === undefined) {
                point = 100
            } else {
                sum += point;
            }
            if (votesMap.has(point)) {
                let votes = votesMap.get(point) + 1;
                votesMap.set(point, votes);
            } else {
                votesMap.set(point, 1);
            }
        }
        let avg = (sum / this.props.players.length).toFixed(2);

        let pointVoteTable = votesMap.keysArray().map(function (key) {
            return (
                <tr>
                    {key !== 100 ? <td>{key}</td> : <td></td>}
                    <td style={{paddingLeft: 80}}>{votesMap.get(key)}</td>
                </tr>)
        });

        return (
            <div className="Border">
                <h2 className="Margins">Stats:</h2>
                <p className="Margins">average: {avg}</p>
                <table className="Margins">
                    <tr>
                        <th>Points</th>
                        <th style={{paddingLeft: 80}}>Votes</th>
                    </tr>
                    {pointVoteTable}
                </table>
            </div>
        )
    }
}