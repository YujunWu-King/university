/**
 * Created by wrl on 2015/6/18.
 * 报名人班级
 */
SurveyBuild.extend("bmrClass","baseComponent",{
    itemName: "报考班级",
    title:"报考班级",
    "StorageType":"S",
    wzsm:"",
    _getHtml : function(data,previewmode){
        var c = "", val = data.value,desc = "";
        if(previewmode){
        	if($("#ClassId").length > 0){
                var classid = $("#ClassId").val();
                var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"CLASSINFO","CLASSID":' + classid + '}}';
                $.ajax({
                    type: "get",
                    dataType: "JSON",
                    data:{
                        tzParams:params
                    },
                    async:false,
                    url:SurveyBuild.tzGeneralURL,
                    success: function(f) {
                        if(f.state.errcode == "0"){
                            val = f.comContent.classCode;
                            desc = f.comContent.className;
                            data.value = val;
                            data.wzsm = desc;
                        }
                    }
                });
            }
            
            c += '<div class="input-list">';
            c += '	<div class="input-list-info left"><span class="red">*</span>' + data.title + '</div>';
            c += '	<div class="input-list-text left">' + data.wzsm + '</div>';
            c += '	<div class="input-list-suffix left"></div>';
            c += '	<div class="clear"></div>';
            c += '</div>';
            c += '<input id="' + data.itemId + '" type="hidden" name="' + data.itemId + '" value="' + data.value + '">';
        }else{
            c += '<div class="question-answer">';
            c += '	<div class="format">';
            c += '		<b class="read-input"></b>';
            c += '		<span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span>';
            c += '	</div>';
            c += '</div>';
        }
        return c;
    },
    _edit : function(data){
        var e = '';
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
//		//自动换行后高度调整
//		var $Input = $("#" + data.itemId);	
//		var $classDiv = $Input.prev(".inpu-list-text-enter");
//		var offHeight = $classDiv.get(0).offsetHeight;
//		var scrHeight = $classDiv.get(0).scrollHeight;
//		if(offHeight < scrHeight){
//			$Input.closest(".input-list").css("padding-bottom","53px");	
//		}
	}
});