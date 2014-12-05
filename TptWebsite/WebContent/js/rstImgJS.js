/**
 * 
 */
window.onload = function() {
	createDownload();
}

function createDownload() {
	var data = new Array();
	data.push('<a class="am-modal-hd" href="downloadImg.jsp"'+'">'+'Download'+'</a>');
	document.getElementById('down').innerHTML = data.join('');
}
