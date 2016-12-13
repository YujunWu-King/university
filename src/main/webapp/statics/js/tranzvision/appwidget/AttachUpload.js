/*====================================================================
+ 功能描述：附件上传控件，可控制上传附件的格式及附件大小(在线调查附件上传)++
+ 开发人：李丹丹
+ 修改人：曹阳													++
+ 开发日期：2015-11-18												++
+ 修改日期：2016-12-08
=====================================================================*/
SurveyBuild.extend("AttachUpload", "baseComponent", {
    itemName: "附件上传",
	title:"附件上传",
	fileType: "docx,txt",//允许上传类型
    fileSize: "2",//允许上传大小
	allowMultiAtta: "Y",//允许多附件上传
	isDownLoad:"Y",//允许打包下载
	isOnlineShow:"N",//PDF在线阅读
	"StorageType":"F",//存储类型-附件
	children: [{"itemId":"attach_Upload","itemName":"附件上传","title":"附件上传","orderby":"","fileName":"","sysFileName":"","accessPath":"","viewFileName":""}],

    _getHtml: function(data, previewmode) {
        var c = "", val = data.value ? data.value: data.defaultval;
        var c = "",t = "", val = data.value ? data.value: data.defaultval;
		var children = data.children;
		var _sysFilename,_fileSuffix;
        if (previewmode) {
            if(data.fileType != "" || data.fileSize != ""){
                var strFileFormat = data.fileType;
                var typeArr = strFileFormat.split(",");
                var strFileType = "";
                for(var i=0;i<typeArr.length;i++){
                    strFileType = strFileType + "." + typeArr[i] + "、";
                }
                if (strFileType != ""){
                    strFileType = strFileType.substring(0,strFileType.length-1);
                }
                t += '<span style="color:#FF0000">(';

                p = data.fileType != "" ? MsgSet["FILETYPE"].replace("【TZ_FILE_TYPE】",data.fileType) : ""; //p = data.fileType != "" ? "请上传格式为"+data.fileType+"的文件" : "";
                p2 = data.fileSize != "" ? (p != "" ? p : "") + " " + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",data.fileSize) : p; //p2 = data.fileSize != "" ? (p != "" ? p+"，" : "") + "文件大小为"+data.fileSize+"M以内" : p;
                t += p2;
                t += ')</span>';
            }
            if(SurveyBuild.accessType == "P"){
                //PC版
                c += '<div class="listcon">';
                c += '	<div class="question">';
                c += '		<span class="fontblue-blod">'+ data.qCode +'.</span>' + data.title + t;
                c += '      <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '          <div class="onShow"></div>';
                c += '      </div>';
                c += '	</div>';
                c += '	<div class="answer">';
                c += '		<div class="fileinput-button blue_btn">';
                c += '			<label for="'+ data.itemId +'">'+MsgSet["UPLOAD_BTN_MSG"]+'</label>';
                c += '			<input id="'+ data.itemId +'" type="file" name="'+ data.itemId +'" onchange="SurveyBuild.uploadAttachment(this,\''+ data.instanceId +'\')">';
                c += '		</div>';

                c += '		<div class="file_list '+ (children[0].sysFileName == "" ? "nobackground":"") +'" id="'+ data.itemId +'_attList">';
                c += '			<ul>';
                //显示已上传附件
                for(var i=0; i<children.length; i++){
                    if(children[i].sysFileName != ""){
                        c += '<li><a onclick="SurveyBuild.downLoadFile(this,\''+data.instanceId+'\')" file-index="'+ children[i].orderby +'">'+ children[i].viewFileName +'</a>';
                        c += '	<div class="img_del" onclick="SurveyBuild.deleteFile(this,\''+data.instanceId+'\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" width="15" height="15"  title="'+MsgSet["DEL"]+'">&nbsp;'+MsgSet["DEL"]+'<div>';
                        c += (_fileSuffix == "PDF" && data.isOnlineShow == "Y" ? "<div class='main_inner_pdf_reader' onclick=SurveyBuild.PDFpreview(this,\""+data.instanceId+"\") file-index='"+children[i].orderby+"'><img src='" + TzUniversityContextPath + "/statics/images/appeditor/preview.png' title='"+MsgSet["PDF_VIEW"]+"'/>&nbsp;</div>":"")+'</li>';
                    }
                }
                c += '			</ul>';
                c += '        <div class="answer">'+(data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a></div>" : "</div>");
                c += '		</div>';
                c += '	</div>';
                c += '</div>';
            } else {
                //手机版
                c += '<div class="listcon">';
                c += '  <div id="' + data.itemId + 'Tip" class="onShow">';
                c += '      <div class="onShow"></div>';
                c += '  </div>';
                c += '	<div class="question">';
                c += '		<span class="fontblue-blod">'+ data.qCode +'.</span>' + data.title + t;
                c += '	</div>';
                c += '	<div class="file-upload" style="margin-top:10px;">';
                c += '		<span class="file_upload_button"></span><span class="upload-text">'+MsgSet["CHOOSE_ATTACH"]+'</span>';
                c += '		<label for="'+ data.itemId +'"></label>';
                c += '		<input type="file" accept="image/*" id="'+ data.itemId +'" name="'+ data.itemId +'" onchange="SurveyBuild.uploadAttachment(this,\''+ data.instanceId +'\')"/>';
                c += '	</div>';
                c += '     <span >'+(data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "</span>");
                //附件列表
                c += '	<div id="'+ data.itemId +'_attList" class="answer file-upload-list '+ (children.length>0 && children[0].sysFileName != "" ? "":"noUpload") +'">';
                c += '		<ul>';
                //显示已上传附件
                for(var i=0; i<children.length; i++){
                    if(children[i].sysFileName != ""){
                        c += '<li><a onclick="SurveyBuild.downLoadFile(this,\''+data.instanceId+'\')" file-index="'+ children[i].orderby +'">'+ children[i].viewFileName +'</a>';
                        c += '	<img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" width="15" height="15"  title="'+MsgSet["DEL"]+'" onclick="SurveyBuild.deleteFile(this,\''+data.instanceId+'\')">&nbsp;'+MsgSet["DEL"]+'<div>';
                        c += (_fileSuffix == "PDF" && data.isOnlineShow == "Y" ? "<div class='main_inner_pdf_reader' onclick=SurveyBuild.PDFpreview(this,\""+data.instanceId+"\") file-index='"+children[i].orderby+"'><img src='" + TzUniversityContextPath + "/statics/images/appeditor/preview.png' title='预览'/>&nbsp;</div>":"")+'</li>';
                    }
                }
                c += '		</ul>';
                c += '	</div>';
                c += '</div>';
            }
        } else {
            c += '<div class="question-answer"><button class="btn btn-small"><i class="icon-upload-alt"></i>'+MsgSet["CHOOSE_ATTACH"]+'</button><span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span></div>';
        }
        return c;
    },
    _edit: function(data) {
        var e = '<div class="edit_item_warp"><span class="edit_item_label">后缀：<a href="#" data-for-id="help_suffix" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/></div>';
        e += '<div class="edit_item_warp"><span class="edit_item_label">后缀链接：</span>  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffixUrl\')" value="' + data.suffixUrl + '"/></div>';

        e += '<div class="edit_paramSet mt10"><span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
		e += '<div class="groupbox">';
		e += '<div class="edit_item_warp"><span class="edit_item_label">格式控制：</span>  <input type="text" class="medium edit_attaType" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'fileType\')" value="' + data.fileType + '"/></div>';
		e += '<div><span style="margin-left:103px; font-size:12px; color:#FF0000">文件格式以“,”分隔<span></div>';
		
		e += '<div class="edit_item_warp"><span class="edit_item_label">大小控制：</span>  <input type="text" class="medium edit_attSize" style="width:175px;" data_id="' + data.instanceId + '" onkeyup="SurveyBuild.saveAttr(this,\'fileSize\')" value="' + data.fileSize + '"/> <span>M</span></div>';
		e += '<div><span style="margin-left: 103px; font-size: 12px; color: #FF0000">输入允许上传的最大文件大小</span></div>';
		
		e += '<div class="edit_item_warp" style="margin-top:5px;"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'allowMultiAtta\')"' + (data.allowMultiAtta == "Y" ? "checked='checked'": "") + ' id="is_allowMultiAtta"> <label for="is_allowMultiAtta">允许多附件上传</label></div>';
		e += '<div class="edit_item_warp" style="margin-top:5px;"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isDownLoad\')"' + (data.isDownLoad == "Y" ? "checked='checked'": "") + ' id="is_allowDownload"> <label for="is_allowDownload">允许打包下载</label></div>';
		e += '<div class="edit_item_warp mb10" style="margin-top:5px;"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isOnlineShow\')"' + (data.isOnlineShow == "Y" ? "checked='checked'": "") + ' id="is_allowOnlineShow"> <label for="is_allowOnlineShow">PDF在线阅读</label></div>';
		e += '</div>';
		
		e += '</div>';

        e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
		e += '<div style="margin-top: 5px;" class="edit_item_warp">';
		e += '<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'": "") + ' id="is_require"> <label for="is_require">是否必填</label>';
		e += '</div>';
		e += '</div>';
        e += '<div class="edit_item_warp"><a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a></div>';
        e += '</div>';

        return e;
    },
	
    _validatorAttr: function(data) {
		var _result = true;
        var $edit_attaType = $("#question-edit .edit_attaType");
		if(data.fileType != ""){
			_result = true;
		}else{
			SurveyBuild.fail($edit_attaType, "允许上传的文件格式必须填写！");
			return false;
			_result = false;
		}
		
		var $edit_attSize = $("#question-edit .edit_attSize");
        if (data.fileSize != "") {
            if (isNaN(data.fileSize)) {
                SurveyBuild.fail($edit_attSize, "允许上传的文件大小必须填写数字！");
                return false;
				_result = false;
            }else{
				_result = true;
			}
        }else{
			SurveyBuild.fail($edit_attSize, "允许上传的文件大小必须填写！");
			return false;	
			_result = false;
		}
		return _result;
    },
	
	_eventbind: function(data) {
        var $fileInput = $("#" + data.itemId);

        $fileInput.formValidator({tipID:(data["itemId"]+'Tip'), onShow:"&nbsp;", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
        $fileInput.functionValidator({
            fun:function(val,el){
                if (data.isRequire == "Y"){
                    if (data.children.length > 1){
                        return 	true;
                    } else if (data.children.length == 1 && data.children[0].fileName != ""){
                        return true;
                    } else {
                        return MsgSet["FILE_UPL_REQUIRE"];
                    }
                }
            }
        });

        $fileInput.functionValidator({
            fun:function(val,elem){

                //执行高级设置中的自定义规则
                /*********************************************\
                 ** 注意：自定义规则中不要使用formValidator **
                 \*********************************************/
                var _result = true;
                if (ValidationRules) {
                    $.each(data["rules"],function(classname, classObj) {
                        //附件上传不需要在高级规则中的必选判断
                        if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y" && classname!="RequireValidator") {
                            var _ruleClass = ValidationRules[classname];
                            if (_ruleClass && _ruleClass._Validator) {
                                _result = _ruleClass._Validator(data["itemId"], classObj["messages"]);
                                if(_result !== true){
                                    return false;
                                }
                            }
                        }
                    });
                    if(_result !== true){
                        return _result;
                    }
                }
                return _result;
            }
        });
	}
});