import React from 'react';
import { useParams } from 'react-router-dom';
import './css/pages.css';

function SvgComponent(onInfoClick ) {
  const { svgContent } = useParams();

  return (
    <div className="svg-container">
      {svgContent && <iframe src={svgContent} title="SVG" width="100%" height="1200px" />}
    </div>
  );
}

export default SvgComponent;