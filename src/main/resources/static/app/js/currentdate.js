
function startTime() {
	alert("hi");
    var today = new Date();
    var h = today.getHours();
    var m = today.getMinutes();
    var s = today.getSeconds();
	var m_names = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
    m = checkTime(m);
    s = checkTime(s);
    document.getElementById('currentdate').innerHTML =
    today.getDate() + '-' + (m_names[today.getMonth()]) + '-' + today.getFullYear();
	
	document.getElementById('currenttime').innerHTML = h + ":" + m + ":" + s;
    var t = setTimeout(startTime, 500);
}


function checkTime(i) {
    if (i < 10) {i = "0" + i};  // add zero in front of numbers < 10
    return i;
}
