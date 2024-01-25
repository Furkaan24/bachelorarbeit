import React, { useState } from 'react';
import { Link } from "react-router-dom";
import setting from '../imags/setting.png';
import contact from '../imags/contact.png';
import Patching from '../imags/patch_dialog.png';
import qrCode from '../imags/qr-code.png';
import out from '../imags/system-log-out-2.png';
import signalpath from '../imags/signalpath-virt.png';
import objectInfo from '../imags/info.png';
import search from '../imags/search.png'
import './css/pages.css'

function Sidebar({onSettingClick, onSignalpathClick, onPatchClick, onInfoClick, isInfoActive }) {

  const [infoActive, setInfoActive] = useState(false);

  const handleInfoClick = () => {
    onInfoClick(!isInfoActive);
  };

  return (
    <div className="sidebar">
      <Link to="/search">
        <div className="icon-container" >
          <img src={search} alt="Logo" className="app-logo"/>
          <span className="icon-tooltip" >Schrankauswahl</span>
        </div>
      </Link>

      <div
        className="icon-container"
        onClick={() => {
          onSettingClick();
        }}
      >
        <img
          src={setting}
          alt="Logo"
          className="app-logo"
          style={{ marginBottom: "32px" }}
        />
        <span className="icon-tooltip">Einstellung</span>
      </div>

      <div className="icon-container"
        onClick={() => {
                          onPatchClick();
                        }}>
          <img src={Patching} alt="Logo" className="app-logo" style={{ marginBottom: "32px" }}/>
          <span className="icon-tooltip">Patchen</span>
        </div>

        <div className={`icon-container ${isInfoActive ? 'active' : ''}`} onClick={handleInfoClick}>
        <img src={objectInfo} alt="Logo" className="app-logo"/>
        <span className="icon-tooltip">Objekt information</span>
      </div>

      <Link to="/search">
        <div className="icon-container">
          <img src={out} alt="Logo" className="app-logo"/>

          <span className="icon-tooltip">Schließen</span>
        </div>
      </Link>

      <Link to="/qrcode">
        <div className="icon-container">
          <img src={qrCode} alt="Logo" className="app-logo"/>
          <span className="icon-tooltip">QR Code</span>
        </div>
      </Link>

      <div
        className="icon-container"
        onClick={() => {
                          onSignalpathClick();
                        }}
        style={{marginLeft: "12px", margin: "2px" }}
        >
        <img
          src={signalpath}
          alt="Logo"
          className="app-logo"
          style={{ margin: "2px" }}
          />
        <span className="icon-tooltip">Signalweg</span>
      </div>

      <Link to="/contact">
        <div className="icon-container">
          <img src={contact} alt="Logo" className="app-logo"/>
          <span className="icon-tooltip">Kontakt</span>
        </div>
      </Link>
    </div>
  );
}
export default Sidebar;