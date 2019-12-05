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
	children: [{"itemId":"attachment_Upload","itemName":"图片上传","title":"图片上传","orderby":"","fileName":"","sysFileName":"","accessPath":"","viewFileName":""}],

    _getHtml: function(data, previewmode) {
        var c = "",msg = "", val = data.value ? data.value: data.defaultval;
		var children = data.children;
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
			    c +='   				<div class="ncsc-upload-btn-p">'+ MsgSet["UPLOAD_BTN_MSG"] +'<img src="' + TzUniversityContextPath + '/statics/images/appeditor/m/upload.png'+'"></div>';
			    c +='      			</a>';  	
			    c +='         	</div>';
			    c +='       </div>';
			    c +='   </div>';
			    c +='<p style="color:#666;font-size:0.56rem;margin-top:5px;display:' + (SurveyBuild._readonly?'none':'block') +' ">'+msg+'</p>';
			    c +='<p style="color:#666;font-size:0.56rem;">'+data.onShowMessage+'</p>';
			    c +='</div>';
			    
			    
			    if(children[0].viewFileName==""){
			    	c += ' <div class="upload_list" id="'+data.itemId+'_AttList" style="display:none">';
					c += '<p style="display:' + (SurveyBuild._readonly?'none':'') +'">'+ MsgSet["UP_FILE_LIST"] +'</p>';
					c += '</div>';
			    }else{
			    	c += ' <div class="upload_list" id="'+data.itemId+'_AttList" style="display:' + (children.length < 1 ? 'none':'block') + '">';
					c += '<p style="display:' + (SurveyBuild._readonly?'none':'') +'">'+ MsgSet["UP_FILE_LIST"] +'</p>';
				    	if(data.allowMultiAtta == "Y"){
			        		for(var i=0; i<children.length; i++){
			        			if (children[i].viewFileName != "" && children[i].sysFileName != ""){
			        				c += '<li class="fileLi"><span><a id="img_'+data.itemId+'_'+i+'" onclick="SurveyBuild.viewImageSet(this,\'' + data.instanceId + '\',\'' + i + '\')" index="'+i+'" file-index="' + children[i].orderby + '">'+ children[i].viewFileName+'</a></span><i  onclick="SurveyBuild.deleteFile(this,\'' + data.instanceId + '\')" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center"></i></li>';
			        			}
			        		}
			        	}else{
			        		for(var i=0; i<children.length; i++){
			        			if (children[i].viewFileName != "" && children[i].sysFileName != ""){
			        				c += '<li class="fileLi"><span><a id="img_'+data.itemId+'_'+i+'"   onclick="SurveyBuild.viewImageSet(this,\'' + data.instanceId + '\',\'' + i + '\')" file-index="' + children[i].orderby + '">'+ children[i].viewFileName+'</a></span><i  onclick="SurveyBuild.deleteFile(this,\'' + data.instanceId + '\')" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';background:url(' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png'+') no-repeat center center"></i></li>';		        				
			        			}
			        		}
			        	}
				 c += '</div>';
			    }
//			    c += '<div class="img_shade" id ="shade_'+data.itemId+'"></div>';
//			    c += '<img class="img_pop_close" id ="close_'+data.itemId+'" src="'+ TzUniversityContextPath + '/statics/images/appeditor/m/rl_btn.png'+'">';
//			    c += '<div class="img_pop_body" id ="body_'+data.itemId+'">'  ;
//			    c += ' <img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" id ="img_'+data.itemId+'">';
//			    c += '</div>';
			}else{
				c += '<div class="input-list-blank margart15">';
	        	c += '	<div class="input-list-info left"><span class="red-star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';           
	        	c += '	<div class="input-list-texttemplate left" style="display:' + (SurveyBuild._readonly?'none':'block') + '">';
	        	c += '		<div class="filebtn left">';
	        	c += '			<div class="filebtn-org"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" />&nbsp;&nbsp;' + MsgSet["UPLOAD_BTN_MSG"] + '</div>';
	        	c += '			<input data-instancid="' + data.instanceId + '" id="'+ data.itemId + '" name="'+ data.itemId + '" title="' + data.itemName + '" onchange="SurveyBuild.uploadAttachment(this,\''+ data.instanceId +'\')" type="file" class="filebtn-orgtext" >';
	        	c += '		</div>';
				c += '	<div class="file-list-suffix" style="display:' + (SurveyBuild._readonly?'none':'block') + '">' + (data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "") +'</div>';
				c += '		<div class="clear"></div>';
	        	c += '		<div>' + msg + '<div id="' + data.itemId + 'Tip" class="onShow" style="line-height:32px;height:18px;"><div class="onShow"></div></div></div>';
	        	c += '	</div>';
	        	c += '	<div class="input-list-info-blank left" style="display:' + (SurveyBuild._readonly?'none':'block') + '"><span class="red"></div>';                
	        	c += '	<div class="input-list-upload left">';

	        	c += '		<div class="input-list-upload-con" id="' + data.itemId + '_AttList" style="display:' + (children.length < 1 ? 'none':'black') + '">';
	        	if(data.allowMultiAtta == "Y"){
	        		for(var i=0; i<children.length; i++){
	        			if (children[i].viewFileName != "" && children[i].sysFileName != ""){
	        				c += '<div class="input-list-uploadcon-list">';
	        				c += '	<div class="input-list-uploadcon-listl left"><a class="input-list-uploadcon-list-a" onclick=SurveyBuild.viewImageSet(this,"' + data.instanceId + '") file-index="' + children[i].orderby + '">' + children[i].viewFileName + '</a></div>';
	        				c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFile(this,\'' + data.instanceId + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
	        				c += '	<div class="clear"></div>';
	        				c += '</div>';
	        			}
	        		}
	        	}else{
	        		for(var i=0; i<children.length; i++){
	        			if (children[i].viewFileName != "" && children[i].sysFileName != ""){
	        				c += '<div class="input-list-uploadcon-list">';
	        				c += '	<div class="input-list-uploadcon-listl left"><a class="input-list-uploadcon-list-a" onclick=SurveyBuild.viewImageSet(this,"' + data.instanceId + '") file-index="' + children[i].orderby + '">' + children[i].viewFileName + '</a></div>';
							c += '<div class="input-list-uploadcon-listr left" style="display: ' + (SurveyBuild._readonly?'none':'block') + ';line-height:46px;" onclick="SurveyBuild.deleteFile(this,\'' + data.instanceId + '\')"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
	        				c += '	<div class="clear"></div>';
	        				c += '</div>';
	        			}
	        		}
	        	}
	        	c += '		</div>';
	        	c += '	</div>';
				/*
	        	c += '	<div class="input-list-suffix-blank left" style="display:' + (SurveyBuild._readonly?'none':'block') + '"><span class="red"></span></div>';
	        	c += '	<div class="input-list-upload left" style="width:63%;display:' + (SurveyBuild._readonly?'none':'block') + '">' + (data.suffixUrl != "" ? "<a href='" + data.suffixUrl + "'>" : "") + (data.suffix != "" ? data.suffix : "") + (data.suffixUrl != "" ? "</a>" : "") +'</div>';*/
	        	c += '	<div class="clear"></div>';
	        	c += '</div>';
				if(data.onShowMessage!=""){
					c += '<div style="margin-top:0px" class="input-list-blank"><div class="input-list-info-blank left"><span class="red"></span></div><div class="input-list-upload left">'+data.onShowMessage+'</div><div class="clear"></div></div>';
				}
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
        e += '<div class="edit_item_warp">';
        e += '  <span class="edit_item_label">提示信息：</span>';
        e += '  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'onShowMessage\')" value="' + data.onShowMessage + '"/>';
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
		if(SurveyBuild.accessType == "M"){
//			$("#shade_"+data.itemId).hide();
//			$("#body_"+data.itemId).hide();
//			$("#close_"+data.itemId).hide();
//			$(window).load(function(){
//				  initStyles();
//				});
//				$(window).resize(function(){
//				  initStyles();
//				});
//
//				function initStyles() {
////				 var allHeight=$(window).height();
////				     var popheight=$("#body_"+data.itemId).height();
////				     $("#body_"+data.itemId).css("top",allHeight/2-popheight/2-10+"px");	
////				     $("#close_"+data.itemId).css("top",allHeight/2-popheight/2-20+"px");
////				     console.log(popheight);	console.log(allHeight);
//				}	
				
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