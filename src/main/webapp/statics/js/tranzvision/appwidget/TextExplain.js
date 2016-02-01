/*====================================================================
+ 功能描述：文字说明控件，用于展示说明性文字，文字格式可富文本编辑	++
+ 开发人：张浪							++
+ 开发日期：2015-05-04												++
=====================================================================*/
SurveyBuild.extend("TextExplain", "baseComponent", {
    itemName: "文字说明",
	title: "文字说明",
	"StorageType":"L",

	_getHtml: function(data, previewmode) {
		var c = '';
		if (previewmode) {
			c += '<div class="question-answer" id="' + data.itemId + '">';
			c += '	<div class="main_inner_connent_info">' + data.title + '</div>';
			c += '</div>';
		} else {
			c = '<div class="question-answer"></div>';
		}
		return c;
	}
})