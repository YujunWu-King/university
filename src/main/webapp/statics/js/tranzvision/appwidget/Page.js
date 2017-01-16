/*====================================================================
 + 功能描述：报名表分页符         									++
 + 开发人：WRL	modity by caoy 2017-1-16						    ++
 + 开发日期：2015-06-03												++
 =====================================================================*/
SurveyBuild.extend("Page", "baseComponent", {
	itemName: "分页符",
	title: "分页符",
	tapWidth: "100",
	tapHeight: "26",
	tapStyle: 'width:100px;height:26px',
	_CommonField: "N",
	fPageId : "",
	_getHtml: function(data, previewmode) {
		if (previewmode) {
			return;
		}
		var c = '<div class="question-title"><div class="question-split"><hr><div class="pagename">' + data.itemName + '</div></div></div>';
		return c;
	},

	_edit: function(data) {
		var e = "";
		e += '<div class="edit_item_warp">';
		e += '  <span class="edit_item_label">上级分隔符编号：</span>';
		e += '  <input type="text" class="medium" id="fPageId" onkeyup="SurveyBuild.saveAttr(this,\'fPageId\')" value="' + data.fPageId + '"/>';
		e += '</div>';
		return e;
	}
});