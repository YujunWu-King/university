/**
 * Created by WRL on 2015/5/19.
 * 报名表图片上传
 */
SurveyBuild.extend("imagesUpload", "baseComponent", {
    itemName: "图片上传",
    title:"图片上传",
	fileType: "jpg,png,jpeg",//允许上传类型
    fileSize: "1",//允许上传大小
    isAllowTailoring:"N",   //是否允许裁剪
    tailoringStandard:"",   //裁剪类型
	allowMultiAtta: "Y",//允许多附件上传
	isDownLoad:"Y",//允许打包下载
	"StorageType":"F",//存储类型-附件
	children: [{"itemId":"attachment_Upload","itemName":"图片上传","title":"图片上传","orderby":"","fileName":"","sysFileName":"","path":"","viewFileName":""}],

    _getHtml: function(data, previewmode) {
        var c = "", val = data.value ? data.value: data.defaultval;
		var children = data.children;
        if (previewmode) {

            c = '<div class="main_inner_content_info_autoheight">';
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
					if (children[i].viewFileName != "" && children[i].sysFileName != ""){
						c += '<li><a class="main_inner_filelist_a" onclick=SurveyBuild.viewImageSet(this,"'+data.instanceId+'") file-index="'+children[i].orderby+'">'+children[i].viewFileName+'</a>';
						c += '<div class="main_inner_file_del" onclick=SurveyBuild.deleteFile(this,"'+data.instanceId+'")><img width="15" height="15" src="/onlineReg/images/del.png" title="'+MsgSet["DEL"]+'">'+MsgSet["DEL"]+'</div></li>';
					}
				}
				c += '</ul></div>';	
				c += '</div>';
			} else {
				c += '<div class="main_inner_single_file_list"><ul>';
				c += '<li><a class="main_inner_file_a" id="'+data.itemId+'_A" file-index="1" onclick=SurveyBuild.viewImageSet(this,"'+data.instanceId+'")>'+ (children[0].viewFileName != "" && children[0].sysFileName != ""? children[0].viewFileName: "") +'</a>';
				c += '<div class="main_inner_file_del" onclick=SurveyBuild.deleteFile(this,"'+data.instanceId+'") style="display:'+(children[0].viewFileName != "" && children[0].sysFileName != "" ? "":"none")+'"><img width="15" height="15" src="/onlineReg/images/del.png" title="'+MsgSet["DEL"]+'">'+MsgSet["DEL"]+'</div></li>';
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
				c += '<div class="main_inner_content_tips_3line" style="clear:left;padding-left:140px;height:auto; margin-bottom: 10px;"><p><span class="font_orange_16px">';
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
            c += '<div class="question-answer"><button class="btn btn-small"><i class="icon-upload-alt"></i>图片上传</button><span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span></div>';
        }

        return c;
    },
    _edit: function(data) {
        var e = '<div class="edit_item_warp">';
        e += '<span class="edit_item_label">后缀：<a data-animation="fade" data-reveal-id="myModal" class="big-link" onclick="SurveyBuild.showMsg(this,event)" data-for-id="help_suffix" href="#">(?)</a></span>';
        e += '  <input type="text" value="' + data.suffix + '" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" class="medium">';
        e += '</div>';

        e += '<div class="edit_item_warp"><span class="edit_item_label">后缀链接：</span>  <input type="text" value="' + data.suffixUrl + '" onkeyup="SurveyBuild.saveAttr(this,\'suffixUrl\')" class="medium"></div>';


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
		/*
        e += '<div class="edit_item_warp"><input type="checkbox" ' + (data.isAllowTailoring == "Y" ? "checked='checked'": "") + ' id="is_AllowTailoring" onchange="SurveyBuild.saveAttr(this,\'isAllowTailoring\')">允许裁剪</div>';

        e += '<div class="edit_item_warp"><span class="edit_item_label"></span>';
        e += '<select onchange="SurveyBuild.saveAttr(this,\'tailoringStandard\')" id="is_preg" style="width:200px" class="edit_tailoringType">';
        e += '<option></option>';
        e += '<option value="A" ' + (data.tailoringStandard == "A" ? ' selected="selected"': "") + '>证件照</option><option value="B" ' + (data.tailoringStandard == "B" ? ' selected="selected"': "") + '>一寸标准照</option>';
        e += '<option value="C" ' + (data.tailoringStandard == "C" ? ' selected="selected"': "") + '>两寸标准照</option><option value="D" ' + (data.tailoringStandard == "D" ? ' selected="selected"': "") + '>任意</option>';
        e += '</select>';
        e += '</div>';
		*/
		e += '<div class="edit_item_warp" style="margin-top:5px;"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'allowMultiAtta\')"' + (data.allowMultiAtta == "Y" ? "checked='checked'": "") + ' id="is_allowMultiAtta"> <label for="is_allowMultiAtta">允许多图片上传</label></div>';
		e += '<div class="edit_item_warp mb10" style="margin-top:5px;"><input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isDownLoad\')"' + (data.isDownLoad == "Y" ? "checked='checked'": "") + ' id="is_allowDownload"> <label for="is_allowDownload">允许打包下载</label></div>';
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