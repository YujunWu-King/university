/*====================================================================
+ 功能描述：分隔符控件，该控件用于排版美观，无任何属性				++
+ 开发人：张浪														++
+ 开发日期：2015-05-05												++
=====================================================================*/
SurveyBuild.extend("Separator", "baseComponent", {
	itemName: "分隔符",
	_CommonField: "N",
	_getHtml: function(data, previewmode) {
		var c = '';
		if (previewmode) {
			c = '<div class="input-list input-line"></div>';
		} else {
			c = '<div class="question-title"><hr style="border:1px dotted #9f9f9f;border-bottom:0;border-right:0;border-left:0;width:778px;margin-left:4px;"></div>';
		}
		return c;
	}
});