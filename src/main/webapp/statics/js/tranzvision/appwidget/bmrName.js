/*====================================================================
 + 功能描述：报名人姓名，该字段只读，默认显示当前登录人的姓名	++
 + 开发人：王瑞立											    ++
 + 开发日期：2015-06-02											++
 =====================================================================*/
SurveyBuild.extend("bmrName", "baseComponent", {
    itemName: "报名人姓名",
    title:"报名人姓名",
    "StorageType":"S",
    _getHtml: function(data, previewmode) {
        var c = "";
        if (previewmode) {
            SurveyBuild.appInsId == "0" && (data.defaultval = "{%BIND:TZ_REALNAME}") ;
            SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
            SurveyBuild.appInsId == "0" && (data.wzsm = data.value);

            c += '<div class="main_inner_content_info_autoheight">';
            c += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>' + data.title + '</div>';
            c += '	<div class="main_inner_content_info_right">';
            c += '		<div class="main_inner_content_info_option_text">' + data.value + '</div>';
            c += '      <input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '" value = "' + data.value + '">';
            c += '		<div class="main_inner_content_info_edit">';
            c += '			<a class="blue" id="' + data.itemId + 'edit" target="_top" href="javascript:void(0);">'+ MsgSet["EDIT"] +'</a>';
            c += '			<a class="blue" target="' + (data.suffixUrl ? "_blank" : "") + '" href="' + (data.suffixUrl ? data.suffixUrl : "javascript:void(0);") + '">' + data.suffix + '</a>';
            c += '		</div>';
            c += '	</div>';
            c += '</div>';

        } else {
            c += '<div class="question-answer">';
            c +=         '<div class="format">';
            c +=             '<b class="read-input"  style="width: 200px;">Jack</b>';
            c +=             '<span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
            c +=         '</div>';
            c += '</div>';
        }

        return c;
    },
    _edit: function(data) {
        var e = "";
        //后缀
        e += '<fieldset>';
        e += '	<legend>';
        e += '      <span class="edit_item_label">后缀：</span>';
        e += '     <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/>';
        e += '	</legend>';
        e += '</fieldset>';

        //后缀链接
        e += '<fieldset>';
        e += '	<legend>';
        e += '      <span class="edit_item_label">后缀链接：</span>';
        e += '     <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffixUrl\')" value="' + data.suffixUrl + '"/>';
        e += '	</legend>';
        e += '</fieldset>';

        return e;
    },
    _eventbind:function(data){
        $("#" + data.itemId + "edit").click(function(){
            var menuId = $("#menuId").val();
            var siteId = $("#siteId").val();
            if(menuId && siteId){
                JumpToColu(siteId,menuId);
            }
        });
    }
});