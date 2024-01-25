import React, { useState, useEffect } from "react";
import Draggable from "react-draggable";
import '../components/css/pages.css';

function SignalPath() {
  const [showTable, setShowTable] = useState(false);
  const [signalData, setSignalData] = useState([]);
  const [searchId, setSearchId] = useState('');
  const [typeOptions, setTypeOptions] = useState([]);
  const [portName, setPortName] = useState('');
  const [showDropdown, setShowDropdown] = useState(false);

  const fetchData = async () => {
    try {
      const response = await fetch(`http://localhost:8080/mobile-data-web/signalpath?type=PortInstance&id=${searchId}`);
      const data = await response.json();
      setSignalData(data);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const fetchPortOptions = async () => {
    const firstOption = { value: '', text: 'Bitte ein Element auswählen' };
    fetch(`http://localhost:8080/mobile-data-web/gettype?type=PortInstance&portname=${portName}`)
      .then(response => response.text())
      .then(htmlData => {
        const parser = new DOMParser();
        const doc = parser.parseFromString(htmlData, "text/html");
        const options = doc.querySelectorAll('option[value]');
        const jsonData = [];

        jsonData.push(firstOption);

        options.forEach(option => {
          const value = option.getAttribute('value');
          const text = option.textContent;
          jsonData.push({ value, text });
        });

        setTypeOptions(jsonData);
      })
      .catch(error => {
        console.error("Fehler beim Abrufen der Informationen:", error);
      });
  };

  const handleButtonClick = () => {
    setShowDropdown(true);
    setShowTable(true);
    fetchData();
    fetchPortOptions();
  };

  const handleDropdownChange = (e) => {
    setSearchId(e.target.value);
  };

  useEffect(() => {
    fetchData();
  }, [searchId, portName]);

  return (
    <Draggable cancel=".input-search, .btn, #searchDropdown">
      <div className="draggable-dialog">
        <div className="draggable-header" style={{ backgroundColor: '#ecf0f1' }}>
          <label htmlFor="portName">Port Name:</label>
          <input
            id="portName"
            className="input-search"
            type="text"
            placeholder="Port-Namen eingeben"
            value={portName}
            onChange={(e) => setPortName(e.target.value)}
          />
          <button className="btn" onClick={handleButtonClick} style={{ fontSize: '10px', width: '121px', marginTop: '-20px' }}>
            Suche
          </button>
        </div>
        {showDropdown && (
          <div style={{ backgroundColor: 'transparent' }}>
            <select
              id="searchDropdown"
              onChange={handleDropdownChange}
              value={searchId}
              style={{ backgroundColor: 'transparent' }}
            >
              {typeOptions.map((option, index) => (
                <option key={index} value={option.value}>
                  {option.text}
                </option>
              ))}
            </select>
          </div>
        )}
        {showTable && (
          <table className="result-tabel">
            <thead>
              <tr style={{ fontSize: '10px' }}>
                <th>Signalweg</th>
                <th>Signalwegtyp</th>
                <th>Symbol</th>
              </tr>
            </thead>
            <tbody style={{ fontSize: '10px' }}>
              {signalData.map((row, index) => (
                <tr key={index}>
                  <td>{row.name}</td>
                  <td>{row.Signalwegtyp}</td>
                  <td>{row.Symbol}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </Draggable>
  );
}

export default SignalPath;
