/**
 * rstList.jsp js
 */
var selectedImg = null;

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

function altRows(id) {
	if (document.getElementsByTagName) {

		var table = document.getElementById(id);
		var rows = table.getElementsByTagName("tr");

		for (i = 0; i < rows.length; i++) {
			if (i % 2 == 0) {
				rows[i].className = "evenrowcolor";
			} else {
				rows[i].className = "oddrowcolor";
			}
		}
	}
}

window.onload = function() {
	var rstContent = window.opener.rstContent;
	showInfo(rstContent);
}

document.ready = function(){
	//loadChart();
}

function showInfo(rstContent) {
	var records=eval(rstContent);  
	var data = new Array();
	data.push('<h3>'+records[records.length-1].content+'</h3>');
	document.getElementById('text').innerHTML = data.join('');
	
	data = new Array();
	data.push('<select id="file">');
	for(var i =0; i< records.length-1; i++){
		data.push('<option ');
		data.push('value="'+records[i].content+'">'+ records[i].content);
		data.push('</option>');
	}
	data.push('</select>');
	document.getElementById('file').innerHTML = data.join('');
	
	data = new Array();
	data.push('<select id="feature">')
	for (var i = 0; i < features.length; i++) {
		data.push('<option value="' + i + '">' + features[i] + '</option>');
	}
	data.push('</select>');
	document.getElementById('feature').innerHTML = data.join('');
}

function requestImg(rowKey){
	openImgWindow(rowKey);
}

function openImgWindow(content) {
	var imgwindow = null;
	if (document.all) // IE
	{
		feature = "dialogWidth:500px;dialogHeight:500px;status:no;help:no";
		imgwindow = window.showModalDialog("rstList.jsp?name=" + content,
				"result", feature);
	} else {
		feature = "menubar=no,toolbar=no,location=no,";
		feature += "scrollbars=no,status=no,modal=yes";
		imgwindow = window.open("rstImg.jsp?name=" + content, "result",
				feature);
	}
	if (imgwindow != null) {

	}
}

function getImg(){
	var name = getSelect('file');
	selectedImg = name;
	var request = createXmlHttpRequest();// create request
	request.open("post", "GetImgInfo?name=" + name,
			true);
	request.onreadystatechange = function()// readyState
	{
		if (request.readyState == 4) // is OK
		{
			if (request.status == 200) {
				var value = request.responseText;// get response
				if (value.indexOf("error") == 0) {
					alert(value);
				} else if (value.indexOf("No match records") == 0) {
					alert(value);
				} else {
					generateTable(value);
				}
			}
		} else {
			// alert("t");
		}
	}
	request.send();
}

function generateTable(content){
	var records=eval(content);  
	data = new Array();
	data.push('<table class="am-table am-table-bd am-table-striped am-table-hover">');
	data.push('<tr><th>Number</th><th>Details</th></tr>');
	for (var i = 0; i < records.length; i++) {
		data.push('<tr class="am-active">');
		data.push('<td>' + (i+1)+ '</td>');
		data.push('<td>' + records[i].content+ '</td>');
		data.push('</tr>');
	}
	data.push('<table>');
	document.getElementById('rstTable').innerHTML = data.join('');
	
	data = new Array();
	data.push('<div id="srcimg"><img alt="" src="loadSrcImg.jsp?name='+selectedImg+'" height="400" width="400"></div>');
	document.getElementById('srcimg').innerHTML = data.join('');
	createDownload();
}

function getSelect(id) {
	var obj = document.getElementById(id);
	if (obj == null) {
		alert("failed to get ele");
	}

	return document.getElementById(id).options[window.document
			.getElementById(id).selectedIndex].text;
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

function createDownload() {
	var data = new Array();
	data.push('<div id="down">');
	data.push('<a class="am-modal-hd" href="downloadImg.jsp?name='+selectedImg+'">'+'Download'+'</a>');
	data.push('</div>');
	document.getElementById('down').innerHTML = data.join('');
}

function getHisotgram(){
	var name = getSelect('feature');
	var request = createXmlHttpRequest();// create request
	request.open("post", "GetHistogram?name=" + name,
			true);
	request.onreadystatechange = function()// readyState
	{
		if (request.readyState == 4) // is OK
		{
			if (request.status == 200) {
				var value = request.responseText;// get response
				if (value.indexOf("error") == 0) {
					alert(value);
				} else if (value.indexOf("No match records") == 0) {
					alert(value);
				} else {
					generateChart(value);
				}
			}
		} else {
			// alert("t");
		}
	}
	request.send();
}

function generateChart(content){
	var data = new Array();
	data.push('<div id="chartDiv">');
	data.push('<div id="chart1" style="margin-top: 20px; margin-left: 20px; width: 300px; height: 300px;"></div>');
	data.push('</div>');
	document.getElementById('chartDiv').innerHTML = data.join('');
	var records=eval(content); 
	$.jqplot.config.enablePlugins = true;
	parseInt();
    var s1 = [];
    var ticks = [];
    for(var i = 0; i<4; i++){
    	s1[i]=parseInt(records[i].content);
    	ticks[i]=records[i].rowKey;
    }
     
    plot1 = $.jqplot('chart1', [s1], {
        // Only animate if we're not using excanvas (not in IE 7 or IE 8)..
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: true }
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: ticks
            }
        },
        highlighter: { show: false }
    });
 
}

function loadChart(){
	var rstContent = window.opener.rstContent;
	var records=eval(rstContent); 
	// Area
	$.jqplot.config.enablePlugins = true;
	parseInt();
    var s1 = [];
    var ticks = [];
    for(var i = 0; i<4; i++){
    	s1[i]=parseInt(records[records.length-13+i].content);
    	ticks[i]=records[records.length-13+i].rowKey;
    }
     
    plot1 = $.jqplot('chart1', [s1], {
        // Only animate if we're not using excanvas (not in IE 7 or IE 8)..
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: true }
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: ticks
            }
        },
        highlighter: { show: false }
    });
 
    $('#chart1').bind('jqplotDataClick', 
        function (ev, seriesIndex, pointIndex, data) {
            $('#info1').html(data[1]);
        }
    );
    
    // Eccentricity
    $.jqplot.config.enablePlugins = true;
    var s2 = [];
    var ticks2 = [];
    for(var i = 0; i<4; i++){
    	s2[i]=parseInt(records[records.length-9+i].content);
    	ticks2[i]=records[records.length-9+i].rowKey;
    }
     
    plot1 = $.jqplot('chart2', [s2], {
        // Only animate if we're not using excanvas (not in IE 7 or IE 8)..
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: true }
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: ticks2
            }
        },
        highlighter: { show: false }
    });
 
    $('#chart2').bind('jqplotDataClick', 
        function (ev, seriesIndex, pointIndex, data) {
            $('#info2').html( data[1]);
        }
    );
    
    $.jqplot.config.enablePlugins = true;
    var s3 = [];
    var ticks3 = [];
    for(var i = 0; i<4; i++){
    	s3[i]=parseInt(records[records.length-5+i].content);
    	ticks3[i]=records[records.length-5+i].rowKey;
    }
     
    plot1 = $.jqplot('chart3', [s3], {
        // Only animate if we're not using excanvas (not in IE 7 or IE 8)..
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: true }
        },
        axes: {
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: ticks3
            }
        },
        highlighter: { show: false }
    });
 
    $('#chart3').bind('jqplotDataClick', 
        function (ev, seriesIndex, pointIndex, data) {
            $('#info3').html(data[1]);
        }
    );
}