/**
 * Created by admin on 2015/6/18.
 * 报名人Last Name
 */
SurveyBuild.extend("bmrLastName", "baseComponent", {
    itemName: "Last Name",
    title: "Last Name",
    "StorageType": "S",
    _getHtml: function(data, previewmode) {
        var c = "";
        if (previewmode) {
            SurveyBuild.appInsId == "0" && (data.defaultval = "{%BIND:TZ_LAST_NAME}");
            SurveyBuild.appInsId == "0" && this._getDefaultVal(data);
            SurveyBuild.appInsId == "0" && (data.wzsm = data.value);
            if(SurveyBuild.accessType == "M"){
          	  c += '<div class="item">';
                c += '    <p>'+data.title+'<span>*</span></p>';
                c += '    <div class="text-box"><input type="text" class="text1" id="' + data.itemId + '"  name="' + data.itemId + '" value = "' + data.value + '" /></div>';
                c += '</div>';
            }else{
            c += '<div class="input-list">';
            c += '	<div class="input-list-info left"><span class="red">*</span>' + data.title + '</div>';
            
            c += '	<div class="input-list-text left">' + data.value + '</div>';
            c += '	<div class="input-list-suffix left" style="display:' + (SurveyBuild._readonly?'none':'') + '"><a id="' + data.itemId + 'edit" target="_top" href="javascript:void(0);" style="color: rgb(0, 112, 198); cursor: pointer;">' + MsgSet["EDIT"] + '</a><a class="blue" target="' + (data.suffixUrl ? "_blank": "") + '" href="' + (data.suffixUrl ? data.suffixUrl: "javascript:void(0);") + '">' + data.suffix + '</a></div>';
            c += '	<div class="clear"></div>';
            c += '</div>';
            c += '<input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '" value = "' + data.value + '">';
               } 
            }
            else {
            c += '<div class="question-answer">';
            c += '<div class="format">';
            c += '<b class="read-input"  style="width: 200px;">Jack</b>';
            c += '<span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
            c += '</div>';
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
    _eventbind: function(data) {
        $("#" + data.itemId + "edit").click(function() {
            var menuId = $("#menuId").val();
            var siteId = $("#siteId").val();
            if (menuId && siteId) {
                JumpToColu(siteId, menuId);
            }
        });
    }
});