import React from "react";
import { useNavigate } from "react-router-dom";

function Out(){
    const navigate = useNavigate();

    const navigateToSearch = () => {
      navigate("/search");
    };
    
    return (
    <div>
      <h1>Out Page</h1>
      <button onClick={navigateToSearch}>Go to Search</button>
    </div>
  );
}

export default Out;