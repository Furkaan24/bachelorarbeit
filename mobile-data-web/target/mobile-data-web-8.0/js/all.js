var xmlhttp;
window.onload = getType('Rack');
/**
 *
 * @param {String} type
 * @param {function} cfunc
 * @returns {undefined}
 */
function loadType(type, cfunc)
{
  if (window.XMLHttpRequest)
  { // code for IE7+, Firefox, Chrome, Opera,
    // Safari
    xmlhttp = new XMLHttpRequest();
  }
  else
  { // code for IE6, IE5
    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  }
  xmlhttp.onreadystatechange = cfunc;
  //xmlhttp.open("GET", "jsp/gettype.jsp?type=" + type, true);
  xmlhttp.open("GET", "gettype?type=" + type, true);
  xmlhttp.send();
}
/**
 * Setzt inneres HTML des Elements mit der id='id' auf den angefragten Text
 * @param {type} type
 * @returns {undefined}
 */
function getType(type)
{
  loadType(type, function ()
  {
    if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
      document.getElementById("id").innerHTML = xmlhttp.responseText;
  });
}


