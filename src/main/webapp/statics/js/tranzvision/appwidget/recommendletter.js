/**
 * Created by LZ on 2015/6/18.
 * 推荐信
 */
SurveyBuild.extend("recommendletter", "baseComponent", { 
    itemName: "推荐信",
    title: "推荐信",
    itemMs: "",
	itemLx:'S',
	toCheck:'Y',
	toCheckRefApp:'N',
	checkRefApp:"",
    isDoubleLine: "Y",
    fixedContainer:"Y",//固定容器标识
	linesNo:[1,2,3],
    children:{
		"recommend_18": {
            "instanceId": "rec_title",
            "itemId": "r_title",
            "itemName": "称呼",
			"title": "称呼",
			"classname":"RefferTitle",
            "orderby": 1,
			"StorageType":"S",
			"useby": "Y",
            "value": "",
			"wzms":""
        },
		"recommend_1": {
            "instanceId": "rec_name",
            "itemId": "r_name",
            "itemName": "姓氏",
			"title": "姓氏",
			"classname":"SingleTextBox",
            "orderby": 2,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
		"recommend_17": {
            "instanceId": "rec_gname",
            "itemId": "r_gname",
            "itemName": "名字",
			"title": "名字",
			"classname":"SingleTextBox",
            "orderby": 3,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
		"recommend_2": {
            "instanceId": "rec_company",
            "itemId": "r_company",
            "itemName": "单位",
			"title": "单位",
			"classname":"SingleTextBox",
            "orderby": 4,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
        "recommend_3": {
            "instanceId": "rec_post",
            "itemId": "r_post",
            "itemName": "职务",
			"title": "职务",
			"classname":"SingleTextBox",
            "orderby": 5,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
		"recommend_16": {
            "instanceId": "rec_phone_area",
            "itemId": "r_phone_area",
            "itemName": "区号",
			"title": "区号",
			"classname":"SingleTextBox",
            "orderby": 6,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
		"recommend_4": {
            "instanceId": "rec_phone_no",
            "itemId": "r_phone_no",
            "itemName": "手机",
			"title": "手机",
			"classname":"SingleTextBox",
            "orderby": 7,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
		"recommend_5": {
            "instanceId": "rec_email",
            "itemId": "r_email",
            "itemName": "邮箱",
			"title": "邮箱",
			"classname":"SingleTextBox",
            "orderby": 8,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
        "recommend_6": {
            "instanceId": "rec_relation",
            "itemId": "r_relation",
            "itemName": "申请人关系",
			"title": "申请人关系",
			"classname":"SingleTextBox",
            "orderby": 9,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_7": {
            "instanceId": "rec_language",
            "itemId": "r_language",
            "itemName": "推荐信语言",
			"title": "推荐信语言",
			"classname":"Radio",
            "orderby": 10,
			"StorageType":"D",
			"useby": "Y",
            "value": "",
			"option": {
                "ZHS": {
                    "checked": "Y",
                    "code": "C",
                    "defaultval": "N",
                    "orderby": 1,
                    "other": "N",
                    "txt": "中文",
                    "weight": 0
                },
                "ENG": {
                    "checked": "N",
                    "code": "E",
                    "defaultval": "N",
                    "orderby": 2,
                    "other": "N",
                    "txt": "英文",
                    "weight": 0
                }
            }
        },
		"recommend_8": {
            "instanceId": "rec_way",
            "itemId": "r_way",
            "itemName": "推荐信类型",
			"title": "推荐信类型",
            "classname": "Radio",
            "orderby": 11,
            "StorageType": "D",
            "useby": "Y",
            "value": "S",
            "option": {
                "SEND": {
                    "checked": "Y",
                    "code": "S",
                    "defaultval": "N",
                    "orderby": 1,
                    "other": "N",
                    "txt": "发送邮件",
                    "weight": 0
                },
                "UPLOAD": {
                    "checked": "N",
                    "code": "U",
                    "defaultval": "N",
                    "orderby": 2,
                    "other": "N",
                    "txt": "上传附件",
                    "weight": 0
                }
            }
        },
		"recommend_9": {
			"instanceId": "rec_attach",
			"itemId": "r_attach",
			"orderby": 12,
			"StorageType":"F",
			"value": "",
			"useby": "Y",
			"itemName": "上传附件",
			"title": "上传附件",
			"filename":"",
			"sysFileName":"",
			"path":"",
			"classname":"refLetterFile",
			"accessPath":"",
			"viewFileName":""
		},
		/*"recommend_9": {
            "instanceId": "rec_attach",
            "itemId": "r_attach",
            "itemName": "上传附件",
			"classname":"SingleTextBox",
            "orderby": 9,
			"useby": "",
            "value": ""
        },*/
		"recommend_10": {
            "instanceId": "rec_by1",
            "itemId": "r_by1",
            "itemName": "备用字段一",
			"title": "备用字段一",
			"classname":"SingleTextBox",
            "orderby": 13,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_11": {
            "instanceId": "rec_by2",
            "itemId": "r_by2",
            "itemName": "备用字段二",
			"title": "备用字段二",
			"classname":"SingleTextBox",
            "orderby": 14,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_12": {
            "instanceId": "rec_by3",
            "itemId": "r_by3",
            "itemName": "备用字段三",
			"title": "备用字段三",
			"classname":"SingleTextBox",
            "orderby": 15,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_13": {
            "instanceId": "rec_by4",
            "itemId": "r_by4",
            "itemName": "备用字段四",
			"title": "备用字段四",
			"classname":"SingleTextBox",
            "orderby": 16,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_14": {
            "instanceId": "rec_by5",
            "itemId": "r_by5",
            "itemName": "备用字段五",
			"title": "备用字段五",
			"classname":"SingleTextBox",
            "orderby": 17,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_15": {
            "instanceId": "rec_sex",
            "itemId": "r_sex",
            "itemName": "性别",
			"title": "性别",
			"classname":"Radio",
            "orderby": 18,
			"StorageType":"D",
			"useby": "",
            "value": "",
			"option":{
				"MAN":{
                    "checked": "N",
					"code": "M",
					"defaultval": "N",
					"orderby": 1,
					"other": "N",
					"txt": MsgSet["SEX_M"],
					"weight": 0
				},"WOMAN":{
					"checked": "N",
					"code": "F",
					"defaultval": "N",
					"orderby": 2,
					"other": "N",
					"txt": MsgSet["SEX_F"],
					"weight": 0
				}
			}
        },
		"recommend_99": {
            "instanceId": "rec_xh",
            "itemId": "r_xh",
            "itemName": "序号",
			"title": "序号",
			"classname":"SingleTextBox",
            "orderby": 99,
			"StorageType":"S",
			"useby": "",
            "value": ""
        }
    },
    minLines: 1,
    maxLines: 4,
	defaultLines:1,
	tjxMaxLinesXh: 1,
    _getHtml: function (data, previewmode) {
		
        var c = "", children = data.children,len = children.length;
        if (previewmode) {
            //title
			/*
			for(var i=1;i<=data.maxLines;i++){
				c += '<div class="main_inner_content_title">';
				//c += '<span class="reg_title_star">' + (data.isRequire == "Y" ?"*":"") + '</span>';
				//c += '<span class="reg_title_star">*</span>';
				c += '<span class="reg_title_grey_17px">' + MsgSet["REFFER"] + ' ' +i+ ' :' + data.title + '</span>';
				//c += '<span class="reg_title_grey_17px">' + data.title + '_'+i+'：</span>';
				c += '</div>';
				c += '<div class="main_inner_content_top"></div>';
				c += '<div class="main_inner_content">';
				var works = this._getHtmlOne(data,i);
				c += works;

				c += '</div>';
				//footer
				c += '<div class="main_inner_content_foot"></div>';
			} 
			*/		

			var showLines;
			var len = data.children.length;
			if(len>=data.defaultLines)
			{
				showLines = len;
			}else{
				showLines = data.defaultLines;
			}
			
			for(var i=1;i<=showLines;i++){
				
				var works = this._getHtmlOne(data,i,'QUERY');
				c += works;
				if(i>1){
					data["linesNo"].shift(); 
				}
			}
			
			/*添加按钮*/
			len = data.children.length;
			c += '<div class="main_inner_content_info">';
			if(len<data.maxLines){
				
				//c += '<div id="main_inner_content_info_save0"><div id="save1" class="bt_blue">' + MsgSet["SAVE"] + '</div><a href="#" class="alpha"></a></div>';
				c += '<div style="display: inherit;float:right;padding-right:30px;" class="main_inner_content_info_add addnextbtn" id="save_and_add0" onclick="SurveyBuild.addTjx(this,\'' + data.instanceId + '\');"><div class="bt_blue">' + MsgSet["ADD_ONE"] + '</div></div>';
			}else{
				c += '<div style="display: none;float:right;padding-right:30px;" class="main_inner_content_info_add addnextbtn" id="save_and_add0" onclick="SurveyBuild.addTjx(this,\'' + data.instanceId + '\');"><div class="bt_blue">' + MsgSet["ADD_ONE"] + '</div></div>';
			}
			c += '</div>';
        } else {
            c = '<div class="question-answer">' + (data.itemMs ? '<div class="edu_exper_itemMs" style="background-color:#d8d8d8;padding:2px 5px;margin-bottom:10px;">'+ data.itemMs +'</div>' : "");
            c += '<div class="DHContainer" style="border:1px solid #ddd;padding:10px 20px;">'
			
			//title
			c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_title_1'+'" '+(data.children.recommend_18["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_title'+'">'+data.children.recommend_18["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//姓氏
			c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_name_1'+'" '+(data.children.recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_name'+'">'+data.children.recommend_1["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//名字
			c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_gname_1'+'" '+(data.children.recommend_17["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_gname'+'">'+data.children.recommend_17["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//单位
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_company_1'+'" '+(data.children.recommend_2["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_company'+'">'+data.children.recommend_2["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//职务
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_post_1'+'" '+(data.children.recommend_3["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_post'+'">'+data.children.recommend_3["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//手机
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_phone_no_1'+'" '+(data.children.recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_phone_no'+'">'+data.children.recommend_4["itemName"]+'：</span>';
			c += '<b class="read-input" style="min-width:63px;width:63px">&nbsp;</b>';
            c += ' <b class="read-input" style="min-width:180px;width:183px">&nbsp;</b>';
            c += '</div>';
			//邮箱
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_email_1'+'" '+(data.children.recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_email'+'">'+data.children.recommend_5["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//性别
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_sex_1'+'" '+(data.children.recommend_15["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_sex'+'">'+data.children.recommend_15["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//申请人关系
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_relation_1'+'" '+(data.children.recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_relation'+'">'+data.children.recommend_6["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//备用字段一
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by1_1'+'" '+(data.children.recommend_10["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_by1'+'">'+data.children.recommend_10["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//备用字段二
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by2_1'+'" '+(data.children.recommend_11["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_by2'+'">'+data.children.recommend_11["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//备用字段三
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by3_1'+'" '+(data.children.recommend_12["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_by3'+'">'+data.children.recommend_12["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//备用字段四
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by4_1'+'" '+(data.children.recommend_13["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_by4'+'">'+data.children.recommend_13["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//备用字段五
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by5_1'+'" '+(data.children.recommend_14["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_by5'+'">'+data.children.recommend_14["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//推荐信语言
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_language_1'+'" '+(data.children.recommend_7["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_language'+'">'+data.children.recommend_7["itemName"]+'：</span>';
			c += '<b class="read-check" style="display:inline-block;margin-right:-5px;min-width:20px;">&nbsp;</b> 中文 ';
			c += '<b class="read-check" style="display:inline-block;margin-right:-5px;min-width:20px;">&nbsp;</b> 英文';
            c += '</div>';
			//推荐信类型
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_way_1'+'" '+(data.children.recommend_8["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_way'+'">'+data.children.recommend_8["itemName"]+'：</span>';
			c += '<b class="read-check" style="display:inline-block;margin-right:-5px;min-width:20px;">&nbsp;</b> 发邮件 ';
			c += '<b class="read-check" style="display:inline-block;margin-right:-5px;min-width:20px;">&nbsp;</b> 上传附件';
            c += '</div>';
			//上传附件
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_attach_1'+'" '+(data.children.recommend_9["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
			c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_attach'+'">'+data.children.recommend_9["itemName"]+'：</span><button class="btn btn-small"><i class="icon-upload-alt"></i>上传</button></div>';
			c += '</div>';

            c += '</div></div>'
        }
        return c;
    },
    _edit: function (data) {
	
		/**/
        var refAppclass = [];
        var params = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"REFAPPCLASS"}}';
        $.ajax({
            type: "get",
            dataType: "JSON",
            data:{tzParams:params},
            async:false,
            url:SurveyBuild.tzGeneralURL,
            success: function(f) {
                if(f.state.errcode == "0"){
                    refAppclass = f.comContent;
                }
            }
        });
        /*推荐信校验条件appclassEnd*/
	
		var e="";
		//推荐信数目
		e +='<div class="edit_rqjls"><span class="title"><i class="icon-envelope"></i> 推荐信数量</span>';
		e +='<div class="groupbox">';
		e +='<div class="edit_item_warp">';
		e +='<span class="edit_item_label">最少封数：</span>';
		e +='  <input type="text" class="medium" maxlength="1" style="ime-mode:disabled" onkeyup="SurveyBuild.saveAttr(this,\'minLines\')" value="' + data.minLines + '"/>';
		e +='</div>';
		e +='<div class="edit_item_warp"><span class="edit_item_label">最多封数：</span>';
		e +='  <input type="text" class="medium" maxlength="1" onkeyup="SurveyBuild.saveAttr(this,\'maxLines\')" value="' + data.maxLines + '"/>';
		e +='</div>';
		e +='<div class="edit_item_warp mb10"><span class="edit_item_label">默认显示：</span>';
		e +='  <input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'defaultLines\')" value="' + data.defaultLines + '"/>';
		e +='</div>';
		e +='</div>';
		e +='</div>';

		//信息项
		e += '<div class="edit_item_warp" style="margin-top:5px;">';
		var list = "";
		var child = data.children;
		
		//称呼-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_title">';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_18\')" value="'+child.recommend_18["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_18\')" value="'+child.recommend_18["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" value="rec_title" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_18\')" '+(child.recommend_18["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
		list += '</tr>';
		//称呼-结束
		
		//姓氏-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_name">';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_1\')" value="'+child.recommend_1["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_1\')" value="'+child.recommend_1["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" value="rec_name" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
		list += '</tr>';
		//姓名-结束
		
		//名字-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_gname">';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_17\')" value="'+child.recommend_17["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_17\')" value="'+child.recommend_17["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" value="rec_gname" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_17\')" '+(child.recommend_17["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
		list += '</tr>';
		//姓名-结束
		
		//单位-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_company">';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_2\')" value="'+child.recommend_2["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_2\')" value="'+child.recommend_2["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_company" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
        list += '</tr>';
		//单位-结束
		//职务-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_post">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_3\')" value="'+child.recommend_3["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_3\')" value="'+child.recommend_3["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_post" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
        list += '</tr>';
		//职务-结束
		//手机区号-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_phone_area">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_16\')" value="'+child.recommend_16["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_16\')" value="'+child.recommend_16["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_phone_area" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
        list += '</tr>';
		//手机-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_phone_no">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_4\')" value="'+child.recommend_4["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_4\')" value="'+child.recommend_4["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_phone_no" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
        list += '</tr>';
		//手机-结束
		//邮箱-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_email">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_5\')" value="'+child.recommend_5["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_5\')" value="'+child.recommend_5["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_email" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
        list += '</tr>';
		//邮箱-结束
		//性别-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_sex">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_15\')" value="'+child.recommend_15["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_15\')" value="'+child.recommend_15["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_sex" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_15\')" '+(child.recommend_15["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//性别-结束
		//申请人关系-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_relation">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_6\')" value="'+child.recommend_6["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_6\')" value="'+child.recommend_6["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_relation" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_6\')" '+(child.recommend_6["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//申请人关系-结束
		//备用字段一-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by1">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_10\')" value="'+child.recommend_10["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_10\')" value="'+child.recommend_10["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by1" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_10\')" '+(child.recommend_10["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段一-结束
		//备用字段二-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by2">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_11\')" value="'+child.recommend_11["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_11\')" value="'+child.recommend_11["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by2" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_11\')" '+(child.recommend_11["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段二-结束
		//备用字段三-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by3">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_12\')" value="'+child.recommend_12["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_12\')" value="'+child.recommend_12["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by3" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_12\')" '+(child.recommend_12["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段三-结束
		//备用字段四-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by4">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_13\')" value="'+child.recommend_13["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_13\')" value="'+child.recommend_13["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by4" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_13\')" '+(child.recommend_13["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段四-结束
		//备用字段五-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by5">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_14\')" value="'+child.recommend_14["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_14\')" value="'+child.recommend_14["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by5" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_14\')" '+(child.recommend_14["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段五-结束
		//推荐信语言-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_language">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_7\')" value="'+child.recommend_7["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_7\')" value="'+child.recommend_7["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_language" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
        list += '</tr>';
		//推荐信语言-结束
		//推荐信类型-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_way">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_8\')" value="'+child.recommend_8["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_8\')" value="'+child.recommend_8["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_way" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_8\')" '+(child.recommend_8["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//推荐信类型-结束
		//上传附件-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_attach">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_9\')" value="'+child.recommend_9["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_9\')" value="'+child.recommend_9["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_attach" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_9\')" '+(child.recommend_9["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//上传附件-结束

		e += '<fieldset id="option-box">';
        e += '	<span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 信息项</span>';
		e += '<table class="table table-bordered data-table">';
		e += '<thead><tr><th width="55">Label Name</th><th width="55">Field Title</th><th width="55">Field Name</th><th width="20">启用</th></tr></thead><tbody class="ui-sortable">' + list + '</tbody>';
		e += '</table>';
		e += '</fieldset>';

		e += '</div>';


		e += '<div class="edit_jygz">';
		e += '	<span class="title"><i class="icon-info-sign"></i> 参数设置</span>';
		e += '  <div class="groupbox">';
		//中文（英文）推荐信设置
		e += '   <div class="edit_item_warp">';
        e += '    <a href="javascript:void(0);" onclick="SurveyBuild.RulesZHS(this);">中文推荐信设置</a>';
		e += '   </div>';
		e += '   <div class="edit_item_warp">';
		e += '    <a href="javascript:void(0);" onclick="SurveyBuild.RulesENG(this);">英文推荐信设置</a>';
        e += '   </div>';
		//推荐信类型
		e += '   <div class="edit_item_warp mb10"><span class="edit_item_label">推荐信类型：</span>';
		e += '    <select name="bmb_mb_id" id="bmb_mb_id" class="selectCss" onchange="SurveyBuild.saveAttr(this,\'itemLx\')">';
		e += '     <option value="">请选择</option><option value="F" '+(data.itemLx=="F"?"selected='selected'":"")+'>发送邮件</option><option value="S" '+(data.itemLx=="S"?"selected='selected'":"")+'>上传附件</option><option value="L" '+(data.itemLx=="L"?"selected='selected'":"")+'>两者都行</option>';
		e += '    </select>';
		e += '   </div>';
		e += '  </div>';
		e += '</div>';

        e += '<div class="edit_jygz"><span class="title"><i class="icon-cog"></i> 校验规则</span>';
		e += '  <div class="groupbox">';
		//是否必填
        //e += '<div class="edit_item_warp" style="margin-top:5px;">';
        //e += '<input type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'" : "") + ' id="is_require"/> 是否必填';
        //e += '<a href="#" data-for-id="help_isRequire" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
        //e += '</div>';

		//是否邮件通知申请人
		e += '<div class="edit_item_warp" style="margin-top:5px;">';
		e += '<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'toSendE\')"' + (data.toSendE == "Y" ? "checked='checked'" : "") + ' id="is_toSendE" > <label for="is_toSendE">邮件通知申请人</label>';
		e += '</div>';
		//收齐推荐信前禁止提交报名表
		e += '<div class="edit_item_warp" style="margin-top:5px;">';
		e += '<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'toCheck\')"' + (data.toCheck == "Y" ? "checked='checked'" : "") + ' id="is_toCheck" > <label for="is_toCheck">收齐推荐信前禁止提交报名表</label>';
		e += '</div>';
		//满足条件后无需填写推荐信
		e += '<div class="edit_item_warp" style="margin-top:5px;">';
		e += '<input class="mbIE" type="checkbox" onchange="SurveyBuild.saveAttr(this,\'toCheckRefApp\')"' + (data.toCheckRefApp == "Y" ? "checked='checked'" : "") + ' id="is_toCheckRefApp" > <label for="is_toCheckRefApp">满足条件后无需填写推荐信</label>';
		
		e += '</div>';
		e += '<div class="edit_item_warp" style="margin-top:5px;margin-bottom:10px;">';
		e += '   <select class="edit_boxSize elewidth-select" id="checkRefApp" style="width:295px;" onchange="SurveyBuild.saveAttr(this,\'checkRefApp\')">';
        e += '       <option value="">请选择</option>';
        for (var i in refAppclass) {
            e += '<option ' + (data.checkRefApp == refAppclass[i]["appclassid"] ? "selected='selected'": "") + 'value="' + refAppclass[i]["appclassid"] + '">' + refAppclass[i]["appclassname"] + '</option>';
        }
        e += '   </select>';
		e += '</div>';
		e += '</div>';
		
		//高级设置
        e += '<div class="edit_item_warp">';
        e += '<a href="javascript:void(0);" onclick="SurveyBuild.RulesSet(this);"><i class="icon-cogs"></i> 高级设置</a>';
        e += '</div>';

        e += '</div>';

        return e;
    },
    _getHtmlOne: function(data,rownum,showtype){
	
		/**/
		if (SurveyBuild.appInsId=="0"&&data.children.length<data.defaultLines){
			SurveyBuild.showTjx(this,data.instanceId);
			
		}else if (data.children.length<data.defaultLines){
			SurveyBuild.showTjx(this,data.instanceId);
		}
		
		/*已填写的数据的行数*/
		var len = data.children.length;
		var works = "",child = [];
		child = data.children;
		
		var i=rownum-1;
		
		/*加载推荐信称呼*/
		var tjxTitleParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONREG_OTHER_STD","OperateType":"EJSON","comParams":{"OType":"TZ_APP_REF_TITLE","LANG":"'+SurveyBuild.BMB_LANG+'"}}';
            $.ajax({
                type: "get",
                dataType: "JSON",
                data:{
                    tzParams:tjxTitleParams
                },
                async:false,
                url:SurveyBuild.tzGeneralURL,
                success: function(f) {
                    if(f.state.errcode == "0"){
                        child[i].recommend_18.option = f.comContent;
                    }
                }
            });
		
		/*推荐信序号*/
		var _tjrXh;
		_tjrXh = child[i].recommend_99["value"];
		if(_tjrXh == "")
		{	
			if(showtype=='QUERY'){
				_tjrXh = rownum;
				child[i].recommend_99["value"] = rownum;
				data.tjxMaxLinesXh = child[len-1].recommend_99["value"];
			}else
			{
				_tjrXh = rownum;
				child[i].recommend_99["value"] = parseInt(data.tjxMaxLinesXh) + 1;
				data.tjxMaxLinesXh = child[i].recommend_99["value"];
			}
		}else
		{
			data.tjxMaxLinesXh = child[len-1].recommend_99["value"];
		}
		var deleteFlag = true;
		var tz_app_ins_id=SurveyBuild.appInsId;
		var _class_id=SurveyBuild.classId;
		var tz_app_tpl_id = SurveyBuild.appTplId;
		var _tjx_zt="";
		var _qy_zhs="";
		var _qy_eng="";
		var _refAppInsId = "";
		var _refLetterId = "";
		var _refAppTplId = "";
		var _refFileName = "";
		var _refFileUrl = "";
		var params = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"EJSON","comParams":{"APP_INS_ID":"'+tz_app_ins_id+'","rownum":"'+_tjrXh+'","class_id":"'+_class_id+'","TZ_APP_TPL_ID":"'+tz_app_tpl_id+'"}}';
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
					_tjx_zt = f.comContent.TJX_ZT;
					_qy_zhs = f.comContent.zhs_qy;
					_qy_eng = f.comContent.eng_qy;
					_refAppInsId = f.comContent.tjxAppInsID;
					_refLetterId = f.comContent.refLetterId;
					_refAppTplId = f.comContent.refAppTplId;
					_refFileName = f.comContent.refFileName;
					_refFileUrl = f.comContent.refFileUrl;
				}
			}
		});
		 
		var refLetterUrl = "";
		if(SurveyBuild.appManager == "Y")
		{
			if(_refAppInsId!="0"&&_refAppInsId!=""&&_refLetterId!="0"&&_refLetterId!=""){
				refLetterUrl = SurveyBuild.tzGeneralURL + "?classid=appId&TZ_APP_INS_ID="+_refAppInsId+"&TZ_REF_LETTER_ID="+_refLetterId+"&TZ_APP_TPL_ID="+_refAppTplId+"&TZ_MANAGER=Y";
			}
		}
		if (child[i].recommend_8["value"]=="U")
		{
			_tjx_zt="未发送";
		}
		if(_refFileUrl!="")
		{
			//_tjx_zt="已完成";
		}
		var _zd="";
		if (_tjx_zt=="已完成"){
			_tjx_zt=MsgSet["Completed"];
			_zd="Z";
			deleteFlag = false;
		}
		if (_tjx_zt=="已发送"){
			_tjx_zt=MsgSet["SendEmail"];
			_zd="Y";
			deleteFlag = false;
		}
		if (_tjx_zt=="未发送"){
			_tjx_zt=MsgSet["Unsent"];
		}
		
		works += '<div id="main_inner_content_para'+i+'" class="main_inner_content_para" style="display: inherit;" >';
		
		works += '<div class="main_inner_content_title">';
		//works += '<span class="reg_title_star">' + (data.isRequire == "Y" ?"*":"") + '</span>';
		//works += '<span class="reg_title_star">*</span>';
		works += '<span class="reg_title_grey_17px">' + MsgSet["REFFER"] + ' ' +rownum+ ' :' + data.title + '</span>';
		if(rownum > data.defaultLines){
			
			if(deleteFlag == true)
			{
				works += '<div onclick="SurveyBuild.deleteTjx(this);" class="main_inner_content_del" id="tjx_delete_'+i+'" style="line-height:35px;display:block"><img width="15" height="15" src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png">' + MsgSet["DEL"] + '</div>';
			}else
			{
				works += '<div onclick=SurveyBuild.deleteTjx(this,"'+data.title+'"); class="main_inner_content_del" id="tjx_delete_'+i+'" style="line-height:35px;display:none"><img width="15" height="15" src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png">' + MsgSet["DEL"] + '</div>';
			}
		}
		//c += '<span class="reg_title_grey_17px">' + data.title + '_'+i+'：</span>';
		
		works += '</div>';
		if(child[i].recommend_18["useby"] == "Y"){
			works += '<div class="main_inner_content_top"><span class="reg_title_grey_17px" style="padding-left:15px;line-height:36px;font-size:15px;font-family: helvetica,arial,verdana,sans-serif;">' + MsgSet["None-blank"] + '</span></div>';
			works += '<div class="main_inner_content" style="padding-top:20px">';
		}else{
			works += '<div class="main_inner_content_top"></div>';
			works += '<div class="main_inner_content">';
		}
		//称呼-开始
        if(SurveyBuild._readonly){
            //只读模式
            var desc = "";
            for (var i101 in child[i].recommend_18.option) {
                if (child[i].recommend_18.value == "Ms.") {
                    child[i].recommend_18.value = "MS";
                } else if (child[i].recommend_18.value == "Mr.") {
                    child[i].recommend_18.value = "MR";
                } else if (child[i].recommend_18.value == "Professor") {
                    child[i].recommend_18.value = "PF";
                } else if (child[i].recommend_18.value == "Director") {
                    child[i].recommend_18.value = "DT";
                } else if (child[i].recommend_18.value == "None-blank") {
                    child[i].recommend_18.value = "NB";
                }
                if(child[i].recommend_18.value == child[i].recommend_18["option"][i101]["code"]){
                    desc = child[i].recommend_18["option"][i101]["txt"];
                }
            }
            works += '<div class="main_inner_content_info_autoheight cLH" ' + (child[i].recommend_18["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
            works += '  <div class="main_inner_connent_info_left">' + '<span class="reg_title_star">*</span>' + child[i].recommend_18["itemName"] + '：</div>';
            works += '	<div class="main_inner_content_info_right">' + desc + '</div>';
            works += '</div>'
        }else {
            //编辑模式
            works += '<div class="main_inner_content_info_autoheight" ' + (child[i].recommend_18["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
            works += '<div class="main_inner_connent_info_left">' + '<span class="reg_title_star">*</span>' + child[i].recommend_18["itemName"] + '：</div>';
            works += '	<div class="main_inner_content_info_right">';
            works += '  	<div class="main_inner_content_info_right_option_251px ' + ((_zd == "Y" || _zd == "Z") ? "bmbtjxtitle" : "") + '" style="margin-right:5px">';
            works += '      	<select name="' + data.itemId + child[i].recommend_18["itemId"] + '" class="chosen-select" id="' + data.itemId + child[i].recommend_18["itemId"] + '" style="width:251px;" title="' + child[i].recommend_18["title"] + '">';
            works += '          	<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
            for (var i101 in child[i].recommend_18.option) {

                if (child[i].recommend_18.value == "Ms.") {
                    child[i].recommend_18.value = "MS";
                } else if (child[i].recommend_18.value == "Mr.") {
                    child[i].recommend_18.value = "MR";
                } else if (child[i].recommend_18.value == "Professor") {
                    child[i].recommend_18.value = "PF";
                } else if (child[i].recommend_18.value == "Director") {
                    child[i].recommend_18.value = "DT";
                } else if (child[i].recommend_18.value == "None-blank") {
                    child[i].recommend_18.value = "NB";
                }

                works += '<option ' + (child[i].recommend_18.value == child[i].recommend_18["option"][i101]["code"] ? "selected='selected'" : "") + 'value="' + child[i].recommend_18["option"][i101]["code"] + '">' + child[i].recommend_18["option"][i101]["txt"] + '</option>';
            }
            works += '			</select>';
            works += '		</div>';
            works += '  	<div style="margin-top:0px;margin-left:254px">';
            works += '      	<div id="' + data.itemId + child[i].recommend_18["itemId"] + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
            works += '          	<div class="onShow"></div>';
            works += '      	</div>';
            works += '  	</div>';
            works += '	</div>';
            works += '</div>'
        }
		//称呼-结束
		
		//姓氏-开始
        if(SurveyBuild._readonly){
            //只读模式
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_1["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_readonly_right">' + child[i].recommend_1["value"] + '</div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_1["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_right">';
            works += '<input type="text" title="'+ child[i].recommend_1["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_1["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_1["itemId"] +'" value="' + child[i].recommend_1["value"] + '">';
            works += '<div style="margin-top: -40px; margin-left: 256px">';
            works += '	<div id="' + data.itemId + child[i].recommend_1["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '		<div class="onShow">&nbsp;</div>';
            works += '	</div>';
            works += '</div>';
            works += '</div>';
            works += '</div>';
        }
		//姓氏-结束
		
		//名字-开始
        if(SurveyBuild._readonly){
            //只读模式
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_17["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_17["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_readonly_right">' + child[i].recommend_17["value"] + '</div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_17["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_17["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_right">';
            works += '<input type="text" title="'+ child[i].recommend_17["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_17["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_17["itemId"] +'" value="' + child[i].recommend_17["value"] + '">';
            works += '<div style="margin-top: -40px; margin-left: 256px">';
            works += '	<div id="' + data.itemId + child[i].recommend_17["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '		<div class="onShow">&nbsp;</div>';
            works += '	</div>';
            works += '</div>';
            works += '</div>';
            works += '</div>';
        }
		//姓名-结束
		
		//单位-开始
        if(SurveyBuild._readonly){
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_2["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_2["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_readonly_right">' + child[i].recommend_2["value"] + '</div>';
            works += '</div>';
        }else{
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_2["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_2["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_right">';
            works += '		<input type="text" title="'+ child[i].recommend_2["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_2["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_2["itemId"] +'" value="' + child[i].recommend_2["value"] + '">';
            works += '		<div style="margin-top: -40px; margin-left: 256px">';
            works += '			<div id="' + data.itemId + child[i].recommend_2["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '				<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '	</div>';
            works += '</div>';
        }
		//单位-结束

		//职务-开始
        if(SurveyBuild._readonly){
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_3["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_3["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_readonly_right">' + child[i].recommend_3["value"] +  '</div>';
            works += '</div>';
        }else{
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_3["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_3["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_right">';
            works += '		<input type="text" title="'+ child[i].recommend_3["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_3["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_3["itemId"] +'" value="' + child[i].recommend_3["value"] + '">';
            works += '		<div style="margin-top: -40px; margin-left: 256px">';
            works += '			<div id="' + data.itemId + child[i].recommend_3["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '				<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '	</div>';
            works += '</div>';
        }
		//职务-结束

		//手机-开始
        if(SurveyBuild._readonly){
            //只读模式
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_4["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_readonly_right">';
            works += (child[i].recommend_16["value"] ? "(" : "") + child[i].recommend_16["value"] + (child[i].recommend_16["value"] ? ")&nbsp;" : "") + child[i].recommend_4["value"];
            works += '</div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_4["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_right">';
            /*区号*/
            works += '		<input type="text" placeholder="' + child[i].recommend_16["title"] + '" title="'+ child[i].recommend_16["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_16["itemId"] +'" class="input_63px" name="' + data.itemId + child[i].recommend_16["itemId"] +'" value="' + child[i].recommend_16["value"] + '">';
            works += '		<input type="text" placeholder="' + child[i].recommend_4["title"] + '" title="'+ child[i].recommend_4["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_4["itemId"] +'" class="input_180px" name="' + data.itemId + child[i].recommend_4["itemId"] +'" value="' + child[i].recommend_4["value"] + '">';
            works += '      <span>&nbsp;&nbsp;&nbsp;'+MsgSet["REF_PHONE_EG"]+'</span>';
            works += '		<div style="margin-top: -40px; margin-left: 256px">';
            works += '			<div id="' + data.itemId + child[i].recommend_4["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '				<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '	</div>';
            works += '</div>';
        }
		//手机-结束

		//邮箱-开始
        if(SurveyBuild._readonly){
            //只读模式
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_5["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_readonly_right">' + child[i].recommend_5["value"] + '</div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_5["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_right">';
            works += '		<input type="text" title="'+ child[i].recommend_5["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_5["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_5["itemId"] +'" value="' + child[i].recommend_5["value"] + '">';
            works += '		<div style="margin-top: -40px; margin-left: 256px">';
            works += '			<div id="' + data.itemId + child[i].recommend_5["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '				<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '	</div>';
            works += '</div>';
        }
		//邮箱-结束
		
		//性别-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_15["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_15["itemName"] +'：</div>';
		works += '<div class="main_inner_content_info_right">';
		
		works += '<div class="tz_radio_div '+(child[i].recommend_15["option"]["MAN"]["checked"] == "Y" ? "on_check" : "")+'">';
        works += '<input class="tz_radio_checkbox input_251px" ' + (child[i].recommend_15["option"]["MAN"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_15["title"] +'" id="' + data.itemId + child[i].recommend_15["itemId"] + '_M" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="M" style="width:20px;height:20px;" onclick="SurveyBuild.clickOnRadio(this);">';
        //works += '<label for="' + data.itemId + child[i].recommend_15["itemId"] + '_M" style="margin:0 0;"></label>';
		works += '</div><span style="margin-left:3px;">' + MsgSet["SEX_M"]+'</span>&nbsp;&nbsp;&nbsp;&nbsp;';
		
		works += '<div class="tz_radio_div '+(child[i].recommend_15["option"]["WOMAN"]["checked"] == "Y" ? "on_check" : "")+'">';
        works += '<input class="tz_radio_checkbox input_251px" ' + (child[i].recommend_15["option"]["WOMAN"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_15["title"] +'" id="' + data.itemId + child[i].recommend_15["itemId"] + '_F" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="F" style="width:20px;height:20px;" onclick="SurveyBuild.clickOnRadio(this);">';
        //works += '<label for="' + data.itemId + child[i].recommend_15["itemId"] + '_F" style="margin:0 0;"></label> ';
		works += '</div><span style="margin-left:3px;">'+MsgSet["SEX_F"]+'</span>';
        works += '<input type="hidden" style="width:10px;" id="' + data.itemId + child[i].recommend_15["itemId"] + '" value="' + child[i].recommend_15["value"] + '">';

        /*
         if (child[i].recommend_15["value"]=="F"){
             works += '<input class="main_inner_radio input_251px" type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_15["itemName"] +'" id="' + data.itemId + child[i].recommend_15["itemId"] + '_M" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="M">';
             works += '<label for="' + data.itemId + child[i].recommend_15["itemId"] + '_M" style="margin:0 0;"></label> '+MsgSet["SEX_M"]+'&nbsp;&nbsp;&nbsp;&nbsp;';
             works += '<input class="main_inner_radio input_251px" type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_15["itemName"] +'" id="' + data.itemId + child[i].recommend_15["itemId"] + '_F" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="F">';
             works += '<label for="' + data.itemId + child[i].recommend_15["itemId"] + '_F" style="margin:0 0;"></label> '+MsgSet["SEX_F"];
             works += '<input type="hidden" id="' + data.itemId + child[i].recommend_15["itemId"] + '" value="F">';
         }else{
             works += '<input class="main_inner_radio input_251px" type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_15["itemName"] +'" id="' + data.itemId + child[i].recommend_15["itemId"] + '_M" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="M">';
             works += '<label for="' + data.itemId + child[i].recommend_15["itemId"] + '_M" style="margin:0 0;"></label> '+MsgSet["SEX_M"]+'&nbsp;&nbsp;&nbsp;&nbsp;';
             works += '<input class="main_inner_radio input_251px" type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_15["itemName"] +'" id="' + data.itemId + child[i].recommend_15["itemId"] + '_F" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="F">';
             works += '<label for="' + data.itemId + child[i].recommend_15["itemId"] + '_F" style="margin:0 0;"></label> '+MsgSet["SEX_F"];
             works += '<input type="hidden" id="' + data.itemId + child[i].recommend_15["itemId"] + '" value="M">';
         }
        */
		works += '		<div style="margin-top: -40px; margin-left: 256px">';
		works += '			<div id="' + data.itemId + child[i].recommend_15["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '				<div class="onShow">&nbsp;</div>';
		works += '			</div>';
		works += '		</div>';
		works += '</div>';
		works += '</div>';
		//性别-结束

		//申请人关系-开始
        if(SurveyBuild._readonly){
            //只读模式
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_6["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_readonly_right">' + child[i].recommend_6["value"] + '</div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_6["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_right">';
            works += '		<input type="text" title="'+ child[i].recommend_6["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_6["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_6["itemId"] +'" value="' + child[i].recommend_6["value"] + '">';
            works += '		<div style="margin-top: -40px; margin-left: 256px">';
            works += '			<div id="' + data.itemId + child[i].recommend_6["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '				<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '	</div>';
            works += '</div>';
        }
		//申请人关系-结束

		//备用字段一-开始
        if(SurveyBuild._readonly){
            //只读模式
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_10["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_10["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_readonly_right">' + child[i].recommend_10["value"] + '</div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_10["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_10["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_right">';
            works += '		<input type="text" title="'+ child[i].recommend_10["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_10["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_10["itemId"] +'" value="' + child[i].recommend_10["value"] + '">';
            works += '		<div style="margin-top: -40px; margin-left: 256px">';
            works += '			<div id="' + data.itemId + child[i].recommend_10["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '				<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '	</div>';
            works += '</div>';
        }
		//备用字段一-结束

		//备用字段二-开始
        if(SurveyBuild._readonly){
            //只读模式
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_11["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_11["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_readonly_right">' + child[i].recommend_11["value"] + '</div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_11["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_11["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_right">';
            works += '		<input type="text" title="'+ child[i].recommend_11["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_11["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_11["itemId"] +'" value="' + child[i].recommend_11["value"] + '">';
            works += '		<div style="margin-top: -40px; margin-left: 256px">';
            works += '			<div id="' + data.itemId + child[i].recommend_11["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '				<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '	</div>';
            works += '</div>';
        }
		//备用字段二-结束

		//备用字段三-开始
        if(SurveyBuild._readonly){
            //只读模式
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_12["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_12["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_readonly_right">' + child[i].recommend_12["value"] + '</div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_12["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_12["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_right">';
            works += '		<input type="text" title="'+ child[i].recommend_12["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_12["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_12["itemId"] +'" value="' + child[i].recommend_12["value"] + '">';
            works += '		<div style="margin-top: -40px; margin-left: 256px">';
            works += '			<div id="' + data.itemId + child[i].recommend_12["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '				<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '	</div>';
            works += '</div>';
        }
		//备用字段三-结束

		//备用字段四-开始
        if(SurveyBuild._readonly){
            //只读模式
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_13["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_13["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_readonly_right">' + child[i].recommend_13["value"] + '</div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_13["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_13["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_right">';
            works += '		<input type="text" title="'+ child[i].recommend_13["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_13["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_13["itemId"] +'" value="' + child[i].recommend_13["value"] + '">';
            works += '		<div style="margin-top: -40px; margin-left: 256px">';
            works += '			<div id="' + data.itemId + child[i].recommend_13["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '				<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '	</div>';
            works += '</div>';
        }
		//备用字段四-结束

		//备用字段五-开始
        if(SurveyBuild._readonly){
            //只读模式
            works += '<div class="main_inner_content_info_autoheight cLH" '+(child[i].recommend_14["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_14["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_readonly_right">' + child[i].recommend_14["value"] + '</div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_14["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_14["itemName"] +'：</div>';
            works += '	<div class="main_inner_content_info_right">';
            works += '		<input type="text" title="'+ child[i].recommend_14["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_14["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_14["itemId"] +'" value="' + child[i].recommend_14["value"] + '">';
            works += '		<div style="margin-top: -40px; margin-left: 256px">';
            works += '			<div id="' + data.itemId + child[i].recommend_14["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '				<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '	</div>';
            works += '</div>';
        }
		//备用字段五结束

		//推荐信语言-开始
		if (_qy_zhs=="Y"&&_qy_eng=="Y")
		{
			//console.log(child[i].recommend_7);
			var tjx_language = "";
			if(child[i].recommend_7["option"]["ZHS"]["checked"]=="Y")
			{
				tjx_language = "C";
			}else if(child[i].recommend_7["option"]["ENG"]["checked"]=="Y")
			{
				tjx_language = "E";
			}else
			{
				tjx_language = "C";
				child[i].recommend_7["option"]["ZHS"]["checked"]="Y";
				child[i].recommend_7["option"]["ENG"]["checked"]="N"
			}
			works += '<div class="main_inner_content_info_autoheight">';
			works += '<div class="main_inner_connent_info_left">'+ '<span class="reg_title_star">*</span>'+ child[i].recommend_7["itemName"] +'：</div>';
			works += '<div class="main_inner_content_info_right">';
				works += '<div class="tz_radio_div '+(child[i].recommend_7["option"]["ZHS"]["checked"] == "Y" ? "on_check" : "")+'">';
				works += '<input class="tz_radio_checkbox input_411px"' + (child[i].recommend_7["option"]["ZHS"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_7["title"] +'" id="' + data.itemId + child[i].recommend_7["itemId"] + '_C" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="C" style="width:20px;height:20px;" onclick="SurveyBuild.clickOnRadio(this);">';
				//works += '<label for="' + data.itemId + child[i].recommend_7["itemId"] + '_C" style="margin:0 0;"></label> ';
				works += '</div><span style="margin-left:3px;">'+MsgSet["LANGUAGE_C"]+'</span>&nbsp;&nbsp;&nbsp;&nbsp;';
				
				works += '<div class="tz_radio_div '+(child[i].recommend_7["option"]["ENG"]["checked"] == "Y" ? "on_check" : "")+'">';
				works += '<input class="tz_radio_checkbox input_411px"' + (child[i].recommend_7["option"]["ENG"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_7["title"] +'" id="' + data.itemId + child[i].recommend_7["itemId"] + '_E" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="E" style="width:20px;height:20px;" onclick="SurveyBuild.clickOnRadio(this);">';
				//works += '<label for="' + data.itemId + child[i].recommend_7["itemId"] + '_E" style="margin:0 0;"></label> ';
				works += '</div><span style="margin-left:3px;">'+MsgSet["LANGUAGE_E"]+'</span>';
			/*
			if (child[i].recommend_7["value"]=="E"){
				works += '<input class="main_inner_radio input_411px" type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_7["itemName"] +'" id="' + data.itemId + child[i].recommend_7["itemId"] + '_C" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="C">';
				works += '<label for="' + data.itemId + child[i].recommend_7["itemId"] + '_C" style="margin:0 0;"></label> '+MsgSet["LANGUAGE_C"]+'&nbsp;&nbsp;&nbsp;&nbsp;';
				works += '<input class="main_inner_radio input_411px" type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_7["itemName"] +'" id="' + data.itemId + child[i].recommend_7["itemId"] + '_E" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="E">';
				works += '<label for="' + data.itemId + child[i].recommend_7["itemId"] + '_E" style="margin:0 0;"></label> '+MsgSet["LANGUAGE_E"];
			}else{
				works += '<input class="main_inner_radio input_411px" type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_7["itemName"] +'" id="' + data.itemId + child[i].recommend_7["itemId"] + '_C" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="C">';
				works += '<label for="' + data.itemId + child[i].recommend_7["itemId"] + '_C" style="margin:0 0;"></label> '+MsgSet["LANGUAGE_C"]+'&nbsp;&nbsp;&nbsp;&nbsp;';
				works += '<input class="main_inner_radio input_411px" type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_7["itemName"] +'" id="' + data.itemId + child[i].recommend_7["itemId"] + '_E" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="E">';
				works += '<label for="' + data.itemId + child[i].recommend_7["itemId"] + '_E" style="margin:0 0;"></label> '+MsgSet["LANGUAGE_E"];
			}*/
			/*works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="' + child[i].recommend_7["value"] + '">';*/
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="' + tjx_language + '">';
			works += '<div style="margin-top: -40px; margin-left: 110px"><span id="' + data.itemId + child[i].recommend_7["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow"><div class="onShow"></div></span></div>';
			works += '</div>';
			works += '</div>';
		}else if (_qy_zhs=="Y"&&_qy_eng!="Y")
		{
			child[i].recommend_7["option"]["ZHS"]["checked"] = "Y";
			child[i].recommend_7["option"]["ENG"]["checked"] = "N";
			works += '<div class="main_inner_content_info_autoheight" style="display:none">';
			works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_7["itemName"] +'：</div>';
			works += '<div class="main_inner_content_info_right">';
			works += '<div>'+MsgSet["LANGUAGE_C"]+'</div>';
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="C">';
			works += '</div>';
			works += '</div>';
		}else if (_qy_zhs!="Y"&&_qy_eng=="Y")
		{
			child[i].recommend_7["option"]["ZHS"]["checked"] = "N";
			child[i].recommend_7["option"]["ENG"]["checked"] = "Y";
			works += '<div class="main_inner_content_info_autoheight" style="display:none">';
			works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_7["itemName"] +'：</div>';
			works += '<div class="main_inner_content_info_right">';
			works += '<div>'+MsgSet["LANGUAGE_E"]+'</div>';
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="E">';
			works += '</div>';
			works += '</div>';
		}
		//推荐信语言-结束

		//推荐信类型-开始
		if (data.itemLx=="L"){
			
			works += '<div class="main_inner_content_info_autoheight">';
			works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_8["itemName"] +'：</div>';
			works += '<div class="main_inner_content_info_right">';
			
			if(child[i].recommend_8["option"]["SEND"]["checked"] == "Y"){
				child[i].recommend_8["option"]["SEND"]["checked"] = "Y";
				child[i].recommend_8["option"]["UPLOAD"]["checked"] = "N";
			}else if(child[i].recommend_8["option"]["UPLOAD"]["checked"] == "Y")
			{
				child[i].recommend_8["option"]["SEND"]["checked"] = "N";
				child[i].recommend_8["option"]["UPLOAD"]["checked"] = "Y";
			}else{
				child[i].recommend_8["option"]["SEND"]["checked"] = "Y";
				child[i].recommend_8["option"]["UPLOAD"]["checked"] = "N";
			}

			works += '<div class="tz_radio_div '+(child[i].recommend_8["option"]["SEND"]["checked"] == "Y" ? "on_check" : "")+'">';
			works += '<input class="tz_radio_checkbox input_411px" ' + (child[i].recommend_8["option"]["SEND"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["title"] +'" id="' + data.itemId + child[i].recommend_8["itemId"] + '_S" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="S" style="width:20px;height:20px;" onclick="SurveyBuild.clickOnRadio(this);">';
			//works += '<label for="' + data.itemId + child[i].recommend_8["itemId"] + '_S" style="margin:0 0;"></label>';
			works += '</div><span style="margin-left:3px;">' + MsgSet["Send_mail"]+'</span>&nbsp;&nbsp;&nbsp;&nbsp;';
			
			works += '<div class="tz_radio_div '+(child[i].recommend_8["option"]["UPLOAD"]["checked"] == "Y" ? "on_check" : "")+'">';
			works += '<input class="tz_radio_checkbox input_411px" ' + (child[i].recommend_8["option"]["UPLOAD"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["title"] +'" id="' + data.itemId + child[i].recommend_8["itemId"] + '_U" class="input_411px" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="U" style="width:20px;height:20px;" onclick="SurveyBuild.clickOnRadio(this);">';
			//works += '<label for="' + data.itemId + child[i].recommend_8["itemId"] + '_U" style="margin:0 0;"></label>';
			works += '</div><span style="margin-left:3px;">' + MsgSet["Upload"] + '</sapn>';
	            	/*
	             	if (child[i].recommend_8["value"]=="U"){
	                 	works += '<input class="main_inner_radio input_411px" type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["itemName"] +'" id="' + data.itemId + child[i].recommend_8["itemId"] + '_S" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="S">';
	                 	works += '<label for="' + data.itemId + child[i].recommend_8["itemId"] + '_S" style="margin:0 0;"></label>'+MsgSet["Send_mail"]+'&nbsp;';
	                 	works += '<input class="main_inner_radio input_411px" type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_8["itemName"] +'" id="' + data.itemId + child[i].recommend_8["itemId"] + '_U" class="input_411px" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="U">';
	                 	works += '<label for="' + data.itemId + child[i].recommend_8["itemId"] + '_U" style="margin:0 0;"></label>'+MsgSet["Upload"];
	             	}else{
	                 	works += '<input class="main_inner_radio input_411px" type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_8["itemName"] +'" id="' + data.itemId + child[i].recommend_8["itemId"] + '_S" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="S">';
	                 	works += '<label for="' + data.itemId + child[i].recommend_8["itemId"] + '_S" style="margin:0 0;"></label>'+MsgSet["Send_mail"]+'&nbsp;';
	                 	works += '<input class="main_inner_radio input_411px" type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["itemName"] +'" id="' + data.itemId + child[i].recommend_8["itemId"] + '_U" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="U">';
	                 	works += '<label for="' + data.itemId + child[i].recommend_8["itemId"] + '_U" style="margin:0 0;"></label>'+MsgSet["Upload"];
	             	}
	            	*/
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_8["itemId"] + '" value="' + child[i].recommend_8["value"] + '">';
			works += '<div style="margin-top: -40px; margin-left: 110px"><span id="' + data.itemId + child[i].recommend_8["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow"><div class="onShow"></div></span></div>';
			works += '</div>';
			works += '</div>';
		}else if(data.itemLx == "F"){
			child[i].recommend_8["option"]["SEND"]["checked"] = "Y";
			child[i].recommend_8["option"]["UPLOAD"]["checked"] = "N";
            works += '<input type="hidden" id="' + data.itemId + child[i].recommend_8["itemId"] + '" value="S">';
        }else{
			child[i].recommend_8["option"]["SEND"]["checked"] = "N";
			child[i].recommend_8["option"]["UPLOAD"]["checked"] = "Y";
            works += '<input type="hidden" id="' + data.itemId + child[i].recommend_8["itemId"] + '" value="U">';
        }
		//推荐信类型-结束
		
		//上传附件
		if ((data.itemLx == "L" && child[i].recommend_8["value"] == "U") || data.itemLx == "S"){
			works += '<div class="main_inner_content_info_autoheight" id="Tjxfj_show_'+i+'">';
			works += '	<div class="main_inner_connent_info_left">'+child[i].recommend_9["itemName"]+'：</div>';
			works += '	<div class="main_inner_content_info_right">';
			works += '		<div class="file_upload_button">';
			works += '			<div class="bt_blue">'+MsgSet["UPLOAD_BTN_MSG"]+'</div>';
			//works += '			<input id="'+data.itemId+child[i].recommend_9["itemId"]+'File" class="fileupload_input" type="file" name="'+data.itemId+child[i].recommend_9["itemId"]+'File" style="width:80px;" onchange=SurveyBuild.TjxUpload(this,"recommend_9",'+i+')>';
			works += '			<input id="'+data.itemId+child[i].recommend_9["itemId"]+'File" class="fileupload_input" type="file" name="websitefile" style="width:80px;" onchange=SurveyBuild.TjxUpload(this,"recommend_9",'+i+')>';
			works += '			<div style="margin-top:-35px;margin-left:280px">';
			works += '				<span id="'+data.itemId+child[i].recommend_9["itemId"]+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;">';
			works += '					<div class="onCorrect">&nbsp;</div>';
			works += '				</span>';
			works += '			</div>';
			works += '		</div>';
			works += '		<div id="'+data.itemId+i+'_AttList" class="main_inner_file_list">';
			if (child[i].recommend_9["viewFileName"]!="")
			{
				works += '		<ul><li>';
				works += '			<a class="main_inner_filelist_a" onclick=SurveyBuild.TjxdownLoad(this,"recommend_9",'+i+') file-index="'+i+'">'+child[i].recommend_9["viewFileName"]+'</a>';
				works += '			<div class="main_inner_file_del" onclick=SurveyBuild.Tjxdelete(this,"recommend_9",'+i+')><img width="15" height="15" src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="'+MsgSet["DEL"]+'">'+MsgSet["DEL"]+'</div>';
				works += '		</li></ul>';
			}
			works += '		</div>';
			works += '	</div>';
			works += '	<input id="'+data.itemId+child[i].recommend_9["itemId"]+'" type="hidden" name="'+data.itemId+child[i].recommend_9["itemId"]+'" value="'+child[i].recommend_9["value"]+'">';
			works += '</div>';
		}else{
			works += '<div class="main_inner_content_info_autoheight" style="display:none" id="Tjxfj_show_'+i+'">';
			works += '	<div class="main_inner_connent_info_left">'+child[i].recommend_9["itemName"]+'：</div>';
			works += '	<div class="main_inner_content_info_right">';
			works += '		<div class="file_upload_button">';
			works += '			<div class="bt_blue">'+MsgSet["UPLOAD_BTN_MSG"]+'</div>';
			//works += '			<input id="'+data.itemId+child[i].recommend_9["itemId"]+'File" class="fileupload_input" type="file" name="'+data.itemId+child[i].recommend_9["itemId"]+'File" style="width:80px;" onchange=SurveyBuild.TjxUpload(this,"recommend_9",'+i+')>';
			works += '			<input id="'+data.itemId+child[i].recommend_9["itemId"]+'File" class="fileupload_input" type="file" name="websitefile" style="width:80px;" onchange=SurveyBuild.TjxUpload(this,"recommend_9",'+i+')>';
			works += '			<div style="margin-top:-35px;margin-left:280px">';
			works += '				<span id="'+data.itemId+child[i].recommend_9["itemId"]+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;">';
			works += '					<div class="onCorrect">&nbsp;</div>';
			works += '				</span>';
			works += '			</div>';
			works += '		</div>';
			works += '		<div id="'+data.itemId+i+'_AttList" class="main_inner_file_list">';
			//works += '		<ul><li>';
			//works += '			<a class="main_inner_filelist_a" onclick=SurveyBuild.downLoadFile(this,\"'+data.instanceId+'\") file-index="1">'+child[i].recommend_9["filename"]+'</a>';
			//works += '			<div class="main_inner_file_del" onclick=SurveyBuild.deleteFile(this,\"'+data.instanceId+'\")><img width="15" height="15" src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="'+MsgSet["DEL"]+'">'+MsgSet["DEL"]+'</div>';
			//works += '		</li></ul>';
			works += '		</div>';
			works += '	</div>';
			works += '	<input id="'+data.itemId+child[i].recommend_9["itemId"]+'" type="hidden" name="'+data.itemId+child[i].recommend_9["itemId"]+'" value="'+child[i].recommend_9["value"]+'">';
			works += '</div>';
		}
		//上传附件-结束
		
		if ((data.itemLx=="L"&&child[i].recommend_8["value"]!="U")||data.itemLx=="F"){
			works += '<div class="main_inner_content_info" style="margin: 0 auto;text-align:center;'+((_zd=="Z")?"display:none":"")+'">';
			//发送邮件
			works += '<div style="padding-left:266px;padding-top:5px;">';
			works += '<div id="sendEmailS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;'+((_zd=="Y"||_zd=="Z")?"display:none":"")+'">';
			works += '<div id="sendEmail_'+i+'" class="bt_blue" style="width:175px">'+MsgSet["Send"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			//重新发送
			works += '<div id="reSendEmailS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;'+((_zd!="Y")?"display:none":"")+'">';
			works += '<div id="reSendEmail_'+i+'" class="bt_blue" style="width:175px">'+MsgSet["Resend"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			//更换推荐人
			works += '<div id="changeRecS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;'+((_zd!="Y")?"display:none":"")+'">';
			works += '<div id="changeRec_'+i+'" class="bt_blue" style="width:175px">'+MsgSet["RepRecom"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			works += '</div>';
			works += '</div>';
			//推荐信状态
			works += '<div class="main_inner_content_info_autoheight" style="text-align:center" id="Tjxzt_'+i+'">';
			works += '<div id="tjxzt_desc_'+i+'">'+MsgSet["ReLeSt"]+': <span class="font_orange_16px">'+_tjx_zt+'</span>';
			
			/*推荐信链接*/
			if(SurveyBuild.appManager == "Y"){
				if(refLetterUrl!=""){
					works += '&nbsp;&nbsp;<a href="javascript:void(0);" style="color:#0088cc;text-decoration:underline;cursor:pointer" onclick=viewUrl("'+refLetterUrl+'")>'+MsgSet["VIEWREFLETTER"] + '</a>';
				}else{
					if(_refFileUrl!=""){
						//图片格式
						var refPicType = ['BMP','JPG','JPEG','PNG','GIF'];
						var refFileSuffix = (_refFileName.substring(_refFileName.lastIndexOf(".") + 1)).toUpperCase();
						if (refPicType.indexOf(refFileSuffix) != -1){
							//上传的推荐信文件是图片
							works += '&nbsp;&nbsp;<a style="color:#0088cc;text-decoration:underline;cursor:pointer" onclick=viewRefImg("'+_refFileUrl+'")>'+MsgSet["VIEWREFLETTER"] + '</a>';
						}else{
							if (refFileSuffix == 'PDF'){
								works += '&nbsp;&nbsp;<a href="javascript:void(0);" style="color:#0088cc;text-decoration:underline;cursor:pointer" onclick=viewRefPdf("'+_refFileUrl+'")>'+MsgSet["VIEWREFLETTER"] + '</a>';
							}else{
								works += '&nbsp;&nbsp;<a href="'+_refFileUrl+'" style="color:#0088cc;text-decoration:underline;cursor:pointer">'+MsgSet["VIEWREFLETTER"] + '</a>';
							}
						}
					}
				}
			}	
			works += '</div>';
			
			works += '</div>';
			
		}else{
			works += '<div class="main_inner_content_info" style="margin: 0 auto;text-align:center">';
			works += '<div style="padding-left:266px;padding-top:5px;">';
			//发送邮件
			works += '<div id="sendEmailS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;display:none;">';
			works += '<div id="sendEmail_'+i+'" class="bt_blue" style="width:175px">'+MsgSet["Send"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			//重新发送
			works += '<div id="reSendEmailS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;'+((_zd!="Y"&&_zd!="Z")?"display:none":"")+'">';
			works += '<div id="reSendEmail_'+i+'" class="bt_blue" style="width:175px">'+MsgSet["Resend"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			//更换推荐人
			works += '<div id="changeRecS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;'+((_zd!="Y"&&_zd!="Z")?"display:none":"")+'">';
			works += '<div id="changeRec_'+i+'" class="bt_blue" style="width:175px">'+MsgSet["RepRecom"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			works += '</div>';
			works += '</div>';
			//推荐信状态
			works += '<div class="main_inner_content_info_autoheight" id="Tjxzt_'+i+'" style="text-align:center;display:none">';
			works += '<div id="tjxzt_desc_'+i+'">'+MsgSet["ReLeSt"]+': <span class="font_orange_16px">'+_tjx_zt+'</span>';
			//推荐信链接
			if(SurveyBuild.appManager == "Y"){
				if(refLetterUrl!=""){
					works += '&nbsp;&nbsp;<a href="javascript:void(0);" style="color:#0088cc;text-decoration:underline;cursor:pointer" onclick=viewUrl("'+refLetterUrl+'")>'+MsgSet["VIEWREFLETTER"] + '</a>';
				}else{
					if(_refFileUrl!=""){
						//图片格式
						var refPicType = ['BMP','JPG','JPEG','PNG','GIF'];
						var refFileSuffix = (_refFileName.substring(_refFileName.lastIndexOf(".") + 1)).toUpperCase();
						if (refPicType.indexOf(refFileSuffix) != -1){
							//上传的推荐信文件是图片
							works += '&nbsp;&nbsp;<a style="color:#0088cc;text-decoration:underline;cursor:pointer" onclick=viewRefImg("'+_refFileUrl+'")>'+MsgSet["VIEWREFLETTER"] + '</a>';
						}else{
							if (refFileSuffix == 'PDF'){
								works += '&nbsp;&nbsp;<a href="javascript:void(0);" style="color:#0088cc;text-decoration:underline;cursor:pointer" onclick=viewRefPdf("'+_refFileUrl+'")>'+MsgSet["VIEWREFLETTER"] + '</a>';
							}else{
								works += '&nbsp;&nbsp;<a href="'+_refFileUrl+'" style="color:#0088cc;text-decoration:underline;cursor:pointer">'+MsgSet["VIEWREFLETTER"] + '</a>';
							}
						}
					}
				}
			}
			works += '</div>';
			works += '</div>';
		}
		/*保存推荐信信息*/
		works += '<div style="display:none">';
		works += '<div id="saveRec_'+i+'" class="bt_blue" style="width:125px">保存</div><a href="#" class="alpha"></a>';
		works += '</div>';
		

		works += '<div><input type="hidden" id="yincang_tx" value="'+data.toSendE+'"></div>';
		works += '<div><input type="hidden" id="max_tjx_ts" value="'+data.maxLines+'"></div>';
				
		works += '</div>';
		
		//footer
		works += '<div class="main_inner_content_foot"></div>';
		
		works += '</div>';
		
        return works; 
    },
    _eventbind: function(data) {
		
        var children = data.children,
        len = children.length;

		for (var i=1;i<=len;i++)
		{
			var num=i;
			
			//发送邮件
			$("#sendEmail_"+(Number(num)-1)).unbind("click");
			$("#sendEmail_"+(Number(num)-1)).click(function(){
				var lineno = parseInt($(this).closest(".main_inner_content_para").index());
				
				var m=Number(lineno)+1;
				var mm = this.id.split("_")[1];
				var mm=Number(mm)+1;
				var _yz="";
				
				var rec_title = $("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).val();
				if (children[m-1].recommend_18["useby"]=="Y"&&rec_title==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).blur();
				}
				
				var rec_gname = $("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).val();
				if (children[m-1].recommend_17["useby"]=="Y"&&rec_gname==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).blur();
				}
				
				var rec_name = $("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).val();
				if (children[m-1].recommend_1["useby"]=="Y"&&rec_name==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).blur();
				}
				var rec_company = $("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).val();
				if (children[m-1].recommend_2["useby"]=="Y"&&rec_company==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).blur();
				}
				
				var rec_post = $("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).val();
				if (children[m-1].recommend_3["useby"]=="Y"&&rec_post==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).blur();
				}
				
				var rec_phone_area = $("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).val();
				if (children[m-1].recommend_16["useby"]=="Y"&&rec_phone_area==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).blur();
				}
				var _result_area = /^[\d-+]+$/.test(rec_phone_area);
				if(!_result_area){
                    _yz="2";
					$("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).blur();
                }
				var rec_phone_no = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
				if (children[m-1].recommend_4["useby"]=="Y"&&rec_phone_no==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).blur();
				}
				var _result = /^[\d-+]+$/.test(rec_phone_no);
				if(!_result){
                    _yz="2";
					$("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).blur();
                }
				var rec_email = $("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).val();
				if (children[m-1].recommend_5["useby"]=="Y"&&rec_email==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).blur();
				}
				
				var rec_sex = $("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).val();
				//var rec_sex = $("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]:checked").val();
				if (children[m-1].recommend_15["useby"]=="Y"&&rec_sex==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).blur();
				}else if (children[m-1].recommend_15["useby"]!="Y")
				{
					rec_sex="";
				}
				var rec_relation = $("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).val();
				if (children[m-1].recommend_6["useby"]=="Y"&&rec_relation==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).blur();
				}
				var rec_language = $("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).val();
				//var rec_language = $("input[name="+data["itemId"]+children[0].recommend_7["itemId"]+"]:checked").val();
				if (children[m-1].recommend_7["useby"]=="Y"&&rec_language==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).blur();
				}
				//var rec_num= m;
				var rec_num = children[m-1].recommend_99["value"];
				var rec_by1 = $("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).val();
				if (children[m-1].recommend_10["useby"]=="Y"&&rec_by1==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).blur();
				}
				var rec_by2 = $("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).val();
				if (children[m-1].recommend_11["useby"]=="Y"&&rec_by2==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).blur();
				}
				var rec_by3 = $("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).val();
				if (children[m-1].recommend_12["useby"]=="Y"&&rec_by3==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).blur();
				}
				var rec_by4 = $("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).val();
				if (children[m-1].recommend_13["useby"]=="Y"&&rec_by4==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).blur();
				}
				var rec_by5 = $("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).val();
				if (children[m-1].recommend_14["useby"]=="Y"&&rec_by5==""){
					_yz="1";
					$("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).blur();
				}
				rec_title = SurveyBuild.specialCharReplace(rec_title);
				rec_gname = SurveyBuild.specialCharReplace(rec_gname);
				rec_name = SurveyBuild.specialCharReplace(rec_name);
				rec_company = SurveyBuild.specialCharReplace(rec_company);
				rec_post = SurveyBuild.specialCharReplace(rec_post);
				rec_phone_area = SurveyBuild.specialCharReplace(rec_phone_area);
				rec_phone_no = SurveyBuild.specialCharReplace(rec_phone_no);
				rec_email = SurveyBuild.specialCharReplace(rec_email);
				rec_relation = SurveyBuild.specialCharReplace(rec_relation);
				rec_by1 = SurveyBuild.specialCharReplace(rec_by1);
				rec_by2 = SurveyBuild.specialCharReplace(rec_by2);
				rec_by3 = SurveyBuild.specialCharReplace(rec_by3);
				rec_by4 = SurveyBuild.specialCharReplace(rec_by4);
				rec_by5 = SurveyBuild.specialCharReplace(rec_by5);
				var _file=children[m-1].recommend_9["filename"];
				var _sysfile=children[m-1].recommend_9["sysFileName"];
				var _accessPath=children[m-1].recommend_9["accessPath"];
				_file = SurveyBuild.specialCharReplace(_file);
				_sysfile = SurveyBuild.specialCharReplace(_sysfile);
				_accessPath = SurveyBuild.specialCharReplace(_accessPath);
				
				if (_yz==""){
					
					var std=/^([\w\-\.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\w\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$/;
					if (!std.test(rec_email))
					{
						$("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).focus();
						$("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).blur();
						alert(MsgSet["REC_ERR"]);
					}else{
						
						var _tz_app_ins_id=SurveyBuild.appInsId;
						var _tz_app_version_id=SurveyBuild.appInsVersion;
						var _email_tx = $("#yincang_tx").val();
						_email_tx = SurveyBuild.specialCharReplace(_email_tx);
				
						var _Url = SurveyBuild.tzGeneralURL + "?tzParams=";
						/*
						var param = "{'ComID':'TZ_GD_TJX_COM','PageID':'TZ_SEND_REF_STD','OperateType':'SEND','comParams':{'rec_app_ins_id':'"+_tz_app_ins_id+"','TZ_APP_INS_VERSION':'"+_tz_app_version_id+"','rec_num':'"+rec_num+"','rec_title':'"+rec_title+"','rec_gname':'"+rec_gname+"','rec_name':'"+rec_name+"','rec_company':'"+rec_company+"','rec_post':'"+rec_post+"','rec_phone_area':'"+rec_phone_area+"','rec_phone_no':'"+rec_phone_no+"','rec_email':'"+rec_email+"','rec_sex':'"+rec_sex+"','rec_relation':'"+rec_relation+"','rec_language':'"+rec_language+"','email_tx':'"+_email_tx+"','rec_by1':'"+rec_by1+"','rec_by2':'"+rec_by2+"','rec_by3':'"+rec_by3+"','rec_by4':'"+rec_by4+"','rec_by5':'"+rec_by5+"'}}";*/

						var param = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"SEND","comParams":{"rec_app_ins_id":"'+_tz_app_ins_id+'","TZ_APP_INS_VERSION":"'+_tz_app_version_id+'","rec_num":"'+rec_num+'","rec_title":"'+rec_title+'","rec_gname":"'+rec_gname+'","rec_name":"'+rec_name+'","rec_company":"'+rec_company+'","rec_post":"'+rec_post+'","rec_phone_area":"'+rec_phone_area+'","rec_phone_no":"'+rec_phone_no+'","rec_email":"'+rec_email+'","rec_sex":"'+rec_sex+'","rec_relation":"'+rec_relation+'","rec_language":"'+rec_language+'","email_tx":"'+_email_tx+'","rec_by1":"'+rec_by1+'","rec_by2":"'+rec_by2+'","rec_by3":"'+rec_by3+'","rec_by4":"'+rec_by4+'","rec_by5":"'+rec_by5+'","accessPath":"'+_accessPath+'","filename":"'+_file+'","sysfilename":"'+_sysfile+'"}}';

						$.ajax({
							type: "post",
							url: _Url + encodeURIComponent(param),
							dataType: "json",
							success: function(result){
								if (result.comContent=="SUCCESS"){
									$("#sendEmailS_"+(Number(mm)-1)).css("display","none");
									$("#reSendEmailS_"+(Number(mm)-1)).css("display","block");
									$("#changeRecS_"+(Number(mm)-1)).css("display","block");
									alert(MsgSet["SEND_SC"]);
									$("#tjxzt_desc_"+(Number(mm)-1)).html(MsgSet["ReLeSt"]+"：<span class='font_orange_16px'>"+MsgSet["SendEmail"]+"</span>");
									$("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).prop("readonly", true);
									$("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).prop("readonly", true);
									$("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).prop("readonly", true);
									$("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).prop("readonly", true);
									$("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).prop("readonly", true);
									$("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).prop("readonly", true);
									$("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]").prop("disabled", true);
									$("input[name="+data["itemId"]+children[m-1].recommend_7["itemId"]+"]").prop("disabled", true);
									$("input[name="+data["itemId"]+children[m-1].recommend_8["itemId"]+"]").prop("disabled", true);
									$("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).prop("readonly", true);
									$("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).prop("readonly", true);
									$("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).prop("readonly", true);
									$("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).prop("readonly", true);
									$("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).prop("readonly", true);
									$("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).prop("readonly", true);
									//$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).prop("disabled", true);
									//console.log($("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).closest(".main_inner_content_para").find(".chosen-container").html());
									$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).next(".chosen-container").unbind();
									
									//console.log($("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).html());
									//$("#" + data["itemId"] + children[m-1].recommend_18["itemId"] + "_chosen").find(".chosen-results").hide();
									//$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).chosen("destroy");
									$("#tjx_delete_"+(Number(m)-1)).hide();
									$("#app_save").click();
								}else {
									alert(result.comContent);
								}
							}
						});
					}
				}else if (_yz="2")
				{
					alert(MsgSet["REC_ERR"]);
					//$("#app_save").click();
				}else{
					alert(MsgSet["REC_ERR"]);
					//$("#app_save").click();
					//alert(MsgSet["TJX_NO_COMPLETE"]);
				}
			});
			//重发邮件
			$("#reSendEmail_"+(Number(num)-1)).unbind("click");
			$("#reSendEmail_"+(Number(num)-1)).click(function(){
				var lineno = parseInt($(this).closest(".main_inner_content_para").index());
				//var m = this.id.split("_")[1];
				var m=Number(lineno)+1;
				var rec_title = $("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).val();
				var rec_gname = $("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).val();
				var rec_name = $("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).val();
				var rec_company = $("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).val();
				var rec_post = $("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).val();
				var rec_phone_area = $("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).val();
				var rec_phone_no = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
				var rec_email = $("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).val();
				//var rec_sex = $("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]:checked").val();
				//var rec_sex = $("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]:checked").val();
				var rec_sex = $("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).val();
				if (children[m-1].recommend_15["useby"]=="Y"&&rec_sex==""){
				}else{
					rec_sex="";
				}
				var rec_relation = $("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).val();
				//var rec_language = $("input[name="+data["itemId"]+children[m-1].recommend_7["itemId"]+"]:checked").val();
				var rec_language = $("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).val();
				//var rec_num= m;
				var rec_num = children[m-1].recommend_99["value"];
				var rec_by1 = $("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).val();
				var rec_by2 = $("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).val();
				var rec_by3 = $("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).val();
				var rec_by4 = $("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).val();
				var rec_by5 = $("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).val();
				rec_title = SurveyBuild.specialCharReplace(rec_title);
				rec_gname = SurveyBuild.specialCharReplace(rec_gname);
				rec_name = SurveyBuild.specialCharReplace(rec_name);
				rec_company = SurveyBuild.specialCharReplace(rec_company);
				rec_post = SurveyBuild.specialCharReplace(rec_post);
				rec_phone_area = SurveyBuild.specialCharReplace(rec_phone_area);
				rec_phone_no = SurveyBuild.specialCharReplace(rec_phone_no);
				rec_email = SurveyBuild.specialCharReplace(rec_email);
				rec_relation = SurveyBuild.specialCharReplace(rec_relation);
				rec_by1 = SurveyBuild.specialCharReplace(rec_by1);
				rec_by2 = SurveyBuild.specialCharReplace(rec_by2);
				rec_by3 = SurveyBuild.specialCharReplace(rec_by3);
				rec_by4 = SurveyBuild.specialCharReplace(rec_by4);
				rec_by5 = SurveyBuild.specialCharReplace(rec_by5);
				
				var _file=children[m-1].recommend_9["filename"];
				var _sysfile=children[m-1].recommend_9["sysFileName"];
				var _accessPath=children[m-1].recommend_9["accessPath"];
				_file = SurveyBuild.specialCharReplace(_file);
				_sysfile = SurveyBuild.specialCharReplace(_sysfile);
				_accessPath = SurveyBuild.specialCharReplace(_accessPath);
				
				var _tz_app_ins_id=SurveyBuild.appInsId;
				var _tz_app_version_id=SurveyBuild.appInsVersion;
				var _email_tx = $("#yincang_tx").val();
				_email_tx = SurveyBuild.specialCharReplace(_email_tx);
				var _Url = SurveyBuild.tzGeneralURL + "?tzParams=";
				var param = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"SEND","comParams":{"rec_app_ins_id":"'+_tz_app_ins_id+'","TZ_APP_INS_VERSION":"'+_tz_app_version_id+'","rec_num":"'+rec_num+'","rec_title":"'+rec_title+'","rec_gname":"'+rec_gname+'","rec_name":"'+rec_name+'","rec_company":"'+rec_company+'","rec_post":"'+rec_post+'","rec_phone_area":"'+rec_phone_area+'","rec_phone_no":"'+rec_phone_no+'","rec_email":"'+rec_email+'","rec_sex":"'+rec_sex+'","rec_relation":"'+rec_relation+'","rec_language":"'+rec_language+'","email_tx":"'+_email_tx+'","rec_by1":"'+rec_by1+'","rec_by2":"'+rec_by2+'","rec_by3":"'+rec_by3+'","rec_by4":"'+rec_by4+'","rec_by5":"'+rec_by5+'","accessPath":"'+_accessPath+'","filename":"'+_file+'","sysfilename":"'+_sysfile+'"}}';
				
				$.ajax({
					type: "post",
					url: _Url + encodeURIComponent(param),
					dataType: "json",
					success: function(result){
						if (result.comContent=="SUCCESS"){
							alert(MsgSet["SEND_SC"]);
							$("#app_save").click();
						}else {
							alert(result.comContent);
						}
					}
				});
			});
			//保存推荐信信息
			$("#saveRec_"+(Number(num)-1)).unbind("click"); 
			$("#saveRec_"+(Number(num)-1)).click(function(){
				var lineno = parseInt($(this).closest(".main_inner_content_para").index());
				//var m = this.id.split("_")[1];
				var m;
				m=Number(lineno)+1;

				var _yz="";
				
				var rec_title = $("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).val();
				if (children[m-1].recommend_18["useby"]=="Y"&&rec_title==""){
					_yz="1";
				}
				var rec_gname = $("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).val();
				if (children[m-1].recommend_17["useby"]=="Y"&&rec_gname==""){
					_yz="1";
				}
				var rec_name = $("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).val();
				if (children[m-1].recommend_1["useby"]=="Y"&&rec_name==""){
					_yz="1";
				}
				var rec_company = $("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).val();
				if (children[m-1].recommend_2["useby"]=="Y"&&rec_company==""){
					_yz="1";
				}
				var rec_post = $("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).val();
				if (children[m-1].recommend_3["useby"]=="Y"&&rec_post==""){
					_yz="1";
				}
				var rec_phone_area = $("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).val();
				if (children[m-1].recommend_16["useby"]=="Y"&&rec_phone_area==""){
					_yz="1";
				}
				var _result_phone_area = /^[\d-+]+$/.test(rec_phone_area);
				if(!_result_phone_area){
                    _yz="1";
                }
				var rec_phone_no = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
				if (children[m-1].recommend_4["useby"]=="Y"&&rec_phone_no==""){
					_yz="1";
				}
				var _result = /^[\d-+]+$/.test(rec_phone_no);
                if(!_result){
                    _yz="1";
                }
				var rec_email = $("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).val();
				if (children[m-1].recommend_5["useby"]=="Y"&&rec_email==""){
					_yz="1";
				}
				var rec_sex = $("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).val();
				//var rec_sex = $("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]:checked").val();
				if (children[m-1].recommend_15["useby"]=="Y"&&rec_sex==""){
					_yz="1";
				}else if (children[m-1].recommend_15["useby"]!="Y")
				{
					rec_sex="";
				}
				/*
				var rec_sex = $("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]:checked").val();
				if (children[m-1].recommend_15["useby"]=="Y"&&rec_sex==""){
					_yz="1";
				}else{
					rec_sex="";
				}*/
				var rec_relation = $("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).val();
				if (children[m-1].recommend_6["useby"]=="Y"&&rec_relation==""){
					_yz="1";
				}
				var rec_language = $("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).val();
				if (children[m-1].recommend_7["useby"]=="Y"&&rec_language==""){
					_yz="1";
				}
				var rec_by1 = $("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).val();
				if (children[m-1].recommend_10["useby"]=="Y"&&rec_by1==""){
					_yz="1";
				}
				var rec_by2 = $("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).val();
				if (children[m-1].recommend_11["useby"]=="Y"&&rec_by2==""){
					_yz="1";
				}
				var rec_by3 = $("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).val();
				if (children[m-1].recommend_12["useby"]=="Y"&&rec_by3==""){
					_yz="1";
				}
				var rec_by4 = $("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).val();
				if (children[m-1].recommend_13["useby"]=="Y"&&rec_by4==""){
					_yz="1";
				}
				var rec_by5 = $("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).val();
				if (children[m-1].recommend_14["useby"]=="Y"&&rec_by5==""){
					_yz="1";
				}
				var std=/^([\w\-\.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\w\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$/;
				if (!std.test(rec_email)){
					_yz="1";
				}
				
				/*使用新增的行序号*/
				//var rec_num= m;
				var rec_num = children[m-1].recommend_99["value"];
				var rec_type = $("#" + data["itemId"] + children[m-1].recommend_8["itemId"]).val();
				
				var _file=children[m-1].recommend_9["filename"];
				var _sysfile=children[m-1].recommend_9["sysFileName"];
				var _accessPath=children[m-1].recommend_9["accessPath"];
				rec_title = SurveyBuild.specialCharReplace(rec_title);
				rec_gname = SurveyBuild.specialCharReplace(rec_gname);
				rec_name = SurveyBuild.specialCharReplace(rec_name);
				rec_company = SurveyBuild.specialCharReplace(rec_company);
				rec_post = SurveyBuild.specialCharReplace(rec_post);
				rec_phone_area = SurveyBuild.specialCharReplace(rec_phone_area);
				rec_phone_no = SurveyBuild.specialCharReplace(rec_phone_no);
				rec_email = SurveyBuild.specialCharReplace(rec_email);
				rec_relation = SurveyBuild.specialCharReplace(rec_relation);
				rec_by1 = SurveyBuild.specialCharReplace(rec_by1);
				rec_by2 = SurveyBuild.specialCharReplace(rec_by2);
				rec_by3 = SurveyBuild.specialCharReplace(rec_by3);
				rec_by4 = SurveyBuild.specialCharReplace(rec_by4);
				rec_by5 = SurveyBuild.specialCharReplace(rec_by5);
				_file = SurveyBuild.specialCharReplace(_file);
				_sysfile = SurveyBuild.specialCharReplace(_sysfile);
				_accessPath = SurveyBuild.specialCharReplace(_accessPath);
				if (rec_type=="U")
				{
					var _tz_tjx_valid = "N";
					if(_yz==""){
						_tz_tjx_valid = "Y";
					}
					var _tz_app_ins_id=SurveyBuild.appInsId;
					var _tz_app_version_id=SurveyBuild.appInsVersion;
					var _Url = SurveyBuild.tzGeneralURL + "?tzParams=";
					var param = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"SAVE","comParams":{"rec_app_ins_id":"'+_tz_app_ins_id+'","TZ_APP_INS_VERSION":"'+_tz_app_version_id+'","tjx_valid":"'+_tz_tjx_valid+'","rec_num":"'+rec_num+'","rec_title":"'+rec_title+'","rec_gname":"'+rec_gname+'","rec_name":"'+rec_name+'","rec_company":"'+rec_company+'","rec_post":"'+rec_post+'","rec_phone_area":"'+rec_phone_area+'","rec_phone_no":"'+rec_phone_no+'","rec_email":"'+rec_email+'","rec_sex":"'+rec_sex+'","rec_relation":"'+rec_relation+'","rec_language":"'+rec_language+'","email_tx":"'+_email_tx+ '","rec_type":"'+rec_type+ '","rec_by1":"'+rec_by1+'","rec_by2":"'+rec_by2+'","rec_by3":"'+rec_by3+'","rec_by4":"'+rec_by4+'","rec_by5":"'+rec_by5+'","accessPath":"'+_accessPath+'","filename":"'+_file+'","sysfilename":"'+_sysfile+'"}}';
					var _email_tx = $("#yincang_tx").val();
					//$("#"+data.itemId+child[cins].itemId+"Attch").text(obj.msg.fileName);
					$.ajax({
						type: "post",
						url: _Url + encodeURIComponent(param),
						dataType: "json",
						async: false,
						success: function(result){
							if (result.comContent=="SUCCESS"){
								
							}else {
								//alert(result.comContent);
							}
						}
					});
				}
			});
			//更换推荐人
			$("#changeRec_"+(Number(num)-1)).unbind("click");
			$("#changeRec_"+(Number(num)-1)).click(function(){
				var lineno = parseInt($(this).closest(".main_inner_content_para").index());
				
				var m=Number(lineno)+1;
				var mm = this.id.split("_")[1];
				var mm=Number(mm)+1;
				var rec_title = $("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).val();
				var rec_gname = $("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).val();
				var rec_name = $("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).val();
				var rec_company = $("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).val();
				var rec_post = $("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).val();
				var rec_phone_area = $("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).val();
				var rec_phone_no = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
				var rec_email = $("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).val();
				var rec_sex = $("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).val();
				var rec_relation = $("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).val();
				//var rec_language = $("input[name="+data["itemId"]+children[m-1].recommend_7["itemId"]+"]:checked").val();
				var rec_language = $("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).val();
				//var rec_num= m;
				var rec_num = children[m-1].recommend_99["value"];
				var rec_by1 = $("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).val();
				var rec_by2 = $("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).val();
				var rec_by3 = $("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).val();
				var rec_by4 = $("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).val();
				var rec_by5 = $("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).val();
				rec_title = SurveyBuild.specialCharReplace(rec_title);
				rec_gname = SurveyBuild.specialCharReplace(rec_gname);
				rec_name = SurveyBuild.specialCharReplace(rec_name);
				rec_company = SurveyBuild.specialCharReplace(rec_company);
				rec_post = SurveyBuild.specialCharReplace(rec_post);
				rec_phone_area = SurveyBuild.specialCharReplace(rec_phone_area);
				rec_phone_no = SurveyBuild.specialCharReplace(rec_phone_no);
				rec_email = SurveyBuild.specialCharReplace(rec_email);
				rec_relation = SurveyBuild.specialCharReplace(rec_relation);
				rec_by1 = SurveyBuild.specialCharReplace(rec_by1);
				rec_by2 = SurveyBuild.specialCharReplace(rec_by2);
				rec_by3 = SurveyBuild.specialCharReplace(rec_by3);
				rec_by4 = SurveyBuild.specialCharReplace(rec_by4);
				rec_by5 = SurveyBuild.specialCharReplace(rec_by5);
				
				var _tz_app_ins_id=SurveyBuild.appInsId;
				var _tz_app_version_id=SurveyBuild.appInsVersion;
				var _Url = SurveyBuild.tzGeneralURL + "?tzParams=";
				var param = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"CHANGE","comParams":{"rec_app_ins_id":"'+_tz_app_ins_id+'","TZ_APP_INS_VERSION":"'+_tz_app_version_id+'","rec_num":"'+rec_num+'","rec_title":"'+rec_title+'","rec_gname":"'+rec_gname+'","rec_name":"'+rec_name+'","rec_company":"'+rec_company+'","rec_post":"'+rec_post+'","rec_phone_area":"'+rec_phone_area+'","rec_phone_no":"'+rec_phone_no+'","rec_email":"'+rec_email+'","rec_sex":"'+rec_sex+'","rec_relation":"'+rec_relation+'","rec_language":"'+rec_language+'","email_tx":"'+_email_tx+'","rec_by1":"'+rec_by1+'","rec_by2":"'+rec_by2+'","rec_by3":"'+rec_by3+'","rec_by4":"'+rec_by4+'","rec_by5":"'+rec_by5+'"}}';
				var _email_tx = $("#yincang_tx").val();
				//console.log(encodeURIComponent(param));
				
				$.ajax({
					type: "post",
					url: _Url + encodeURIComponent(param),
					dataType: "json",
					success: function(result){
						if (result.comContent=="SUCCESS"){
							$("#sendEmailS_"+(Number(mm)-1)).css("display","block");
							$("#reSendEmailS_"+(Number(mm)-1)).css("display","none");
							$("#changeRecS_"+(Number(mm)-1)).css("display","none");
							$("#tjxzt_desc_"+(Number(mm)-1)).html(MsgSet["ReLeSt"]+"：<span class='font_orange_16px'>"+MsgSet["Unsent"]+"</span>");
							//$("#tjxzt_desc_"+(Number(rec_num)-1)).html("推荐信状态：未发送");
							$("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).removeAttr("readonly");
							$("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]").removeAttr("disabled");
							$("input[name="+data["itemId"]+children[m-1].recommend_7["itemId"]+"]").removeAttr("disabled");
							$("input[name="+data["itemId"]+children[m-1].recommend_8["itemId"]+"]").removeAttr("disabled");
							$("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).removeAttr("readonly");
							console.log($("#" + data["itemId"] + children[m-1].recommend_18["itemId"]));
							//$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).trigger("liszt:updated");
							//$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).find(".chosen-results").show();
							//$("#" + data["itemId"] + children[m-1].recommend_18["itemId"] + "_chosen").find(".chosen-results").show();
							$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).chosen("destroy");
							$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).chosen();
							$("#tjx_delete_"+(Number(mm)-1)).show();
							$("#app_save").click();
						}else {
							alert(result.comContent);
						}
					}
				});
			});
			$("#"+data.itemId + children[num-1].recommend_15["itemId"]+"_M").click(function(){
				
                var lineno = parseInt($(this).closest(".main_inner_content_para").index());

                children[lineno].recommend_15["option"]["MAN"]["checked"] = "Y";
                children[lineno].recommend_15["option"]["WOMAN"]["checked"] = "N";

				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
				
				$("#"+m).focus();
				$("#"+m).blur();
				
			});
			$("#"+data.itemId + children[num-1].recommend_15["itemId"]+"_F").click(function(){
				
                var lineno = parseInt($(this).closest(".main_inner_content_para").index());
                children[lineno].recommend_15["option"]["MAN"]["checked"] = "N";
                children[lineno].recommend_15["option"]["WOMAN"]["checked"] = "Y";
				
				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
				$("#"+m).focus();
				$("#"+m).blur();
			});
			$("#"+data.itemId + children[num-1].recommend_8["itemId"]+"_U").click(function(){
			
                var lineno = parseInt($(this).closest(".main_inner_content_para").index());
                children[lineno].recommend_8["option"]["SEND"]["checked"] = "N";
                children[lineno].recommend_8["option"]["UPLOAD"]["checked"] = "Y";
				
				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
				var n = $(this).attr("align");

				$("#sendEmailS_"+n).css("display","none");
				$("#Tjxzt_"+n).css("display","none");
				$("#Tjxfj_show_"+n).css("display","block");
			});
			$("#"+data.itemId + children[num-1].recommend_8["itemId"]+"_S").click(function(){
				
                var lineno = parseInt($(this).closest(".main_inner_content_para").index());
                children[lineno].recommend_8["option"]["SEND"]["checked"] = "Y";
                children[lineno].recommend_8["option"]["UPLOAD"]["checked"] = "N";
				
				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
				var n = $(this).attr("align");

				var instanceId = $(this).closest(".dhcontainer").attr("data-instancid");
				var data = SurveyBuild._items[instanceId];
				var child = data.children[n];
				var itemId = data.itemId;
				children[lineno]["recommend_9"].filename = "";
				children[lineno]["recommend_9"].sysFileName = "";
				children[lineno]["recommend_9"].orderby = "";
				children[lineno]["recommend_9"].viewFileName = "";
				$("#"+data.itemId+n+"_AttList").html("");
				
				//SurveyBuild.Tjxdelete(this,\"'+cins+'\",'+index+')
				$("#sendEmailS_"+n).css("display","block");
				$("#Tjxzt_"+n).css("display","block");
				$("#Tjxfj_show_"+n).css("display","none");
			});
			$("#"+data.itemId + children[num-1].recommend_7["itemId"]+"_C").click(function(){
			
				var lineno = parseInt($(this).closest(".main_inner_content_para").index());
                children[lineno].recommend_7["option"]["ZHS"]["checked"] = "Y";
                children[lineno].recommend_7["option"]["ENG"]["checked"] = "N";
				
				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
			});
			$("#"+data.itemId + children[num-1].recommend_7["itemId"]+"_E").click(function(){
			
				var lineno = parseInt($(this).closest(".main_inner_content_para").index());
                children[lineno].recommend_7["option"]["ZHS"]["checked"] = "N";
                children[lineno].recommend_7["option"]["ENG"]["checked"] = "Y";
				
				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
			});
			
			//上传按钮
			var $fileInput = $("#"+data.itemId + children[num-1].recommend_9["itemId"]+"File");
			$fileInput.mouseover(function(e){
				$uplBtn = $(this).prev(".bt_blue");
				$uplBtn.css("opacity","0.8");	
			});
			$fileInput.mouseout(function(e) {
				$uplBtn = $(this).prev(".bt_blue");
				$uplBtn.css("opacity","1");
			});
			
			/*推荐信称呼*/
		
			$("#"+data.itemId + children[num-1].recommend_18["itemId"]).change(function(){
				var lineno = parseInt($(this).closest(".main_inner_content_para").index());
				
				children[lineno].recommend_18.wzsm = $("#" + this.id)[0].options[$("#" + this.id)[0].selectedIndex].text;
				
			});
			
		}

		for (var i=1;i<=data.children.length;i++)
		{
			var $tjr_title = $("#" + data.itemId + children[i - 1].recommend_18["itemId"]);
			var $tjr_gname = $("#" + data.itemId + children[i - 1].recommend_17["itemId"]);
			var $tjr_name = $("#" + data.itemId + children[i - 1].recommend_1["itemId"]);
			var $tjr_company = $("#" + data.itemId + children[i - 1].recommend_2["itemId"]);
			var $tjr_post = $("#" + data.itemId + children[i - 1].recommend_3["itemId"]);
			var $tjr_phone_area = $("#" + data.itemId + children[i - 1].recommend_16["itemId"]);
			var $tjr_phone_no = $("#" + data.itemId + children[i - 1].recommend_4["itemId"]);
			var $tjr_email = $("#" + data.itemId + children[i - 1].recommend_5["itemId"]);
			var $tjr_sex = $("#" + data.itemId + children[i - 1].recommend_15["itemId"]);
			var $tjr_relation = $("#" + data.itemId + children[i - 1].recommend_6["itemId"]);
			var $tjr_by1 = $("#" + data.itemId + children[i - 1].recommend_10["itemId"]);
			var $tjr_by2 = $("#" + data.itemId + children[i - 1].recommend_11["itemId"]);
			var $tjr_by3 = $("#" + data.itemId + children[i - 1].recommend_12["itemId"]);
			var $tjr_by4 = $("#" + data.itemId + children[i - 1].recommend_13["itemId"]);
			var $tjr_by5 = $("#" + data.itemId + children[i - 1].recommend_14["itemId"]);
			
			//称呼
			$tjr_title.formValidator({tipID:data["itemId"] + children[i - 1].recommend_18["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_title.functionValidator({
				fun:function(val,elem){
					
					var _title=val;
					
					//姓氏
					var _name_id=elem.id.replace("r_title","r_name");
					var _name=$("#"+_name_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_title","r_gname");
					var _gname=$("#"+_gname_id).val();
					
					//公司
					var _company_id=elem.id.replace("r_title","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_title","r_post");
					var _post=$("#"+_post_id).val();
					
					//区号
					var _phone_area_id=elem.id.replace("r_title","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//手机
					var _phone_no_id=elem.id.replace("r_title","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_title","r_email");
					var _email=$("#"+_email_id).val();
					
					//性别
					var _sex_id=elem.id.replace("r_title","r_sex");
					var _sex=$("#"+_sex_id).val();
					
					//关系
					var _relation_id=elem.id.replace("r_title","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_title","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_title","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_title","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_title","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_title","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_name!=""||_gname!=""||_company!=""||_post!=""||_phone_area!=""||_phone_no!=""||_email!=""||_sex!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_title=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			
			//姓名
			$tjr_name.formValidator({tipID:data["itemId"] + children[i - 1].recommend_1["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_name.functionValidator({
				fun:function(val,elem){
					var _name=val;
					
					//称呼
					var _title_id=elem.id.replace("r_name","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_name","r_gname");
					var _gname=$("#"+_gname_id).val();
					
					//公司
					var _company_id=elem.id.replace("r_name","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_name","r_post");
					var _post=$("#"+_post_id).val();
					
					//区号
					var _phone_area_id=elem.id.replace("r_name","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//手机
					var _phone_no_id=elem.id.replace("r_name","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_name","r_email");
					var _email=$("#"+_email_id).val();
					
					//性别
					var _sex_id=elem.id.replace("r_name","r_sex");
					var _sex=$("#"+_sex_id).val();
					
					//关系
					var _relation_id=elem.id.replace("r_name","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_name","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_name","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_name","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_name","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_name","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_title!=""||_gname!=""||_company!=""||_post!=""||_phone_area!=""||_phone_no!=""||_email!=""||_sex!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_name=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			
			//名字
			$tjr_gname.formValidator({tipID:data["itemId"] + children[i - 1].recommend_17["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_gname.functionValidator({
				fun:function(val,elem){
					var _gname=val;
					
					//称呼
					var _title_id=elem.id.replace("r_gname","r_title");
					var _title=$("#"+_title_id).val();
					
					//姓氏
					var _name_id=elem.id.replace("r_gname","r_name");
					var _name=$("#"+_name_id).val();
					
					//公司
					var _company_id=elem.id.replace("r_gname","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_gname","r_post");
					var _post=$("#"+_post_id).val();
					
					//区号
					var _phone_area_id=elem.id.replace("r_gname","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//手机
					var _phone_no_id=elem.id.replace("r_gname","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_gname","r_email");
					var _email=$("#"+_email_id).val();
					
					//性别
					var _sex_id=elem.id.replace("r_gname","r_sex");
					var _sex=$("#"+_sex_id).val();
					
					//关系
					var _relation_id=elem.id.replace("r_gname","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_gname","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_gname","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_gname","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_gname","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_gname","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_title!=""||_name!=""||_company!=""||_post!=""||_phone_area!=""||_phone_no!=""||_email!=""||_sex!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_gname=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			//公司
			$tjr_company.formValidator({tipID:data["itemId"] + children[i - 1].recommend_2["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_company.functionValidator({
				fun:function(val,elem){
					var _company=val;
					
					//称呼
					var _title_id=elem.id.replace("r_company","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_company","r_gname");
					var _gname=$("#"+_gname_id).val();
					
					//姓名
					var _name_id=elem.id.replace("r_company","r_name");
					var _name=$("#"+_name_id).val();
					//职务
					var _post_id=elem.id.replace("r_company","r_post");
					var _post=$("#"+_post_id).val();
					
					//区号
					var _phone_area_id=elem.id.replace("r_company","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//手机
					var _phone_no_id=elem.id.replace("r_company","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_company","r_email");
					var _email=$("#"+_email_id).val();
					//性别
					var _sex_id=elem.id.replace("r_company","r_sex");
					var _sex=$("#"+_sex_id).val();
					//关系
					var _relation_id=elem.id.replace("r_company","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_company","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_company","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_company","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_company","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_company","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_title!=""||_gname!=""||_name!=""||_post!=""||_phone_area!=""||_phone_no!=""||_email!=""||_sex!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_company=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			//职务
			$tjr_post.formValidator({tipID:data["itemId"] + children[i - 1].recommend_3["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_post.functionValidator({
				fun:function(val,elem){
					var _post=val;
					//称呼
					var _title_id=elem.id.replace("r_post","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_post","r_gname");
					var _gname=$("#"+_gname_id).val();
					//姓名
					var _name_id=elem.id.replace("r_post","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_post","r_company");
					var _company=$("#"+_company_id).val();
					//区号
					var _phone_area_id=elem.id.replace("r_post","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//手机
					var _phone_no_id=elem.id.replace("r_post","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_post","r_email");
					var _email=$("#"+_email_id).val();
					//性别
					var _sex_id=elem.id.replace("r_post","r_sex");
					var _sex=$("#"+_sex_id).val();
					//关系
					var _relation_id=elem.id.replace("r_post","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_post","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_post","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_post","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_post","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_post","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_title!=""||_gname!=""||_name!=""||_company!=""||_phone_area!=""||_phone_no!=""||_email!=""||_sex!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_post=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			
			//区号
			$tjr_phone_area.formValidator({tipID:data["itemId"] + children[i - 1].recommend_4["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_phone_area.functionValidator({
				fun:function(val,elem){
					
					var _phone_area = val;
					//称呼
					var _title_id=elem.id.replace("r_phone_area","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_phone_area","r_gname");
					var _gname=$("#"+_gname_id).val();
					/*手机*/
					var _phone_no_id= elem.id.replace("r_phone_area","r_phone_no");
					var _phone_no = $("#"+_phone_no_id).val();
					//姓名
					var _name_id=elem.id.replace("r_phone_area","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_phone_area","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_phone_area","r_post");
					var _post=$("#"+_post_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_phone_area","r_email");
					var _email=$("#"+_email_id).val();
					//性别
					var _sex_id=elem.id.replace("r_phone_area","r_sex");
					var _sex=$("#"+_sex_id).val();
					//关系
					var _relation_id=elem.id.replace("r_phone_area","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_phone_area","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_phone_area","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_phone_area","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_phone_area","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_phone_area","r_by5");
					var _by5=$("#"+_by5_id).val();
					
					if ((_title!=""||_gname!=""||_name!=""||_company!=""||_post!=""||_email!=""||_sex!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!=""||_phone_no!="")&&_phone_area=="")
					{
						return MsgSet["REQUIRE"];
					}else{
						var _result = /^[\d-+]+$/.test(_phone_area);
						if(!_result&&_phone_area!=""){
							return MsgSet["FORMATERROR"];
						}
					}
				}
			});
			
			//手机
			$tjr_phone_no.formValidator({tipID:data["itemId"] + children[i - 1].recommend_4["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_phone_no.functionValidator({
				fun:function(val,elem){
					var _phone_no=val;
					//称呼
					var _title_id=elem.id.replace("r_phone_no","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_phone_no","r_gname");
					var _gname=$("#"+_gname_id).val();
					//区号
					var _phone_area_id=elem.id.replace("r_phone_no","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//姓名
					var _name_id=elem.id.replace("r_phone_no","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_phone_no","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_phone_no","r_post");
					var _post=$("#"+_post_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_phone_no","r_email");
					var _email=$("#"+_email_id).val();
					//性别
					var _sex_id=elem.id.replace("r_phone_no","r_sex");
					var _sex=$("#"+_sex_id).val();
					//关系
					var _relation_id=elem.id.replace("r_phone_no","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_phone_no","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_phone_no","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_phone_no","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_phone_no","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_phone_no","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_title!=""||_gname!=""||_name!=""||_company!=""||_post!=""||_email!=""||_sex!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!=""||_phone_area!="")&&_phone_no=="")
					{
						return MsgSet["REQUIRE"];
					}else{
						var _result = /^[\d-+]+$/.test(_phone_no);
						if(!_result&&_phone_no!=""){
							return MsgSet["FORMATERROR"];
						}
					}
				}
			});
			//邮箱
			
			$tjr_email.formValidator({tipID:data["itemId"] + children[i - 1].recommend_5["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_email.functionValidator({
				fun:function(val,elem){
					var _email=val;
					//称呼
					var _title_id=elem.id.replace("r_email","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_email","r_gname");
					var _gname=$("#"+_gname_id).val();
					//姓名
					var _name_id=elem.id.replace("r_email","r_name");
					var _name=$("#"+_name_id).val();
					
					//公司
					var _company_id=elem.id.replace("r_email","r_company");
					var _company=$("#"+_company_id).val();
					
					//职务
					var _post_id=elem.id.replace("r_email","r_post");
					var _post=$("#"+_post_id).val();
					
					//区号
					var _phone_area_id=elem.id.replace("r_email","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
				
					//手机
					var _phone_no_id=elem.id.replace("r_email","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					
					//性别
					var _sex_id=elem.id.replace("r_email","r_sex");
					var _sex=$("#"+_sex_id).val();
					
					//关系
					var _relation_id=elem.id.replace("r_email","r_relation");
					var _relation=$("#"+_relation_id).val();
					
					//备用字段一
					var _by1_id=elem.id.replace("r_email","r_by1");
					var _by1=$("#"+_by1_id).val();
					
					//备用字段二
					var _by2_id=elem.id.replace("r_email","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_email","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_email","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_email","r_by5");
					var _by5=$("#"+_by5_id).val();
					
					var std=/^([\w\-\.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\w\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$/;
					if ((_title!=""||_gname!=""||_name!=""||_company!=""||_post!=""||_phone_area!=""||_phone_no!=""||_sex!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_email=="")
					{
						return MsgSet["REQUIRE"];
					}else{
						if (!std.test(_email)&&_email!="")
						{
							return MsgSet["FORMATERROR"];
						}
					}
				}
			});
			
			//性别
			
			$tjr_sex.formValidator({tipID:data["itemId"] + children[i - 1].recommend_15["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_sex.functionValidator({
				fun:function(val,elem){
					var _sex=val;
					//称呼
					var _title_id=elem.id.replace("r_sex","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_sex","r_gname");
					var _gname=$("#"+_gname_id).val();
					//姓名
					var _name_id=elem.id.replace("r_sex","r_name");
					var _name=$("#"+_name_id).val();
					
					//公司
					var _company_id=elem.id.replace("r_sex","r_company");
					var _company=$("#"+_company_id).val();
					
					//职务
					var _post_id=elem.id.replace("r_sex","r_post");
					var _post=$("#"+_post_id).val();
					
					//区号
					var _phone_area_id=elem.id.replace("r_sex","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
				
					//手机
					var _phone_no_id=elem.id.replace("r_sex","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_sex","r_email");
					var _email=$("#"+_email_id).val();
					//关系
					var _relation_id=elem.id.replace("r_sex","r_relation");
					var _relation=$("#"+_relation_id).val();
					
					//备用字段一
					var _by1_id=elem.id.replace("r_sex","r_by1");
					var _by1=$("#"+_by1_id).val();
					
					//备用字段二
					var _by2_id=elem.id.replace("r_sex","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_sex","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_sex","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_sex","r_by5");
					var _by5=$("#"+_by5_id).val();
					
					if ((_title!=""||_gname!=""||_name!=""||_company!=""||_post!=""||_phone_area!=""||_phone_no!=""||_email!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_sex=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			//$tjr_email.regexValidator({regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onError:(children[0].recommend_5["itemName"]+MsgSet["FORMATERROR"])});
			//与申请人关系
			$tjr_relation.formValidator({tipID:data["itemId"] + children[i - 1].recommend_6["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_relation.functionValidator({
				fun:function(val,elem){
					var _relation=val;
					//称呼
					var _title_id=elem.id.replace("r_relation","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_relation","r_gname");
					var _gname=$("#"+_gname_id).val();
					//姓名
					var _name_id=elem.id.replace("r_relation","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_relation","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_relation","r_post");
					var _post=$("#"+_post_id).val();
					//区号
					var _phone_area_id=elem.id.replace("r_relation","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//手机
					var _phone_no_id=elem.id.replace("r_relation","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_relation","r_email");
					var _email=$("#"+_email_id).val();
					//性别
					var _sex_id=elem.id.replace("r_relation","r_sex");
					var _sex=$("#"+_sex_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_relation","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_relation","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_relation","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_relation","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_relation","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_title!=""||_gname!=""||_name!=""||_company!=""||_post!=""||_phone_area!=""||_phone_no!=""||_email!=""||_sex!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_relation=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			//备用字段一
			$tjr_by1.formValidator({tipID:data["itemId"] + children[i - 1].recommend_10["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by1.functionValidator({
				fun:function(val,elem){
					var _by1=val;
					//称呼
					var _title_id=elem.id.replace("r_by1","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_by1","r_gname");
					var _gname=$("#"+_gname_id).val();
					//姓名
					var _name_id=elem.id.replace("r_by1","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_by1","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_by1","r_post");
					var _post=$("#"+_post_id).val();
					//区号
					var _phone_area_id=elem.id.replace("r_by1","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//手机
					var _phone_no_id=elem.id.replace("r_by1","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_by1","r_email");
					var _email=$("#"+_email_id).val();
					//性别
					var _sex_id=elem.id.replace("r_by1","r_sex");
					var _sex=$("#"+_sex_id).val();
					//关系
					var _relation_id=elem.id.replace("r_by1","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_by1","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_by1","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_by1","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_by1","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_title!=""||_gname!=""||_name!=""||_company!=""||_post!=""||_phone_area!=""||_phone_no!=""||_email!=""||_sex!=""||_relation!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_by1=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			//备用字段二
			$tjr_by2.formValidator({tipID:data["itemId"] + children[i - 1].recommend_11["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by2.functionValidator({
				fun:function(val,elem){
					var _by2=val;
					//称呼
					var _title_id=elem.id.replace("r_by2","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_by2","r_gname");
					var _gname=$("#"+_gname_id).val();
					//姓名
					var _name_id=elem.id.replace("r_by2","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_by2","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_by2","r_post");
					var _post=$("#"+_post_id).val();
					//区号
					var _phone_area_id=elem.id.replace("r_by2","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//手机
					var _phone_no_id=elem.id.replace("r_by2","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_by2","r_email");
					var _email=$("#"+_email_id).val();
					//性别
					var _sex_id=elem.id.replace("r_by2","r_sex");
					var _sex=$("#"+_sex_id).val();
					//关系
					var _relation_id=elem.id.replace("r_by2","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_by2","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_by2","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_by2","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_by2","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_title!=""||_gname!=""||_name!=""||_company!=""||_post!=""||_phone_area!=""||_phone_no!=""||_email!=""||_sex!=""||_relation!=""||_by1!=""||_by3!=""||_by4!=""||_by5!="")&&_by2=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			//备用字段三
			$tjr_by3.formValidator({tipID:data["itemId"] + children[i - 1].recommend_12["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by3.functionValidator({
				fun:function(val,elem){
					var _by3=val;
					//称呼
					var _title_id=elem.id.replace("r_by3","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_by3","r_gname");
					var _gname=$("#"+_gname_id).val();
					//姓名
					var _name_id=elem.id.replace("r_by3","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_by3","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_by3","r_post");
					var _post=$("#"+_post_id).val();
					//区号
					var _phone_area_id=elem.id.replace("r_by3","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//手机
					var _phone_no_id=elem.id.replace("r_by3","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_by3","r_email");
					var _email=$("#"+_email_id).val();
					//性别
					var _sex_id=elem.id.replace("r_by3","r_sex");
					var _sex=$("#"+_sex_id).val();
					//关系
					var _relation_id=elem.id.replace("r_by3","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_by3","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_by3","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_by3","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_by3","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_title!=""||_gname!=""||_name!=""||_company!=""||_post!=""||_phone_area!=""||_phone_no!=""||_email!=""||_sex!=""||_relation!=""||_by1!=""||_by2!=""||_by4!=""||_by5!="")&&_by3=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			//备用字段四
			$tjr_by4.formValidator({tipID:data["itemId"] + children[i - 1].recommend_13["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by4.functionValidator({
				fun:function(val,elem){
					var _by4=val;
					//称呼
					var _title_id=elem.id.replace("r_by4","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_by4","r_gname");
					var _gname=$("#"+_gname_id).val();
					//姓名
					var _name_id=elem.id.replace("r_by4","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_by4","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_by4","r_post");
					var _post=$("#"+_post_id).val();
					//区号
					var _phone_area_id=elem.id.replace("r_by4","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//手机
					var _phone_no_id=elem.id.replace("r_by4","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_by4","r_email");
					var _email=$("#"+_email_id).val();
					//性别
					var _sex_id=elem.id.replace("r_by4","r_sex");
					var _sex=$("#"+_sex_id).val();
					//关系
					var _relation_id=elem.id.replace("r_by4","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_by4","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_by4","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_by4","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_by4","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_title!=""||_gname!=""||_name!=""||_company!=""||_post!=""||_phone_area!=""||_phone_no!=""||_email!=""||_sex!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by5!="")&&_by4=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			//备用字段五
			$tjr_by5.formValidator({tipID:data["itemId"] + children[i - 1].recommend_14["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by5.functionValidator({
				fun:function(val,elem){
					var _by5=val;
					//称呼
					var _title_id=elem.id.replace("r_by5","r_title");
					var _title=$("#"+_title_id).val();
					
					//名字
					var _gname_id=elem.id.replace("r_by5","r_gname");
					var _gname=$("#"+_gname_id).val();
					//姓名
					var _name_id=elem.id.replace("r_by5","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_by5","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_by5","r_post");
					var _post=$("#"+_post_id).val();
					//区号
					var _phone_area_id=elem.id.replace("r_by5","r_phone_area");
					var _phone_area=$("#"+_phone_area_id).val();
					//手机
					var _phone_no_id=elem.id.replace("r_by5","r_phone_no");
					var _phone_no=$("#"+_phone_no_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_by5","r_email");
					var _email=$("#"+_email_id).val();
					//性别
					var _sex_id=elem.id.replace("r_by5","r_sex");
					var _sex=$("#"+_sex_id).val();
					//关系
					var _relation_id=elem.id.replace("r_by5","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_by5","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_by5","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_by5","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_by5","r_by4");
					var _by4=$("#"+_by4_id).val();
					if ((_title!=""||_gname!=""||_name!=""||_company!=""||_post!=""||_phone_area!=""||_phone_no!=""||_email!=""||_sex!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!="")&&_by5=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
		}
    }
})