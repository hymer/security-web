/**
 * jQuery extends.
 */
jQuery.extend({
	/**
	 * Extend jQuery to serialize a object to json string.
	 *
	 * @param Object,
	 *            support type:
	 *            object,array,string,function,number,boolean,regexp *
	 * @return json string
	 */
	toJSON : function(object) {
		if(object === null)
			return "null";
		if(!object)
			return null;
		var type = typeof object;
		if('object' == type) {
			if(Array == object.constructor)
				type = 'array';
			else if(RegExp == object.constructor)
				type = 'regexp';
			else
				type = 'object';
		}
		switch (type) {
			case 'undefined' :
			case 'unknown' :
				return;
				break;
			case 'function' :
			case 'boolean' :
			case 'regexp' :
				return object.toString();
				break;
			case 'number' :
				return isFinite(object) ? object.toString() : 'null';
				break;
			case 'string' :
				return '"' + object.replace(/(\\|\")/g, "\\$1").replace(/\n|\r|\t/g, function() {
					var a = arguments[0];
					return (a == '\n') ? '\\n' : (a == '\r') ? '\\r' : (a == '\t') ? '\\t' : ""
				}) + '"';
				break;
			case 'object' :
				if(object === null)
					return 'null';
				var results = [];
				for(var property in object) {
					var value = jQuery.toJSON(object[property]);
					if(value !== undefined)
						results.push(jQuery.toJSON(property) + ':' + value);
				}
				return '{' + results.join(',') + '}';
				break;
			case 'array' :
				var results = [];
				for(var i = 0; i < object.length; i++) {
					var value = jQuery.toJSON(object[i]);
					if(value !== undefined)
						results.push(value);
				}
				return '[' + results.join(',') + ']';
				break;
		}
	},
	fromJSON : function(jStr) {
		return (new Function('return ' + jStr))();
	}
});

/****************************************************************
 *
 * this script file contains some common useful function.
 *
 * **************************************************************
 */

/**
 * do post AJAX, by default
 */
function doAjax(url, param, method, callback) {
	$.ajax({
		dataType : "json",
		type : method,
		timeout : 30000,
		url : url,
		data : param,
		success : function(data, textStatus, XMLHttpRequest) {
			if(data && data.error) {
				showMsg(data);
				return;
			}
			if( typeof callback == 'function') {
				callback(data, textStatus, XMLHttpRequest);
			}
		}
	});
}

/**
 * jquery post ajax method.
 */
function doPostAjax(url, param, callback) {
	doAjax(url, param, "post", callback);
}

/**
 * jquery get ajax method.
 */
function doGetAjax(url, param, callback) {
	doAjax(url, param, "get", callback);
}

/**
 * submit a form AJAX.
 */
function ajaxSubmitForm(formId, callback) {
	$("#" + formId).ajaxSubmit({
		//target : "msgDiv", //配置此项，则会用返回的数据更新此div，并且不会执行success callback！。
		beforeSubmit : function(formData, jqForm, options) {

		},
		success : function(resp, statusText, xhr, $form) {
			if(resp.result == true) {
				$("#" + formId + "_div").dialog("close");
				showMsg(resp);
			} else {
				showMsg(resp);
			}
			if( typeof callback == 'function') {
				callback();
			}
		}
	});
}

/**
 * fill out a form with a object.
 */
function fillForm(formId, obj) {
	var form = $("#" + formId);
	for(var attr in obj) {
		if( typeof (obj[attr]) == 'function') {
			continue;
		}
		var $input = $("input[name='" + attr + "']", form);
		var type = $input.attr("type");
		if($input.length == 0) {
			//说明不是text/password/radio/checkbox
			var select = $("select[name='" + attr + "']", form);
			if(select.length == 0) {
				var textarea = $("textarea[name='" + attr + "']", form);
				if(textarea.length != 0) {
					//textarea
					textarea.val(obj[attr]);
				}
			} else {
				// select
				select.find("option[value='" + obj[attr] + "']").attr('selected', 'selected');
			}
		} else {
			if(type == "checkbox" || type == "radio") {
				var avalues;
				if(obj[attr].toString().indexOf(',') >= 0) {
					avalues = obj[attr].toString().split(",");
				} else {
					avalues = [obj[attr]];
				}
				for(var v = 0; v < avalues.length; v++) {
					$input.each(function(i, n) {
						if($(this).val() == avalues[v].toString()) {
							$(this).attr("checked", "checked");
						}
					});
				}
			} else {
				$input.val(obj[attr]);
			}
		}
	}
}

