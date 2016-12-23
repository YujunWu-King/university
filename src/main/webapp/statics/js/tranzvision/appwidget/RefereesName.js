/*====================================================================
 + 功能描述：推荐人姓名												++
 + 开发人：王瑞立											    ++
 + 开发日期：2016-04-02											++
 =====================================================================*/
SurveyBuild.extend("RefereesName", "baseComponent", {
    itemName: "推荐人姓名",
    title:"推荐人姓名",
    "StorageType":"S",
    "value":"",
    _getHtml: function(data, previewmode) {
        var c = "";
        if (previewmode) {
            var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"RNAME","insid":' + SurveyBuild.appInsId + '}}';
            $.ajax({
                type: "get",
                dataType: "JSON",
                data: {
                    tzParams: params
                },
                async: false,
                url: SurveyBuild.tzGeneralURL,
                success: function(f) {
                    if (f.state.errcode == "0") {
                    	data.value = f.comContent.refName;
                    }
                }
            });
            
            c += '<div class="input-list">';
            c += '	<div class="input-list-info left">' + data.title + '</div>';
            c += '	<div class="input-list-text left">' + data.value + '</div>';
            c += '	<div class="input-list-suffix left"></div>';
            c += '	<div class="clear"></div>';
            c += '</div>';
            c += '<input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '" value = "' + data.value + '">';

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
        return "";
    }
});