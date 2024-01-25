import React from "react";
import "./css/Setting.css"

function Setting() {
    const handleCheck = (e) => {

        console.log(e.target.checked)
    }
    const handleRadio = (e) => {
        console.log(e.target.value)
    }

    return (
        <div>
            <h1>Beschreibung</h1>
            <h5>Objekte</h5>
            <h5>Ports</h5>

            <h2>Instanztexte</h2>
            <input className="checkbox1" type="checkbox" name="Objekte" onChange={handleCheck}></input>
            <input className="checkbox1"  type="checkbox" name="Objekte" onChange={handleCheck}></input>

            <h2>Freitexte</h2>
            <input className="checkbox3" type="checkbox" name="Objekte" onChange={handleCheck}></input>
            <input className="checkbox4"  type="checkbox" name="Objekte" onChange={handleCheck}></input>

            <h2>Regelbeschriftung</h2>
            <input className="checkbox3" type="checkbox" name="Objekte" onChange={handleCheck}></input>
            <input className="checkbox4"  type="checkbox" name="Objekte" onChange={handleCheck}></input>


            <h1>Patches</h1>
            <h2>Keine:</h2>
            <input className="radio-input1" type="radio" name="patchChoice" value="Keine" onChange={handleRadio}></input>

            <h2>Am Mauszeiger:</h2>
            <input className="radio-input1" type="radio" name="patchChoice" value="Keine" onChange={handleRadio}></input>

            <h2>Alle:</h2>
            <input className="radio-input1" type="radio" name="patchChoice" value="Keine" onChange={handleRadio}></input>
        </div>
    );
}

export default Setting;