function showMessage(title, content, cb) {
	var div = $("<div></div>");
	div.html("<font>" + content + "</font>");
	var w = 420, h = 150;
	if(content.length > 100) {
		w = 560;
		h = 240;
	}
	div.dialog({
		draggable : false,
		title : title,
		width : w,
		modal : true,
		// position:'center', 'left', 'right', 'top', 'bottom'.  or [350,100]
		resizable : false,
		// draggable : true,
		// autoOpen : false,
		// show:'slide',
		buttons : {
			"OK" : function() {
				$(this).dialog("close");
				if (typeof cb == 'function') {
					cb();
				}
			}
		}
	});
}

/**
 * show message.
 * message object should contain 'result' and 'msg' field.
 */
function showMsg(resp, cb) {
	var div = $("<div></div>");
	var w = 420;
	if( typeof resp == 'object') {
		if(resp.result) {
			div.html("<font>" + resp.msg + "</font>");
		} else {
			div.html("<font color='red'>" + resp.msg + "</font>");
		}
		if(resp.msg.length > 100) {
			w = 560;
		}
	} else if( typeof resp == 'string') {
		div.html(resp);
		if(resp.length > 100) {
			w = 560;
		}
	} else {
		return false;
	}
	div.dialog({
		draggable : false,
		title : '系统提示',
		width : w,
		modal : true,
		resizable : false,
		buttons : {
			"OK" : function() {
				$(this).dialog("close");
				if (typeof cb == 'function') {
					cb();
				}
			}
		}
	});
}

/**
 * confirm dialog.
 */
function showConfirm(text, yesCallback, noCallback) {
	var div = $("<div></div>");
	if( typeof text == 'string') {
		div.html(text);
	} else {
		return false;
	}
	div.dialog({
		draggable : false,
		title : '确认框',
		width : 480,
		modal : true,
		resizable : false,
		draggable : true,
		buttons : {
			"是" : function() {
				$(this).dialog("close");
				if( typeof yesCallback == 'function') {
					yesCallback();
				}
			},
			"否" : function() {
				$(this).dialog("close");
				if( typeof noCallback == 'function') {
					noCallback();
				}
			}
		}
	});
}

function getParam(paras) {
	var url = location.href;
	var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
	var paraObj = {}
	for( i = 0; j = paraString[i]; i++) {
		paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
	}
	var returnValue = paraObj[paras.toLowerCase()];
	if( typeof (returnValue) == "undefined") {
		return "";
	} else {
		return returnValue;
	}
}

function gatherParam(container) {
	var conditions = [];
	var param = {};
	var ele = container.find(':input[name]');
	ele.each(function(i) {
		var map = {};
		var key = $(this).attr("name");
		var isExist = false;
		if(param[key]) {
			isExist = true;
		}
		// for(var j = 0; j < conditions.length; j++) {
		// if(key == conditions[j].key) {
		// isExist = true;
		// map = conditions[j];
		// break;
		// }
		// }
		var value = $(this).val();
		var type = $(this).attr("type");
		if((type == 'checkbox' || type == 'radio')) {
			if(($(this).attr("checked") !== true) && ($(this).attr('checked') != 'checked')) {
				value = "";
			}
		}
		if(isExist) {
			var v0 = param[key];
			param[key] = v0 + encodeURIComponent("," + value);
			// map.value += encodeURIComponent("," + value);
		} else {
			// param[key] = encodeURIComponent(value);
			param[key] = value;

			// map.key = key;
			// map.value = encodeURIComponent(value);
			// conditions.push(map);
		}
	});
	// return conditions;
	return param;
}

String.prototype.startsWith=function(str){     
  var reg=new RegExp("^"+str);     
  return reg.test(this);        
} 
 
String.prototype.endsWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
}


function setPageHeight() {
	//在页面高度不够的情况下，使footer保持在最底部
	var bodyHeight = Math.max(document.documentElement.clientHeight,document.body.scrollHeight);
	/*var isIE = $.browser.msie;
	if(!isIE) {
		bodyHeight = document.body.scrollHeight;
	} else {
		var iev = $.browser.version;
		if(iev == '7.0') {
			bodyHeight = Math.max(document.documentElement.clientHeight,document.body.scrollHeight);
		} else if(iev == '8.0') {
			bodyHeight = Math.max(document.documentElement.clientHeight,document.body.scrollHeight);
		} else if(iev == '9.0') {
			bodyHeight = Math.max(document.documentElement.clientHeight,document.body.scrollHeight);
		} else if(iev == '10.0') {
			bodyHeight = Math.max(document.documentElement.clientHeight,document.body.scrollHeight);
		}
	}*/
	var minHeight;
	var hasHead = (($("#head").length > 0) ? true : false);
	if(hasHead) {
		var headH = $("#head").css("height");
		minHeight = bodyHeight - (180 + parseInt(headH));
	} else {
		minHeight = bodyHeight - 180;
	}
	$("#main").css("min-height", minHeight + "px");
}

$(function() {
	setPageHeight();
});
