import React, { useState, useEffect } from "react";
import '../components/css/pages.css';
import info from '../imags/info.png'

function ObjectInfo() {
  const [selectedZone, setSelectedZone] = useState("");
  const [selectedObjectId, setSelectedObjectId] = useState("");
  const [nameInfo, setNameInfo] = useState([]);

  const handleZoneChange = (event) => {
    setSelectedZone(event.target.value); 
  };

  const handleObjectChange = (event) => {
    setSelectedObjectId(event.target.value);
  };

  const fetchTypeOptions = () => {
    const firstOption = { value: '', text: 'Bitte ein Element auswählen' };

    fetch(`http://localhost:8080/mobile-data-web/gettype?type=${selectedZone}`)
      .then(response => response.text())
      .then(htmlData => {
        const parser = new DOMParser();
        const doc = parser.parseFromString(htmlData, "text/html");
        const options = doc.querySelectorAll('option[value]');
        const jsonData = [];

        jsonData.push(firstOption);

        options.forEach(option => {
          jsonData.push({
            value: option.getAttribute('value'),
            text: option.textContent
          });
        });
        setNameInfo(jsonData);
      })
      .catch(error => {
        console.error("Fehler beim Abrufen der Informationen:", error);
        setNameInfo([]);
      });
  };

const fetchObjectInfo = () => {
  fetch(`http://localhost:8080/mobile-data-web/info?type=${selectedZone}&id=${selectedObjectId}`)
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.text();
    })
    .then(htmlData => {
      const newTab = window.open();
      newTab.document.write(htmlData);
    })
    .catch(error => {
      console.error("Fehler beim Abrufen der Informationen:", error);
    });
};

  const handleClick = () => {
    if (selectedZone && selectedObjectId) {
      fetchObjectInfo();
    }
  };

  useEffect(() => {
    if (selectedZone !== "") {
      fetchTypeOptions();
    }
  }, [selectedZone]);

  return (
    <div className="form">
      <h1 style={{ fontSize: "15px", marginTop: "-10px" }}>Information</h1>
      <h2 style={{ fontSize: "11px", marginTop: "-15px" }}>Informationen zu welchem Element?</h2>
      <h2 style={{ fontSize: "11px", marginTop: "-5px" }}>Typ:</h2>
      <select className="select-search" onChange={handleZoneChange}>
        <option disabled selected value="">
          Typ auswählen:
        </option>
        <option value="Rack">Schränke und Gestelle</option>
        <option value="Instance">Instanz</option>
        <option value="PortInstance">Portinstanzen</option>
        <option value="Zone">Zonen</option>
        <option value="Rack">Hierarchie anzeigen</option>
      </select>

      <h2 style={{ fontSize: "11px", marginTop: "4px" }}>Name:</h2>
      <select
        className="select-search"
        onChange={handleObjectChange}
      >
        {nameInfo.length > 0 ? (
          nameInfo.map((item, index) => (
            <option key={index} value={item.value}>
              {item.text}
            </option>
          ))
        ) : (
          <option value="">Bitte zuerst einen Typ auswählen</option>
        )}
      </select>

      <br />
      <button className="button-search" onClick={handleClick} value="Info anzeigen">
              <img src={info} alt="Logo" className="app-logo" />
              <h1 style={{ fontSize: "10px", marginTop: "-26px" }} className="suchen-text">Info anzeigen</h1>
      </button>
    </div>
  );
}

export default ObjectInfo;

