/**
 * JS for basic.jsp
 */
var features = [ '', 'Area', 'Perimeter', 'Eccentricity', 'Circularity',
		'MajorAxisLength', 'MinorAxisLength', 'Extent', 'Solidity', 'FSD1',
		'FSD2', 'FSD3', 'FSD4', 'FSD5', 'FSD6', 'HematoxlyinMeanIntensity',
		'HematoxlyinMeanMedianDifferenceIntensity', 'HematoxlyinMaxIntensity',
		'HematoxlyinMinIntensity', 'HematoxlyinStdIntensity',
		'HematoxlyinEntropy', 'HematoxlyinEnergy', 'HematoxlyinSkewness',
		'HematoxlyinKurtosis', 'HematoxlyinMeanGradMag',
		'HematoxlyinStdGradMag', 'HematoxlyinEntropyGradMag',
		'HematoxlyinEnergyGradMag', 'HematoxlyinSkewnessGradMag',
		'HematoxlyinKurtosisGradMag', 'HematoxlyinSumCanny',
		'HematoxlyinMeanCanny', 'CytoplasmMeanIntensity',
		'CytoplasmMeanMedianDifferenceIntensity', 'CytoplasmMaxIntensity',
		'CytoplasmMinIntensity', 'CytoplasmStdIntensity', 'CytoplasmEntropy',
		'CytoplasmEnergy', 'CytoplasmSkewness', 'CytoplasmKurtosis',
		'CytoplasmMeanGradMag', 'CytoplasmStdGradMag',
		'CytoplasmEntropyGradMag', 'CytoplasmEnergyGradMag',
		'CytoplasmSkewnessGradMag', 'CytoplasmKurtosisGradMag',
		'CytoplasmSumCanny', 'CytoplasmMeanCanny' ];

var rstContent = null;

var fid = new Array();

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

function getValue(num) {
	var opid = 'op' + num;
	var vid = 'inputtext' + num;
	var op = document.getElementById(opid).options[window.document
			.getElementById(opid).selectedIndex].text;
	var value = document.getElementById(vid).value;
	return op + value;
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
	for (var j = 0; j < fid.length; j++) {
		var nid = getSelect(fid[j]);
		data[nid] = getValue(j);
	}
	var jsonString = JSON.stringify(data);

	var request = createXmlHttpRequest();// create request
	request.open("post", "ParseBasicSelection?basicSelection=" + jsonString,
			true);
	request.onreadystatechange = function()// readyState
	{
		if (request.readyState == 4) // is OK
		{
			if (request.status == 200) {
				closeLoading();
				var value = request.responseText;// get response
				if (value.indexOf("error") == 0) {
					alert(value);
				} else if (value.indexOf("No match records") == 0) {
					alert(value);
				} else {
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

function addCondition() {
	if (fid == null) {
		fid = new Array();
	}
	var data = '';
	data += '<div class="am-form-group">';
	data += '<select id="feature' + (fid.length + 1) + '"'
			+ ' onchange="featureChange(\'feature' + (fid.length + 1) + '\')">';
	fid.push('feature' + (fid.length + 1));
	var curId = 'feature' + fid.length;
	for (var i = 0; i < features.length; i++) {
		data += '<option value="' + i + '">' + features[i] + '</option>';
	}
	data += '</select>';
	data += '<select id="op' + (fid.length - 1) + '">';
	data += '<option value="0"> = </option>';
	data += '<option value="1"> > </option>';
	data += '<option value="2"> >= </option>';
	data += '<option value="3"> < </option>';
	data += '<option value="4"> <= </option>';
	data += '</select>';
	data += '<input type="textfield" id = "inputtext' + (fid.length - 1)
			+ '"/>';
	data += '<p id ="' + curId + 'p" >' + '</p>';
	data += '</div>';

	$('#addFeature').append(data);
}
function featureChange(id) {
	var name = getSelect(id);
	if (name === ('')) {
	} else {
		var request = createXmlHttpRequest();// create request
		request.open("post", "GetRange?name=" + name, true);
		request.onreadystatechange = function()// readyState
		{
			if (request.readyState == 4) // is OK
			{
				if (request.status == 200) {
					closeLoading();
					var value = request.responseText;// get response
					if (value.indexOf("error") == 0) {
						alert(value);
					} else if (value.indexOf("No match records") == 0) {
						alert(value);
					} else {
						var data = new Array();
						data.push('<p id ="' + id + 'p" >' + value + '</p>');
						document.getElementById(id + 'p').innerHTML = data
								.join('');
					}
				}
			} else {
				// alert("t");

			}
		}
		request.send();
	}
}
function showLoading() {
	alert('Start searching...');
}

function closeLoading() {
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
