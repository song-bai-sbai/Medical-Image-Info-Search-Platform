/**
 * advanced.jsp js
 */
var rstContent = null;
function getSelect(id) {
	var obj = document.getElementById(id);
	if (obj == null) {
		alert("failed to get ele");
	}

	return document.getElementById(id).options[window.document
			.getElementById(id).selectedIndex].text;
}

function getEleName(id) {
	var obj = document.getElementById(id);
	if (obj == null) {
		alert("failed to get ele");
	}

	return obj.name;
}

function createXmlHttpRequest() {
	var xmlreq = false;
	if (window.XMLHttpRequest) {
		// Firefox, Opera 8.0+, Safari
		xmlreq = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		try {
			// IE
			xmlreq = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e1) {
			try {
				xmlreq = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e2) {
			}
		}
	}
	return xmlreq;
}

function submitForm() {
	var id;
	var data = {};
	for (var i = 0; i < arguments.length; i++) {
		id = arguments[i];
		data[id] = getSelect(id);
	}
	data['query'] = getQuery();
	var jsonString = JSON.stringify(data);

	var request = createXmlHttpRequest();// create request
	request.open("post", "ParseAdvancedSelection?advancedSelection=" + jsonString, true);
	request.onreadystatechange = function()// readyState
	{
		if (request.readyState == 4) // is OK
		{
			if (request.status == 200) {
				closeLoading();
				var value = request.responseText;// get response
				if (value.indexOf("error") == 0) {
					alert(value);
				}else if(value.indexOf("No match records")==0){
					alert(value);
				}  else {
					rstContent = value;
					openListWindow("");
				}
			}
		} else {
			// alert("t");

		}
	}
	request.send();
	showLoading();
}

function getQuery() {
	var obj = document.getElementById("query");
	if (obj == null) {
		alert("failed to get query");
	}

	return obj.value;
}

function openListWindow(content) {
	var imgwindow = null;
	if (document.all) // IE
	{
		feature = "dialogWidth:500px;dialogHeight:500px;status:no;help:no";
		imgwindow = window.showModalDialog("rstList.jsp?name=" + content,
				"result", feature);
	} else {
		feature = "menubar=no,toolbar=no,location=no,";
		feature += "scrollbars=no,status=no,modal=yes";
		imgwindow = window.open("rstList.jsp?name=" + content, "result",
				feature);
	}
	if (imgwindow != null) {

	}
}

function showLoading() {
	alert('Start searching...');
}

function closeLoading() {
}