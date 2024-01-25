import React from 'react';
import infoCable from '../imags/splash.png';

const Header = () => {
  return (
    <>
      <header role="banner" style={{ textAlign: 'center', paddingTop: '20px' }}>
        <img src={infoCable} alt="Logo" className="app-logo" style={{ width: '400px', height: 'auto' }} />
      </header>
    </>
  );
};

export default Header;