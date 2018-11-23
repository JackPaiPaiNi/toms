$(function() {
	selectBDSCTarget($("#type").val());
});
function importSales() {
	showImportWin(locale("excel.target.import"), baseUrl
			+ "platform/importBDSCTarget.action?what=month");

}
function importSalesYear() {
	showImportWin(locale("excel.target.import"), baseUrl
			+ "platform/importBDSCTarget.action?what=year");

}

function selectBDSCTarget(type) {
	$("#loadingImport").show();
	var date = $("#date").val();
	var o = {};
	o.type = type;
	if (date != null && date != "") {
		o.beginDate = date + "-01";
		o.endDate = date + "-31";
	}
	o.what = "month";

	$.ajax({
		url : baseUrl + "platform/selectBDSCTarget.action",
		type : "POST",
		data : o,
		success : function(data) {
			$("#loadingImport").hide();
			loadLeftBody(data);
		}
	});

}

function selectBDSCTargetYear(type) {
	$("#loadingImport").show();
	var date = $("#dateYear").val();
	var o = {};
	o.type = type;
	o.what = "year";
	if (date != null && date != "") {
		o.date = date;
	}

	$.ajax({
		url : baseUrl + "platform/selectBDSCTarget.action",
		type : "POST",
		data : o,
		success : function(data) {
			$("#loadingImport").hide();
			loadLeftBodyYear(data);
		}
	});

}

function loadLeftBody(obj) {
	var country = obj[0].country;
	var line = obj[0].line;
	var data = obj[0].data;
	var leftHtml = "";
	var wid = 110 * line.length;
	var lineHtml = '<tr>	<th class="lineHtmlW" colspan="'
			+ line.length
			+ '" style="border-top: 1px solid #bbb;text-indent: -160px;">So Target</th></tr>';

	var rightHtml = "";
	var totalQty = 0;

	var totalLineHtml = "";
	for (var b = 0; b < line.length; b++) {
		lineHtml += "<th>" + line[b].line + "</th>"
		var totalLine = 0;
		var qty = 0;
		for (var c = 0; c < data.length; c++) {

			if (data[c].line == line[b].line) {
				qty = data[c].targetQty;
				totalLine += qty;
			}
		}
		totalLineHtml += "<th>" + isStringNullAvaliableNum(totalLine) + "</th>";
	}

	for (var a = 0; a < country.length; a++) {
		leftHtml += "<tr>";
		leftHtml += "<td>" + country[a].country + "</td>";
		leftHtml += "<td>" + country[a].date + "</td>";
		leftHtml += "<td>" + country[a].type + "</td>";
		leftHtml += "<td>" + toThousands(country[a].targetQty) + "</td>";
		leftHtml += "</tr>  ";
		rightHtml += "<tr>";
		totalQty += country[a].targetQty;
		for (var b = 0; b < line.length; b++) {
			rightHtml += "<td>";
			var qty = 0;
			for (var c = 0; c < data.length; c++) {

				if (data[c].country == country[a].country
						&& data[c].line == line[b].line) {
					qty = data[c].targetQty;
				}
			}
			rightHtml += toThousands(qty) + "</td>";
		}
		rightHtml += "</tr>"
	}
	lineHtml += '</tr>';

	if (country.length > 0) {
		leftHtml += "<tr>";
		leftHtml += "<th>TOTAL</th>";
		leftHtml += "<th>" + country[0].date + "</th>";
		leftHtml += "<th>" + country[0].type + "</th>";
		leftHtml += "<th>" + isStringNullAvaliableNum(totalQty) + "</th>";
		leftHtml += "</tr>  ";
		rightHtml += "<tr>"
		rightHtml += totalLineHtml;
		rightHtml += "</tr>"
	}
	$("#tbody").html("");
	$("#tbody").html(leftHtml);
	$("#rightBody").html("");
	$("#rightBody").html(rightHtml);
	$("#lineMap").html("");
	$("#lineMap").html(lineHtml);

	widthAndHeight('test_Right','test_Head');
	$(window).resize(function() {
		/* parent.location.reload(); */
		widthAndHeight('test_Right','test_Head');
	});
}

function loadLeftBodyYear(obj) {
	var country = obj[0].country;
	var line = obj[0].line;
	var data = obj[0].data;
	var leftHtml = "";
	var totalQty = 0;
	var udTotal = 0;
	for (var a = 0; a < country.length; a++) {
		leftHtml += "<tr>";
		leftHtml += "<td>" + country[a].country + "</td>";
		leftHtml += "<td>" + country[a].date + "</td>";
		leftHtml += "<td>" + country[a].type + "</td>";
		leftHtml += "<td>" + toThousands(country[a].targetQty) + "</td>";
		totalQty += country[a].targetQty;
		var qty = 0;
		for (var c = 0; c < data.length; c++) {

			if (data[c].country == country[a].country) {
				qty = data[c].targetQty;
			}
		}
		udTotal += qty;
		leftHtml += "<td>" + toThousands(qty) + "</td>";
		leftHtml += "</tr>  ";
	}
	if (country.length > 0) {
		leftHtml += "<tr>";
		leftHtml += "<th>TOTAL</th>";
		leftHtml += "<th>" + country[0].date + "</th>";
		leftHtml += "<th>" + country[0].type + "</th>";
		leftHtml += "<th>" + isStringNullAvaliableNum(totalQty) + "</th>";
		leftHtml += "<th>" + isStringNullAvaliableNum(udTotal) + "</th>";
		leftHtml += "</tr>  ";

	}

	$("#leftBodyYear").html("");
	$("#leftBodyYear").html(leftHtml);

}

function isStringNullAvaliableNum(val) {// 字符串是否为空
	if (typeof (val) != "undefined" && val != '' && val != null) {
		return toThousands(val);
	}
	return 0;
};
function toThousands(num) {// 千位符
	return (num || 0).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
};