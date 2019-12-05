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
	children: [{"itemId":"attachment_Upload","itemName":"附件上传","title":"附件上传","orderby":"","fileName":"","sysFileName":"","accessPath":"","viewFileName":""}],

    _getHtml: function(data, previewmode) {
        var c = "", val = data.value ? data.value: data.defaultval;
		var children = data.children;
		var _sysFilename,_fileSuffix;
        if (previewmode) {
        	if(data.fileType != "" || data.fileSize != ""){
        		var typeArr = data.fileType.split(",");
        		var fileType = "";
        		for(var i = 0; i < typeArr.length; i++){
        			if (SurveyBuild.BMB_LANG == "ENG"){
        				fileType = fileType + typeArr[i] + ",";
        			} else {
        				fileType = fileType + "." + typeArr[i] + "、";
        			}	
        		}
        		if (fileType != ""){
        			fileType = fileType.substring(0,fileType.length-1);
        		}

        		typeMsg = fileType != "" ? MsgSet["FILETYPE"].replace("【TZ_FILE_TYPE】",fileType) : "";
        		if (SurveyBuild.BMB_LANG == "ENG"){
        			msg = data.fileSize != "" ? (typeMsg != "" ? typeMsg : "") + " " + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",data.fileSize) : typeMsg;
        		}else{
        			msg = data.fileSize != "" ? (typeMsg != "" ? typeMsg : "") + "，" + MsgSet["FILESIZE"].replace("【TZ_FILE_SIZE】",data.fileSize) : typeMsg;	
        		}
        	}
        	if(SurveyBuild.accessType == "M"){
        		c += '<div class="item">';
				c += '	<p>'+data.title+'<span>'+(data.isRequire == "Y" ? "*": "")+'</span></p>';
				c += '  <div id="' + data.itemId + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
                c += ' 	<div class="text-box" style="border:none;display:' + (SurveyBuild._readonly?'none':'block') +' " >';
                c += '		<div class="handle">';
                c += '   		<div class="ncsc-upload-btn">';
                c += '    			 <a href="#" class="ncsc-upload-btn-a">';
			    c += '					<span class="ncsc-upload-btn-span">'; 
			    c +=' 						<input type="file" hidefocus="true" size="1" class="input-file" name="goods_image" data-instancid="' + data.instanceId + '" id="'+ data.itemId + '" name="'+ data.itemId + '" title="' + data.itemName + '" onchange="SurveyBuild.uploadAttachment(this,\''+ data.instanceId +'\')"  ></span>';
			    c +='   				<div class="ncsc-upload-btn-p">'+ MsgSet["UPLOAD_BTN_MSG"] +'<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png'+'"></div>';
			    c +='      			</a>';  
			    c +='         	</div>';
			    c +='       </div>';
			    c +='   </div>';
			    c +='<p style="color:#666;font-size:0.56rem;margin-top:5px;display:' + (SurveyBuild._readonly?'none':'block') +' ">'+msg+'</p>';
			    c +='</div>';
			    if(children[0].viewFileName==""){
			    	c += ' <div class="upload_list" id="'+data.itemId+'_AttList" style="display:none">';
					c += ' <p style="display:' + (SurveyBuild._readonly?'none':'') +'">'+ MsgSet["UP_FILE_LIST"] +'</p>';
					c += '</div>';
			    }else{
			    c += ' <div class="upload_list"  id="'+data.itemId+'_AttList" style="display:' + (children.length < 1 ? 'none':'block') + '">';
				c += ' <p style="display:' + (SurveyBuild._readonly?'none':'') +'">'+ MsgSet["UP_FILE_LIST"] +'</p>';
			    	if(data.allowMultiAtta == "Y"){
	        	   		for(var i = 0; i < children.length; i++){
	        	   			if (children[i].viewFileName != "" && children[i].sysFileName != ""){
		        				c += '<li class="fileLi"><span><a id="img_'+data.itemId+'_'+i+'"   onclick="SurveyBuild.viewImageSet(this,\'' + data.instanceId + '\',\'' + i + '\')" file-index="' + children[i].orderby + '">'+ children[i].viewFileName+'</a></span><i  onclick="SurveyBuild.deleteFile(this,\'' + data.instanceId + '\')" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center""></i></li>';
	        	   			}
	        	   		}
			    	}else{
			    		for(var i=0; i<children.length; i++){
		        			if (children[i].viewFileName != "" && children[i].sysFileName != ""){
        				c += '<li class="fileLi"><span><a  id="img_'+data.itemId+'_'+i+'"   onclick="SurveyBuild.viewImageSet(this,\'' + data.instanceId + '\',\'' + i + '\')" file-index="' + children[i].orderby + '">'+ children[i].viewFileName+'</a></span><i  onclick="SurveyBuild.deleteFile(this,\'' + data.instanceId + '\')" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center""></i></li>';
		        			}
			    		}
			    	}
			    	c +='</div>';	
			    }	
			    c += '<div class="img_shade" id ="shade_'+data.itemId+'" style="display:none"></div>';
			    c += '<img class="img_pop_close" style="display:none"id ="close_'+data.itemId+'" src="'+ TzUniversityContextPath + '/statics/images/appeditor/m/rl_btn.png'+'">';
			    c += '<div class="img_pop_body" style="display:none" id ="body_'+data.itemId+'">'  ;
			    c += ' <img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/" id ="img_'+data.itemId+'">';
			    c += '</div>';
        	}else{c += '<div class="input-list-blank margart15" id="upload_' + data.itemId + '">';
        	c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
        	c += '   	<div class="input-list-texttemplate left" style="display:' + (SurveyBuild._readonly?'none':'block') + '">';
        	c += '		<div>' + data.onShowMessage + '</div>';
        	c += '		<div class="filebtn left">';
        	c += '			<div class="filebtn-org"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" />&nbsp;&nbsp;' + MsgSet["UPLOAD_BTN_MSG"] + '</div>';
        	c += '			<input type="file" class="filebtn-orgtext" data-instancid = "' + data.instanceId + '" id = "' + data.itemId + '" name = "' + data.itemId + '" title="' + data.itemName + '" onchange="SurveyBuild.uploadAttachment(this,\'' + data.instanceId + '\')">';
        	c += '		</div>';
			/*c += '		<div class="file-list-suffix">'+ data.suffix +'</div>';*/
			c += '	<div class="file-list-suffix" style="display:' + (SurveyBuild._readonly?'none':'block') + '">' + (data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "") +'</div>';
			c += '		<div class="clear"></div>';
        	c += '		<div>' + msg + '<div id="' + data.itemId + 'Tip" class="onShow" style="line-height:32px;height:18px;"><div class="onShow"></div></div></div>';
        	c += '	</div>';
        	c += '	<div class="input-list-info-blank left" style="display:' + (SurveyBuild._readonly?'none':'block') + '"><span class="red"></div>';                
        	c += '   	<div class="input-list-upload left">';
        	c += '		<div class="input-list-upload-con" id="'+data.itemId+'_AttList" style="display:' + (children.length < 1 ? 'none':'black') + '">';
        	if(data.allowMultiAtta == "Y"){
    	   		for(var i = 0; i < children.length; i++){
    	   			_sysFilename = children[i].sysFileName;
    	   			_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
    	   			if (children[i].viewFileName != "" && children[i].sysFileName != ""){
    	   				c += '<div class="input-list-uploadcon-list">';
    	   				c += '	<div class="input-list-uploadcon-listl left">';
    	   				c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFile(this,"' + data.instanceId + '") file-index = "' + children[i].orderby + '">' + children[i].viewFileName + '</a>';
    	   				if(_fileSuffix == "PDF" && data.isOnlineShow == "Y"){
    	   					c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreview(this,\'' + data.instanceId + '\')" file-index = "' + children[i].orderby + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
    	   				}
    	   				c +='	</div>';
    	   				/*c += '	<div class="input-list-uploadcon-listr left" style="display:' + (SurveyBuild._readonly?'none':'block') + '"><button class="upload-del" onclick="SurveyBuild.deleteFile(this,\'' + data.instanceId + '\')">' + MsgSet["DEL"] + '</button></div>';*/
						c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFile(this,\'' + data.instanceId + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
    	   				c += '	<div class="clear"></div>';
    	   				c += '</div>';
    	   			}
    	   		}
        	}else{
        	   		for(var i = 0; i < children.length; i++){
        	   			_sysFilename = children[i].sysFileName;
        	   			_fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
        	   			if (children[i].viewFileName != "" && children[i].sysFileName != ""){
        	   				c += '<div class="input-list-uploadcon-list">';
        	   				c += '	<div class="input-list-uploadcon-listl left">';
        	   				c += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.downLoadFile(this,"' + data.instanceId + '") file-index = "' + children[i].orderby + '">' + children[i].viewFileName + '</a>';
        	   				if(_fileSuffix == "PDF" && data.isOnlineShow == "Y"){
        	   					c += '<div class="input-list-uploadcon-list-pdf" onclick="SurveyBuild.PDFpreview(this,\'' + data.instanceId + '\')" file-index = "' + children[i].orderby + '">&nbsp;&nbsp;<img src="' + TzUniversityContextPath + '/statics/images/appeditor/preview.png" title="' + MsgSet["PDF_VIEW"] + '"/>&nbsp;</div>';
        	   				}
        	   				c +='	</div>';
        	   				/*c += '	<div class="input-list-uploadcon-listr left" style="display:' + (SurveyBuild._readonly?'none':'block') + '"><button class="upload-del" onclick="SurveyBuild.deleteFile(this,\'' + data.instanceId + '\')">' + MsgSet["DEL"] + '</button></div>';*/
							c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFile(this,\'' + data.instanceId + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
        	   				c += '	<div class="clear"></div>';
        	   				c += '</div>';
        	   			}
        	   		}
        	}

        	c += '		</div>';
        	c += '	</div>';
        	c += '	<div class="input-list-suffix-blank left"></div>';
        	c += '	<div class="clear"></div>';
        	c += '</div>';}
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
		if(SurveyBuild.accessType == "M"){
			
		}
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