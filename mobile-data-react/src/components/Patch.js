import React, { useState, useEffect } from "react";
import './css/patch.css'
import mouse from '../imags/mouse-pointer-2.png';
import patch from '../imags/patch.png'
import cable from '../imags/cable.png'
import Draggable from "react-draggable";

function Patch() {
  const [productList, setProductList] = useState([]);
  const [poiid1, setPoiid1] = useState('');
  const [poiid2, setPoiid2] = useState('');
  const [cablelength, setCablelength] = useState('');
  const [signalData1, setSignalData1] = useState([]);
  const [signalData2, setSignalData2] = useState([]);
  const [endknoten1Value, setEndknoten1Value] = useState("");
  const [endknoten2Value, setEndknoten2Value] = useState("");
  const [typeOptions, setTypeOptions] = useState([]);
  const [typeOptions2, setTypeOptions2] = useState([]);
  const [portName, setPortName] = useState('');
  const [portName2, setPortName2] = useState('');
  const [cableLength, setCableLength] = useState('');
  const [selectedCableId, setSelectedCableId] = useState(null);
  const [cableName, setCableName] = useState('');
  const [portId, setPortId] = useState(null);
  const [kkList, setKkList] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (poiid1 && poiid2) {
          const responseCableLength = await fetch(`http://localhost:8080/mobile-data-web/importcable?poiid1=${poiid1}&poiid2=${poiid2}`);
          const cableLengthData = await responseCableLength.json();
          const calculatedCableLength = cableLengthData.cablelength;
          setCableLength(calculatedCableLength);

          const responseCableData = await fetch(`http://localhost:8080/mobile-data-web/importcable?poiid1=${poiid1}&poiid2=${poiid2}&cablelength=${calculatedCableLength}`);
          const cableData = await responseCableData.json();

          const kkList = JSON.parse(cableData.kkList || '[]');
          const updatedProductList = kkList.map((product, index) => product.classNames);
          setKkList(kkList);

          if (JSON.stringify(updatedProductList) !== JSON.stringify(productList)) {
            setProductList(updatedProductList);
          }
        }
        if (poiid1) {
          const response1 = await fetch(`http://localhost:8080/mobile-data-web/signalpath?type=PortInstance&id=${poiid1}`);
          const data1 = await response1.json();
          setSignalData1(data1);
        }

        if (poiid2) {
          const response2 = await fetch(`http://localhost:8080/mobile-data-web/signalpath?type=PortInstance&id=${poiid2}`);
          const data2 = await response2.json();
          setSignalData2(data2);
        }
        fetchPortOptions();
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [poiid1, poiid2, cablelength, productList]);

  useEffect(() => {
    fetchPortOptions();
  }, [portName, portName2, productList]);

  const handleDropdownChange = (e, signalType) => {
    const selectedValue = e.target.value;
    if (signalType === '1') {
      setPoiid1(selectedValue);
    } else if (signalType === '2') {
      setPoiid2(selectedValue);
    }
  };

  const handleCableChange = (e) => {
    const selectedCableName = e.target.value;
    const selectedCable = kkList.find(product => product.classNames === selectedCableName);

    if (selectedCable) {
      setSelectedCableId(selectedCable.id);
      console.log("selected cable:", selectedCable.id);
    }
  };

  const handleCableNameChange = (e) => {
    setCableName(e.target.value);
    console.log("cablename", cableName);
  };


  const handleMouseClick = () => {
    console.log("Bild Mausanzeiger wurde angeklickt. Port ID:", portId);
  };

  const fetchPortOptions = async () => {
    if (portName.trim().length > 0) {
      const firstOption = {value: '', text: 'Bitte ein Element auswählen'};
      try {
        const response = await fetch(`http://localhost:8080/mobile-data-web/gettype?type=PortInstance&portname=${portName}`);
        const htmlData = await response.text();
        const parser = new DOMParser();
        const doc = parser.parseFromString(htmlData, "text/html");
        const options = doc.querySelectorAll('option[value]');
        const jsonData = [firstOption];

        options.forEach(option => {
          const value = option.getAttribute('value');
          const text = option.textContent;
          jsonData.push({value, text});
        });

        setTypeOptions(jsonData);
      } catch (error) {
        console.error("Fehler beim Abrufen der Informationen:", error);
      }
    }
    if (portName2.trim().length > 0) {
      const firstOption = {value: '', text: 'Bitte ein Element auswählen'};
      try {
        const response = await fetch(`http://localhost:8080/mobile-data-web/gettype?type=PortInstance&portname=${portName2}`);
        const htmlData = await response.text();
        const parser = new DOMParser();
        const doc = parser.parseFromString(htmlData, "text/html");
        const options = doc.querySelectorAll('option[value]');
        const jsonData = [firstOption];

        options.forEach(option => {
          const value = option.getAttribute('value');
          const text = option.textContent;
          jsonData.push({value, text});
        });

        setTypeOptions2(jsonData);
      } catch (error) {
        console.error("Fehler beim Abrufen der Informationen:", error);
      }
    } else {
      console.log('Please enter a Port Name before fetching data.');
    }
  };

  const handlePatchenClick = async () => {
  try {
    if (poiid1 && poiid2 && selectedCableId) {
      const response = await fetch(`http://localhost:8080/mobile-data-web/importcable?poiid1=${poiid1}&poiid2=${poiid2}&ktidcable=${selectedCableId}&cablename=${cableName}`, {
        method: 'POST',
      });

      const result = await response.json();
      console.log('Patchen response:', result);
      window.location.reload();
    }
  } catch (error) {
    console.error('Error while patching:', error);
  }
};

  return (
          <Draggable cancel=".produkte-row, .ports-row, .signalWeg-row, .endknoten-row, .name">
            <div className="draggable-dialog">
              <div style={{backgroundColor: "#ecf0f1"}}>
                <h1 className="patchseiten" style={{marginTop: "-13px", fontSize: "11px", marginBottom: "-26.5px", fontFamily: "Verdana, Geneva, Tahoma, sans-serif"}}>Patchseiten</h1>

                <div className="ports-row">
                  <div className="port">
                    <h3 style={{marginTop: "-8px", fontSize: "8px"}}>Port 1</h3>
                    <img
                      src={mouse}
                      alt="Logo"
                      className="app-logo"
                      style={{width: '15px', height: 'auto', marginTop: "-22px", marginLeft: "45px"}}
                      onClick={handleMouseClick}
                      />
                    <input
                      style={{fontSize: "11px", width: "98px", height: "10px"}}
                      value={portName}
                      onChange={(e) => setPortName(e.target.value)}
                      ></input>
                    <select
                      style={{fontSize: "11px", width: "108px", height: "22px", marginTop: "5px"}}
                      onChange={(e) => handleDropdownChange(e, '1')}
                      >
                      {typeOptions.map((option) => (
                        <option key={option.value} value={option.value}>{option.text}</option>
                                  ))}
                    </select>
                  </div>

                  <div className="port">
                    <h3 style={{marginTop: "-8px", fontSize: "8px", marginLeft: "-7px"}}>Port 2</h3>
                    <img src={mouse} alt="Logo" className="app-logo" style={{width: '15px', height: 'auto', marginTop: "-23px", marginLeft: "50px", marginBottom: "1px"}} onClick={() => {
                        console.log("Bild Mausanzeiger 2 wurde angeklickt")
                           }} />
                    <input
                      style={{fontSize: "11px", width: "98px", marginLeft: "-7px", height: "10px"}}
                      value={portName2}
                      onChange={(e) => setPortName2(e.target.value)}
                      ></input>
                    <select
                      style={{fontSize: "11px", width: "108px", height: "22px", marginTop: "5px", marginLeft: "-7px"}}
                      onChange={(e) => handleDropdownChange(e, '2')}
                      >
                      {typeOptions2.map((option) => (
                        <option key={option.value} value={option.value}>{option.text}</option>
                                  ))}
                    </select>
                  </div>
                </div>

                <div className="signalWeg-row">
                  <div className="signalWeg">
                    <div className="signalWeg-header">
                      <h3 style={{marginTop: "-50px", marginLeft: "-15px", fontSize: "8px"}}>Signalweg 1</h3>
                      <input type="radio" name="patchChoice" value="AmMauszeiger" style={{marginTop: "-69px", marginLeft: "-15px"}} />
                    </div>
                    <select
                      style={{backgroundColor: "white", fontSize: "11px", width: "108px", marginTop: "-40px", height: "22px"}}
                      onChange={(e) => handleDropdownChange(e, '1')}
                      >
                      <option></option>
                      {signalData1.map((item) => (
                        <option key={item.id}>{item.name}</option>
                                  ))}
                    </select>
                  </div>

                  <div className="signalWeg">
                    <div className="signalWeg-header">
                      <h3 style={{marginTop: "-50px", fontSize: "8px", marginLeft: "-15px"}}>Signalweg 2</h3>
                      <input type="radio" name="patchChoice" value="AmMauszeiger" style={{marginTop: "-69px", marginLeft: "-15px"}} />
                    </div>
                    <select
                      style={{backgroundColor: "white", fontSize: "11px", width: "108px", marginTop: "-40px", height: "22px"}}
                      onChange={(e) => handleDropdownChange(e, '2')}
                      >
                      <option></option>
                      {signalData2.map((item) => (
                        <option key={item.id}>{item.name}</option>
                                  ))}
                    </select>
                  </div>
                </div>
                <div className="endknoten-row">
                  <div className="Endknoten">
                    <h3 style={{marginTop: "-80px", fontSize: "9px"}}>Endknoten 1</h3>
                    <input style={{fontSize: "11px", width: "98px", marginTop: "-8px", height: "10px"}}
                           value={endknoten1Value}
                           readOnly></input>
                  </div>

                  <div className="Endknoten">
                    <h3 style={{marginTop: "-80px", fontSize: "9px"}}>Endknoten 2</h3>
                    <input style={{fontSize: "11px", width: "98px", marginTop: "-8px", height: "10px"}}
                           value={endknoten2Value}
                           readOnly></input>
                  </div>
                </div>

              </div>

              <div style={{backgroundColor: "#ecf0f1"}}>
                <h1 style={{marginTop: "-115px", fontSize: "11px", fontFamily: "Verdana, Geneva, Tahoma, sans-serif"}} className="kabel">Kabel</h1>
                <h1 style={{fontSize: "8px", marginBottom: "-3px", marginLeft: "30px"}}>Produkte</h1>
                <img src={cable} alt="Logo" className="app-logo" style={{width: '17px', height: 'auto', marginBottom: "-10px", marginLeft: "120px"}} />

                <div className="produkte-row" >
                  <div className="produkte" >
                    <select
                      style={{
                          fontSize: "11px",
                          marginTop: "-52px",
                          marginLeft: "0px",
                          height: "22px",
                          width: "115px",
                          backgroundColor: "white",
                          borderRadius: "4px",
                        }}
                      onChange={(e) => {
                          handleDropdownChange(e);
                          handleCableChange(e); // Call the new function on cable change
                        }}
                      >
                      <option></option>
                      {(Array.isArray(productList) ? productList : []).map((product, index) => (
                        <option key={index}>{product}</option>
                                  ))}
                    </select>

                  </div>
                  <div className="produkte" >
                    <input style={{fontSize: "11px", width: "80px", marginLeft: "-1px", marginTop: "-52px", height: "12px"}}
                           value={cableLength}
                           readOnly
                           ></input>
                    <div className="" style={{backgroundColor: "#ecf0f1", fontSize: "8px", paddingBottom: "0", marginLeft: "-9px", marginTop: "-45px"}}>Länge in m</div>
                  </div>
                </div>


                <div className="name">
                  <h3>Name</h3>
                <input value={cableName} onChange={handleCableNameChange} />
                </div>

                <button
                  className="button-search" style={{marginLeft: "90px", marginTop: "-30px"}} onClick={() => {
                                  handlePatchenClick();
                                }}>
                  <img src={patch} alt="Logo" className="app-logo" style={{width: '25px', height: 'auto', marginBottom: "5px", marginRight: "5px", marginLeft: "-40px"}} />
                  <h1 className="suchen-text" style={{fontSize: "10px", marginTop: "-26px"}}>Patchen</h1>
                </button>
              </div>

            </div>
          </Draggable>
          )
}
export default Patch;