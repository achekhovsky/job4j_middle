var ORDERS;

function getFormsInputValues(formId) {
	let formData = {};
	let node;
	node = document.querySelectorAll("form[id = '" + formId + "'] input");
	if(node.length == 0) {
		node = document.querySelectorAll("input[form='" + formId + "']");
	}
	node.forEach(
			function(item) {
				if(item.type == "checkbox") {
					formData[item.name] = item.checked;
					console.log("CHECKED - " + item.checked);
				} else {
					formData[item.name] = item.value;
				}
				}
			);
	return formData; 
}

function findEmptyInputs() {
	let isEmtpy = false;
	let inputs = document.getElementsByTagName("input");
	for (let i = 0; i < inputs.length; i++) {
		if(inputs[i].getAttribute("type") == "text" && inputs[i].value == "") {
			isEmtpy = true;
			break;
		}
	}
	return isEmtpy;
}

function getFormData(formId) {
	var obj = "";
	var jArray = $("#" + formId).serializeArray();
	$.each(jArray, function() {
		obj += this.name + "=" + this.value + "&";
	});
	return obj;
}

function doAction(url, action, formId) {
	$.ajax({
		type:"POST",
		url: url,
		data: processParamsToSend(action, getFormsInputValues(formId)),
		success: function (data) {	
			ORDERS = JSON.parse(data);
			setRows("orderstable", ORDERS);
		}});
		return false;
}


function processParamsToSend(action, form) {
	form["getAction"] = arguments[0];
	return form;
}

function clearTable(tableId) {
	document.getElementById(tableId).tBodies[0].innerHTML = "";
}

function setRows(tableId, orders) {
	clearTable(tableId);
	if(orders instanceof Array) {
		orders.forEach(function (item) {
			addRow(document.getElementById(tableId), item);
		});
	}
}

function addRow(table, order) {
	var row = document.createElement("tr");
	var frm = document.createElement("form");
	frm.id = order['id'];
	frm.name = "orders";
	frm.action = "orders";
	frm.method = "POST";
	row.insertAdjacentElement("beforeEnd", frm);
		for(key in order) {
			let td = document.createElement("td");
			let inp = document.createElement("input");
			inp.name = key;
			inp.value = order[key];
			inp.setAttribute('form', order['id']);
			if (key == "done") {
				inp.type = "checkbox";
				inp.checked = order[key];
				inp.addEventListener("change", function() {
					doAction("orders", "updateStatus", order['id']);
				});
			} else {
				inp.type = "hidden";
				td.textContent = order[key];
			}
			td.insertAdjacentElement("beforeEnd", inp);
			row.insertAdjacentElement("beforeEnd", td);
		}
		let td = document.createElement("td");
		td.style.textAlign = "right";
		let btn = document.createElement("button");
		btn.className = "btn btn-outline-danger btn-sm";
		btn.name = "getAction";
		btn.value = "deleteOrder"; 
		btn.type = "button";
		btn.formMethod = "post";
		btn.setAttribute('form', order['id']);
		btn.textContent = "Delete";
		btn.addEventListener("click", function() {
			doAction("orders", "deleteOrder", order['id']);
		});
		frm.insertAdjacentElement("beforeEnd", btn);
		td.insertAdjacentElement("beforeEnd", btn);
		row.insertAdjacentElement("beforeEnd", td);
	if(table.tBodies.length == 0) {
		table.insertAdjacentElement("beforeEnd", document.createElement("tbody"));
	} 
	table.tBodies[0].insertAdjacentElement("beforeEnd", row);
}

