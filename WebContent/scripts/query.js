function initQuery(obj) {
	doGetAjax(obj.basePath + "/load_query.ajax", "queryId=" + obj.queryId, function(resp) {
		if(resp.length > 0) {
			var table = $("#" + obj.queryId);
			var thead = $("<thead></thead>");
			thead.append($(obj.checkbox));
			for(var i = 0; i < resp.length; i++) {
				var col = resp[i];
				var th = $("<th></th>");
				th.attr("colname", col.key).attr("display", col.display).css("width", col.width).css("align", col.align).html(col.header);
				thead.append(th);
			}
			thead.append(obj.ops);
			table.append(thead);
			t = new SimpleTable(obj.queryId, {
				paging : obj.paging == false ? false : true,
				autoLoad : obj.autoLoad == false ? false : true,
				param : obj.queryId + "Search",
				url : obj.basePath + '/query.html',
				sortables : obj.sortables,
				checkMode : obj.checkMode == "single" ? "single" : "mul",
				formatters : obj.formatters
			});
		}
	});
}