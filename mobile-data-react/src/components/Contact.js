import React, { Component } from "react";
import '../components/css/pages.css';
import logoGfai from '../imags/gfai.png';

export default class Contact extends Component{
    render(){
        return(

            <div className="contact-container">

                <img src={logoGfai} alt="Logo" className="gfai-logo"/>
                <h1 style={{fontSize:"15px"}}>GFaI Gesellschaft zur Förderung</h1>
                <h1 style={{fontSize:"15px"}}>angewandter Informatik e. V.</h1> <br /> <br />
                <p style={{fontSize:"11px", marginTop:"-40px"}}>Volmerstraße 3</p>
                <p style={{fontSize:"11px"}}>D-12489 Berlin</p> <br />
                <p style={{fontSize:"11px"}}>Telefon: +49 30 814563-300 </p>
                <p style={{fontSize:"11px"}}>Fax: +49 30 814563-302 </p>
                <p style={{fontSize:"11px"}}>eMail: sekretariat@gfai.de</p>
                <p style={{fontSize: "11px",color:"black"}}>
                    Webseite: <a href="https://www.gfai.de" target="_blank" rel="noopener noreferrer"
                    style={{color:"black", marginLeft:"1px"}}>.gfai.de</a>
                </p>

            </div>

        )
    }
}