/*====================================================================
+ 功能描述：附件上传控件，可控制上传附件的格式及附件大小						++
+ 开发人：张浪															++
+ 开发日期：2015-05-04												++
=====================================================================*/
SurveyBuild.extend("AttachmentUpload", "baseComponent", {
    itemName: "附件上传",
	title:"附件上传",
	fileType: "docx,txt",//允许上传类型
    fileSize: "2",//允许上传大小
	allowMultiAtta: "Y",//允许多附件上传
	isDownLoad:"Y",//允许打包下载
	isOnlineShow:"N",//PDF在线阅读
	"StorageType":"F",//存储类型-附件
	children: [{"itemId":"attachment_Upload","itemName":"附件上传","title":"附件上传","orderby":"","fileName":"","sysFileName":"","path":"","viewFileName":""}],

    _getHtml: function(data, previewmode) {
        var c = "", val = data.value ? data.value: data.defaultval;
		var children = data.children;
		var _sysFilename,_fileSuffix;
        if (previewmode) {
			
			c = '<div class="main_inner_content_info_autoheight" id="upload_' + data.itemId + '">';
            c += '<div class="main_inner_connent_info_left"><span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
			c += '<div class="main_inner_content_info_right" style="height:auto;position: relative;">';
			c += '<div class="file_upload_button"><div class="bt_blue">'+MsgSet["UPLOAD_BTN_MSG"]+'</div>';
			c += '<input data-instancid = "' + data.instanceId + '" id="'+data.itemId+'" name="'+data.itemId+'" title="'+data.itemName+'" class="fileupload_input" type="file" style="width:80px;" onchange="SurveyBuild.uploadAttachment(this,\''+ data.instanceId +'\')">';
			c += '<div style="margin-left:426px;position: absolute;"><div id="' + data.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow"></div></div></div>';
			c += '</div>';
			
			
			//多附件列表
			if(data.allowMultiAtta == "Y"){
				
				c += '<div class="main_inner_file_list" id="'+data.itemId+'_AttList"><ul>';
				for(var i=0; i<children.length; i++){
					_sysFilename = children[i].sysFileName;
					_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
					
					if (children[i].viewFileName != "" && children[i].sysFileName != ""){
						c += '<li style="clear:both;"><a class="main_inner_filelist_a" onclick=SurveyBuild.downLoadFile(this,"'+data.instanceId+'") file-index="'+children[i].orderby+'">'+children[i].viewFileName+'</a>';
						c += '<div class="main_inner_file_del" onclick=SurveyBuild.deleteFile(this,"'+data.instanceId+'")><img width="15" height="15" src="/onlineReg/images/del.png" title="'+MsgSet["DEL"]+'">'+MsgSet["DEL"]+'</div>';
						c += (_fileSuffix == "PDF" && data.isOnlineShow == "Y" ? "<div class='main_inner_pdf_reader' onclick=SurveyBuild.PDFpreview(this,\""+data.instanceId+"\") file-index='"+children[i].orderby+"'><img src='/onlineReg/images/preview.png' title='"+MsgSet["PDF_VIEW"]+"'/>&nbsp;</div>":"")+'</li>';
					}
				}
				c += '</ul></div>';	
				c += '</div>';
			} else {
				_sysFilename = children[0].sysFileName;
				_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
					
				c += '<div class="main_inner_single_file_list"><ul>';
				c += '<li><a class="main_inner_file_a" id="'+data.itemId+'_A" file-index="1" onclick=SurveyBuild.downLoadFile(this,"'+data.instanceId+'")>'+ (children[0].viewFileName != "" && children[0].sysFileName != "" ? children[0].viewFileName: "") +'</a>';
				c += '<div class="main_inner_file_del" onclick=SurveyBuild.deleteFile(this,"'+data.instanceId+'") style="display:'+(children[0].viewFileName != "" && children[0].sysFileName != "" ? "":"none")+'"><img width="15" height="15" src="/onlineReg/images/del.png" title="'+MsgSet["DEL"]+'">'+MsgSet["DEL"]+'</div>';
				c += (_fileSuffix == "PDF" && data.isOnlineShow == "Y" ? "<div class='main_inner_pdf_reader' onclick=SurveyBuild.PDFpreview(this,\""+data.instanceId+"\") file-index='1'><img src='/onlineReg/images/preview.png' title='"+MsgSet["PDF_VIEW"]+"'/>&nbsp;</div>":"")+'</li>';
				c += '</ul></div>';
				c += '</div>';	
			}
			c += '</div>';
			//后缀
			c += '<div class="main_inner_content_suffix">'+(data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a></div>" : "</div>");
			
			if(data.fileType != "" || data.fileSize != ""){
				var strFileFormat = data.fileType;
				var typeArr = strFileFormat.split(",");
				var strFileType = "";
				for(var i=0;i<typeArr.length;i++){
					if (SurveyBuild.BMB_LANG == "ENG"){
						strFileType = strFileType + typeArr[i] + ",";
					} else {
						strFileType = strFileType + "." + typeArr[i] + "、";
					}	
				}
				if (strFileType != ""){
					strFileType = strFileType.substring(0,strFileType.length-1);	
				}
				c += '<div id="desc' + data.itemId + '" class="main_inner_content_tips_3line" style="clear:left;padding-left:140px;height:auto; margin-bottom: 10px;"><p><span class="font_orange_16px">';
				p = strFileType != "" ? MsgSet["FILETYPE"].replace("【TZ_FILE_TYPE】",strFileType) : "";
				if (SurveyBuild.BMB_LANG == "ENG"){
					p2 = data.fileSize != "" ? (p != "" ? p : "") + " " + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",data.fileSize) : p;
				}else{
					p2 = data.fileSize != "" ? (p != "" ? p : "") + "，" + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",data.fileSize) : p;
				}
				
				c += p2;
				c += '</span></p></div>';
			}
			
			
        } else {
			 c += '<div class="question-answer"><button class="btn btn-small"><i class="icon-upload-alt"></i>附件上传</button><span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span></div>';
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
		var $uplBtn = $fileInput.prev(".bt_blue");

		$fileInput.mouseover(function(e){
			$uplBtn.css("opacity","0.8");	
		});
		
		$fileInput.mouseout(function(e) {
            $uplBtn.css("opacity","1");
        });
		
		$fileInput.formValidator({tipID:(data["itemId"]+'Tip'), onShow:"", onFocus:"&nbsp;", onCorrect:"&nbsp;"});
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
						//if ($.inArray(classname, SurveyBuild._baseRules) == -1 && data["rules"][classname]["isEnable"] == "Y") {
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