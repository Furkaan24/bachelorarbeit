import React, { useEffect, useState } from "react";
import './css/qrCode.css';
import './css/pages.css';
import qrcode from '../imags/qr-code.png'

function QrCode() {
    const [qrSize, setQrSize] = useState(200);
    const [selectedType, setSelectedType] = useState("");
    const [selectedId, setSelectedId] = useState("");
    const [qrCodes, setQrCodes] = useState([]);

    const handleQrSizeChange = (event) => {
        setQrSize(event.target.value);
    };

    const handleTypeChange = (event) => {
        setSelectedType(event.target.value);
        setSelectedId(event.target.value);
    };

    const [nameInfo, setNameInfo] = useState("");

    const fetchData = (url) => {
      const firstOption = { value: '', text: 'Bitte ein Element auswählen' };
    fetch(`http://localhost:8080/mobile-data-web/gettype?type=Rack`)
      .then(response => response.text())
      .then(htmlData => {
        const parser = new DOMParser();
        const doc = parser.parseFromString(htmlData, "text/html");
        const optgroups = doc.querySelectorAll('optgroup');
        const jsonData = [];

        jsonData.push(firstOption);

        optgroups.forEach(optgroup => {
          const optgroupLabel = optgroup.getAttribute('label');
          const options = optgroup.querySelectorAll('option');

          jsonData.push({ value: optgroupLabel, text: optgroupLabel, isOptgroup: true, disabled: true });

          options.forEach(option => {
            const value = option.getAttribute('value');
            const text = option.textContent;
            jsonData.push({ value, text, optgroupLabel });
          });
        });

        if (jsonData.length > 0) {
          setNameInfo(JSON.stringify(jsonData));
        } else {
          setNameInfo("Keine Informationen gefunden");
        }
      })
      .catch(error => {
        console.error("Fehler beim Abrufen der Informationen:", error);
        setNameInfo("Fehler beim Abrufen der Informationen");
      });
  };

    const fetchDataForImage = (url) => {
        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.blob();
            })
            .then(imageBlob => {
                const newTab = window.open();
                const imageUrl = URL.createObjectURL(imageBlob);
                newTab.document.write(`<html><body><img src="${imageUrl}" alt="QR Code" /></body></html>`);
                newTab.document.close();
            })
            .catch(error => {
                console.error('Error fetching image data:', error);
            });
    };

    const handleClick = () => {
        if (selectedType) {
            const secondApiUrl = `http://localhost:8080/mobile-data-web/qrcode/?type=KTI&id=${selectedId}&qrsize=${qrSize}`;
            fetchDataForImage(secondApiUrl);
        }
    };

    useEffect(() => {
        fetchData(`http://localhost:8080/mobile-data-web/gettype?type=Rack`);
    }, []);

  return (
        <div className="qr-code-container">
            <div className="form">
                <h1 style={{ fontSize: "10px", marginBottom: "5px" }}>Für welche Schränke soll ein QR-Code angezeigt werden?</h1>

                <select className="select-search" onChange={handleTypeChange}>
              {nameInfo ? (
                                  JSON.parse(nameInfo).map((item, index) => (
                                      <option
                                                  key={index}
                                                  value={item.value}
                                                  data-optgroup={item.optgroupLabel}
                                                  data-highlight={!item.isOptgroup && !item.disabled}
                                                >
                                                  {item.text}
                                                </option>
                      ))
                    ) : (
                      <option value="">Bitte zuerst einen Typ auswählen</option>
                    )}
      </select>
                <input className="input-fenester" placeholder=" Or type to search..."></input>

                <label style={{ fontSize: "10px" }}>
                    Wie groß sollen die QR-Codes sein [px]?
                    <input
                        style={{ width: "18%", marginLeft: "2px", height: "0px", fontSize: "10px", marginBottom: "-7px" }}
                        className="input-px"
                        type="number"
                        name="qrsize"
                        id="qrsize"
                        value={qrSize}
                        min="0"
                        max="2000"
                        step="10"
                        onChange={handleQrSizeChange}
                    />
                </label>
                <button className="button" onClick={handleClick} style={{marginLeft: "110px"}}>
                  <span>QR-Code generieren</span>
                </button>


            </div>
            <div className="qr-codes">
                {qrCodes.map((code, index) => (
                    <img key={index} src={code} alt={`QR Code ${index + 1}`} />
                ))}
            </div>

        </div>
    );
}

export default QrCode;
