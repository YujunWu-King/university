/*====================================================================
+ 功能描述：文字说明控件，用于展示说明性文字，文字格式可富文本编辑						++
+ 开发人：张浪														++
+ 开发日期：2015-05-04												++
=====================================================================*/
SurveyBuild.extend("TextExplain", "baseComponent", {
	itemName: "文字说明",
	title: "文字说明",
	"StorageType": "L",
	"isDownLoad": "N", //是否导出
	_getHtml: function(data, previewmode) {
		var c = '';
		if (previewmode) {
			c += '<div id="' + data.itemId + '">' + data.title + '</div>';
		} else {
			c += '<div class="question-answer"></div>';
		}
		return c;
	},
	_edit: function(data) {
		var e = "";

		e += '<div class="edit_paramSet mt10"><span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
		e += '	<div class="groupbox">';
		e += '		<div class="edit_item_warp" style="margin-top:5px;"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isDownLoad\')"' + (data.isDownLoad == "Y" ? "checked='checked'": "") + ' id="is_isDownLoad"> <label for="is_isDownLoad">是否导出</label></div>';
		e += '	</div>';
		e += '</div>';

		return e;
	},
})