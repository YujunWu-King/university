/*====================================================================
+ 功能描述：在线调查图片上传													++
+ 开发人：张浪															++
+ 开发日期：2015-10-15												++
=====================================================================*/
SurveyBuild.extend("ImgUpload", "baseComponent", {
    itemName: "图片上传",
    title:"图片上传",
	fileType: "jpg,png,jpeg",//允许上传类型
    fileSize: "1",//允许上传大小
	allowMultiAtta: "Y",//允许多附件上传
	isDownLoad:"Y",//允许打包下载
	"StorageType":"F",//存储类型-附件
	children: [{"itemId":"Image_upload","itemName":"图片上传","title":"图片上传","orderby":"","fileName":"","sysFileName":"","path":"","viewFileName":""}],

    _getHtml: function(data, previewmode) {
        var c = "",t = "", val = data.value ? data.value: data.defaultval;
		var children = data.children;
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
				//p = data.fileType != "" ? "请上传格式为"+data.fileType+"的文件" : "";
                p = data.fileType != "" ? MsgSet["FILETYPE"].replace("【TZ_FILE_TYPE】",data.fileType) : "";

				//p2 = data.fileSize != "" ? (p != "" ? p+"，" : "") + "文件大小为"+data.fileSize+"M以内" : p;
                p2 = data.fileSize != "" ? (p != "" ? p : "") + " " + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",data.fileSize) : p;
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
				//显示已上传图片
				for(var i=0; i<children.length; i++){
					if(children[i].sysFileName != ""){
						c += '<li><a onclick="SurveyBuild.viewImageSet(this,\''+data.instanceId+'\')" file-index="'+ children[i].orderby +'">'+ children[i].viewFileName +'</a>';
						c += '	<div class="img_del" onclick="SurveyBuild.deleteFile(this,\''+data.instanceId+'\')"><img src="/onlineSurvey/images/del.png" width="15" height="15"  title="'+MsgSet["DEL"]+'">&nbsp;'+MsgSet["DEL"]+'<div>';
						c += '</li>';
					}
				}
				c += '			</ul>';
                                c += '         <div class="answer">'+(data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a></div>" : "</div>");
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
				c += '		<span class="upload-icon"><img src="/onlineSurvey/images/m/addImg.png"></span><span class="upload-text">'+MsgSet["CHOOSE_PICTURE"]+'</span>';
				c += '		<label for="'+ data.itemId +'"></label>';
				c += '		<input type="file" accept="image/*" id="'+ data.itemId +'" name="'+ data.itemId +'" onchange="SurveyBuild.uploadAttachment(this,\''+ data.instanceId +'\')"/>';
				c += '	</div>';
                                c += '     <span >'+(data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "</span>");

                //图片集
				c += '	<div id="'+ data.itemId +'_attList" class="answer file-upload-list '+ (children.length>0 && children[0].sysFileName != "" ? "":"noUpload") +'">';
				c += '		<ul>';
				//显示已上传的图片
				for(var i=0; i<children.length; i++){
					if(children[i].sysFileName != ""){
						c += '<li><a onclick="SurveyBuild.viewImageSet(this,\''+data.instanceId+'\')" file-index="'+ children[i].orderby +'">'+ children[i].viewFileName +'</a><img src="/onlineSurvey/images/m/del.png" onclick="SurveyBuild.deleteFile(this,\''+data.instanceId+'\')" title="'+MsgSet["DEL"]+'">'+MsgSet["DEL"]+'</li>';
					}
				}
				c += '		</ul>';
				c += '	</div>';
				c += '</div>';
			}
        } else {
            c += '<div class="question-answer"><button class="btn btn-small"><i class="icon-upload-alt"></i>'+MsgSet["CHOOSE_PICTURE"]+'</button><span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span></div>';
        }
        return c;
    },
    _edit: function(data) {
        var e = '<div class="edit_item_warp">';
        e += '<span class="edit_item_label">后缀：<a data-animation="fade" data-reveal-id="myModal" class="big-link" onclick="SurveyBuild.showMsg(this,event)" data-for-id="help_suffix" href="#">(?)</a></span>';
        e += '  <input type="text" value="' + data.suffix + '" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" class="medium">';
        e += '<div class="edit_item_warp"><span class="edit_item_label">后缀链接：</span>  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffixUrl\')" value="' + data.suffixUrl + '"/></div>';
        e += '</div>';

        e += '<div class="edit_paramSet mt10"><span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
		e += '<div class="groupbox">';
        e += '<div class="edit_item_warp"><span class="edit_item_label">格式控制：</span>';
        e += '  <input type="text" value="' + data.fileType + '" onkeyup="SurveyBuild.saveAttr(this,\'fileType\')" data_id="' + data.instanceId + '"  class="medium edit_attaType">';
        e += '</div>';
        e += '<div><span style="margin-left: 103px; font-size: 12px; color: #FF0000">文件格式以“,”分隔</span></div>';
		
        e += '<div class="edit_item_warp"><span class="edit_item_label">大小控制：</span>';
        e += '  <input type="text" value="' + data.fileSize + '" onkeyup="SurveyBuild.saveAttr(this,\'fileSize\')" data_id="' + data.instanceId + '" style="width: 175px;" class="medium edit_attSize">';
        e += ' <span>M</span>';
		e += '<div><span style="margin-left: 103px; font-size: 12px; color: #FF0000">输入允许上传的最大文件大小</span></div>';
        e += '</div>';
		e += '<div class="edit_item_warp" style="margin-top:5px;"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'allowMultiAtta\')"' + (data.allowMultiAtta == "Y" ? "checked='checked'": "") + ' id="is_allowMultiAtta"> <label for="is_allowMultiAtta">允许多图片上传</label></div>';
		/*e += '<div class="edit_item_warp mb10" style="margin-top:5px;"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isDownLoad\')"' + (data.isDownLoad == "Y" ? "checked='checked'": "") + ' id="is_allowDownload"> <label for="is_allowDownload">允许打包下载</label></div>';*/
		e += '</div>';
		e += '</div>';

        e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
        e += ' <div style="margin-top: 5px;" class="edit_item_warp"><input class="mbIE" type="checkbox" id="is_require" ' + (data.isRequire == "Y" ? "checked='checked'": "") + ' onchange="SurveyBuild.saveAttr(this,\'isRequire\')"> <label for="is_require">是否必填</label></div>';
		e += ' </div>';
		
        e += '<div class="edit_item_warp"><a onclick="SurveyBuild.RulesSet(this);" href="javascript:void(0);"><i class="icon-cogs"></i> 高级设置</a></div>';
        e += '</div>';

        return e;
    },
    _validatorAttr: function(data) {
		var _result = true;
		var $edit_attaType = $("#question-edit .edit_attaType");
		if(data.fileType != ""){
			_result = true;
			var allType = ['BMP','JPG','JPEG','PNG','GIF'];
			var typeArr = data.fileType.split(",");
			for(var i=0;i<typeArr.length;i++){
				if(allType.indexOf(typeArr[i].toUpperCase()) == -1){
					SurveyBuild.fail($edit_attaType, "请填写('BMP','JPG','JPEG','PNG','GIF')中的图片格式！");
					return false;
					_result = false;
					break;	
				}		
			}
		}else{
			SurveyBuild.fail($edit_attaType, "允许上传的图片格式必须填写！");
			return false;
			_result = false;
		}
		
		var $edit_attSize = $("#question-edit .edit_attSize");
        if (data.fileSize != "") {
            if (isNaN(data.fileSize)) {
                SurveyBuild.fail($edit_attSize, "允许上传的图片大小必须填写数字！");
                return false;
				_result = false;
            }else{
				_result = true;
			}
        }else{
			SurveyBuild.fail($edit_attSize, "允许上传的图片大小必须填写！");
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