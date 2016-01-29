/**
 * Created by LZ on 2015/6/18.
 * 推荐信
 */
SurveyBuild.extend("recommendInfo", "baseComponent", {
    itemName: "推荐人信息",
    title: "推荐人信息",
    itemMs: "",
    isDoubleLine: "Y",
    fixedContainer:"Y",//固定容器标识
	isRequire:"N",
    children:{
		"recommend_1": {
            "instanceId": "rec_name",
            "itemId": "r_name",
            "itemName": "姓名",
            "orderby": 1,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_2": {
            "instanceId": "rec_company",
            "itemId": "r_company",
            "itemName": "单位",
            "orderby": 2,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
        "recommend_3": {
            "instanceId": "rec_post",
            "itemId": "r_post",
            "itemName": "职务",
            "orderby": 3,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_4": {
            "instanceId": "rec_phone",
            "itemId": "r_phone",
            "itemName": "手机",
            "orderby": 4,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_5": {
            "instanceId": "rec_email",
            "itemId": "r_email",
            "itemName": "邮箱",
            "orderby": 5,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
        "recommend_6": {
            "instanceId": "rec_relation",
            "itemId": "r_relation",
            "itemName": "申请人关系",
            "orderby": 6,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_7": {
            "instanceId": "rec_language",
            "itemId": "r_language",
            "itemName": "推荐信语音",
            "orderby": 7,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_10": {
            "instanceId": "rec_by1",
            "itemId": "r_by1",
            "itemName": "备用字段一",
            "orderby": 10,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_11": {
            "instanceId": "rec_by2",
            "itemId": "r_by2",
            "itemName": "备用字段二",
            "orderby": 11,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_12": {
            "instanceId": "rec_by3",
            "itemId": "r_by3",
            "itemName": "备用字段三",
            "orderby": 12,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_13": {
            "instanceId": "rec_by4",
            "itemId": "r_by4",
            "itemName": "备用字段四",
            "orderby": 13,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_14": {
            "instanceId": "rec_by5",
            "itemId": "r_by5",
            "itemName": "备用字段五",
            "orderby": 14,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_15": {
            "instanceId": "rec_sex",
            "itemId": "r_sex",
            "itemName": "性别",
            "orderby": 15,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        }
    },
    minLines: 1,
    maxLines: 1,
    _getHtml: function (data, previewmode) {

        var c = "", children = data.children,len = children.length;
        if (previewmode) {
            //title
            c = '<div class="main_inner_content_title">';
            //c += '<span class="reg_title_star">' + (data.isRequire == "Y" ?"*":"") + '</span><span class="reg_title_grey_17px">' + data.itemName + '：</span>';
			c += '<span class="font_gray_16px">' + data.title + '</span>';
            c += '</div>';
            c += '<div class="main_inner_content_top"></div>';

            //content
            c += '<div class="main_inner_content">';

            var works = this._getHtmlOne(data,0);

            c += works;
            //footer
            c += '<div class="main_inner_content_foot"></div>';

        } else {
            c = '<div class="question-answer">' + (data.itemMs ? '<div class="edu_exper_itemMs" style="background-color:#d8d8d8;padding:2px 5px;margin-bottom:10px;">'+ data.itemMs +'</div>' : "");
            c += '<div class="DHContainer" style="border:1px solid #ddd;padding:10px 20px;">'

			//姓名
			c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_name_1'+'" '+(data.children.recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_name'+'">'+data.children.recommend_1["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//单位
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_company_1'+'" '+(data.children.recommend_2["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_company'+'">'+data.children.recommend_2["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//职务
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_post_1'+'" '+(data.children.recommend_3["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_post'+'">'+data.children.recommend_3["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//手机
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_phone_1'+'" '+(data.children.recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_phone'+'">'+data.children.recommend_4["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//邮箱
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_email_1'+'" '+(data.children.recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_email'+'">'+data.children.recommend_5["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//性别
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_sex_1'+'" '+(data.children.recommend_15["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_sex'+'">'+data.children.recommend_15["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//申请人关系
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_relation_1'+'" '+(data.children.recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_relation'+'">'+data.children.recommend_6["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//备用字段一
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by1_1'+'" '+(data.children.recommend_10["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_by1'+'">'+data.children.recommend_10["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//备用字段二
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by2_1'+'" '+(data.children.recommend_11["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_by2'+'">'+data.children.recommend_11["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//备用字段三
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by3_1'+'" '+(data.children.recommend_12["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_by3'+'">'+data.children.recommend_12["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//备用字段四
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by4_1'+'" '+(data.children.recommend_13["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_by4'+'">'+data.children.recommend_13["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
			//备用字段五
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by5_1'+'" '+(data.children.recommend_14["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_by5'+'">'+data.children.recommend_14["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';

			c += '</div>';

            c += '</div></div>'
        }
        return c;
    },
    _edit: function (data) {
        var e = '<div class="edit_item_warp"><span class="edit_item_label">描述：</span><input type="text" class="medium" onkeyup="SurveyBuild.saveAttr(this,\'itemMs\')" value="' + data.itemMs + '"/></div>';

        e += '<div class="edit_jygz">';
		
		//字段显示隐藏
		e += '<div class="edit_item_warp" style="margin-top:5px;">';
		var list = "";
		var child = [];
		child = data.children;
		
		//姓名-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_name">';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_1\')" value="'+child.recommend_1["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
		list += '<td><input type="text" value="rec_name" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_1\')" '+(child.recommend_1["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
		list += '</tr>';
		//姓名-结束
		
		//单位-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_company">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_2\')" value="'+child.recommend_2["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_company" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_2\')" '+(child.recommend_2["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//单位-结束

		//职务-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_post">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_3\')" value="'+child.recommend_3["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_post" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_3\')" '+(child.recommend_3["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//职务-结束

		//手机-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_phone">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_4\')" value="'+child.recommend_4["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_phone" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_4\')" '+(child.recommend_4["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//手机-结束

		//邮箱-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_email">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_5\')" value="'+child.recommend_5["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_email" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_5\')" '+(child.recommend_5["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
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

		e += '<fieldset id="option-box"><span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 信息项</span><table class="table table-bordered data-table"><thead><tr><th width="55">Label Name</th><th width="55">Field Name</th><th width="20">启用</th></tr></thead><tbody class="ui-sortable">' + list + '</tbody></table></fieldset>';
		e += '</div>';


        e += '</div>';

        return e;
    },
    _getHtmlOne: function(data,rownum){

        var len = data.children.length;
        var works = "",j = 0,child = [];
        if (parseInt(rownum) == parseInt(len)){
            //返回最后一条记录的HTML及最后一个工作经历（主要用于新增）
            j = rownum - 1;
            child.push(data.children[rownum-1]);
        }else{
            child = data.children;
        }
		var tz_app_ins_id=SurveyBuild.appInsId;
		var tz_ref_id=SurveyBuild.refLetterId;
		var _ref_name="";
		var _ref_company="";
		var _ref_zw="";
		var _ref_phone="";
		var _ref_email="";
		var _ref_gx="";
		var _ref_yl1="";
		var _ref_yl2="";
		var _ref_yl3="";
		var _ref_yl4="";
		var _ref_yl5="";
		var _ref_sex="";
		var params = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_GD_TJX_PZ_STD","OperateType":"EJSON","comParams":{"APP_INS_ID":"'+tz_app_ins_id+'","TZ_REF_LETTER_ID":"'+tz_ref_id+'"}}';
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

					_ref_name=SurveyBuild.htmlCharReplace(f.comContent.TJR_NAME);
					_ref_company=SurveyBuild.htmlCharReplace(f.comContent.TJR_COMPANY);
					_ref_zw=SurveyBuild.htmlCharReplace(f.comContent.TJR_ZW);
					_ref_phone=f.comContent.TJR_PHONE;
					_ref_email=f.comContent.TJR_EMAIL;
					_ref_gx=SurveyBuild.htmlCharReplace(f.comContent.TJR_GX);
					_ref_yl1=SurveyBuild.htmlCharReplace(f.comContent.TJR_YL1);
					_ref_yl2=SurveyBuild.htmlCharReplace(f.comContent.TJR_YL2);
					_ref_yl3=SurveyBuild.htmlCharReplace(f.comContent.TJR_YL3);
					_ref_yl4=SurveyBuild.htmlCharReplace(f.comContent.TJR_YL4);
					_ref_yl5=SurveyBuild.htmlCharReplace(f.comContent.TJR_YL5);
					_ref_sex=f.comContent.TZ_SEX;
				}
			}
		});
		if (_ref_sex=="M")
		{
			_ref_sex=MsgSet["SEX_M"];
		}else if (_ref_sex=="F")
		{
			_ref_sex=MsgSet["SEX_F"];
		}
        for (var i in child) {
            works += '<div id="main_inner_content_para' + j + '" style="display: inherit;" >';
            if(j != 0){
                works += '<div class="main_inner_content_top"></div><div class="padding_div"></div><div class="main_inner_content_foot"></div>';
            }
			//姓名-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
                works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_1["itemName"] +'：</div>';
                works += '<div class="main_inner_content_info_right">' + _ref_name + '</div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
                works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_1["itemName"] +'：</div>';
                works += '<div class="main_inner_content_info_right">';
                works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_1["itemName"] +'" id="' + data.itemId + child[i].recommend_1["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_1["itemId"] + '" value="' + _ref_name + '">';
                works += '</div>';
                works += '</div>';
            }
			//姓名-结束

			//单位-开始
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_2["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_2["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_right">';
            works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_2["itemName"] +'" id="' + data.itemId + child[i].recommend_2["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_2["itemId"] + '" value="' + _ref_company + '">';
            works += '</div>';
            works += '</div>';
			//单位-结束

			//职务-开始
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_3["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_3["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_right">';
            works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_3["itemName"] +'" id="' + data.itemId + child[i].recommend_3["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_3["itemId"] + '" value="' + _ref_zw + '">';
            works += '</div>';
            works += '</div>';
			//职务-结束

			//手机-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
                works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_4["itemName"] +'：</div>';
                works += '<div class="main_inner_content_info_right">' + _ref_phone + '</div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
                works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_4["itemName"] +'：</div>';
                works += '<div class="main_inner_content_info_right">';
                works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_4["itemName"] +'" id="' + data.itemId + child[i].recommend_4["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_4["itemId"] + '" value="' + _ref_phone + '">';
                works += '</div>';
                works += '</div>';
            }
			//手机-结束

			//邮箱-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
                works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_5["itemName"] +'：</div>';
                works += '<div class="main_inner_content_info_right">' + _ref_email + '</div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
                works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_5["itemName"] +'：</div>';
                works += '<div class="main_inner_content_info_right">';
                works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_5["itemName"] +'" id="' + data.itemId + child[i].recommend_5["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_5["itemId"] + '" value="' + _ref_email + '">';
                works += '</div>';
                works += '</div>';
            }
			//邮箱-结束

			//性别-开始
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_15["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_15["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_right">';
			works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_5["itemName"] +'" id="' + data.itemId + child[i].recommend_15["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_15["itemId"] + '" value="' + _ref_sex + '">';
            works += '</div>';
            works += '</div>';
			//性别-结束

			//申请人关系-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
                works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_6["itemName"] +'：</div>';
                works += '<div class="main_inner_content_info_right">' + _ref_gx + '</div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
                works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_6["itemName"] +'：</div>';
                works += '<div class="main_inner_content_info_right">';
                works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_6["itemName"] +'" id="' + data.itemId + child[i].recommend_6["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_6["itemId"] + '" value="' + _ref_gx + '">';
                works += '</div>';
                works += '</div>';
            }
			//申请人关系-结束

			//备用字段一-开始
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_10["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_10["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_right">';
            works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_10["itemName"] +'" id="' + data.itemId + child[i].recommend_10["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_10["itemId"] + '" value="' + _ref_yl1 + '">';
            works += '</div>';
            works += '</div>';
			//备用字段一-结束

			//备用字段二-开始
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_11["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_11["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_right">';
            works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_11["itemName"] +'" id="' + data.itemId + child[i].recommend_11["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_11["itemId"] + '" value="' + _ref_yl2 + '">';
            works += '</div>';
            works += '</div>';
			//备用字段二-结束

			//备用字段三-开始
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_12["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_12["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_right">';
            works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_12["itemName"] +'" id="' + data.itemId + child[i].recommend_12["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_12["itemId"] + '" value="' + _ref_yl3 + '">';
            works += '</div>';
            works += '</div>';
			//备用字段三-结束

			//备用字段四-开始
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_13["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_13["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_right">';
            works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_13["itemName"] +'" id="' + data.itemId + child[i].recommend_13["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_13["itemId"] + '" value="' + _ref_yl4 + '">';
            works += '</div>';
            works += '</div>';
			//备用字段四-结束

			//备用字段五-开始
            works += '<div class="main_inner_content_info_autoheight" '+(child[i].recommend_14["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="main_inner_connent_info_left">'+ child[i].recommend_14["itemName"] +'：</div>';
            works += '<div class="main_inner_content_info_right">';
            works += '<input type="text" disabled="disabled" title="'+ child[i].recommend_14["itemName"] +'" id="' + data.itemId + child[i].recommend_14["itemId"] + '" class="input_251px" name="' + data.itemId + child[i].recommend_14["itemId"] + '" value="' + _ref_yl5 + '">';
            works += '</div>';
            works += '</div>';
			//备用字段五结束

            works += '</div>';
            j++;
        }
        return works;
    },
    _eventbind: function(data) {
        var children = data.children,
        len = children.length;
    }
})