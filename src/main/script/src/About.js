import React from 'react';
import Header from './Header'

export default class About extends React.Component {
    render() {
        return (
            <div>
                {<Header/>}
                <div style={{width: 800, marginLeft: 10, fontWeight: 200, textAlign: "justify"}}>
                    <p>
                        Welcome,
                    </p>
                    <p>
                        Story Pointer is a consensus-based, gamified technique for estimating, mostly used to estimate effort or relative size of development goals in software development.
                    </p>
                    <p>
                        The reason to use this is to avoid the influence of the other participants. If a number is spoken, it can sound like a suggestion and influence the other participants' sizing. Planning poker should force people to think independently and propose their numbers simultaneously.
                    </p>
                    <p>
                        Feel free to use it and provide feedback.
                    </p>
                    <p style={{textAlign: "right"}}>
                        Thanks, <br/>
                        Gaurav Sharma <br/>
                        gaurav2908@gmail.com
                    </p>
                </div>
            </div>
        )
    }
}