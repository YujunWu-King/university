/*====================================================================
 + 功能描述：报名表分页符         									++
 + 开发人：WRL													    ++
 + 开发日期：2015-06-03												++
 =====================================================================*/
SurveyBuild.extend("Page", "baseComponent", {
	itemName: "分页符",
	title: "分页符",
	tapWidth: "100",
	tapHeight: "26",
	tapStyle: 'width:100px;height:26px',
	_CommonField: "N",
	_getHtml: function(data, previewmode) {
		if (previewmode) {
			return;
		}
		var c = '<div class="question-title"><div class="question-split"><hr><div class="pagename">' + data.itemName + '</div></div></div>';
		return c;
	}
});