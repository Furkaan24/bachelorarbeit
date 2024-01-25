import React, { useEffect } from "react";
import Draggable from "react-draggable";
import "./css/Setting.css";
import "./css/pages.css";

function Setting({
  isPpvVisibility,
  isInstanceTextObject,
  isInstanceTextPort,
  isFreeTextObject,
  isFreeTextPort,
  isRuleTextObject,
  isRuleTextPort,
  onPpvVisibilityChange,
  onInstanceTextObjectChange,
  onInstanceTextPortChange,
  onFreeTextObjectChange,
  onFreeTextPortChange,
  onRuleTextObjectChange,
  onRuleTextPortChange,
  onNeuVisualisieren,
}) {
  const handleCheck = (e) => {
    const checkboxName = e.target.name;
    const checkboxValue = e.target.checked;

    switch (checkboxName) {
      case "instanceTextObject":
        onInstanceTextObjectChange(checkboxValue);
        break;
      case "instanceTextPort":
        onInstanceTextPortChange(checkboxValue);
        break;
      case "freeTextObject":
        onFreeTextObjectChange(checkboxValue);
        break;
      case "freeTextPort":
        onFreeTextPortChange(checkboxValue);
        break;
      case "ruleTextObject":
        onRuleTextObjectChange(checkboxValue);
        break;
      case "ruleTextPort":
        onRuleTextPortChange(checkboxValue);
        break;
      default:
        break;
    }
  };

  const handleRadio = (e) => {
    const radioName = e.target.name;
    const radioValue = e.target.value;

    switch (radioName) {
      case "patchChoice":
        onPpvVisibilityChange(radioValue === "true");
        break;
      default:
        break;
    }
  };

  const handleNeuVisualisieren = () => {
    onNeuVisualisieren();
  };

  useEffect(() => {
    console.log("Current State Values:");
    console.log("isInstanceTextObject:", isInstanceTextObject);
    console.log("isInstanceTextPort:", isInstanceTextPort);
    console.log("isFreeTextObject:", isFreeTextObject);
    console.log("isFreeTextPort:", isFreeTextPort);
    console.log("isRuleTextObject:", isRuleTextObject);
    console.log("isRuleTextPort:", isRuleTextPort);
    console.log("isPpvVisibility:", isPpvVisibility);
  }, [isPpvVisibility, isInstanceTextObject, isInstanceTextPort, isFreeTextObject, isFreeTextPort, isRuleTextObject, isRuleTextPort]);

  return (
    <Draggable>
      <div className="draggable-dialog">
        <div className="layout">
          <h1>Beschreibung:</h1>
          <div className="title1 label" style={{ marginLeft: "-40px", marginTop: "0px" }}>
            Objekte
          </div>
          <div className="title2 label" style={{ marginLeft: "-20px", marginTop: "0px" }}>
            Ports
          </div>

          <div className="label1 label" style={{ fontSize: "11px", marginTop: "-20px" }}>
            Instanztexte:
          </div>
          <div className="checkbox1">
            <input
              type="checkbox"
              name="instanceTextObject"
              onChange={handleCheck}
              style={{ marginLeft: "-30px" }}
              checked={isInstanceTextObject}
            />
          </div>
          <div className="checkbox2">
            <input
              type="checkbox"
              name="instanceTextPort"
              onChange={handleCheck}
              style={{ marginLeft: "-15px" }}
              checked={isInstanceTextPort}
            />
          </div>

          <div className="label2 label" style={{ fontSize: "11px", marginTop: "-25px" }}>
            Freitexte:
          </div>
          <div className="checkbox3">
            <input
              type="checkbox"
              name="freeTextObject"
              onChange={handleCheck}
              style={{ marginLeft: "-30px" }}
              checked={isFreeTextObject}
            />
          </div>
          <div className="checkbox4">
            <input
              type="checkbox"
              name="freeTextPort"
              onChange={handleCheck}
              style={{ marginLeft: "-15px" }}
              checked={isFreeTextPort}
            />
          </div>

          <div className="label3 label" style={{ fontSize: "11px", marginTop: "-22px" }}>
            Regelbeschriftung:
          </div>
          <div className="checkbox5">
            <input
              type="checkbox"
              name="ruleTextObject"
              onChange={handleCheck}
              style={{ marginLeft: "-30px" }}
              checked={isRuleTextObject}
            />
          </div>
          <div className="checkbox6">
            <input
              type="checkbox"
              name="ruleTextPort"
              onChange={handleCheck}
              style={{ marginLeft: "-15px" }}
              checked={isRuleTextPort}
            />
          </div>
        </div>
        <div className="radio-layout" style={{ marginTop: "-20px" }}>
          <h1>Patches:</h1>
          <div className="leer"></div>

          <div className="radio-klein" style={{ marginTop: "-20px" }}>
            Keine:
          </div>
          <div className="radio-input1">
            <input type="radio" name="patchChoice" value="false" onChange={handleRadio} style={{ marginTop: "-40px" }} checked={!isPpvVisibility} />
          </div>

          <div className="radio-AmMauszeiger" style={{ marginTop: "-30px" }}>
            Am Mauszeiger:
          </div>
          <div className="radio-input2">
            <input type="radio" name="patchChoice" value="AmMauszeiger" onChange={handleRadio} />
          </div>

          <div className="radio-Alle" style={{ marginTop: "-18px" }}>
            Alle:
          </div>
          <div className="radio-input3">
            <input type="radio" name="patchChoice" value="true" onChange={handleRadio} checked={isPpvVisibility} />
          </div>

          <button onClick={handleNeuVisualisieren}>Neu Visualisieren</button>
        </div>
      </div>
    </Draggable>
  );
}

export default Setting;
