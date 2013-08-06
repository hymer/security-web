/*
 * ! import js files and css files.
 */
function importJs(jsFilePath) {
	document.write('<script language="javascript" charset="utf-8" src="' + jsFilePath + '"></script>');
}

function importCss(cssFilePath) {
	document.write('<link rel="stylesheet" type="text/css" href="' + cssFilePath + '" media="all" />');
}
/**
 * required:
 * 1.jquery-1.7.2.min.js
 * 2.jquery-ui-1.8.20.custom.min.js
 * 3.common-tools.js
 *
 * 4.css/main.css
 * 5.css/jquery-ui-1.8.20.custom.css
 */
var js = [
"<%=request.getContextPath()%>/scripts/jquery-1.7.2.min.js", 
"<%=request.getContextPath()%>/scripts/jquery-ui-1.8.20.custom.min.js",
"<%=request.getContextPath()%>/scripts/jquery.tooltip.js",
"<%=request.getContextPath()%>/scripts/common-tools.js",
"<%=request.getContextPath()%>/scripts/common-simpletable.js", 
"<%=request.getContextPath()%>/scripts/jquery.form.js",
"<%=request.getContextPath()%>/scripts/jquery.validate.min.js",
"<%=request.getContextPath()%>/scripts/highcharts.js",
"<%=request.getContextPath()%>/scripts/kindeditor-min.js",
"<%=request.getContextPath()%>/scripts/messages_cn.js",
"<%=request.getContextPath()%>/scripts/zh_CN.js",
"<%=request.getContextPath()%>/scripts/common-multiselector.js",
"<%=request.getContextPath()%>/scripts/common-ajaxselector.js",
"<%=request.getContextPath()%>/scripts/jquery.ztree.all-3.4.min.js"
];

var css = ["<%=request.getContextPath()%>/css/main.css", 
"<%=request.getContextPath()%>/css/jquery-ui-1.8.20.custom.css", 
"<%=request.getContextPath()%>/css/common-simpletable.css",
"<%=request.getContextPath()%>/scripts/zTreeStyle/zTreeStyle.css"
];

for(var i = 0; i < js.length; i++) {
	importJs(js[i]);
}
for(var i = 0; i < css.length; i++) {
	importCss(css[i]);
}