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
    isDoubleLine: "Y",
    fixedContainer:"Y",//固定容器标识
    children:{
		"recommend_1": {
            "instanceId": "rec_name",
            "itemId": "r_name",
            "itemName": "姓名",
			"classname":"text",
            "orderby": 1,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
		"recommend_2": {
            "instanceId": "rec_company",
            "itemId": "r_company",
            "itemName": "单位",
			"classname":"text",
            "orderby": 2,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
        "recommend_3": {
            "instanceId": "rec_post",
            "itemId": "r_post",
            "itemName": "职务",
			"classname":"text",
            "orderby": 3,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
		"recommend_4": {
            "instanceId": "rec_phone",
            "itemId": "r_phone",
            "itemName": "手机",
			"classname":"text",
            "orderby": 4,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
		"recommend_5": {
            "instanceId": "rec_email",
            "itemId": "r_email",
            "itemName": "邮箱",
			"classname":"text",
            "orderby": 5,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
        "recommend_6": {
            "instanceId": "rec_relation",
            "itemId": "r_relation",
            "itemName": "申请人关系",
			"classname":"text",
            "orderby": 6,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_7": {
            "instanceId": "rec_language",
            "itemId": "r_language",
            "itemName": "推荐信语言",
			"classname":"radio",
            "orderby": 7,
			"StorageType":"D",
			"useby": "Y",
            "value": ""
        },
		"recommend_8": {
            "instanceId": "rec_way",
            "itemId": "r_way",
            "itemName": "推荐信类型",
			"classname":"radio",
            "orderby": 8,
			"StorageType":"D",
			"useby": "Y",
            "value": ""
        },
		"recommend_9": {
			"instanceId": "rec_attach",
			"itemId": "r_attach",
			"orderby": 9,
			"StorageType":"F",
			"value": "",
			"useby": "Y",
			"itemName": "上传附件",
			"filename":"",
			"sysFileName":"",
			"path":"",
			"classname":"refLetterFile",
			"accessPath":""
		},
		/*"recommend_9": {
            "instanceId": "rec_attach",
            "itemId": "r_attach",
            "itemName": "上传附件",
			"classname":"text",
            "orderby": 9,
			"useby": "",
            "value": ""
        },*/
		"recommend_10": {
            "instanceId": "rec_by1",
            "itemId": "r_by1",
            "itemName": "备用字段一",
			"classname":"text",
            "orderby": 10,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_11": {
            "instanceId": "rec_by2",
            "itemId": "r_by2",
            "itemName": "备用字段二",
			"classname":"text",
            "orderby": 11,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_12": {
            "instanceId": "rec_by3",
            "itemId": "r_by3",
            "itemName": "备用字段三",
			"classname":"text",
            "orderby": 12,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_13": {
            "instanceId": "rec_by4",
            "itemId": "r_by4",
            "itemName": "备用字段四",
			"classname":"text",
            "orderby": 13,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_14": {
            "instanceId": "rec_by5",
            "itemId": "r_by5",
            "itemName": "备用字段五",
			"classname":"text",
            "orderby": 14,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
		"recommend_15": {
            "instanceId": "rec_sex",
            "itemId": "r_sex",
            "itemName": "性别",
			"classname":"radio",
            "orderby": 15,
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
					"txt": "男",
					"weight": 0
				},"WOMAN":{
					"checked": "N",
					"code": "F",
					"defaultval": "N",
					"orderby": 2,
					"other": "N",
					"txt": "女",
					"weight": 0
				}
			}
        }
    },
    minLines: 1,
    maxLines: 4,
    _getHtml: function (data, previewmode) {
        var c = "", children = data.children,len = children.length;
        if (previewmode) {

            //title
			for(var i=1;i<=data.maxLines;i++){
				c += '<div class="main_inner_content_title">';
				//c += '<span class="reg_title_star">' + (data.isRequire == "Y" ?"*":"") + '</span>';
				c += '<span class="reg_title_star">*</span>';
				c += '<span class="reg_title_grey_17px">' + MsgSet["REFFER"] + ' ' +i+ ' :' + data.title + '</span>';
				//c += '<span class="reg_title_grey_17px">' + data.title + '_'+i+'：</span>';
				c += '</div>';
				c += '<div class="main_inner_content_top"></div>';
				c += '<div class="main_inner_content">';
				console.log(data.children);
				var works = this._getHtmlOne(data,i);
				c += works;

				c += '</div>';
				//footer
				c += '<div class="main_inner_content_foot"></div>';
			}
        } else {
            c = '<div class="question-answer">' + (data.itemMs ? '<div class="edu_exper_itemMs" style="background-color:#d8d8d8;padding:2px 5px;margin-bottom:10px;">'+ data.itemMs +'</div>' : "");
            c += '<div class="DHContainer" style="border:1px solid #ddd;padding:10px 20px;">'

			//姓名
			c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_name_1'+'" '+(data.children.recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_name'+'">'+data.children.recommend_1["itemName"]+'：</span>';
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
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_phone_1'+'" '+(data.children.recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_phone'+'">'+data.children.recommend_4["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
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
		var e="";
		//推荐信数目
		e +='<div class="edit_rqjls"><span>容器记录数：</span>';
		e +='<div class="edit_item_warp">';
		e +='<span class="edit_item_label">最小行数：</span>';
		e +='<input type="text" class="medium" style="ime-mode:disabled" onkeyup="SurveyBuild.saveAttr(this,\'minLines\')" value="' + data.minLines + '"/>';
		e +='</div>';
		e +='<div class="edit_item_warp"><span class="edit_item_label">最大行数：</span>';
		e +='<input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'maxLines\')" value="' + data.maxLines + '"/>';
		e +='</div>';
		e +='</div>';

        e += '<div class="edit_jygz"><span>校验规则：</span>';
		//是否必填
        //e += '<div class="edit_item_warp" style="margin-top:5px;">';
        //e += '<input type="checkbox" onchange="SurveyBuild.saveAttr(this,\'isRequire\')"' + (data.isRequire == "Y" ? "checked='checked'" : "") + ' id="is_require"/> 是否必填';
        //e += '<a href="#" data-for-id="help_isRequire" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
        //e += '</div>';

		//是否邮件通知申请人
		e += '<div class="edit_item_warp" style="margin-top:5px;">';
		e += '<input type="checkbox" onchange="SurveyBuild.saveAttr(this,\'toSendE\')"' + (data.toSendE == "Y" ? "checked='checked'" : "") + ' id="is_toSendE"> 邮件通知申请人</input>';
		e += '</div>';
		//收齐推荐信前禁止提交报名表
		e += '<div class="edit_item_warp" style="margin-top:5px;">';
		e += '<input type="checkbox" onchange="SurveyBuild.saveAttr(this,\'toCheck\')"' + (data.toCheck == "Y" ? "checked='checked'" : "") + ' id="is_toCheck"> 收齐推荐信前禁止提交报名表</input>';
		e += '</div>';
		//中文（英文）推荐信设置
		e += '<div class="edit_item_warp">';
        e += '<a href="javascript:void(0);" style="color:#59ff9a;" onclick="SurveyBuild.RulesZHS(this);">中文推荐信设置</a>';
		e += '&nbsp;&nbsp;&nbsp;&nbsp;';
		e += '<a href="javascript:void(0);" style="color:#59ff9a;" onclick="SurveyBuild.RulesENG(this);">英文推荐信设置</a>';
        e += '</div>';
		//推荐信类型
		e += '<div class="edit_item_warp"><span class="edit_item_label">推荐信类型：</span>';
		e += '<select name="bmb_mb_id" id="bmb_mb_id" class="selectCss" onchange="SurveyBuild.saveAttr(this,\'itemLx\')">';
		e += '<option value="">请选择</option><option value="F" '+(data.itemLx=="F"?"selected='selected'":"")+'>发送邮件</option><option value="S" '+(data.itemLx=="S"?"selected='selected'":"")+'>上传附件</option><option value="L" '+(data.itemLx=="L"?"selected='selected'":"")+'>两者都行</option>';
		e += '</select>';
		e += '</div>';
		//字段显示隐藏
		e += '<div class="edit_item_warp" style="margin-top:5px;">';
		var list = "";
		var child = data.children;
		//姓名-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_name">';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_1\')" value="'+child.recommend_1["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
		list += '<td><input type="text" value="rec_name" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
		list += '</tr>';
		//姓名-结束
		
		//单位-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_company">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_2\')" value="'+child.recommend_2["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_company" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
        list += '</tr>';
		//单位-结束
		//职务-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_post">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_3\')" value="'+child.recommend_3["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_post" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
        list += '</tr>';
		//职务-结束
		//手机-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_phone">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_4\')" value="'+child.recommend_4["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_phone" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
        list += '</tr>';
		//手机-结束
		//邮箱-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_email">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_5\')" value="'+child.recommend_5["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_email" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
        list += '</tr>';
		//邮箱-结束
		//性别-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_sex">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_15\')" value="'+child.recommend_15["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_sex" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_15\')" '+(child.recommend_15["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//性别-结束
		//申请人关系-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_relation">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_6\')" value="'+child.recommend_6["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_relation" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_6\')" '+(child.recommend_6["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//申请人关系-结束
		//备用字段一-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by1">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_10\')" value="'+child.recommend_10["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by1" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_10\')" '+(child.recommend_10["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段一-结束
		//备用字段二-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by2">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_11\')" value="'+child.recommend_11["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by2" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_11\')" '+(child.recommend_11["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段二-结束
		//备用字段三-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by3">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_12\')" value="'+child.recommend_12["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by3" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_12\')" '+(child.recommend_12["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段三-结束
		//备用字段四-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by4">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_13\')" value="'+child.recommend_13["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by4" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_13\')" '+(child.recommend_13["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段四-结束
		//备用字段五-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by5">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_14\')" value="'+child.recommend_14["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by5" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_14\')" '+(child.recommend_14["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段五-结束
		//推荐信语言-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_language">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_7\')" value="'+child.recommend_7["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_language" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" checked="checked" value="1"></td>';
        list += '</tr>';
		//推荐信语言-结束
		//推荐信类型-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_way">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_8\')" value="'+child.recommend_8["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_way" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_8\')" '+(child.recommend_8["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//推荐信类型-结束
		//上传附件-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_attach">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_9\')" value="'+child.recommend_9["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_attach" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" disabled="disabled" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_9\')" '+(child.recommend_9["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//上传附件-结束

		e += '<fieldset id="option-box">';
		e += '<span class="edit_item_label">信息项：</span>';
		e += '<table class="table table-bordered data-table">';
		e += '<thead><tr><th width="55">Label Name</th><th width="55">Field Name</th><th width="20">启用</th></tr></thead><tbody class="ui-sortable">' + list + '</tbody>';
		e += '</table>';
		e += '</fieldset>';

		e += '</div>';
		//高级设置
        e += '<div class="edit_item_warp">';
        e += '<a href="javascript:void(0);" style="color:#59ff9a;" onclick="SurveyBuild.RulesSet(this);">高级设置</a>';
        e += '<a href="#" data-for-id="help_advancedSetup" onclick="SurveyBuild.showMsg(this,event)" class="big-link" data-reveal-id="myModal" data-animation="fade">(?)</a>';
        e += '</div>';

        e += '</div>';

        return e;
    },
    _getHtmlOne: function(data,rownum){
		if (SurveyBuild.appInsId=="0"&&data.children.length<data.maxLines){
			SurveyBuild.showTjx(this,data.instanceId);
		}else if (data.children.length<data.maxLines){
			SurveyBuild.showTjx(this,data.instanceId);
		}
        var works = "",child = [];
		child = data.children;
		var i=rownum-1;
		var tz_app_ins_id=SurveyBuild.appInsId;
		var _class_id=SurveyBuild.classId;
		var _tjx_zt="";
		var _qy_zhs="";
		var _qy_eng="";
		var params = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"EJSON","comParams":{"APP_INS_ID":"'+tz_app_ins_id+'","rownum":"'+rownum+'","class_id":"'+_class_id+'"}}';
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
				}
			}
		});
		if (child[i].recommend_8["value"]=="U")
		{
			_tjx_zt="未发送";
		}
		var _zd="";
		if (_tjx_zt=="已完成"){
			_tjx_zt=MsgSet["Completed"];
			_zd="Z";
		}
		if (_tjx_zt=="已发送"){
			_tjx_zt=MsgSet["SendEmail"];
			_zd="Y";
		}
		if (_tjx_zt=="未发送"){
			_tjx_zt=MsgSet["Unsent"];
		}
		works += '<div id="main_inner_content_para'+i+'" class="main_inner_content_para" style="display: inherit;" >';
		//姓名-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_1["itemName"] +'：</div>';
		works += '<div class="main_inner_content_info_right">';
		works += '<input type="text" title="'+ child[i].recommend_1["itemName"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_1["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_1["itemId"] +'" value="' + child[i].recommend_1["value"] + '">';
		works += '<div style="margin-top: -40px; margin-left: 265px">';
		works += '	<div id="' + data.itemId + child[i].recommend_1["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '		<div class="onShow">&nbsp;</div>';
		works += '	</div>';
		works += '</div>';
		works += '</div>';
		works += '</div>';
		//姓名-结束
		
		//单位-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_2["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_2["itemName"] +'：</div>';
		works += '	<div class="main_inner_content_info_right">';
		works += '		<input type="text" title="'+ child[i].recommend_2["itemName"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_2["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_2["itemId"] +'" value="' + child[i].recommend_2["value"] + '">';
		works += '		<div style="margin-top: -40px; margin-left: 265px">';
		works += '			<div id="' + data.itemId + child[i].recommend_2["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '				<div class="onShow">&nbsp;</div>';
		works += '			</div>';
		works += '		</div>';
		works += '	</div>';
		works += '</div>';
		//单位-结束

		//职务-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_3["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_3["itemName"] +'：</div>';
		works += '	<div class="main_inner_content_info_right">';
		works += '		<input type="text" title="'+ child[i].recommend_3["itemName"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_3["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_3["itemId"] +'" value="' + child[i].recommend_3["value"] + '">';
		works += '		<div style="margin-top: -40px; margin-left: 265px">';
		works += '			<div id="' + data.itemId + child[i].recommend_3["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '				<div class="onShow">&nbsp;</div>';
		works += '			</div>';
		works += '		</div>';
		works += '	</div>';
		works += '</div>';
		//职务-结束

		//手机-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_4["itemName"] +'：</div>';
		works += '	<div class="main_inner_content_info_right">';
		works += '		<input type="text" title="'+ child[i].recommend_4["itemName"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_4["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_4["itemId"] +'" value="' + child[i].recommend_4["value"] + '">';
		works += '		<div style="margin-top: -40px; margin-left: 265px">';
		works += '			<div id="' + data.itemId + child[i].recommend_4["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '				<div class="onShow">&nbsp;</div>';
		works += '			</div>';
		works += '		</div>';
		works += '	</div>';
		works += '</div>';
		//手机-结束

		//邮箱-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_5["itemName"] +'：</div>';
		works += '	<div class="main_inner_content_info_right">';
		works += '		<input type="text" title="'+ child[i].recommend_5["itemName"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_5["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_5["itemId"] +'" value="' + child[i].recommend_5["value"] + '">';
		works += '		<div style="margin-top: -40px; margin-left: 265px">';
		works += '			<div id="' + data.itemId + child[i].recommend_5["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '				<div class="onShow">&nbsp;</div>';
		works += '			</div>';
		works += '		</div>';
		works += '	</div>';
		works += '</div>';
		//邮箱-结束
		
		//性别-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_15["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_15["itemName"] +'：</div>';
		works += '<div class="main_inner_content_info_right">';
		if (child[i].recommend_15["value"]=="F"){
			works += '<input type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_15["itemName"] +'" id="' + data.itemId + child[i].recommend_15["itemId"] + '_M" class="input_251px" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="M"> '+MsgSet["SEX_M"]+'&nbsp;&nbsp;&nbsp;&nbsp;';
			works += '<input type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_15["itemName"] +'" id="' + data.itemId + child[i].recommend_15["itemId"] + '_F" class="input_251px" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="F"> '+MsgSet["SEX_F"];
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_15["itemId"] + '" value="F">';
		}else{
			works += '<input type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_15["itemName"] +'" id="' + data.itemId + child[i].recommend_15["itemId"] + '_M" class="input_251px" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="M"> '+MsgSet["SEX_M"]+'&nbsp;&nbsp;&nbsp;&nbsp;';
			works += '<input type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_15["itemName"] +'" id="' + data.itemId + child[i].recommend_15["itemId"] + '_F" class="input_251px" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="F"> '+MsgSet["SEX_F"];
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_15["itemId"] + '" value="M">';
		}
		works += '<div style="margin-top: -40px; margin-left: 110px"><span id="' + data.itemId + child[i].recommend_15["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow"><div class="onShow"></div></span></div>';
		works += '</div>';
		works += '</div>';
		//性别-结束

		//申请人关系-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_6["itemName"] +'：</div>';
		works += '	<div class="main_inner_content_info_right">';
		works += '		<input type="text" title="'+ child[i].recommend_6["itemName"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_6["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_6["itemId"] +'" value="' + child[i].recommend_6["value"] + '">';
		works += '		<div style="margin-top: -40px; margin-left: 265px">';
		works += '			<div id="' + data.itemId + child[i].recommend_6["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '				<div class="onShow">&nbsp;</div>';
		works += '			</div>';
		works += '		</div>';
		works += '	</div>';
		works += '</div>';
		//申请人关系-结束

		//备用字段一-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_10["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_10["itemName"] +'：</div>';
		works += '	<div class="main_inner_content_info_right">';
		works += '		<input type="text" title="'+ child[i].recommend_10["itemName"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_10["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_10["itemId"] +'" value="' + child[i].recommend_10["value"] + '">';
		works += '		<div style="margin-top: -40px; margin-left: 265px">';
		works += '			<div id="' + data.itemId + child[i].recommend_10["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '				<div class="onShow">&nbsp;</div>';
		works += '			</div>';
		works += '		</div>';
		works += '	</div>';
		works += '</div>';
		//备用字段一-结束

		//备用字段二-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_11["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_11["itemName"] +'：</div>';
		works += '	<div class="main_inner_content_info_right">';
		works += '		<input type="text" title="'+ child[i].recommend_11["itemName"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_11["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_11["itemId"] +'" value="' + child[i].recommend_11["value"] + '">';
		works += '		<div style="margin-top: -40px; margin-left: 265px">';
		works += '			<div id="' + data.itemId + child[i].recommend_11["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '				<div class="onShow">&nbsp;</div>';
		works += '			</div>';
		works += '		</div>';
		works += '	</div>';
		works += '</div>';
		//备用字段二-结束

		//备用字段三-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_12["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_12["itemName"] +'：</div>';
		works += '	<div class="main_inner_content_info_right">';
		works += '		<input type="text" title="'+ child[i].recommend_12["itemName"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_12["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_12["itemId"] +'" value="' + child[i].recommend_12["value"] + '">';
		works += '		<div style="margin-top: -40px; margin-left: 265px">';
		works += '			<div id="' + data.itemId + child[i].recommend_12["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '				<div class="onShow">&nbsp;</div>';
		works += '			</div>';
		works += '		</div>';
		works += '	</div>';
		works += '</div>';
		//备用字段三-结束

		//备用字段四-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_13["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_13["itemName"] +'：</div>';
		works += '	<div class="main_inner_content_info_right">';
		works += '		<input type="text" title="'+ child[i].recommend_13["itemName"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_13["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_13["itemId"] +'" value="' + child[i].recommend_13["value"] + '">';
		works += '		<div style="margin-top: -40px; margin-left: 265px">';
		works += '			<div id="' + data.itemId + child[i].recommend_13["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '				<div class="onShow">&nbsp;</div>';
		works += '			</div>';
		works += '		</div>';
		works += '	</div>';
		works += '</div>';
		//备用字段四-结束

		//备用字段五-开始
		works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_14["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_14["itemName"] +'：</div>';
		works += '	<div class="main_inner_content_info_right">';
		works += '		<input type="text" title="'+ child[i].recommend_14["itemName"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_14["itemId"] +'" class="input_251px" name="' + data.itemId + child[i].recommend_14["itemId"] +'" value="' + child[i].recommend_14["value"] + '">';
		works += '		<div style="margin-top: -40px; margin-left: 265px">';
		works += '			<div id="' + data.itemId + child[i].recommend_14["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '				<div class="onShow">&nbsp;</div>';
		works += '			</div>';
		works += '		</div>';
		works += '	</div>';
		works += '</div>';
		//备用字段五结束

		//推荐信语言-开始
		if (_qy_zhs=="Y"&&_qy_eng=="Y")
		{
			works += '<div class="main_inner_content_info_autoheight">';
			works += '<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_7["itemName"] +'：</div>';
			works += '<div class="main_inner_content_info_right">';
			if (child[i].recommend_7["value"]=="E"){
				works += '<input type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_7["itemName"] +'" id="' + data.itemId + child[i].recommend_7["itemId"] + '_C" class="input_411px" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="C"> '+MsgSet["LANGUAGE_C"]+'&nbsp;&nbsp;&nbsp;&nbsp;';
				works += '<input type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_7["itemName"] +'" id="' + data.itemId + child[i].recommend_7["itemId"] + '_E" class="input_411px" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="E"> '+MsgSet["LANGUAGE_E"];
			}else{
				works += '<input type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_7["itemName"] +'" id="' + data.itemId + child[i].recommend_7["itemId"] + '_C" class="input_411px" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="C"> '+MsgSet["LANGUAGE_C"]+'&nbsp;&nbsp;&nbsp;&nbsp;';
				works += '<input type="radio" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_7["itemName"] +'" id="' + data.itemId + child[i].recommend_7["itemId"] + '_E" class="input_411px" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="E"> '+MsgSet["LANGUAGE_E"];
			}
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="' + child[i].recommend_7["value"] + '">';
			works += '<div style="margin-top: -40px; margin-left: 110px"><span id="' + data.itemId + child[i].recommend_7["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow"><div class="onShow"></div></span></div>';
			works += '</div>';
			works += '</div>';
		}else if (_qy_zhs=="Y"&&_qy_eng!="Y")
		{
			works += '<div class="main_inner_content_info_autoheight">';
			works += '<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_7["itemName"] +'：</div>';
			works += '<div class="main_inner_content_info_right">';
			works += '<div>'+MsgSet["LANGUAGE_C"]+'</div>';
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="C">';
			works += '</div>';
			works += '</div>';
		}else if (_qy_zhs!="Y"&&_qy_eng=="Y")
		{
			works += '<div class="main_inner_content_info_autoheight">';
			works += '<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_7["itemName"] +'：</div>';
			works += '<div class="main_inner_content_info_right">';
			works += '<div>'+MsgSet["LANGUAGE_E"]+'</div>';
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="E">';
			works += '</div>';
			works += '</div>';
		}
		//推荐信语言-结束

		//推荐信类型-开始
		if (data.itemLx=="L")
		{
			works += '<div class="main_inner_content_info_autoheight">';
			works += '<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+ child[i].recommend_8["itemName"] +'：</div>';
			works += '<div class="main_inner_content_info_right">';
			if (child[i].recommend_8["value"]=="U"){
				works += '<input type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["itemName"] +'" id="' + data.itemId + child[i].recommend_8["itemId"] + '_S" class="input_411px" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="S">'+MsgSet["Send_mail"]+'&nbsp;';
				works += '<input type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_8["itemName"] +'" id="' + data.itemId + child[i].recommend_8["itemId"] + '_U" class="input_411px" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="U">'+MsgSet["Upload"];
			}else{
				works += '<input type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' checked="checked" title="'+ child[i].recommend_8["itemName"] +'" id="' + data.itemId + child[i].recommend_8["itemId"] + '_S" class="input_411px" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="S">'+MsgSet["Send_mail"]+'&nbsp;';
				works += '<input type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["itemName"] +'" id="' + data.itemId + child[i].recommend_8["itemId"] + '_U" class="input_411px" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="U">'+MsgSet["Upload"];
			}
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_8["itemId"] + '" value="' + child[i].recommend_8["value"] + '">';
			works += '<div style="margin-top: -40px; margin-left: 110px"><span id="' + data.itemId + child[i].recommend_8["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow"><div class="onShow"></div></span></div>';
			works += '</div>';
			works += '</div>';
		}
		//推荐信类型-结束
		
		//上传附件
		if ((data.itemLx=="L"&&child[i].recommend_8["value"]=="U")||data.itemLx=="S")
		{
			works += '<div class="main_inner_content_info_autoheight" id="Tjxfj_show_'+i+'">';
			works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+child[i].recommend_9["itemName"]+'：</div>';
			works += '	<div class="main_inner_content_info_right">';
			works += '		<div class="file_upload_button">';
			works += '			<div class="bt_blue">'+MsgSet["UPLOAD_BTN_MSG"]+'</div>';
			works += '			<input id="'+data.itemId+child[i].recommend_9["itemId"]+'File" class="fileupload_input" type="file" name="'+data.itemId+child[i].recommend_9["itemId"]+'File" style="width:80px;" onchange=SurveyBuild.TjxUpload(this,"recommend_9",'+i+')>';
			works += '			<div style="margin-top:-35px;margin-left:280px">';
			works += '				<span id="'+data.itemId+child[i].recommend_9["itemId"]+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;">';
			works += '					<div class="onCorrect">&nbsp;</div>';
			works += '				</span>';
			works += '			</div>';
			works += '		</div>';
			works += '	</div>';
			works += '	<div id="'+data.itemId+i+'_AttList" class="main_inner_file_list">';
			if (child[i].recommend_9["filename"]!="")
			{
				works += '		<ul><li>';
				works += '			<a class="main_inner_filelist_a" onclick=SurveyBuild.TjxdownLoad(this,"recommend_9",'+i+') file-index="'+i+'">'+child[i].recommend_9["filename"]+'</a>';
				works += '			<div class="main_inner_file_del" onclick=SurveyBuild.Tjxdelete(this,"recommend_9",'+i+')><img width="15" height="15" src="/onlineReg/images/del.png" title="'+MsgSet["DEL"]+'">'+MsgSet["DEL"]+'</div>';
				works += '		</li></ul>';
			}
			works += '	</div>';
			works += '	<input id="'+data.itemId+child[i].recommend_9["itemId"]+'" type="hidden" name="'+data.itemId+child[i].recommend_9["itemId"]+'" value="'+child[i].recommend_9["value"]+'">';
			works += '</div>';
		}else{
			works += '<div class="main_inner_content_info_autoheight" style="display:none" id="Tjxfj_show_'+i+'">';
			works += '	<div class="main_inner_connent_info_left"><span class="reg_title_star">*</span>'+child[i].recommend_9["itemName"]+'：</div>';
			works += '	<div class="main_inner_content_info_right">';
			works += '		<div class="file_upload_button">';
			works += '			<div class="bt_blue">'+MsgSet["UPLOAD_BTN_MSG"]+'</div>';
			works += '			<input id="'+data.itemId+child[i].recommend_9["itemId"]+'File" class="fileupload_input" type="file" name="'+data.itemId+child[i].recommend_9["itemId"]+'File" style="width:80px;" onchange=SurveyBuild.TjxUpload(this,"recommend_9",'+i+')>';
			works += '			<div style="margin-top:-35px;margin-left:280px">';
			works += '				<span id="'+data.itemId+child[i].recommend_9["itemId"]+'Tip" class="onShow" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;">';
			works += '					<div class="onCorrect">&nbsp;</div>';
			works += '				</span>';
			works += '			</div>';
			works += '		</div>';
			works += '	</div>';
			works += '	<div id="'+data.itemId+i+'_AttList" class="main_inner_file_list">';
			//works += '		<ul><li>';
			//works += '			<a class="main_inner_filelist_a" onclick=SurveyBuild.downLoadFile(this,\"'+data.instanceId+'\") file-index="1">'+child[i].recommend_9["filename"]+'</a>';
			//works += '			<div class="main_inner_file_del" onclick=SurveyBuild.deleteFile(this,\"'+data.instanceId+'\")><img width="15" height="15" src="/onlineReg/images/del.png" title="'+MsgSet["DEL"]+'">'+MsgSet["DEL"]+'</div>';
			//works += '		</li></ul>';
			works += '	</div>';
			works += '	<input id="'+data.itemId+child[i].recommend_9["itemId"]+'" type="hidden" name="'+data.itemId+child[i].recommend_9["itemId"]+'" value="'+child[i].recommend_9["value"]+'">';
			works += '</div>';
		}
		//上传附件-结束
		if ((data.itemLx=="L"&&child[i].recommend_8["value"]!="U")||data.itemLx=="F"){
			works += '<div class="main_inner_content_info" style="'+((_zd=="Z")?"display:none":"")+'">';
			//发送邮件
			works += '<div id="sendEmailS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;'+((_zd=="Y"||_zd=="Z")?"display:none":"")+'">';
			works += '<div id="sendEmail_'+i+'" class="bt_blue" style="width:125px">'+MsgSet["Send"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			//重新发送
			works += '<div id="reSendEmailS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;'+((_zd!="Y")?"display:none":"")+'">';
			works += '<div id="reSendEmail_'+i+'" class="bt_blue" style="width:125px">'+MsgSet["Resend"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			//更换推荐人
			works += '<div id="changeRecS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;'+((_zd!="Y")?"display:none":"")+'">';
			works += '<div id="changeRec_'+i+'" class="bt_blue" style="width:125px">'+MsgSet["RepRecom"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			works += '</div>';
			//推荐信状态
			works += '<div class="main_inner_content_info" id="Tjxzt_'+i+'">';
			works += '<div id="tjxzt_desc_'+i+'">'+MsgSet["ReLeSt"]+': <span class="font_orange_16px">'+_tjx_zt+'</span></div>';
			works += '</div>';
		}else{
			works += '<div class="main_inner_content_info" >';
			//发送邮件
			works += '<div id="sendEmailS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;display:none;">';
			works += '<div id="sendEmail_'+i+'" class="bt_blue" style="width:125px">'+MsgSet["Send"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			//重新发送
			works += '<div id="reSendEmailS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;'+((_zd!="Y"&&_zd!="Z")?"display:none":"")+'">';
			works += '<div id="reSendEmail_'+i+'" class="bt_blue" style="width:125px">'+MsgSet["Resend"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			//更换推荐人
			works += '<div id="changeRecS_'+i+'" style="padding-left:20px;padding-right:5px;float:left;'+((_zd!="Y"&&_zd!="Z")?"display:none":"")+'">';
			works += '<div id="changeRec_'+i+'" class="bt_blue" style="width:125px">'+MsgSet["RepRecom"]+'</div><a href="#" class="alpha"></a>';
			works += '</div>';
			works += '</div>';
			//推荐信状态
			works += '<div class="main_inner_content_info" id="Tjxzt_'+i+'" style="display:none">';
			works += '<div id="tjxzt_desc_'+i+'">'+MsgSet["ReLeSt"]+': <span class="font_orange_16px">'+_tjx_zt+'</span></div>';
			works += '</div>';
		}
		//保存推荐信信息
		works += '<div style="display:none">';
		works += '<div id="saveRec_'+i+'" class="bt_blue" style="width:125px">保存</div><a href="#" class="alpha"></a>';
		works += '</div>';

		works += '<div><input type="hidden" id="yincang_tx" value="'+data.toSendE+'"></div>';

		works += '<div><input type="hidden" id="max_tjx_ts" value="'+data.maxLines+'"></div>';
		works += '</div>';

        return works;
    },
    _eventbind: function(data) {
        var children = data.children,
        len = children.length;
		for (var i=1;i<=data.maxLines;i++)
		{
			var num=i;
			//发送邮件
			$("#sendEmail_"+(Number(num)-1)).click(function(){
				var m = this.id.split("_")[1];
				m=Number(m)+1;
				var _yz="";
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
				var rec_phone = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
				if (children[m-1].recommend_4["useby"]=="Y"&&rec_phone==""){
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
				var rec_relation = $("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).val();
				if (children[m-1].recommend_6["useby"]=="Y"&&rec_relation==""){
					_yz="1";
				}
				var rec_language = $("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).val();
				//var rec_language = $("input[name="+data["itemId"]+children[0].recommend_7["itemId"]+"]:checked").val();
				if (children[m-1].recommend_7["useby"]=="Y"&&rec_language==""){
					_yz="1";
				}
				var rec_num= m;
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
				if (_yz==""){
					$("#app_save").click();
					var std=/^([\w\-\.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\w\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$/;
					if (!std.test(rec_email))
					{
						alert(MsgSet["REC_ERR"]);
					}else{
						var _tz_app_ins_id=SurveyBuild.appInsId;
						var _email_tx = $("#yincang_tx").val();
						var _Url = SurveyBuild.tzGeneralURL;
						$.ajax({
							type: "post",
							url: _Url+"?tzParams={'ComID':'TZ_GD_TJX_COM','PageID':'TZ_SEND_REF_STD','OperateType':'SEND','comParams':{'rec_app_ins_id':'"+_tz_app_ins_id+"','rec_num':'"+rec_num+"','rec_name':'"+rec_name+"','rec_company':'"+rec_company+"','rec_post':'"+rec_post+"','rec_phone':'"+rec_phone+"','rec_email':'"+rec_email+"','rec_sex':'"+rec_sex+"','rec_relation':'"+rec_relation+"','rec_language':'"+rec_language+"','email_tx':'"+_email_tx+"','rec_by1':'"+rec_by1+"','rec_by2':'"+rec_by2+"','rec_by3':'"+rec_by3+"','rec_by4':'"+rec_by4+"','rec_by5':'"+rec_by5+"'}}",
							dataType: "json",
							success: function(result){
								if (result.comContent=="SUCCESS"){
									$("#sendEmailS_"+(Number(m)-1)).css("display","none");
									$("#reSendEmailS_"+(Number(m)-1)).css("display","block");
									$("#changeRecS_"+(Number(m)-1)).css("display","block");
									alert(MsgSet["SEND_SC"]);
									$("#tjxzt_desc_"+(Number(rec_num)-1)).html(MsgSet["RepRecom"]+"：<span class='font_orange_16px'>"+MsgSet["SendEmail"]+"</span>");
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
								}else {
									alert(result.comContent);
								}
							}
						});
					}
				}else{
					alert(MsgSet["REC_ERR"]);
					$("#app_save").click();
					//alert(MsgSet["TJX_NO_COMPLETE"]);
				}
			});
			//重发邮件
			$("#reSendEmail_"+(Number(num)-1)).click(function(){
				var m = this.id.split("_")[1];
				m=Number(m)+1;
				var rec_name = $("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).val();
				var rec_company = $("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).val();
				var rec_post = $("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).val();
				var rec_phone = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
				var rec_email = $("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).val();
				//var rec_sex = $("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]:checked").val();
				var rec_sex = $("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]:checked").val();
				if (children[m-1].recommend_15["useby"]=="Y"&&rec_sex==""){
					_yz="1";
				}else{
					rec_sex="";
				}
				var rec_relation = $("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).val();
				//var rec_language = $("input[name="+data["itemId"]+children[m-1].recommend_7["itemId"]+"]:checked").val();
				var rec_language = $("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).val();
				var rec_num= m;
				var rec_by1 = $("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).val();
				var rec_by2 = $("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).val();
				var rec_by3 = $("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).val();
				var rec_by4 = $("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).val();
				var rec_by5 = $("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).val();
				var _tz_app_ins_id=SurveyBuild.appInsId;
				var _Url = SurveyBuild.tzGeneralURL;
				var _email_tx = $("#yincang_tx").val();
				$.ajax({
					type: "post",
					url: _Url+"?tzParams={'ComID':'TZ_GD_TJX_COM','PageID':'TZ_SEND_REF_STD','OperateType':'SEND','comParams':{'rec_app_ins_id':'"+_tz_app_ins_id+"','rec_num':'"+rec_num+"','rec_name':'"+rec_name+"','rec_company':'"+rec_company+"','rec_post':'"+rec_post+"','rec_phone':'"+rec_phone+"','rec_email':'"+rec_email+"','rec_sex':'"+rec_sex+"','rec_relation':'"+rec_relation+"','rec_language':'"+rec_language+"','email_tx':'"+_email_tx+"','rec_by1':'"+rec_by1+"','rec_by2':'"+rec_by2+"','rec_by3':'"+rec_by3+"','rec_by4':'"+rec_by4+"','rec_by5':'"+rec_by5+"'}}",
					dataType: "json",
					success: function(result){
						if (result.comContent=="SUCCESS"){
							alert(MsgSet["RESEND_SC"]);
						}else {
							alert(result.comContent);
						}
					}
				});
			});
			//保存推荐信信息
			$("#saveRec_"+(Number(num)-1)).click(function(){
				var m = this.id.split("_")[1];
				m=Number(m)+1;

				var _yz="";
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
				var rec_phone = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
				if (children[m-1].recommend_4["useby"]=="Y"&&rec_phone==""){
					_yz="1";
				}
				var rec_email = $("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).val();
				if (children[m-1].recommend_5["useby"]=="Y"&&rec_email==""){
					_yz="1";
				}
				var rec_sex = $("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]:checked").val();
				if (children[m-1].recommend_15["useby"]=="Y"&&rec_sex==""){
					_yz="1";
				}else{
					rec_sex="";
				}
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
				var rec_num= m;
				var rec_type = $("#" + data["itemId"] + children[m-1].recommend_8["itemId"]).val();
				var _file=children[m-1].recommend_9["filename"];
				var _sysfile=children[m-1].recommend_9["sysFileName"];
				if (rec_type=="U"&&_yz=="")
				{
					var _tz_app_ins_id=SurveyBuild.appInsId;
					var _Url = SurveyBuild.tzGeneralURL;
					var _email_tx = $("#yincang_tx").val();
					//$("#"+data.itemId+child[cins].itemId+"Attch").text(obj.msg.fileName);
					$.ajax({
						type: "post",
						url: _Url+"?tzParams={'ComID':'TZ_GD_TJX_COM','PageID':'TZ_SEND_REF_STD','OperateType':'SAVE','comParams':{'rec_app_ins_id':'"+_tz_app_ins_id+"','rec_num':'"+rec_num+"','rec_name':'"+rec_name+"','rec_company':'"+rec_company+"','rec_post':'"+rec_post+"','rec_phone':'"+rec_phone+"','rec_email':'"+rec_email+"','rec_sex':'"+rec_sex+"','rec_relation':'"+rec_relation+"','rec_language':'"+rec_language+"','email_tx':'"+_email_tx+"','rec_by1':'"+rec_by1+"','rec_by2':'"+rec_by2+"','rec_by3':'"+rec_by3+"','rec_by4':'"+rec_by4+"','rec_by5':'"+rec_by5+"','filename':'"+_file+"','sysfilename':'"+_sysfile+"'}}",
						dataType: "json",
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
			$("#changeRec_"+(Number(num)-1)).click(function(){
				var m = this.id.split("_")[1];
				m=Number(m)+1;
				var rec_name = $("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).val();
				var rec_company = $("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).val();
				var rec_post = $("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).val();
				var rec_phone = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
				var rec_email = $("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).val();
				var rec_sex = $("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]:checked").val();
				var rec_relation = $("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).val();
				//var rec_language = $("input[name="+data["itemId"]+children[m-1].recommend_7["itemId"]+"]:checked").val();
				var rec_language = $("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).val();
				var rec_num= m;
				var rec_by1 = $("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).val();
				var rec_by2 = $("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).val();
				var rec_by3 = $("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).val();
				var rec_by4 = $("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).val();
				var rec_by5 = $("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).val();
				var _tz_app_ins_id=SurveyBuild.appInsId;
				var _Url = SurveyBuild.tzGeneralURL;
				var _email_tx = $("#yincang_tx").val();
				$.ajax({
					type: "post",
					url: _Url+"?tzParams={'ComID':'TZ_GD_TJX_COM','PageID':'TZ_SEND_REF_STD','OperateType':'CHANGE','comParams':{'rec_app_ins_id':'"+_tz_app_ins_id+"','rec_num':'"+rec_num+"','rec_name':'"+rec_name+"','rec_company':'"+rec_company+"','rec_post':'"+rec_post+"','rec_phone':'"+rec_phone+"','rec_email':'"+rec_email+"','rec_sex':'"+rec_sex+"','rec_relation':'"+rec_relation+"','rec_language':'"+rec_language+"','email_tx':'"+_email_tx+"','rec_by1':'"+rec_by1+"','rec_by2':'"+rec_by2+"','rec_by3':'"+rec_by3+"','rec_by4':'"+rec_by4+"','rec_by5':'"+rec_by5+"'}}",
					dataType: "json",
					success: function(result){
						if (result.comContent=="SUCCESS"){
							//alert(MsgSet["REP_SC"]);
							$("#sendEmailS_"+(Number(m)-1)).css("display","block");
							$("#reSendEmailS_"+(Number(m)-1)).css("display","none");
							$("#changeRecS_"+(Number(m)-1)).css("display","none");
							$("#tjxzt_desc_"+(Number(rec_num)-1)).html(MsgSet["RepRecom"]+"：<span class='font_orange_16px'>"+MsgSet["Unsent"]+"</span>");
							//$("#tjxzt_desc_"+(Number(rec_num)-1)).html("推荐信状态：未发送");
							$("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).removeAttr("readonly");
							$("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).removeAttr("readonly");
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
						}else {
							alert(result.comContent);
						}
					}
				});
			});
			$("#"+data.itemId + children[num-1].recommend_15["itemId"]+"_M").click(function(){
				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
			});
			$("#"+data.itemId + children[num-1].recommend_15["itemId"]+"_F").click(function(){
				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
			});
			$("#"+data.itemId + children[num-1].recommend_8["itemId"]+"_U").click(function(){
				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
				var n = this.align;
				$("#sendEmailS_"+n).css("display","none");
				$("#Tjxzt_"+n).css("display","none");
				$("#Tjxfj_show_"+n).css("display","block");
			});
			$("#"+data.itemId + children[num-1].recommend_8["itemId"]+"_S").click(function(){
				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
				var n = this.align;
				var instanceId = $(this).closest(".dhcontainer").attr("data-instancid");
				var data = SurveyBuild._items[instanceId];
				var child = data.children[n];
				var itemId = data.itemId;
				child["recommend_9"].filename = "";
				child["recommend_9"].sysFileName = "";
				child["recommend_9"].orderby = "";
				$("#"+data.itemId+n+"_AttList").html("");
				
				//SurveyBuild.Tjxdelete(this,\"'+cins+'\",'+index+')
				$("#sendEmailS_"+n).css("display","block");
				$("#Tjxzt_"+n).css("display","block");
				$("#Tjxfj_show_"+n).css("display","none");
			});
			$("#"+data.itemId + children[num-1].recommend_7["itemId"]+"_C").click(function(){
				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
			});
			$("#"+data.itemId + children[num-1].recommend_7["itemId"]+"_E").click(function(){
				var m = this.name;
				var value=this.value;
				$("#"+m).val(value);
			});
		}

		for (var i=1;i<=data.children.length;i++)
		{
			var $tjr_name = $("#" + data.itemId + children[i - 1].recommend_1["itemId"]);
			var $tjr_company = $("#" + data.itemId + children[i - 1].recommend_2["itemId"]);
			var $tjr_post = $("#" + data.itemId + children[i - 1].recommend_3["itemId"]);
			var $tjr_phone = $("#" + data.itemId + children[i - 1].recommend_4["itemId"]);
			var $tjr_email = $("#" + data.itemId + children[i - 1].recommend_5["itemId"]);
			var $tjr_relation = $("#" + data.itemId + children[i - 1].recommend_6["itemId"]);
			var $tjr_by1 = $("#" + data.itemId + children[i - 1].recommend_10["itemId"]);
			var $tjr_by2 = $("#" + data.itemId + children[i - 1].recommend_11["itemId"]);
			var $tjr_by3 = $("#" + data.itemId + children[i - 1].recommend_12["itemId"]);
			var $tjr_by4 = $("#" + data.itemId + children[i - 1].recommend_13["itemId"]);
			var $tjr_by5 = $("#" + data.itemId + children[i - 1].recommend_14["itemId"]);
			//姓名
			$tjr_name.formValidator({tipID:data["itemId"] + children[i - 1].recommend_1["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_name.functionValidator({
				fun:function(val,elem){
					var _name=val;
					//公司
					var _company_id=elem.id.replace("r_name","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_name","r_post");
					var _post=$("#"+_post_id).val();
					//手机
					var _phone_id=elem.id.replace("r_name","r_phone");
					var _phone=$("#"+_phone_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_name","r_email");
					var _email=$("#"+_email_id).val();
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
					if ((_company!=""||_post!=""||_phone!=""||_email!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_name=="")
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
					//姓名
					var _name_id=elem.id.replace("r_company","r_name");
					var _name=$("#"+_name_id).val();
					//职务
					var _post_id=elem.id.replace("r_company","r_post");
					var _post=$("#"+_post_id).val();
					//手机
					var _phone_id=elem.id.replace("r_company","r_phone");
					var _phone=$("#"+_phone_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_company","r_email");
					var _email=$("#"+_email_id).val();
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
					if ((_name!=""||_post!=""||_phone!=""||_email!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_company=="")
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
					//姓名
					var _name_id=elem.id.replace("r_post","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_post","r_company");
					var _company=$("#"+_company_id).val();
					//手机
					var _phone_id=elem.id.replace("r_post","r_phone");
					var _phone=$("#"+_phone_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_post","r_email");
					var _email=$("#"+_email_id).val();
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
					if ((_name!=""||_company!=""||_phone!=""||_email!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_post=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			//手机
			$tjr_phone.formValidator({tipID:data["itemId"] + children[i - 1].recommend_4["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_phone.functionValidator({
				fun:function(val,elem){
					var _phone=val;
					//姓名
					var _name_id=elem.id.replace("r_phone","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_phone","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_phone","r_post");
					var _post=$("#"+_post_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_phone","r_email");
					var _email=$("#"+_email_id).val();
					//关系
					var _relation_id=elem.id.replace("r_phone","r_relation");
					var _relation=$("#"+_relation_id).val();
					//备用字段一
					var _by1_id=elem.id.replace("r_phone","r_by1");
					var _by1=$("#"+_by1_id).val();
					//备用字段二
					var _by2_id=elem.id.replace("r_phone","r_by2");
					var _by2=$("#"+_by2_id).val();
					//备用字段三
					var _by3_id=elem.id.replace("r_phone","r_by3");
					var _by3=$("#"+_by3_id).val();
					//备用字段四
					var _by4_id=elem.id.replace("r_phone","r_by4");
					var _by4=$("#"+_by4_id).val();
					//备用字段五
					var _by5_id=elem.id.replace("r_phone","r_by5");
					var _by5=$("#"+_by5_id).val();
					if ((_name!=""||_company!=""||_post!=""||_email!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_phone=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
			//邮箱
			$tjr_email.formValidator({tipID:data["itemId"] + children[i - 1].recommend_5["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_email.functionValidator({
				fun:function(val,elem){
					var _email=val;
					//姓名
					var _name_id=elem.id.replace("r_email","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_email","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_email","r_post");
					var _post=$("#"+_post_id).val();
					//手机
					var _phone_id=elem.id.replace("r_email","r_phone");
					var _phone=$("#"+_phone_id).val();
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
					if ((_name!=""||_company!=""||_post!=""||_phone!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_email=="")
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
			//$tjr_email.regexValidator({regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onError:(children[0].recommend_5["itemName"]+MsgSet["FORMATERROR"])});
			//与申请人关系
			$tjr_relation.formValidator({tipID:data["itemId"] + children[i - 1].recommend_6["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_relation.functionValidator({
				fun:function(val,elem){
					var _relation=val;
					//姓名
					var _name_id=elem.id.replace("r_relation","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_relation","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_relation","r_post");
					var _post=$("#"+_post_id).val();
					//手机
					var _phone_id=elem.id.replace("r_relation","r_phone");
					var _phone=$("#"+_phone_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_relation","r_email");
					var _email=$("#"+_email_id).val();
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
					if ((_name!=""||_company!=""||_post!=""||_phone!=""||_email!=""||_by1!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_relation=="")
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
					//姓名
					var _name_id=elem.id.replace("r_by1","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_by1","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_by1","r_post");
					var _post=$("#"+_post_id).val();
					//手机
					var _phone_id=elem.id.replace("r_by1","r_phone");
					var _phone=$("#"+_phone_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_by1","r_email");
					var _email=$("#"+_email_id).val();
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
					if ((_name!=""||_company!=""||_post!=""||_phone!=""||_email!=""||_relation!=""||_by2!=""||_by3!=""||_by4!=""||_by5!="")&&_by1=="")
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
					//姓名
					var _name_id=elem.id.replace("r_by2","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_by2","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_by2","r_post");
					var _post=$("#"+_post_id).val();
					//手机
					var _phone_id=elem.id.replace("r_by2","r_phone");
					var _phone=$("#"+_phone_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_by2","r_email");
					var _email=$("#"+_email_id).val();
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
					if ((_name!=""||_company!=""||_post!=""||_phone!=""||_email!=""||_relation!=""||_by1!=""||_by3!=""||_by4!=""||_by5!="")&&_by2=="")
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
					//姓名
					var _name_id=elem.id.replace("r_by3","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_by3","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_by3","r_post");
					var _post=$("#"+_post_id).val();
					//手机
					var _phone_id=elem.id.replace("r_by3","r_phone");
					var _phone=$("#"+_phone_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_by3","r_email");
					var _email=$("#"+_email_id).val();
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
					if ((_name!=""||_company!=""||_post!=""||_phone!=""||_email!=""||_relation!=""||_by1!=""||_by2!=""||_by4!=""||_by5!="")&&_by3=="")
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
					//姓名
					var _name_id=elem.id.replace("r_by4","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_by4","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_by4","r_post");
					var _post=$("#"+_post_id).val();
					//手机
					var _phone_id=elem.id.replace("r_by4","r_phone");
					var _phone=$("#"+_phone_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_by4","r_email");
					var _email=$("#"+_email_id).val();
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
					if ((_name!=""||_company!=""||_post!=""||_phone!=""||_email!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by5!="")&&_by4=="")
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
					//姓名
					var _name_id=elem.id.replace("r_by5","r_name");
					var _name=$("#"+_name_id).val();
					//公司
					var _company_id=elem.id.replace("r_by5","r_company");
					var _company=$("#"+_company_id).val();
					//职务
					var _post_id=elem.id.replace("r_by5","r_post");
					var _post=$("#"+_post_id).val();
					//手机
					var _phone_id=elem.id.replace("r_by5","r_phone");
					var _phone=$("#"+_phone_id).val();
					//邮箱
					var _email_id=elem.id.replace("r_by5","r_email");
					var _email=$("#"+_email_id).val();
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
					if ((_name!=""||_company!=""||_post!=""||_phone!=""||_email!=""||_relation!=""||_by1!=""||_by2!=""||_by3!=""||_by4!="")&&_by5=="")
					{
						return MsgSet["REQUIRE"];
					}
				}
			});
		}
    }
})