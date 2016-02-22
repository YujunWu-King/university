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
		    c = '<div class="main_inner_content_info_autoheight" id="upload_' + data.itemId + '">';
            c += '<div class="main_inner_connent_info_left"><span class="reg_title_star">' + (data.isRequire == "Y" ? "*": "") + '</span>' + data.title + '</div>';
			c += '<div class="main_inner_content_info_right" style="height:auto;position: relative;">';
			c += '<div style="margin-left:426px;position: absolute;"><div id="' + data.itemId + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;"><div class="onShow"></div></div></div>';
			c += '</div>';
            //报名表实例ID
            var appInsId = SurveyBuild.appInsId;
            var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"VIEWATTACH","INSTANCEID":"' + appInsId + '"}}';
            $.ajax({
                type: "get",
                dataType: "JSON",
                data: {
                    tzParams: params
                },
                async: false,
                url: SurveyBuild.tzGeneralURL,
                success: function (data2) {
                    if (data2.state.errcode == "0"){
                    var num=1;
                    children =  data2.comContent;
                    data.children = children;
                    c += '<div class="main_inner_file_list" id="'+data.itemId+'_AttList"><ul>';
                    for(var i=0; i<children.length; i++){
                        _sysFilename = children[i].sysFileName;
                        _fileName=children[i].fileName;
						_accessPath=children[i].accessPath;
                        _fileSuffix = (_sysFilename.substring(_sysFilename.lastIndexOf(".") + 1)).toUpperCase();
                        if (children[i].viewFileName != "" && children[i].sysFileName != ""){
                            if(num==1){
                                c += '<li  class="main_inner_content_info_right"><a class="main_inner_filelist_a" onclick=SurveyBuild.downLoadBmbFile("'+ data.instanceId +'","'+_sysFilename+'","' + _accessPath + '") file-index="'+children[i].orderby+'">'+children[i].viewFileName+'</a>';
                                num=num+1;
                            }else{
                                c += '<li  style="margin-left: 330px"><a class="main_inner_filelist_a" onclick=SurveyBuild.downLoadBmbFile("'+ data.instanceId +'","'+_sysFilename+'","' + _accessPath + '") file-index="'+children[i].orderby+'">'+children[i].viewFileName+'</a>';
                            }
                        }
                    }
                    c += '</ul></div>';
                    }
                }
            });
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
				c += '<div id="desc' + data.itemId + '" class="main_inner_content_tips_3line" style="clear:left;padding-left:140px;height:auto; margin-bottom: 10px;">';
				c += '</div>';
			}
        } else {
            //编辑模式
			 c += '<div class="question-answer"><span class="suffix">' + (data["suffix"] ? data.suffix: "") + '</span></div>';
            //c += '<div class="question-answer"></div>';
        }
        return c;
    },
    _edit: function(data) {
        var e = '<div class="edit_item_warp"><span class="edit_item_label">后缀：<a href="#" data-for-id="help_suffix" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a></span>  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffix\')" value="' + data.suffix + '"/></div>';
            e += '<div class="edit_item_warp"><span class="edit_item_label">后缀链接：</span>  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'suffixUrl\')" value="' + data.suffixUrl + '"/></div>';

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
				/*/*//*********************************************\
				 ** 注意：自定义规则中不要使用formValidator **
				*********************************************//*/*/
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