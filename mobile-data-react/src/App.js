import React, { useState, useEffect } from 'react';
import './App.css';
import { Route, Routes, useNavigate, useLocation, BrowserRouter } from 'react-router-dom';
import Setting from './components/Setting';
import Contact from './components/Contact';
import Search from './components/search';
import Patch from './components/Patch';
import Header from './components/Header';
import Sidebar from './components/Sidebar';
import QrCode from './components/QrCode';
import Out from './components/out';
import SignalPath from './components/signalpath';
import ObjectInfo from './components/ObjectInfo';
import '../src/components/css/pages.css';
import Login from './components/Login';
import SvgComponent from './components/SvgComponent';

function App() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [isSettingOpen, setIsSettingOpen] = useState(false);
  const [isSignalpathOpen, setIsSignalpathOpen] = useState(false);
  const [isPatchOpen, setIsPatchOpen] = useState(false);
  const [svgContent, setSvgContent] = useState('');
  const [isPpvVisibility, setIsPpvVisibility] = useState(true);
  const [isInstanceTextObject, setIsInstanceTextObject] = useState(true);
  const [isInstanceTextPort, setIsInstanceTextPort] = useState(true);
  const [isFreeTextObject, setIsFreeTextObject] = useState(true);
  const [isFreeTextPort, setIsFreeTextPort] = useState(true);
  const [isRuleTextObject, setIsRuleTextObject] = useState(true);
  const [isRuleTextPort, setIsRuleTextPort] = useState(true);
  const [isInfoActive, setIsInfoActive] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();
  const selectedId = (location && location.state) ? location.state.selectedId : '';

  const handleSvgContent = (content) => {
    setSvgContent(content);
  };

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  const toggleSetting = () => {
    setIsSettingOpen((prevIsSettingOpen) => !prevIsSettingOpen);
  };

  const toggleSignalpath = () => {
    setIsSignalpathOpen((prevIsSignalpathOpen) => !prevIsSignalpathOpen);
  };

  const togglePatch = () => {
    setIsPatchOpen((prevIsPatchOpen) => !prevIsPatchOpen);
  };

  const closeSetting = () => {
    setIsSettingOpen(false);
  };

  const closeSignalpath = () => {
    setIsSignalpathOpen(false);
  };

  const closePatch = () => {
    setIsPatchOpen(false);
  };

  const handlePpvVisibilityChange = (value) => {
    setIsPpvVisibility(value);
  };

  const handleInstanceTextObjectChange = (value) => {
    setIsInstanceTextObject(value);
  };

  const handleInstanceTextPortChange = (value) => {
    setIsInstanceTextPort(value);
  };

  const handleFreeTextObjectChange = (value) => {
    setIsFreeTextObject(value);
  };

  const handleFreeTextPortChange = (value) => {
    setIsFreeTextPort(value);
  };

  const handleRuleTextObjectChange = (value) => {
    setIsRuleTextObject(value);
  };

  const handleRuleTextPortChange = (value) => {
    setIsRuleTextPort(value);
  };

  const openSvg = () => {
    const url = `http://localhost:8080/mobile-data-web/svg?type=Rack&id=${selectedId}&ppvvisibility=${isPpvVisibility}&instancetextobject=${isInstanceTextObject}&instancetextport=${isInstanceTextPort}&freetextobject=${isFreeTextObject}&freetextport=${isFreeTextPort}&ruletextobject=${isRuleTextObject}&ruletextport=${isRuleTextPort}`;
    navigate(`/svg/${encodeURIComponent(url)}`, { state: { selectedId } });
  };

    useEffect(() => {
    console.log("Current State Values:", {
      isInstanceTextObject,
      isInstanceTextPort,
      isFreeTextObject,
      isFreeTextPort,
      isRuleTextObject,
      isRuleTextPort,
      isPpvVisibility
    });
  }, [isInstanceTextObject, isInstanceTextPort, isFreeTextObject, isFreeTextPort, isRuleTextObject, isRuleTextPort, isPpvVisibility]);

  const handleInfoClick = (evt) => {
  console.log('Info clicked!', evt);
};

  return (
    <div className="App">
        <Header />
        <button
          onClick={toggleSidebar}
          className='Button-Sidebar'
          style={{
            position: 'absolute',
            top: '100px',
            left: isSidebarOpen ? '15px' : '0',
            backgroundColor: 'rgb(204, 255, 0)',
            color: 'black',
            fontSize: '20px'
          }}
        >
          {isSidebarOpen ? <i className="fas fa-caret-right"></i> : <i className="fas fa-caret-left"></i>}
        </button>

        {isSidebarOpen && <Sidebar onSettingClick={toggleSetting} onSignalpathClick={toggleSignalpath} onPatchClick={togglePatch} onInfoClick={setIsInfoActive} isInfoActive={isInfoActive} />}

        {isSettingOpen && (
          <Setting
          isPpvVisibility={isPpvVisibility}
          isInstanceTextObject={isInstanceTextObject}
          isInstanceTextPort={isInstanceTextPort}
          isFreeTextObject={isFreeTextObject}
          isFreeTextPort={isFreeTextPort}
          isRuleTextObject={isRuleTextObject}
          isRuleTextPort={isRuleTextPort}
          onPpvVisibilityChange={handlePpvVisibilityChange}
          onInstanceTextObjectChange={handleInstanceTextObjectChange}
          onInstanceTextPortChange={handleInstanceTextPortChange}
          onFreeTextObjectChange={handleFreeTextObjectChange}
          onFreeTextPortChange={handleFreeTextPortChange}
          onRuleTextObjectChange={handleRuleTextObjectChange}
          onRuleTextPortChange={handleRuleTextPortChange}
          onNeuVisualisieren={openSvg}
        />
        )}


        {isSignalpathOpen && <SignalPath onClose={closeSignalpath} />}

        {isPatchOpen && <Patch onClose={closePatch} />}

        <Routes>
          <Route path="/patch" element={<Patch />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/setting" element={<Setting
            isInstanceTextPort={isInstanceTextPort}
            isFreeTextPort={isFreeTextPort}
            isRuleTextPort={isRuleTextPort}
            onPpvVisibilityChange={handlePpvVisibilityChange}
            onInstanceTextObjectChange={handleInstanceTextObjectChange}
            onInstanceTextPortChange={handleInstanceTextPortChange}
            onFreeTextObjectChange={handleFreeTextObjectChange}
            onFreeTextPortChange={handleFreeTextPortChange}
            onRuleTextObjectChange={handleRuleTextObjectChange}
            onRuleTextPortChange={handleRuleTextPortChange}
            onNeuVisualisieren={openSvg}
          />} />
          <Route path="/search" element={<Search />} />
          <Route path="/qrcode" element={<QrCode />} />
          <Route path="/out" element={<Out />} />
          <Route path="/signalpath" element={<SignalPath />} />
          <Route path="/info" element={<ObjectInfo />} />
          <Route path="/login" element={<Login />} />
          <Route path="/svg/:svgContent" element={<SvgComponent onInfoClick={handleInfoClick} />} />
        </Routes>
    </div>
  );
}

export default App;