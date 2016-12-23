/*====================================================================
+ 功能描述：附件上传控件，可控制上传附件的格式及附件大小						++
+ 开发人：张浪															++
+ 开发日期：2015-05-04												++
=====================================================================*/
SurveyBuild.extend("ViewAttachment", "baseComponent", {
    itemName: "查看附件",
	title:"查看附件",
	fileType: "docx,txt",//允许上传类型
    fileSize: "2",//允许上传大小
	allowMultiAtta: "Y",//允许多附件上传
	isDownLoad:"Y",//允许打包下载
	isOnlineShow:"N",//PDF在线阅读
	"StorageType":"F",//存储类型-附件
    children:[],
    _getHtml: function(data, previewmode) {
        var c = "", val = data.value ? data.value: data.defaultval;
        var _fileSuffix,_fileName,children,_sysFilename;
        if (previewmode) {
        	c += '<div class="input-list-blank margart15" id="upload_' + data.itemId + '">';
        	c += '	<div class="input-list-info left">' + data.title + '</div>';
        	c += '	<div class="input-list-upload left">';
        	//报名表实例ID
        	var appInsId = SurveyBuild.appInsId;
        	var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"VIEWATTACH","INSTANCEID":"' + appInsId + '"}}';
        	$.ajax({
        		type: "get",
        		dataType: "JSON",
        		data: {tzParams: params},
        		async: false,
        		url: SurveyBuild.tzGeneralURL,
        		success: function (data2) {
        			if (data2.state.errcode == "0"){
        				var num = 1;
        				children =  data2.comContent;
        				data.children = children;
        				c += '<div class="input-list-upload-con" id="' + data.itemId + '_AttList" style="display:' + (children.length < 1 ? 'none':'black') + '">';
        				for(var i = 0; i < children.length; i++){
        					_sysFilename = children[i].sysFileName;
        					_fileName = children[i].fileName;
        					_accessPath = children[i].accessPath;
        					_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
        					
        					if (children[i].viewFileName != "" && children[i].sysFileName != ""){
        						c += '<div class="input-list-uploadcon-list">';
        						c += '	<div class="input-list-uploadcon-listl left"><a onclick="SurveyBuild.downLoadBmbFile(\'' + data.instanceId + '\',\'' + _sysFilename + '\',\'' + _accessPath + '\')" file-index="' + children[i].orderby + '">' + children[i].viewFileName + '</a></div>';
        						c += '	<div class="clear"></div>';
        						c += '</div>';
        					}
        				}
        				c += '</div>';
        			}
        		}
        	});
        	c += '	</div>';
        	c += '	<div class="input-list-suffix-blank left"></div>';
        	c += '	<div class="clear"></div>';
        	c += '</div>';

        } else {
        	//编辑模式
        	c += '<div class="question-answer"><span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span></div>';
        }
        return c;
    },
    _edit: function(data) {
        var e = '<div class="edit_item_warp"><span class="edit_item_label">后缀：<a href="#" data-for-id="help_suffix" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/></div>';
            e += '<div class="edit_item_warp"><span class="edit_item_label">后缀链接：</span>  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffixUrl\')" value="' + data.suffixUrl + '"/></div>';

            e += '</div>';
        return e;
    }
});