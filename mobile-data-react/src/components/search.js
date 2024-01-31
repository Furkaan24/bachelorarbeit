import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import '../components/css/pages.css';
import LoadingOverlay from './LoadingOverlay';

function Search() {
  const [selectedId, setSelectedId] = useState("");
  const [nameInfo, setNameInfo] = useState("");
  const [Url, setUrl] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();


  const handleSelect2Change = (event) => {
    setSelectedId(event.target.value);
  };

  useEffect(() => {
      handleSearchClick();
  }, []);

  const openSvg = async () => {
    setIsLoading(true);
    handleSearchClick();

    const url = `http://localhost:8080/mobile-data-web/svg?type=Rack&id=${selectedId}`;

    setUrl(url);

    try {
      await fetch(url);
      navigate(`/svg/${encodeURIComponent(url)}`, { state: { selectedId } });
    } catch (error) {
      console.error('Error fetching SVG:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSearchClick = () => {
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

  useEffect(() => {
      handleSearchClick();
    });

  return (
          <div className="form">
            <h1 style={{fontSize: "10px", marginBottom: "5px"}}>Schrank auswählen:</h1>
            <select className="select-search" onChange={handleSelect2Change}>
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

      <br />
      <button className="button-search" onClick={openSvg}>Visualisieren
      </button>
      {isLoading && <LoadingOverlay />}

    </div>
          );
}

export default Search;
