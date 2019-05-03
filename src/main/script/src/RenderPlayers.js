import React from 'react';
import './style/main.css'
import Check from 'react-icons/lib/fa/check';
// import Remove from 'react-icons/lib/fa/minus-circle';
import Remove from 'react-icons/lib/io/ios-minus-outline';

export default class RenderPlayers extends React.Component {

    handleRemovePlayer(playerId) {
        let url = window.location.origin + '/session/remove-player/' + playerId;
        fetch(url, {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            credentials: 'same-origin'
        });
    }

    render() {
        let playerPointTable = this.props.players.map(function (player) {
            return (
                <tr>
                    <td style={{width: 20}}>
                        {player.voted && <Check/>}
                    </td>
                    <td style={{paddingLeft: 10}}>{player.name}</td>
                    <td style={{paddingLeft: 80}}>{player.point}</td>
                    <td>
                        {
                            // this.props.host &&
                            <div>
                                <div style={{width: 40}}>
                                    <div style={{float: 'left'}}>
                                    <span title="Remove Player" onClick={() => this.handleRemovePlayer(player.playerId)}>
                                    <Remove/>
                                    </span>
                                    </div>
                                </div>
                            </div>
                        }
                    </td>
                </tr>)
        }, this);
        return (
            <div>
                <table style={{marginLeft: 10}}>
                    <tr>
                        <th style={{width: 20}}></th>
                        <th style={{paddingLeft: 10}}>Player</th>
                        <th style={{paddingLeft: 80}}>Point</th>
                        <th style={{width: 20}}></th>
                    </tr>
                    {playerPointTable}
                </table>
            </div>)
    }
}