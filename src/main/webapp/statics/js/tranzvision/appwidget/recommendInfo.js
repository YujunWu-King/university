/**
 * Created by LZ on 2015/6/18.
 * 推荐信  modity by caoy 2017/1/18 copy by 清华MBA版本
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
            "itemName": "姓氏",
            "orderby": 1,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
        "recommend_21": {
            "instanceId": "rec_gname",
            "itemId": "r_gname",
            "itemName": "名字",
			"classname":"SingleTextBox",
            "orderby": 2,
			"StorageType":"S",
			"useby": "Y",
            "value": ""
        },
		"recommend_2": {
            "instanceId": "rec_company",
            "itemId": "r_company",
            "itemName": "单位",
            "orderby": 3,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
        "recommend_3": {
            "instanceId": "rec_post",
            "itemId": "r_post",
            "itemName": "职务",
            "orderby": 4,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_4": {
            "instanceId": "rec_phone",
            "itemId": "r_phone",
            "itemName": "手机",
            "orderby": 5,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_5": {
            "instanceId": "rec_email",
            "itemId": "r_email",
            "itemName": "邮箱",
            "orderby": 6,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
        "recommend_6": {
            "instanceId": "rec_relation",
            "itemId": "r_relation",
            "itemName": "申请人关系",
            "orderby": 7,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_7": {
            "instanceId": "rec_language",
            "itemId": "r_language",
            "itemName": "推荐信语音",
            "orderby": 8,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_10": {
            "instanceId": "rec_by1",
            "itemId": "r_by1",
            "itemName": "备用字段一",
            "orderby": 9,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_11": {
            "instanceId": "rec_by2",
            "itemId": "r_by2",
            "itemName": "备用字段二",
            "orderby": 10,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_12": {
            "instanceId": "rec_by3",
            "itemId": "r_by3",
            "itemName": "备用字段三",
            "orderby": 11,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_13": {
            "instanceId": "rec_by4",
            "itemId": "r_by4",
            "itemName": "备用字段四",
            "orderby": 12,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_14": {
            "instanceId": "rec_by5",
            "itemId": "r_by5",
            "itemName": "备用字段五",
            "orderby": 13,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
		"recommend_15": {            	
            "instanceId": "rec_sex",
            "itemId": "r_sex",
            "itemName": "性别",
    		"title": "性别",
   			"classname":"Radio",
            "orderby": 14,
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
        "recommend_16": {
            "instanceId": "rec_by6",
            "itemId": "r_by6",
            "itemName": "备用字段六",
            "orderby": 15,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },"recommend_17": {
            "instanceId": "rec_by7",
            "itemId": "r_by7",
            "itemName": "备用字段七",
            "orderby": 16,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
        "recommend_18": {
            "instanceId": "rec_by8",
            "itemId": "r_by8",
            "itemName": "备用字段八",
            "orderby": 17,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
        "recommend_19": {
            "instanceId": "rec_by9",
            "itemId": "r_by9",
            "itemName": "备用字段九",
            "orderby": 18,
			"useby":"",
            "value": "",
            "StorageType":"S",
            "classname":"SingleTextBox"
        },
        "recommend_20": {
            "instanceId": "rec_by10",
            "itemId": "r_by10",
            "itemName": "备用字段十",
            "orderby": 19,
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
        	c += '<div class="mainright-title"><span class="title-line"></span>' + data.title + '</div>';
        	c += '<div class="mainright-box">';
        	c += this._getHtmlOne(data,0);
        	c += '</div>';

        } else {
            c = '<div class="question-answer">' + (data.itemMs ? '<div class="edu_exper_itemMs" style="background-color:#d8d8d8;padding:2px 5px;margin-bottom:10px;">'+ data.itemMs +'</div>' : "");
            c += '<div class="DHContainer" style="border:1px solid #ddd;padding:10px 20px;">'

			//姓氏
			c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_name_1'+'" '+(data.children.recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_name'+'">'+data.children.recommend_1["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
            
            //名字
			c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_gname_1'+'" '+(data.children.recommend_21["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_gname'+'">'+data.children.recommend_21["itemName"]+'：</span>';
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
            //备用字段六
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by6_1'+'" '+(data.children.recommend_16["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_by6'+'">'+data.children.recommend_16["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
            //备用字段七
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by7_1'+'" '+(data.children.recommend_17["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_by7'+'">'+data.children.recommend_17["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
            //备用字段八
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by8_1'+'" '+(data.children.recommend_18["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_by8'+'">'+data.children.recommend_18["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
            //备用字段九
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by9_1'+'" '+(data.children.recommend_19["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_by9'+'">'+data.children.recommend_19["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
            //备用字段十
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by10_1'+'" '+(data.children.recommend_20["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" id="'+data.instanceId+'-rec_by10'+'">'+data.children.recommend_20["itemName"]+'：</span>';
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
		
		//姓氏-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_name">';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_1\')" value="'+child.recommend_1["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
		list += '<td><input type="text" value="rec_name" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_1\')" '+(child.recommend_1["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
		list += '</tr>';
		//姓氏-结束
		
		//名子-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_gname">';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_21\')" value="'+child.recommend_21["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
		list += '<td><input type="text" value="rec_gname" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_21\')" '+(child.recommend_21["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
		list += '</tr>';
		//名字-结束
		
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
        
        //备用字段六-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by6">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_16\')" value="'+child.recommend_16["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by6" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_16\')" '+(child.recommend_16["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段六-结束
        
        //备用字段七-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by7">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_17\')" value="'+child.recommend_17["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by7" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_17\')" '+(child.recommend_17["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段五-结束
        
        //备用字段八-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by8">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_18\')" value="'+child.recommend_18["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by8" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_18\')" '+(child.recommend_18["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段八-结束
        
        //备用字段九-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by9">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_19\')" value="'+child.recommend_19["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by9" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_19\')" '+(child.recommend_19["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段九-结束
        
        //备用字段十-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by10">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_20\')" value="'+child.recommend_20["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" onpaste="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by10" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_20\')" '+(child.recommend_20["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段十-结束

		e += '<fieldset id="option-box"><span class="edit_item_label titlewidth"><i class="icon-list-alt"></i> 信息项</span><table class="table table-bordered data-table"><thead><tr><th width="55">Label Name</th><th width="55">Field Name</th><th width="20">启用</th></tr></thead><tbody class="ui-sortable">' + list + '</tbody></table></fieldset>';
		e += '</div>';


        e += '</div>';

        return e;
    },
    _getHtmlOne: function(data,rownum){

        var len = data.children.length;
        
        console.log("len:"+len);
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
		var _ref_gname="";
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
		var _ref_yl6="";
		var _ref_yl7="";
		var _ref_yl8="";
		var _ref_yl9="";
		var _ref_yl10="";
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
					_ref_gname =SurveyBuild.htmlCharReplace(f.comContent.TJR_GNAME);
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
					_ref_yl6=SurveyBuild.htmlCharReplace(f.comContent.TJR_YL6);
					_ref_yl7=SurveyBuild.htmlCharReplace(f.comContent.TJR_YL7);
					_ref_yl8=SurveyBuild.htmlCharReplace(f.comContent.TJR_YL8);
					_ref_yl9=SurveyBuild.htmlCharReplace(f.comContent.TJR_YL9);
					_ref_yl10=SurveyBuild.htmlCharReplace(f.comContent.TJR_YL10);
					
					_ref_sex=f.comContent.TZ_SEX;
				}
			}
		});
		
		/*if (_ref_sex=="M"){
			_ref_sex=MsgSet["SEX_M"];
		}else if (_ref_sex=="F"){
			_ref_sex=MsgSet["SEX_F"];
		} */
		
		var _readOnlyRadio = "N";
		if(SurveyBuild._readonly){
			_readOnlyRadio = "Y";
		}
		
        for (var i in child) {
			//姓氏-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_1["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_name + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">'+ child[i].recommend_1["itemName"] +'：</div>';
                works += '	<div class="input-list-text left"><input  type="text" class="inpu-list-text-enter" title="' + child[i].recommend_1["itemName"] + '" id="' + data.itemId + child[i].recommend_1["itemId"] + '" name="' + data.itemId + child[i].recommend_1["itemId"] + '" value="' + _ref_name + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_1["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//姓氏-结束
            
            //名字-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_21["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_21["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_gname + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_21["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">'+ child[i].recommend_21["itemName"] +'：</div>';
                works += '	<div class="input-list-text left"><input  type="text" class="inpu-list-text-enter" title="' + child[i].recommend_21["itemName"] + '" id="' + data.itemId + child[i].recommend_21["itemId"] + '" name="' + data.itemId + child[i].recommend_21["itemId"] + '" value="' + _ref_gname + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_21["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//名字-结束

			//单位-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_2["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_2["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_company + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_2["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_2["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_2["itemName"] + '" id="' + data.itemId + child[i].recommend_2["itemId"] + '" name="' + data.itemId + child[i].recommend_2["itemId"] + '" value="' + _ref_company + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_2["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//单位-结束

			//职务-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_3["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_3["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_zw + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_3["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_3["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_3["itemName"] + '" id="' + data.itemId + child[i].recommend_3["itemId"] + '" name="' + data.itemId + child[i].recommend_3["itemId"] + '" value="' + _ref_zw + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_3["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//职务-结束

			//手机-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_4["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_phone + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_4["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_4["itemName"] + '" id="' + data.itemId + child[i].recommend_4["itemId"] + '" name="' + data.itemId + child[i].recommend_4["itemId"] + '" value="' + _ref_phone + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_4["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//手机-结束

			//邮箱-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_5["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_email + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_5["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_5["itemName"] + '" id="' + data.itemId + child[i].recommend_5["itemId"] + '" name="' + data.itemId + child[i].recommend_5["itemId"] + '" value="' + _ref_email + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_5["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//邮箱-结束

			//性别-开始                         
           
            if (_ref_sex == "M") {
            	child[i].recommend_15["option"]["MAN"]["checked"] = "Y";
            	child[i].recommend_15["option"]["WOMAN"]["checked"] = "N";
            }
            if (_ref_sex == "F") {
            	child[i].recommend_15["option"]["MAN"]["checked"] = "N";
            	child[i].recommend_15["option"]["WOMAN"]["checked"] = "Y";
            }
            
            works += '<div class="input-list" '+(child[i].recommend_15["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
    		works += '<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_15["itemName"] +'：</div>';
    		works += '<div class="margart8 input-list-textwrap left">';
    		works += '	<ul>'; 
    		works += '	<li>'; 
    		works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '" class="radio-btn '+(_ref_sex == "M" ? "checkedRadio" : "")+'" onclick="SurveyBuild.clickOnRadio(this);" id="' + data.itemId + child[i].recommend_15["itemId"] + '_M">';
            works += '<i><input type="radio"' + (_ref_sex == "M" ? "checked='checked'": "")+' title="'+ child[i].recommend_15["title"] +'" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="M"></i>';

    		works += '</div><span style="margin-left:3px;">' + MsgSet["SEX_M"]+'</span>&nbsp;&nbsp;&nbsp;&nbsp;';

    		works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '" class="radio-btn '+(_ref_sex == "F" ? "checkedRadio" : "")+'" onclick="SurveyBuild.clickOnRadio(this);" id="' + data.itemId + child[i].recommend_15["itemId"] + '_F">';
            works += '<i><input type="radio" ' + (_ref_sex == "F" ? "checked='checked'": "") +' title="'+ child[i].recommend_15["title"] +'" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="F"></i>';
    		works += '</div><span style="margin-left:3px;">'+MsgSet["SEX_F"]+'</span>';
    		works += '	</li>';
    		works += '<div class="clear"></div></ul>';
            works += '<input type="hidden" style="width:10px;" id="' + data.itemId + child[i].recommend_15["itemId"] + '" value="' + _ref_sex + '">';
    		works += '</div>';
    		works += '<div class="input-list-suffix left">';
    		works += '	<div id="' + data.itemId + child[i].recommend_15["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
    		works += '		<div class="onShow">&nbsp;</div>';
    		works += '	</div>';
    		works += '</div>';
    		works += '<div class="clear"></div>';
    		works += '</div>';
            /*if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_15["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_15["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_sex + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_15["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_15["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_15["itemName"] + '" id="' + data.itemId + child[i].recommend_15["itemId"] + '" name="' + data.itemId + child[i].recommend_15["itemId"] + '" value="' + _ref_sex + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_15["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            } */
			//性别-结束

			//申请人关系-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_6["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_gx + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_6["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_6["itemName"] + '" id="' + data.itemId + child[i].recommend_6["itemId"] + '" name="' + data.itemId + child[i].recommend_6["itemId"] + '" value="' + _ref_gx + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_6["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//申请人关系-结束

			//备用字段一-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_10["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_10["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_yl1 + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_10["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_10["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter" title="' + child[i].recommend_10["itemName"] + '" id="' + data.itemId + child[i].recommend_10["itemId"] + '" name="' + data.itemId + child[i].recommend_10["itemId"] + '" value="' + _ref_yl1 + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_10["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//备用字段一-结束

			//备用字段二-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_11["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_11["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_yl2 + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_11["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_11["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_11["itemName"] + '" id="' + data.itemId + child[i].recommend_11["itemId"] + '" name="' + data.itemId + child[i].recommend_11["itemId"] + '" value="' + _ref_yl2 + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_11["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//备用字段二-结束

			//备用字段三-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_12["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_12["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_yl3 + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_12["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_12["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_12["itemName"] + '" id="' + data.itemId + child[i].recommend_12["itemId"] + '" name="' + data.itemId + child[i].recommend_12["itemId"] + '" value="' + _ref_yl3 + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_12["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//备用字段三-结束

			//备用字段四-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_13["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_13["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_yl4 + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_13["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_13["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_13["itemName"] + '" id="' + data.itemId + child[i].recommend_13["itemId"] + '" name="' + data.itemId + child[i].recommend_13["itemId"] + '" value="' + _ref_yl4 + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_13["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//备用字段四-结束

			//备用字段五-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_14["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_14["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_yl5 + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_14["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_14["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter" title="' + child[i].recommend_14["itemName"] + '" id="' + data.itemId + child[i].recommend_14["itemId"] + '" name="' + data.itemId + child[i].recommend_14["itemId"] + '" value="' + _ref_yl5 + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_14["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//备用字段五结束
            
            //备用字段六-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_16["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_16["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_yl6 + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_16["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_16["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_16["itemName"] + '" id="' + data.itemId + child[i].recommend_16["itemId"] + '" name="' + data.itemId + child[i].recommend_16["itemId"] + '" value="' + _ref_yl6 + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_16["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//备用字段六结束
            
            //备用字段七-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_17["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_17["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_yl7 + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_17["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_17["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_17["itemName"] + '" id="' + data.itemId + child[i].recommend_17["itemId"] + '" name="' + data.itemId + child[i].recommend_17["itemId"] + '" value="' + _ref_yl7 + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_17["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//备用字段七结束
            
            //备用字段八-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_18["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_18["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_yl8 + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_18["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_18["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_18["itemName"] + '" id="' + data.itemId + child[i].recommend_18["itemId"] + '" name="' + data.itemId + child[i].recommend_18["itemId"] + '" value="' + _ref_yl8 + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_18["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//备用字段八结束
            
            //备用字段九-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_19["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_19["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_yl9 + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_19["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_19["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_19["itemName"] + '" id="' + data.itemId + child[i].recommend_19["itemId"] + '" name="' + data.itemId + child[i].recommend_19["itemId"] + '" value="' + _ref_yl9 + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_19["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//备用字段九结束
            
            //备用字段十-开始
            if(SurveyBuild._readonly){
                //只读模式
                works += '<div class="input-list" ' + (child[i].recommend_20["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_20["itemName"] + '：</div>';
                works += '	<div class="input-list-text left">' + _ref_yl10 + '</div>';
                works += '	<div class="input-list-suffix left"></div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }else{
                //填写模式
                works += '<div class="input-list" ' + (child[i].recommend_20["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
                works += '	<div class="input-list-info left">' + child[i].recommend_20["itemName"] + '：</div>';
                works += '	<div class="input-list-text left"><input type="text" class="inpu-list-text-enter"  title="' + child[i].recommend_20["itemName"] + '" id="' + data.itemId + child[i].recommend_20["itemId"] + '" name="' + data.itemId + child[i].recommend_20["itemId"] + '" value="' + _ref_yl10 + '"/></div>';
                works += '  <div class="input-list-suffix left">';
                works += '		<div id="' + data.itemId + child[i].recommend_20["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
                works += '			<div class="onShow">&nbsp;</div>';
                works += '		</div>';
                works += '	</div>';
                works += '	<div class="clear"></div>';
                works += '</div>';
            }
			//备用字段十结束
            
        }
        return works;
    },
    _eventbind: function(data) {
        var children = data.children,
        len = children.length;
        console.log("len:"+len);
        
        console.dir(children);
        

        
        
        for (var i=1;i<=len;i++)
		{
			
            
        	
        	 //console.dir(children[i-1]);
        	
        	var raido15 = children[i-1];
        	 
        	$("#"+data.itemId + children[i-1].recommend_15["itemId"]+"_M").click(function(){
    			var readOnly = $(this).attr("readonlyflag");
    		    if(readOnly!="Y"){
    				//var lineno = parseInt($(this).closest(".main_inner_content_para").index());
    		    	
    		    	
    		    	raido15.recommend_15["option"]["MAN"]["checked"] = "Y";
    		    	raido15.recommend_15["option"]["WOMAN"]["checked"] = "N";

    				var child_M = $(this).find('input:radio');
    				var m = child_M.attr("name");
    				var value=child_M.val();
    				$("#"+m).val(value);
    				
    				$("#"+m).focus();
    				$("#"+m).blur();
    			}
    			
    		});
    		$("#"+data.itemId + children[i-1].recommend_15["itemId"]+"_F").click(function(){
    			var readOnly = $(this).attr("readonlyflag");
    		    if(readOnly!="Y"){
    				//var lineno = parseInt($(this).closest(".main_inner_content_para").index());
    		    	raido15.recommend_15["option"]["MAN"]["checked"] = "N";
    		    	raido15.recommend_15["option"]["WOMAN"]["checked"] = "Y";
    				var child_f = $(this).find('input:radio');

    				var m = child_f.attr("name");
    				var value=child_f.val();
    				$("#"+m).val(value);
    				$("#"+m).focus();
    				$("#"+m).blur();
    			}
    		});
        	
        	
        	var $tjr_name = $("#" + data.itemId + children[i - 1].recommend_1["itemId"]);
        	var $tjr_gname = $("#" + data.itemId + children[i - 1].recommend_21["itemId"]);
			var $tjr_company = $("#" + data.itemId + children[i - 1].recommend_2["itemId"]);
			var $tjr_post = $("#" + data.itemId + children[i - 1].recommend_3["itemId"]);
			var $tjr_phone_no = $("#" + data.itemId + children[i - 1].recommend_4["itemId"]);
			var $tjr_email = $("#" + data.itemId + children[i - 1].recommend_5["itemId"]);
			var $tjr_relation = $("#" + data.itemId + children[i - 1].recommend_6["itemId"]);
			var $tjr_by1 = $("#" + data.itemId + children[i - 1].recommend_10["itemId"]);
			var $tjr_by2 = $("#" + data.itemId + children[i - 1].recommend_11["itemId"]);
			var $tjr_by3 = $("#" + data.itemId + children[i - 1].recommend_12["itemId"]);
			var $tjr_by4 = $("#" + data.itemId + children[i - 1].recommend_13["itemId"]);
			var $tjr_by5 = $("#" + data.itemId + children[i - 1].recommend_14["itemId"]);
			
			var $tjr_sex = $("#" + data.itemId + children[i - 1].recommend_15["itemId"]);
			
			var $tjr_by6 = $("#" + data.itemId + children[i - 1].recommend_16["itemId"]);
			var $tjr_by7 = $("#" + data.itemId + children[i - 1].recommend_17["itemId"]);
			var $tjr_by8 = $("#" + data.itemId + children[i - 1].recommend_18["itemId"]);
			var $tjr_by9 = $("#" + data.itemId + children[i - 1].recommend_19["itemId"]);
			var $tjr_by10 = $("#" + data.itemId + children[i - 1].recommend_20["itemId"]);
			
			
			var _checkHtml=  function(val,elem,Regular){
				//console.log(val);
				if (val == "") { //判断 是否为为空
					return elem.title+MsgSet["REQUIRE"];
				}  else {
					if (Regular == 'phone_area') {
						var _result = /^[\d-+]+$/.test(val);
						if(!_result){
							return elem.title+MsgSet["FORMAT_ERROR_MSG"];
						}
					} else if (Regular == 'phone_no') {
						var _result = /^1\d{10}$/.test(val);
						if(!_result){
							return elem.title+MsgSet["FORMAT_ERROR_MSG"];
						}
					} else if (Regular == 'email') {
						var std=/^([\w\-\.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\w\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$/;
						if (!std.test(val)){
							return elem.title+MsgSet["FORMAT_ERROR_MSG"];
						}
					}
				}	
			};
		    
			
			//姓氏
			$tjr_name.formValidator({tipID:data["itemId"] + children[i - 1].recommend_1["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_name.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
				}
			});
			
			//名称
			$tjr_gname.formValidator({tipID:data["itemId"] + children[i - 1].recommend_21["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_gname.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
				}
			});
			//公司
			$tjr_company.formValidator({tipID:data["itemId"] + children[i - 1].recommend_2["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_company.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
				}
			});
			//职务
			$tjr_post.formValidator({tipID:data["itemId"] + children[i - 1].recommend_3["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_post.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
				}
			});
			
			//手机
			$tjr_phone_no.formValidator({tipID:data["itemId"] + children[i - 1].recommend_4["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_phone_no.functionValidator({
				fun:function(val,elem){
					//手机号码只做必填校验，不做格式校验
					return _checkHtml(val,elem,"none");
				}
			});
			//邮箱
			
			$tjr_email.formValidator({tipID:data["itemId"] + children[i - 1].recommend_5["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_email.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"email");
				}
			});
			
			//性别
			
			$tjr_sex.formValidator({tipID:data["itemId"] + children[i - 1].recommend_15["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_sex.functionValidator({
				fun:function(val,elem){
					console.log("sex:"+val);
					return _checkHtml(val,elem,"none");
				}
			});
			//$tjr_email.regexValidator({regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onError:(children[0].recommend_5["itemName"]+MsgSet["FORMATERROR"])});
			//与申请人关系
			$tjr_relation.formValidator({tipID:data["itemId"] + children[i - 1].recommend_6["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_relation.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
				}
			});
			//备用字段一
			$tjr_by1.formValidator({tipID:data["itemId"] + children[i - 1].recommend_10["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by1.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
					
				}
			});
			//备用字段二
			$tjr_by2.formValidator({tipID:data["itemId"] + children[i - 1].recommend_11["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by2.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
					
				}
			});
			//备用字段三
			$tjr_by3.formValidator({tipID:data["itemId"] + children[i - 1].recommend_12["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by3.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
				}
			});
			//备用字段四
			$tjr_by4.formValidator({tipID:data["itemId"] + children[i - 1].recommend_13["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by4.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
					
				}
			});
			//备用字段五
			$tjr_by5.formValidator({tipID:data["itemId"] + children[i - 1].recommend_14["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by5.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
					
				}
			});
			
			//备用字段六
			$tjr_by6.formValidator({tipID:data["itemId"] + children[i - 1].recommend_16["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by6.functionValidator({
				fun:function(val,elem){
					return _checkHtml(val,elem,"none");
				}
			});
			
			//备用字段七
			$tjr_by7.formValidator({tipID:data["itemId"] + children[i - 1].recommend_17["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by7.functionValidator({
				fun:function(val,elem){
					return _checkHtml(val,elem,"none");
				}
			});
			
			//备用字段八
			$tjr_by8.formValidator({tipID:data["itemId"] + children[i - 1].recommend_18["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by8.functionValidator({
				fun:function(val,elem){
					return _checkHtml(val,elem,"none");
				}
			});
			
			//备用字段九
			$tjr_by9.formValidator({tipID:data["itemId"] + children[i - 1].recommend_19["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by9.functionValidator({
				fun:function(val,elem){
					return _checkHtml(val,elem,"none");
				}
			});
			
			//备用字段十
			$tjr_by10.formValidator({tipID:data["itemId"] + children[i - 1].recommend_20["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by10.functionValidator({
				fun:function(val,elem){
					return _checkHtml(val,elem,"none");
				}
			});
		}
    }
})