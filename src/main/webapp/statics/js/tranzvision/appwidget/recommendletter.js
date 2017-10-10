/**
 * Created by LZ on 2015/6/18.
 * 推荐信  modity by caoy 2017/1/18
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
        "recommend_19": {
            "instanceId": "rec_by6",
            "itemId": "r_by6",
            "itemName": "备用字段六",
			"title": "备用字段六",
			"classname":"SingleTextBox",
            "orderby": 18,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
        "recommend_20": {
            "instanceId": "rec_by7",
            "itemId": "r_by7",
            "itemName": "备用字段七",
			"title": "备用字段七",
			"classname":"SingleTextBox",
            "orderby": 19,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
        "recommend_21": {
            "instanceId": "rec_by8",
            "itemId": "r_by8",
            "itemName": "备用字段八",
			"title": "备用字段八",
			"classname":"SingleTextBox",
            "orderby": 20,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
        "recommend_22": {
            "instanceId": "rec_by9",
            "itemId": "r_by9",
            "itemName": "备用字段九",
			"title": "备用字段九",
			"classname":"SingleTextBox",
            "orderby": 21,
			"StorageType":"S",
			"useby": "",
            "value": ""
        },
        "recommend_23": {
            "instanceId": "rec_by10",
            "itemId": "r_by10",
            "itemName": "备用字段十",
			"title": "备用字段十",
			"classname":"SingleTextBox",
            "orderby": 22,
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
            "orderby": 23,
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
	_init: function(d, previewmode) {
		var linesNo = [];
		for (var i = 1; i < this.maxLines; i++) {
			linesNo.push(i);
		}
		this["linesNo"] = linesNo;
	},
    _getHtml: function (data, previewmode) {
		
        var c = "", children = data.children,len = children.length;
        
        if (previewmode) {
          
			var showLines;
			var len = data.children.length;
			if(len>=data.defaultLines)
			{
				showLines = len;
			}else{
				showLines = data.defaultLines;
			}
			console.log("MshowLines----"+showLines);
			if(SurveyBuild.accessType == "M"){
				console.log("showLines-----"+showLines);
				for(var i=1;i<=showLines;i++){
					
					var tempHtmlP = this._getHtmlTwo(data,i,'QUERY');
					c += tempHtmlP;
					if(i>1){
						data["linesNo"].shift(); 
					}
				}

				//非只读模式、添加下一条
                if(!SurveyBuild._readonly){

                	if(len<data.maxLines){

                		c += '<div class="clear"><div class="add_next" onclick="SurveyBuild.addTjx(this,\'' + data.instanceId + '\')">' + MsgSet["ADD_ONE"] + '</div></div>';
                	}else{

                		c += '<div class="clear" style="display: none"><div class="add_next" onclick="SurveyBuild.addTjx(this,\'' + data.instanceId + '\')>' + MsgSet["ADD_ONE"] + '</div></div>';
                	}

                }

			}else{
				
				for(var i=1;i<=showLines;i++){
					
					works = this._getHtmlOne(data,i,'QUERY');
					c += works;
					if(i>1){
						data["linesNo"].shift(); 
					}
				}

                /*添加 (添加下一条) 按钮*/
                if(SurveyBuild._readonly != true){
                    len = data.children.length;
                    c += '<div class="input-list-blank addNext">';
                    if(len<data.maxLines){
                        c += '<div class="input-list-suffix-blank right input-btn">'
                        c += '<div style="display: inherit;float:right;padding-right:30px;" class="input-addbtn" id="save_and_add0" onclick="SurveyBuild.addTjx(this,\'' + data.instanceId + '\');">' + MsgSet["ADD_RE"] + '&nbsp;&nbsp;<span class="input-btn-icon"></span></div>';
                        c += '</div>';
                    }else{
                        c += '<div class="input-list-suffix-blank right input-btn" style="padding-right:15px;">'
                        c += '<div style="display: none;float:right;" class="input-addbtn" id="save_and_add0" onclick="SurveyBuild.addTjx(this,\'' + data.instanceId + '\');">' + MsgSet["ADD_RE"] + '&nbsp;&nbsp;<span class="input-btn-icon"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/add-jia.png" /></span></div>';
                        c += '</div>';
                    }
                    c +='<div class="clear"></div>';
                    c += '</div>';
                }
			}
			
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
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_name'+'">'+data.children.recommend_17["itemName"]+'：</span>';
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
			if(data.children.recommend_16["useby"] == "Y"){
				c += '<b class="read-input" style="min-width:63px;width:63px">&nbsp;</b>';
				c += ' <b class="read-input" style="min-width:180px;width:183px">&nbsp;</b>';
			}else{
				c += '<b class="read-input" style="width:272px">&nbsp;</b>';
			}
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
            //备用字段六
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by6_1'+'" '+(data.children.recommend_19["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_by6'+'">'+data.children.recommend_19["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
            //备用字段七
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by7_1'+'" '+(data.children.recommend_20["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_by7'+'">'+data.children.recommend_20["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
            //备用字段八
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by8_1'+'" '+(data.children.recommend_21["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_by8'+'">'+data.children.recommend_21["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
            //备用字段九
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by9_1'+'" '+(data.children.recommend_22["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_by9'+'">'+data.children.recommend_22["itemName"]+'：</span>';
            c += '<b class="read-input" style="width:272px">&nbsp;</b>';
            c += '</div>';
            //备用字段十
            c += '<div class="edu_item_li" id="'+data.instanceId+'-rec_by10_1'+'" '+(data.children.recommend_23["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            c += '<span class="edu_item_label" style="width:150px;" id="'+data.instanceId+'-rec_by10'+'">'+data.children.recommend_23["itemName"]+'：</span>';
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
		//list += '<td><input type="checkbox" checked="checked" value="1"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_16\')" '+(child.recommend_16["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
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
        //备用字段六-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by6">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_19\')" value="'+child.recommend_19["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_19\')" value="'+child.recommend_19["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by6" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_19\')" '+(child.recommend_19["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段六-结束
        //备用字段七-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by7">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_20\')" value="'+child.recommend_20["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_20\')" value="'+child.recommend_20["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by7" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_20\')" '+(child.recommend_20["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段七-结束
        //备用字段八-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by8">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_21\')" value="'+child.recommend_21["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_21\')" value="'+child.recommend_21["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by8" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_21\')" '+(child.recommend_21["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段八-结束
        //备用字段九-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by9">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_22\')" value="'+child.recommend_22["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_22\')" value="'+child.recommend_22["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by9" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_22\')" '+(child.recommend_22["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段九-结束
        //备用字段十-开始
		list += '<tr class="read-radio" data-id="'+data.instanceId+'-rec_by10">';
        list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'itemName\',\'recommend_23\')" value="'+child.recommend_23["itemName"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
		list += '<td><input type="text" onkeyup="SurveyBuild.saveLevel1Attr1(this,\'title\',\'recommend_23\')" value="'+child.recommend_23["title"]+'" oncontextmenu="return false;" ondragenter="return false" class="option-txt"></td>';
        list += '<td><input type="text" value="rec_by10" class="option-txt" disabled="disabled"></td>';
		list += '<td><input type="checkbox" onchange="SurveyBuild.saveLevel1Attr2(this,\'useby\',\'recommend_23\')" '+(child.recommend_23["useby"] == "Y" ? "checked='checked'" : "")+' value="1"></td>';
        list += '</tr>';
		//备用字段十-结束
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
				child[i].recommend_99["value"] = parseInt(data.tjxMaxLinesXh) + 1;
				data.tjxMaxLinesXh = child[i].recommend_99["value"];
				_tjrXh = data.tjxMaxLinesXh;
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
		var _ref_recommend_1 = "";
		var _ref_recommend_2 = "";
		var _ref_recommend_3 = "";
		var _ref_recommend_4 = "";
		var _ref_recommend_5 = "";
		var _ref_recommend_6 = "";
		var _ref_recommend_7 = "";
		var _ref_recommend_8 = "";
		var _ref_recommend_9 = "";
		var _ref_recommend_10 = "";
		var _ref_recommend_11 = "";
		var _ref_recommend_12 = "";
		var _ref_recommend_13 = "";
		var _ref_recommend_14 = "";
		var _ref_recommend_15 = "";
		var _ref_recommend_16= "";
		var _ref_recommend_17 = "";
		var _ref_recommend_18 = "";
		var _ref_recommend_19 = "";
		var _ref_recommend_20 = "";
		var _ref_recommend_21 = "";
		var _ref_recommend_22 = "";
		var _ref_recommend_23 = "";
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
					_ref_recommend_18 = f.comContent.recommend_18;
					_ref_recommend_1 = f.comContent.recommend_1;
					_ref_recommend_17 = f.comContent.recommend_17;
					_ref_recommend_2 = f.comContent.recommend_2;
					_ref_recommend_3 = f.comContent.recommend_3;
					_ref_recommend_15 = f.comContent.recommend_15;
					_ref_recommend_16 = f.comContent.recommend_16;
					_ref_recommend_4 = f.comContent.recommend_4;
					_ref_recommend_5 = f.comContent.recommend_5;
					_ref_recommend_6 = f.comContent.recommend_6;
					_ref_recommend_10 = f.comContent.recommend_10;
					_ref_recommend_11 = f.comContent.recommend_11;
					_ref_recommend_12 = f.comContent.recommend_12;
					_ref_recommend_13 = f.comContent.recommend_13;
					_ref_recommend_14 = f.comContent.recommend_14;
					_ref_recommend_19 = f.comContent.recommend_19;
					_ref_recommend_20 = f.comContent.recommend_20;
					_ref_recommend_21 = f.comContent.recommend_21;
					_ref_recommend_22 = f.comContent.recommend_22;
					_ref_recommend_23 = f.comContent.recommend_23;
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
		if(isEdit == "Y"){
			_zd="N";
		}
		//SurveyBuild._readonly = true;
		/*推荐信信息从推荐人表中获取*/
		/*称呼*/
		if(_ref_recommend_18!=""){
			child[i].recommend_18.value = _ref_recommend_18;
		}
		/*name*/
		if(_ref_recommend_1!=""){
			child[i].recommend_1.value = _ref_recommend_1;
		}
		/*gname*/
		if(_ref_recommend_17!=""){
			child[i].recommend_17.value = _ref_recommend_17;
		}
		/*公司*/
		if(_ref_recommend_2!=""){
			child[i].recommend_2.value = _ref_recommend_2;
		}
		/*职务*/
		if(_ref_recommend_3!=""){
			child[i].recommend_3.value = _ref_recommend_3;
		}
		/*性别*/
		if(_ref_recommend_15!=""){
			child[i].recommend_15.value = _ref_recommend_15;
		}
		/*区号*/
		if(_ref_recommend_16!=""){
			child[i].recommend_16.value = _ref_recommend_16;
		}
		/*手机*/
		if(_ref_recommend_4!=""){
			child[i].recommend_4.value = _ref_recommend_4;
		}
		/*邮箱*/
		if(_ref_recommend_5!=""){
			child[i].recommend_5.value = _ref_recommend_5;
		}
		/*关系*/
		if(_ref_recommend_6!=""){
			child[i].recommend_6.value = _ref_recommend_6;
		}
		/*备用*/
		if(_ref_recommend_10!=""){
			child[i].recommend_10.value = _ref_recommend_10;
		}
		if(_ref_recommend_11!=""){
			child[i].recommend_11.value = _ref_recommend_11;
		}
		if(_ref_recommend_12!=""){
			child[i].recommend_12.value = _ref_recommend_12;
		}
		if(_ref_recommend_13!=""){
			child[i].recommend_13.value = _ref_recommend_13;
		}
		if(_ref_recommend_14!=""){
			child[i].recommend_14.value = _ref_recommend_14;
		}
		if(_ref_recommend_19!=""){
			child[i].recommend_19.value = _ref_recommend_19;
		}
		if(_ref_recommend_20!=""){
			child[i].recommend_20.value = _ref_recommend_20;
		}
		if(_ref_recommend_21!=""){
			child[i].recommend_21.value = _ref_recommend_21;
		}
		if(_ref_recommend_22!=""){
			child[i].recommend_22.value = _ref_recommend_22;
		}
		if(_ref_recommend_23!=""){
			child[i].recommend_23.value = _ref_recommend_23;
		}
		works += '<div id="main_inner_content_para'+i+'" class="main_inner_content_para">';
		
		works += '<div class="mainright-title">';
		works += '<span class="title-line"></span>' + MsgSet["REFFER"] + ' ' +rownum+ ' :' + data.title + '</div>';
		
		works += '<div class="mainright-box pos-rela">';
		if(SurveyBuild._readonly!=true){
			if(rownum > data.defaultLines){
				if(deleteFlag == true){
					works += '<div class="btn-addcon">';
					works += '	<a href="javascript:void(0);" onclick="SurveyBuild.deleteTjx(this);" id="tjx_delete_' + i + '">';
					works += '		<div class="input-delbtn">' + MsgSet["DEL"] + '&nbsp;&nbsp;<span class="input-btn-icon"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/add-delete.png"></span></div>';
					works += '	</a>';
					works += '</div>';
				}else{
					works += '<div class="btn-addcon">';
					works += '	<a href="javascript:void(0);" style="display:none" onclick="SurveyBuild.deleteTjx(this);" id="tjx_delete_' + i + '">';
					works += '		<div class="input-delbtn">' + MsgSet["DEL"] + '&nbsp;&nbsp;<span class="input-btn-icon"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/add-delete.png"></span></div>';
					works += '	</a>';
					works += '</div>';
				}
			}
		}
		
		var _readOnlyRadio = "N";
		if(SurveyBuild._readonly||_zd=="Y"||_zd=="Z"){
			_readOnlyRadio = "Y";
		}
	
		/*任何情况下都可以修改推荐信*/	
		var tjxEditMode = false;
		
		//称呼-开始
        if(tjxEditMode){
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
            works += '<div class="input-list" ' + (child[i].recommend_18["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
            works += '  <div class="input-list-info-readonly left">' + '<span class="red">*</span>' + child[i].recommend_18["itemName"] + '：</div>';
            works += '	<div class="input-list-wz-readonly left">' + desc + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>'
        }else {
            //编辑模式
            works += '<div class="input-list" ' + (child[i].recommend_18["useby"] == "Y" ? "style='display:block'" : "style='display:none'") + '>';
            works += '<div class="input-list-info left">' + '<span class="red">*</span>' + child[i].recommend_18["itemName"] + '：</div>';
            works += '<div class="input-list-text left input-edu-select">';
            /*works += '  	<div class="main_inner_content_info_right_option_251px ' + ((_zd == "Y" || _zd == "Z") ? "bmbtjxtitle" : "") + '" style="margin-right:5px"></div>';*/
            works += '      	<select name="' + data.itemId + child[i].recommend_18["itemId"] + '" class="chosen-select" id="' + data.itemId + child[i].recommend_18["itemId"] + '" style="width:100%;" title="' + child[i].recommend_18["title"] + '">';
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
            works += '<div class="input-list-suffix left">';
            works += '   <div id="' + data.itemId + child[i].recommend_18["itemId"] + 'Tip" class="onShow" style="margin: 0px; padding: 0px; background: transparent;">';
            works += '          	<div class="onShow"></div>';
            works += '   </div>';
            works += '</div>';
			works += '<div class="clear"></div>';
            works += '</div>'
			
			if(child[i].recommend_18["useby"] == "Y"){
			works += '<div class="input-list-blank">';
			works += '<div class="input-list-info-blank left"><span class="red"></span></div>';
			works += '<div class="input-list-wz left"><span class="blue">' + MsgSet["None-blank"] + '</span></div>';
			works += '<div class="input-list-suffix left"></div>';
			works += '<div class="clear"></div>';
			works += '</div>';
		}
        }
		
		//称呼-结束

		//姓氏-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_1["itemName"] +'：</div>';
            works += '<div class="input-list-wz-readonly left">' + child[i].recommend_1["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_1["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_1["itemName"] +'：</div>';
            works += '<div class="input-list-text left"><input type="text" title="'+ child[i].recommend_1["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_1["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_1["itemId"] +'" value="' + child[i].recommend_1["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '	<div id="' + data.itemId + child[i].recommend_1["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '		<div class="onShow">&nbsp;</div>';
            works += '	</div>';
            works += '</div>';
			works += '<div class="clear"></div>';
            works += '</div>';
        }
		//姓氏-结束
		
		//名字-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_17["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_17["itemName"] +'：</div>';
            works += '<div class="input-list-wz-readonly left">' + child[i].recommend_17["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_17["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_17["itemName"] +'：</div>';
            works += '<div class="input-list-text left"><input type="text" title="'+ child[i].recommend_17["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_17["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_17["itemId"] +'" value="' + child[i].recommend_17["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '	<div id="' + data.itemId + child[i].recommend_17["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '		<div class="onShow">&nbsp;</div>';
            works += '	</div>';
            works += '</div>';
			works += '<div class="clear"></div>';
            works += '</div>';
        }
		//姓名-结束
		
		//单位-开始
        if(tjxEditMode){
            works += '<div class="input-list" '+(child[i].recommend_2["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_2["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_2["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            works += '<div class="input-list" '+(child[i].recommend_2["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_2["itemName"] +'：</div>';
            works += '<div class="input-list-text left"><input type="text" title="'+ child[i].recommend_2["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_2["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_2["itemId"] +'" value="' + child[i].recommend_2["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '	<div id="' + data.itemId + child[i].recommend_2["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '		<div class="onShow">&nbsp;</div>';
            works += '	</div>';
            works += '</div>';
			works += '<div class="clear"></div>';
            works += '</div>';
        }
		//单位-结束

		//职务-开始
        if(tjxEditMode){
            works += '<div class="input-list" '+(child[i].recommend_3["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_3["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_3["value"] +  '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            works += '<div class="input-list" '+(child[i].recommend_3["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_3["itemName"] +'：</div>';
            works += '<div class="input-list-text left"><input type="text" title="'+ child[i].recommend_3["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_3["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_3["itemId"] +'" value="' + child[i].recommend_3["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '	<div id="' + data.itemId + child[i].recommend_3["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '		<div class="onShow">&nbsp;</div>';
            works += '	</div>';
            works += '</div>';
			works += '<div class="clear"></div>';
            works += '</div>';
        }
		//职务-结束

		//手机-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_4["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">';
            works += (child[i].recommend_16["value"] ? "(" : "") + child[i].recommend_16["value"] + (child[i].recommend_16["value"] ? ")&nbsp;" : "") + child[i].recommend_4["value"];
            works += '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_4["itemName"] +'：</div>';
            works += '<div class="input-list-text left">';
			if(child[i].recommend_16["useby"] == "Y"){
				works += '		<input type="text" style="width:30%" title="'+ child[i].recommend_16["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_16["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_16["itemId"] +'" value="' + child[i].recommend_16["value"] + '">';
				works += '		<input type="text" style="width:68%" title="'+ child[i].recommend_4["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_4["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_4["itemId"] +'" value="' + child[i].recommend_4["value"] + '">';
				works += '</div>';
			}else{
				works += '		<input type="text" title="'+ child[i].recommend_4["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_4["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_4["itemId"] +'" value="' + child[i].recommend_4["value"] + '">';
				works += '</div>';
			}
            works += '<div class="input-list-suffix left">';
			/*works += '      <span>&nbsp;&nbsp;&nbsp;'+MsgSet["REF_PHONE_EG"]+'</span>';*/
            works += '	<div id="' + data.itemId + child[i].recommend_4["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '		<div class="onShow">&nbsp;</div>';
            works += '	</div>';
            works += '</div>';
			works += '<div class="clear"></div>';
            works += '</div>';
        }
		//手机-结束

		//邮箱-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_5["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_5["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_5["itemName"] +'：</div>';
            works += '<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_5["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_5["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_5["itemId"] +'" value="' + child[i].recommend_5["value"] + '">';
			works += '</div>';
            works += '<div class="input-list-suffix left">';
            works += '	<div id="' + data.itemId + child[i].recommend_5["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '		<div class="onShow">&nbsp;</div>';
            works += '	</div>';
            works += '</div>';
			works += '<div class="clear"></div>';
            works += '</div>';
        }
		//邮箱-结束
		
		//性别-开始
		works += '<div class="input-list" '+(child[i].recommend_15["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
		works += '<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_15["itemName"] +'：</div>';
		works += '<div class="margart8 input-list-textwrap left">';
		works += '	<ul>'; 
		works += '	<li>'; 
		works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '" class="radio-btn '+(child[i].recommend_15["option"]["MAN"]["checked"] == "Y" ? "checkedRadio" : "")+'" onclick="SurveyBuild.clickOnRadio(this);" id="' + data.itemId + child[i].recommend_15["itemId"] + '_M">';
        works += '<i><input type="radio"' + (child[i].recommend_15["option"]["MAN"]["checked"] == "Y" ? "checked='checked'": "")+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_15["title"] +'" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="M"></i>';

		works += '</div><span style="margin-left:3px;">' + MsgSet["SEX_M"]+'</span>&nbsp;&nbsp;&nbsp;&nbsp;';

		works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '" class="radio-btn '+(child[i].recommend_15["option"]["WOMAN"]["checked"] == "Y" ? "checkedRadio" : "")+'" onclick="SurveyBuild.clickOnRadio(this);" id="' + data.itemId + child[i].recommend_15["itemId"] + '_F">';
        works += '<i><input type="radio" ' + (child[i].recommend_15["option"]["WOMAN"]["checked"] == "Y" ? "checked='checked'": "") + ((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_15["title"] +'" name="' + data.itemId + child[i].recommend_15["itemId"] +'" value="F"></i>';
		works += '</div><span style="margin-left:3px;">'+MsgSet["SEX_F"]+'</span>';
		works += '	</li>';
		works += '<div class="clear"></div></ul>';
        works += '<input type="hidden" style="width:10px;" id="' + data.itemId + child[i].recommend_15["itemId"] + '" value="' + child[i].recommend_15["value"] + '">';
		works += '</div>';
		works += '<div class="input-list-suffix left">';
		works += '	<div id="' + data.itemId + child[i].recommend_15["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
		works += '		<div class="onShow">&nbsp;</div>';
		works += '	</div>';
		works += '</div>';
		works += '<div class="clear"></div>';
		works += '</div>';
		//性别-结束

		//申请人关系-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_6["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_6["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_6["itemName"] +'：</div>';
            works += '	<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_6["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_6["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_6["itemId"] +'" value="' + child[i].recommend_6["value"] + '"></div>';
			works += '<div class="input-list-suffix left">';
            works += '	<div id="' + data.itemId + child[i].recommend_6["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '		<div class="onShow">&nbsp;</div>';
            works += '	</div>';
            works += '</div>';
			works += '<div class="clear"></div>';
            works += '</div>';
        }
		//申请人关系-结束

		//备用字段一-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_10["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_10["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_10["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_10["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_10["itemName"] +'：</div>';
            works += '	<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_10["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_10["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_10["itemId"] +'" value="' + child[i].recommend_10["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '		<div id="' + data.itemId + child[i].recommend_10["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '			<div class="onShow">&nbsp;</div>';
            works += '		</div>';
            works += '</div>';
            works += '<div class="clear"></div>';
            works += '</div>';
        }
		//备用字段一-结束

		//备用字段二-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_11["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_11["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_11["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_11["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_11["itemName"] +'：</div>';
            works += '	<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_11["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_11["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_11["itemId"] +'" value="' + child[i].recommend_11["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '	<div id="' + data.itemId + child[i].recommend_11["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '			<div class="onShow">&nbsp;</div>';
            works += '		</div>';
            works += '	</div>';
            works += '<div class="clear"></div>';
            works += '</div>';
        }
		//备用字段二-结束

		//备用字段三-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_12["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_12["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_12["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_12["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_12["itemName"] +'：</div>';
            works += '	<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_12["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_12["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_12["itemId"] +'" value="' + child[i].recommend_12["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '		<div id="' + data.itemId + child[i].recommend_12["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '			<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '<div class="clear"></div>';
            works += '</div>';
        }
		//备用字段三-结束

		//备用字段四-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_13["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_13["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_13["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_13["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_13["itemName"] +'：</div>';
            works += '	<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_13["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_13["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_13["itemId"] +'" value="' + child[i].recommend_13["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '		<div id="' + data.itemId + child[i].recommend_13["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '			<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '<div class="clear"></div>';
            works += '</div>';
        }
		//备用字段四-结束

		//备用字段五-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_14["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_14["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_14["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_14["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_14["itemName"] +'：</div>';
            works += '	<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_14["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_14["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_14["itemId"] +'" value="' + child[i].recommend_14["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '		<div id="' + data.itemId + child[i].recommend_14["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '			<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '<div class="clear"></div>';
            works += '</div>';
        }
		//备用字段五结束
        
        //备用字段六-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_19["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_19["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_19["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_19["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_19["itemName"] +'：</div>';
            works += '	<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_19["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_19["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_19["itemId"] +'" value="' + child[i].recommend_19["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '		<div id="' + data.itemId + child[i].recommend_19["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '			<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '<div class="clear"></div>';
            works += '</div>';
        }
		//备用字段六结束
        
        //备用字段七-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_20["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_20["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_20["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_20["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_20["itemName"] +'：</div>';
            works += '	<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_20["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_20["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_20["itemId"] +'" value="' + child[i].recommend_20["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '		<div id="' + data.itemId + child[i].recommend_20["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '			<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '<div class="clear"></div>';
            works += '</div>';
        }
		//备用字段七结束
        
        //备用字段八-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_21["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_21["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_21["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_21["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_21["itemName"] +'：</div>';
            works += '	<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_21["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_21["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_21["itemId"] +'" value="' + child[i].recommend_21["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '		<div id="' + data.itemId + child[i].recommend_21["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '			<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '<div class="clear"></div>';
            works += '</div>';
        }
		//备用字段八结束

        //备用字段九-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_22["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_22["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_22["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_22["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_22["itemName"] +'：</div>';
            works += '	<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_22["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_22["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_22["itemId"] +'" value="' + child[i].recommend_22["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '		<div id="' + data.itemId + child[i].recommend_22["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '			<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '<div class="clear"></div>';
            works += '</div>';
        }
		//备用字段九结束
        
        //备用字段十-开始
        if(tjxEditMode){
            //只读模式
            works += '<div class="input-list" '+(child[i].recommend_23["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info-readonly left">'+ '<span class="red">*</span>'+ child[i].recommend_23["itemName"] +'：</div>';
            works += '	<div class="input-list-wz-readonly left">' + child[i].recommend_23["value"] + '</div>';
			works += '	<div class="input-list-suffix left"></div>';
			works += '	<div class="clear"></div>';
            works += '</div>';
        }else{
            //编辑模式
            works += '<div class="input-list" '+(child[i].recommend_23["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
            works += '	<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_23["itemName"] +'：</div>';
            works += '	<div class="input-list-text left">';
            works += '		<input type="text" title="'+ child[i].recommend_23["title"] +'" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_23["itemId"] +'" class="inpu-list-text-enter" name="' + data.itemId + child[i].recommend_23["itemId"] +'" value="' + child[i].recommend_23["value"] + '"></div>';
            works += '<div class="input-list-suffix left">';
            works += '		<div id="' + data.itemId + child[i].recommend_23["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
            works += '			<div class="onShow">&nbsp;</div>';
            works += '			</div>';
            works += '		</div>';
            works += '<div class="clear"></div>';
            works += '</div>';
        }
		//备用字段十结束
        
		//推荐信语言-开始
		if (_qy_zhs=="Y"&&_qy_eng=="Y")
		{
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
			works += '<div class="input-list">';
			works += '<div class="input-list-info left">'+ '<span class="red">*</span>'+ child[i].recommend_7["itemName"] +'：</div>';
			works += '<div class="margart8 input-list-textwrap left">';
			works += '	<ul>'; 
			works += '	<li>'; 
			works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '" class="radio-btn '+(child[i].recommend_7["option"]["ZHS"]["checked"] == "Y" ? "checkedRadio" : "")+'" onclick="SurveyBuild.clickOnRadio(this);" id="' + data.itemId + child[i].recommend_7["itemId"] + '_C">';
			works += '<i><input type="radio"' + (child[i].recommend_7["option"]["ZHS"]["checked"] == "Y" ? "checked='checked'": "") + ((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_7["title"] +'" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="C"></i>';

			works += '</div><span style="margin-left:3px;">'+MsgSet["LANGUAGE_C"]+'</span>&nbsp;&nbsp;&nbsp;&nbsp;';
			
			works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '" class="radio-btn '+(child[i].recommend_7["option"]["ENG"]["checked"] == "Y" ? "checkedRadio" : "")+'" onclick="SurveyBuild.clickOnRadio(this);" id="' + data.itemId + child[i].recommend_7["itemId"] + '_E">';
			works += '<i><input type="radio"' + (child[i].recommend_7["option"]["ENG"]["checked"] == "Y" ? "checked='checked'": "") + ((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_7["title"] +'" name="' + data.itemId + child[i].recommend_7["itemId"] +'" value="E"></i>';

			works += '</div><span style="margin-left:3px;">'+MsgSet["LANGUAGE_E"]+'</span>';
			works += '	</li>';
			works += '<div class="clear"></div></ul>';
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="' + tjx_language + '">';
			works += '<div style="margin-top: -40px; margin-left: 110px"><span id="' + data.itemId + child[i].recommend_7["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow"><div class="onShow"></div></span></div>';
			works += '</div>';
			works += '<div class="input-list-suffix left">';
			works += '	<div id="' + data.itemId + child[i].recommend_7["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
			works += '		<div class="onShow">&nbsp;</div>';
			works += '	</div>';
			works += '</div>';
			works += '<div class="clear"></div>';
			works += '</div>';
		}else if (_qy_zhs=="Y"&&_qy_eng!="Y")
		{
			child[i].recommend_7["option"]["ZHS"]["checked"] = "Y";
			child[i].recommend_7["option"]["ENG"]["checked"] = "N";
			works += '<div class="input-list" style="display:none">';
			works += '<div class="input-list-info left">'+ child[i].recommend_7["itemName"] +'：</div>';
			works += '<div class="input-list-text left">';
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="C">';
			works += '</div>';
			works += '</div>';
		}else if (_qy_zhs!="Y"&&_qy_eng=="Y")
		{
			child[i].recommend_7["option"]["ZHS"]["checked"] = "N";
			child[i].recommend_7["option"]["ENG"]["checked"] = "Y";
			works += '<div class="input-list" style="display:none">';
			works += '<div class="input-list-info left">'+ child[i].recommend_7["itemName"] +'：</div>';
			works += '<div class="input-list-text left">';
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="E">';
			works += '</div>';
			works += '</div>';
		}
		//推荐信语言-结束

		//推荐信类型-开始
		// itemLx=L  2个都可以
		if (data.itemLx=="L"){
			
			works += '<div class="input-list">';
			works += '<div class="input-list-info left">'+ child[i].recommend_8["itemName"] +'：</div>';
			works += '<div class="margart8 input-list-textwrap left">';
			
			
			//如果发送邮件被选择了  recommend_8.value =S 
			if(child[i].recommend_8["option"]["SEND"]["checked"] == "Y"){
				child[i].recommend_8["option"]["SEND"]["checked"] = "Y";
				child[i].recommend_8["option"]["UPLOAD"]["checked"] = "N";
				child[i].recommend_8["value"] = "S";
			}
			//如果 上传附件被选择了   recommend_8.value =U
			else if(child[i].recommend_8["option"]["UPLOAD"]["checked"] == "Y")
			{
				child[i].recommend_8["option"]["SEND"]["checked"] = "N";
				child[i].recommend_8["option"]["UPLOAD"]["checked"] = "Y";
				child[i].recommend_8["value"] = "U";
			}
			//默认 发送邮件
			else{
				child[i].recommend_8["option"]["SEND"]["checked"] = "Y";
				child[i].recommend_8["option"]["UPLOAD"]["checked"] = "N";
				child[i].recommend_8["value"] = "S";
			}
			works += '	<ul>'; 
			works += '	<li>'; 
			works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '" class="radio-btn '+(child[i].recommend_8["option"]["SEND"]["checked"] == "Y" ? "checkedRadio" : "")+'" onclick="SurveyBuild.clickOnRadio(this);" id="' + data.itemId + child[i].recommend_8["itemId"] + '_S">';
			works += '<i><input ' + (child[i].recommend_8["option"]["SEND"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["title"] +'" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="S"></i>';
			works += '</div><span style="margin-left:3px;">' + MsgSet["Send_mail"]+'</span>&nbsp;&nbsp;&nbsp;&nbsp;';
			works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '" class="radio-btn '+(child[i].recommend_8["option"]["UPLOAD"]["checked"] == "Y" ? "checkedRadio" : "")+'" onclick="SurveyBuild.clickOnRadio(this);" id="' + data.itemId + child[i].recommend_8["itemId"] + '_U">';
			works += '<i><input ' + (child[i].recommend_8["option"]["UPLOAD"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["title"] +'" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="U" ></i>';
			works += '</div><span style="margin-left:3px;">' + MsgSet["Upload"] + '</sapn>';
			works += '	</li>';
			works += '<div class="clear"></div></ul>';
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_8["itemId"] + '" value="' + child[i].recommend_8["value"] + '">';
			works += '</div>';
			works += '<div class="input-list-suffix left">';
			works += '	<div id="' + data.itemId + child[i].recommend_8["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
			works += '		<div class="onShow">&nbsp;</div>';
			works += '	</div>';
			works += '</div>';
			works += '<div class="clear"></div>';
			works += '</div>';
		}
		// 如果 设置 itemLx=F  发送邮件
		else if(data.itemLx == "F"){
			child[i].recommend_8["option"]["SEND"]["checked"] = "Y";
			child[i].recommend_8["option"]["UPLOAD"]["checked"] = "N";
            works += '<input type="hidden" id="' + data.itemId + child[i].recommend_8["itemId"] + '" value="S">';
        }
		// 如果 itemLx=S  上传附件
		else{
			child[i].recommend_8["option"]["SEND"]["checked"] = "N";
			child[i].recommend_8["option"]["UPLOAD"]["checked"] = "Y";
            works += '<input type="hidden" id="' + data.itemId + child[i].recommend_8["itemId"] + '" value="U">';
        }
		//推荐信类型-结束
		
		//上传附件
		if ((data.itemLx == "L" && child[i].recommend_8["value"] == "U") || data.itemLx == "S"){
		
			works += '<div class="input-list-blank" style="display:block" id="Tjxfj_show_'+i+'">';
			if(SurveyBuild._readonly!=true){
				works += '	<div class="input-list-info left">'+child[i].recommend_9["itemName"]+'：</div>';
				works += '	<div class="input-list-texttemplate left">';
				works += '		<div class="filebtn">';
				works += '			<div class="template-btn"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" />&nbsp;&nbsp;' + MsgSet["UPLOAD_BTN_MSG"] + '</div>';
				works += '			<input id="'+data.itemId+child[i].recommend_9["itemId"]+'File" class="filebtn-orgtext" type="file" name="'+data.itemId+child[i].recommend_9["itemId"]+'File" style="width:125px;" onchange=SurveyBuild.TjxUpload(this,"recommend_9",'+i+')>';
				works += '		</div>';
			}else{
				works += '	<div class="input-list-info left">'+child[i].recommend_9["itemName"]+'：</div>';
				works += '	<div class="input-list-text left">';
			}

			works += '<div class="input-list-suffix left">';
			works += '</div>';

			/*附件显示 开始*/
			if(SurveyBuild._readonly!=true){
				works += '		<div id="'+data.itemId+i+'_AttList" class="input-list-upload-con">';
			}else{
				works += '		<div id="'+data.itemId+i+'_AttList">';
			}
			if (child[i].recommend_9["viewFileName"]!="")
			{
				if(SurveyBuild._readonly!=true){
					works += '<div class="input-list-uploadcon-list">';
					works += '	<div class="input-list-uploadcon-listl left">';
					works += '			<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.TjxdownLoad(this,"recommend_9",'+i+') file-index="'+i+'">'+child[i].recommend_9["viewFileName"]+'</a>';
					works += '	</div>';
					
					works += '	<div class="input-list-uploadcon-listr left" style="display: block;line-height:46px;" onclick=SurveyBuild.Tjxdelete(this,"recommend_9",'+i+')><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
					
					works += '</div>';
				}else{
					works += '<div class="input-list-uploadcon-list">';
					works += '	<div class="input-list-uploadcon-listl left">';
					works += '	<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.TjxdownLoad(this,"recommend_9",'+i+') file-index="'+i+'">'+child[i].recommend_9["viewFileName"]+'</a>';
					works += '</div>';
					works += '</div>';
				}
			}
			works += '	<div class="clear"></div>';
			works += '		</div>';
			
			/*附件显示 结束*/
			works += '	</div>';
			works += '	<input id="'+data.itemId+child[i].recommend_9["itemId"]+'" type="hidden" name="'+data.itemId+child[i].recommend_9["itemId"]+'" value="'+child[i].recommend_9["value"]+'">';
			works += '	<div class="clear"></div>';
			works += '</div>';
		}else{
			//代码不在网页上显示出来
			works += '<div class="input-list-blank" style="display:none" id="Tjxfj_show_'+i+'">';
			if(SurveyBuild._readonly!=true){
				works += '	<div class="input-list-info left">'+child[i].recommend_9["itemName"]+'：</div>';
				works += '	<div class="input-list-texttemplate left">';
				works += '		<div class="filebtn">';
				works += '			<div class="template-btn"><img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png" />&nbsp;&nbsp;' + MsgSet["UPLOAD_BTN_MSG"] + '</div>';
				works += '			<input id="'+data.itemId+child[i].recommend_9["itemId"]+'File" class="filebtn-orgtext" type="file" name="'+data.itemId+child[i].recommend_9["itemId"]+'File" style="width:125px;" onchange=SurveyBuild.TjxUpload(this,"recommend_9",'+i+')>';
				works += '		</div>';
			}else{
				works += '	<div class="input-list-info left">'+child[i].recommend_9["itemName"]+'：</div>';
				works += '	<div class="input-list-text left">';
			}
			works += '<div class="input-list-suffix left">';
			works += '</div>';

			/*附件显示 开始*/
			if(SurveyBuild._readonly!=true){
				works += '		<div id="'+data.itemId+i+'_AttList" class="input-list-upload-con">';
			}else{
				works += '		<div id="'+data.itemId+i+'_AttList">';
			}
			if (child[i].recommend_9["viewFileName"]!="")
			{
				if(SurveyBuild._readonly!=true){

					works += '<div class="input-list-uploadcon-list">';
					works += '	<div class="input-list-uploadcon-listl left">';
					works += '		<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.TjxdownLoad(this,"recommend_9",'+i+') file-index="'+i+'">'+child[i].recommend_9["viewFileName"]+'</a>';
					works += '	</div>';
					works += '	<div class="input-list-uploadcon-listr left" style="display: block;line-height:46px;" onclick=SurveyBuild.Tjxdelete(this,"recommend_9",'+i+')><img src="' + TzUniversityContextPath + '/statics/images/appeditor/del.png" title="' + MsgSet["DEL"] + '"/>&nbsp;</div>';
					works += '</div>';
				}else{
					works += '<div class="input-list-uploadcon-list">';
					works += '	<div class="input-list-uploadcon-listl left">';
					works += '			<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.TjxdownLoad(this,"recommend_9",'+i+') file-index="'+i+'">'+child[i].recommend_9["viewFileName"]+'</a>';
					works += '	</div>';
					works += '</div>';
				}
			}
			works += '	<div class="clear"></div>';
			works += '		</div>';
			
			/*附件显示 结束*/
			works += '	</div>';
			works += '	<input id="'+data.itemId+child[i].recommend_9["itemId"]+'" type="hidden" name="'+data.itemId+child[i].recommend_9["itemId"]+'" value="'+child[i].recommend_9["value"]+'">';
			works += '	<div class="clear"></div>';
			works += '</div>';
		}
		//上传附件-结束
		
		
		//发送邮件 -开始
		if ((data.itemLx=="L"&&child[i].recommend_8["value"]!="U")||data.itemLx=="F"){
			if(SurveyBuild.appManager != "Y"){

				//如果不是管理员，一直显示
				works += '<div class="input-list-blank" style="margin: 0 auto;" id="Tjx_SendEmail_'+i+'">';
				works += '<div class="input-list-info left"></div>';
				works += '<div class="input-list-texttemplate" style="width:100%">';
				//发送邮件  如果已经发送，那么不显示
				works += '<div id="sendEmailS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+((_zd=="Y"||_zd=="Z")?"display:none":"")+'">';
				works += '<div id="sendEmail_'+i+'" class="template-btn" style="width:150px">'+MsgSet["Send"]+'</div><a href="#" class="alpha"></a>';
				works += '</div>';
				
				//灰色的发送邮件
				works += '<div id="sendEmailH_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;display:none"">';
				works += '<div  class="template-btn" style="width:150px;background:#999">'+MsgSet["Send"]+'</div>';
				works += '</div>';
				
				//重新发送  ，如果没有发送，那么显示
				works += '<div id="reSendEmailS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+((_zd!="Y")?"display:none":"")+'">';
				works += '<div id="reSendEmail_'+i+'" class="template-btn" style="width:150px">'+MsgSet["Resend"]+'</div><a href="#" class="alpha"></a>';
				works += '</div>';
				
				//灰色的重新发送邮件
				works += '<div id="reSendEmailH_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;display:none"">';
				works += '<div  class="template-btn" style="width:150px;background:#999">'+MsgSet["Resend"]+'</div>';
				works += '</div>';
				
				//发送给自己
				works += '<div id="sendEmailToMeS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+((_zd=="Y"||_zd=="Z")?"display:none":"")+'">';
				works += '<div id="sendEmailToMe_'+i+'" class="template-btn" style="width:150px">'+MsgSet["SendToMe"]+'</div><a href="#" class="alpha"></a>';
				works += '</div>';
				
				//灰色的发送给自己
				works += '<div id="sendEmailToMeH_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;display:none"">';
				works += '<div  class="template-btn" style="width:150px;background:#999">'+MsgSet["SendToMe"]+'</div>';
				works += '</div>';
				
				//重新发送给自己
				works += '<div id="reSendEmailToMeS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+((_zd!="Y")?"display:none":"")+'">';
				works += '<div id="reSendEmailToMe_'+i+'" class="template-btn" style="width:150px">'+MsgSet["ResendToMe"]+'</div><a href="#" class="alpha"></a>';
				works += '</div>';
				
				//灰色的重新发送给自己
				works += '<div id="reSendEmailToMeH_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;display:none"">';
				works += '<div  class="template-btn" style="width:150px;background:#999">'+MsgSet["ResendToMe"]+'</div>';
				works += '</div>';
				
				/*更换推荐人一直显示*/
				if(SurveyBuild._readonly){
					works += '<div id="changeRecS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+'">';
				}else{
					works += '<div id="changeRecS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+((_zd!="Y"&&_zd!="Z")?"display:none":"")+'">';
				}
				
				works += '<div id="changeRec_'+i+'" class="template-btn" style="width:150px">'+MsgSet["RepRecom"]+'</div><a href="#" class="alpha"></a>';
				works += '</div>';
				
				works += '<div id="changeRecH_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;display:none"">';
				works += '<div  class="template-btn" style="width:150px;background:#999">'+MsgSet["RepRecom"]+'</div>';
				works += '</div>';
			
				works += '<div class="input-list-suffix left"></div>';
				works += '<div class="clear"></div>';
				works += '</div>';
				works += '</div>';
			}
			//推荐信状态

			works += '<div class="input-list-blank" style="padding-left:6px;" id="Tjxzt_'+i+'">';
			works += '<div class="input-list-info left"></div>';
			works += '<div><input type="hidden" id="tjxzt_info_'+i+'" value="'+_zd+'"></div>';
			works += '<div class="input-list-wz left" id="tjxzt_desc_'+i+'">'+MsgSet["ReLeSt"]+': <span class="blue">'+_tjx_zt+'</span>';
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
			works += '<div class="input-list-suffix left"></div>';
			works += '<div class="clear"></div>';
			works += '</div>';		
		}else{
			if(SurveyBuild._readonly!=true){
				works += '<div class="input-list-blank" style="margin: 0 auto;display:none" id="-'+i+'">';
				works += '<div class="input-list-info left" style="padding-left:6px;"></div>';
				works += '<div class="input-list-texttemplate" style="width:100%">';
			
				//发送邮件
				works += '<div id="sendEmailS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+((_zd=="Y"||_zd=="Z")?"display:none":"")+'">';
				works += '<div id="sendEmail_'+i+'" class="template-btn" style="width:150px">'+MsgSet["Send"]+'</div><a href="#" class="alpha"></a>';
				works += '</div>';
				//灰色的发送邮件
				works += '<div id="sendEmailH_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;display:none"">';
				works += '<div  class="template-btn" style="width:150px;background:#999">'+MsgSet["Send"]+'</div>';
				works += '</div>';
				//重新发送
				works += '<div id="reSendEmailS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+((_zd!="Y"&&_zd!="Z")?"display:none":"")+'">';
				works += '<div id="reSendEmail_'+i+'" class="template-btn" style="width:150px">'+MsgSet["Resend"]+'</div><a href="#" class="alpha"></a>';
				works += '</div>';
				//灰色的重新发送邮件
				works += '<div id="reSendEmailH_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;display:none"">';
				works += '<div  class="template-btn" style="width:150px;background:#999">'+MsgSet["Resend"]+'</div>';
				works += '</div>';
				
				//发送给自己
				works += '<div id="sendEmailToMeS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+((_zd=="Y"||_zd=="Z")?"display:none":"")+'">';
				works += '<div id="sendEmailToMe_'+i+'" class="template-btn" style="width:150px">'+MsgSet["SendToMe"]+'</div><a href="#" class="alpha"></a>';
				works += '</div>';
				//灰色的发送给自己
				works += '<div id="sendEmailToMeH_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;display:none"">';
				works += '<div  class="template-btn" style="width:150px;background:#999">'+MsgSet["SendToMe"]+'</div>';
				works += '</div>';
				
				//重新发送给自己
				works += '<div id="reSendEmailToMeS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+((_zd!="Y")?"display:none":"")+'">';
				works += '<div id="reSendEmailToMe_'+i+'" class="template-btn" style="width:150px">'+MsgSet["ResendToMe"]+'</div><a href="#" class="alpha"></a>';
				works += '</div>';
				
				//灰色的重新发送给自己
				works += '<div id="reSendEmailToMeH_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;display:none"">';
				works += '<div  class="template-btn" style="width:150px;background:#999">'+MsgSet["ResendToMe"]+'</div>';
				works += '</div>';
				
				//更换推荐人
				/*
				works += '<div id="changeRecS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+((_zd!="Y"&&_zd!="Z")?"display:none":"")+'">';
				works += '<div id="changeRec_'+i+'" class="template-btn" style="width:150px">'+MsgSet["RepRecom"]+'</div><a href="#" class="alpha"></a>';
				works += '</div>';
				*/
				
				/*更换推荐人一直显示*/
				
				works += '<div id="changeRecS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+((_zd!="Y"&&_zd!="Z") ?"display:none":"")+'">';
				works += '<div id="changeRec_'+i+'" class="template-btn" style="width:150px">'+MsgSet["RepRecom"]+'</div><a href="#" class="alpha"></a>';
				works += '</div>';
				
				//灰色的更换推荐人
				works += '<div id="changeRecH_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;display:none"">';
				works += '<div  class="template-btn" style="width:150px;background:#999">'+MsgSet["RepRecom"]+'</div>';
				works += '</div>';
			
				works += '</div>';
				works += '<div class="input-list-suffix left"></div>';
				works += '<div class="clear"></div>';
				works += '</div>';
			}
			//推荐信状态
			works += '<div class="input-list-blank" id="Tjxzt_'+i+'" style="padding-left:6px;display:none">';
			works += '<div class="input-list-info left"></div>';
			works += '<div><input type="hidden" id="tjxzt_info_'+i+'" value="'+_zd+'"></div>';
			works += '<div class="input-list-wz left" id="tjxzt_desc_'+i+'">'+MsgSet["ReLeSt"]+': <span class="blue">'+_tjx_zt+'</span>';
			
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
			works += '<div class="input-list-suffix left"></div>';
			works += '<div class="clear"></div>';
			works += '</div>';
		}
		//发送邮件 -结束
		
		/*保存推荐信信息*/
		works += '<div style="display:none">';
		works += '<div id="saveRec_'+i+'" class="bt_blue" style="width:125px">保存</div><a href="#" class="alpha"></a>';
		works += '</div>';

		works += '<div><input type="hidden" id="yincang_tx" value="'+data.toSendE+'"></div>';
		works += '<div><input type="hidden" id="max_tjx_ts" value="'+data.maxLines+'"></div>';
		
		works += '</div>';
		works += '</div>';
		
		
        return works; 
    },
    
    //手机
    _getHtmlTwo: function(data,rownum,showtype){
    	
		if (SurveyBuild.appInsId=="0"&&data.children.length<data.defaultLines){
			SurveyBuild.showTjx(this,data.instanceId);
			
		}else if (data.children.length<data.defaultLines){
			SurveyBuild.showTjx(this,data.instanceId);
		}
		
		/*已填写的数据的行数*/
		var len = data.children.length;
		var works = "",child = [];
		//console.log("data:"+data);
		child = data.children;
	   //	console.log(child);
		var i=rownum-1;
		
		
		//加载推荐信称呼(AppFormListClsServiceImpl)
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
    	if(_tjrXh == ""){	
    		if(showtype=='QUERY'){
    			
    			_tjrXh = rownum;
    			child[i].recommend_99["value"] = rownum;
    			data.tjxMaxLinesXh = child[len-1].recommend_99["value"];
    		}else{
    			child[i].recommend_99["value"] = parseInt(data.tjxMaxLinesXh) + 1;
    			data.tjxMaxLinesXh = child[i].recommend_99["value"];
    			_tjrXh = data.tjxMaxLinesXh;
    		}
    		
    	}else{
    		
    		data.tjxMaxLinesXh = child[len-1].recommend_99["value"];
    	}
    	
    	
    	//集中定义推荐信请求字段
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
		var _ref_recommend_1 = "";
		var _ref_recommend_2 = "";
		var _ref_recommend_3 = "";
		var _ref_recommend_4 = "";
		var _ref_recommend_5 = "";
		var _ref_recommend_6 = "";
		var _ref_recommend_7 = "";
		var _ref_recommend_8 = "";
		var _ref_recommend_9 = "";
		var _ref_recommend_10 = "";
		var _ref_recommend_11 = "";
		var _ref_recommend_12 = "";
		var _ref_recommend_13 = "";
		var _ref_recommend_14 = "";
		var _ref_recommend_15 = "";
		var _ref_recommend_16= "";
		var _ref_recommend_17 = "";
		var _ref_recommend_18 = "";
		var _ref_recommend_19 = "";
		var _ref_recommend_20 = "";
		var _ref_recommend_21 = "";
		var _ref_recommend_22 = "";
		var _ref_recommend_23 = "";
		
		//请求推荐信息(TzIscriptClsServiceImpl)
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
					_ref_recommend_18 = f.comContent.recommend_18;
					_ref_recommend_1 = f.comContent.recommend_1;
					_ref_recommend_17 = f.comContent.recommend_17;
					_ref_recommend_2 = f.comContent.recommend_2;
					_ref_recommend_3 = f.comContent.recommend_3;
					_ref_recommend_15 = f.comContent.recommend_15;
					_ref_recommend_16 = f.comContent.recommend_16;
					_ref_recommend_4 = f.comContent.recommend_4;
					_ref_recommend_5 = f.comContent.recommend_5;
					_ref_recommend_6 = f.comContent.recommend_6;
					_ref_recommend_10 = f.comContent.recommend_10;
					_ref_recommend_11 = f.comContent.recommend_11;
					_ref_recommend_12 = f.comContent.recommend_12;
					_ref_recommend_13 = f.comContent.recommend_13;
					_ref_recommend_14 = f.comContent.recommend_14;
					_ref_recommend_19 = f.comContent.recommend_19;
					_ref_recommend_20 = f.comContent.recommend_20;
					_ref_recommend_21 = f.comContent.recommend_21;
					_ref_recommend_22 = f.comContent.recommend_22;
					_ref_recommend_23 = f.comContent.recommend_23;
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
		if(isEdit == "Y"){
			_zd="N";
		}
		
		/*推荐信信息从推荐人表中获取*/
		/*称呼*/
		if(_ref_recommend_18!=""){
			child[i].recommend_18.value = _ref_recommend_18;
		}
		//name
		if(_ref_recommend_1!=""){
			child[i].recommend_1.value = _ref_recommend_1;
			
		}
		//gname
		if(_ref_recommend_17!=""){
			child[i].recommend_17.value = _ref_recommend_17;
		}
		//公司
		if(_ref_recommend_2!=""){
			child[i].recommend_2.value = _ref_recommend_2;
		}
		//职务
		if(_ref_recommend_3!=""){
			child[i].recommend_3.value = _ref_recommend_3;
		}
		//性别
		if(_ref_recommend_15!=""){
			child[i].recommend_15.value = _ref_recommend_15;
		}
		//区号
		if(_ref_recommend_16!=""){
			child[i].recommend_16.value = _ref_recommend_16;
		}
		//手机
		if(_ref_recommend_4!=""){
			child[i].recommend_4.value = _ref_recommend_4;
		}
		//邮箱
		if(_ref_recommend_5!=""){
			child[i].recommend_5.value = _ref_recommend_5;
		}
		//关系
		if(_ref_recommend_6!=""){
			child[i].recommend_6.value = _ref_recommend_6;
		}
		//备用
		if(_ref_recommend_10!=""){
			child[i].recommend_10.value = _ref_recommend_10;
		}
		//备用
		if(_ref_recommend_11!=""){
			child[i].recommend_11.value = _ref_recommend_11;
		}
		//备用
		if(_ref_recommend_12!=""){
			child[i].recommend_12.value = _ref_recommend_12;
		}
		//备用
		if(_ref_recommend_13!=""){
			child[i].recommend_13.value = _ref_recommend_13;
		}
		//备用
		if(_ref_recommend_14!=""){
			child[i].recommend_14.value = _ref_recommend_14;
		}
		//备用
		if(_ref_recommend_19!=""){
			child[i].recommend_19.value = _ref_recommend_19;
		}
		//备用
		if(_ref_recommend_20!=""){
			child[i].recommend_20.value = _ref_recommend_20;
		}
		//备用
		if(_ref_recommend_21!=""){
			child[i].recommend_21.value = _ref_recommend_21;
		}
		//备用
		if(_ref_recommend_22!=""){
			child[i].recommend_22.value = _ref_recommend_22;
		}
		//备用
		if(_ref_recommend_23!=""){
			child[i].recommend_23.value = _ref_recommend_23;
		}
		
		works += '<div id="main_inner_content_para' + i + '" class="next_record">';
		
		//非只读模式
		console.log("SurveyBuild._readonly"+SurveyBuild._readonly);
		if(SurveyBuild._readonly != true){
			if(rownum > data.defaultLines){
				
				if(deleteFlag == true){
					
					works += '<div onclick="SurveyBuild.deleteTjx(this);" class="btn_delete" id="tjx_delete_' + i + '">' + MsgSet["DEL"] + '<img src="' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png"></div>';
				}else{
					
					works += '<div onclick="SurveyBuild.deleteTjx(this);" class="btn_delete" id="tjx_delete_' + i + '" style="display:none">' + MsgSet["DEL"] + '<img src="' + TzUniversityContextPath + '/statics/images/appeditor/m/de.png"></div>';

				}
			}
		}
		works += '<div class="se_tit1">' + MsgSet["REFFER"] + ' ' +rownum+ ' :' + data.title + '</div>';
		var _readOnlyRadio = "N";
		if(SurveyBuild._readonly||_zd=="Y"||_zd=="Z"){
			_readOnlyRadio = "Y";
		}
		
		/*任何情况下都可以修改推荐信*/	
		var tjxEditMode = false;
		
		//称呼
        //works += '<div class="w_96">';
		works += '<div class="index_body" style="margin-top: 25px;">';
		works += '<div class="mainright-box pos-rela">';
		
        works += '<div class="item" '+(child[i].recommend_18["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_18["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<select name="' + data.itemId + child[i].recommend_18["itemId"] + '" class="select1" id="' + data.itemId + child[i].recommend_18["itemId"] + '" title="' + child[i].recommend_18["title"] + '">';
        works += '<option value="">' + MsgSet["PLEASE_SELECT"] + '</option>';
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
        works += '</select>';
        works += '</div>';
        works += '</div>';
			
        if(child[i].recommend_18["useby"] == "Y"){
			works += '<div style="color: #0070c6;margin-bottom: 20px;margin-top: 10px;font-size:0.56rem;">';
			works += MsgSet["None-blank"];
			works += '</div>';
		}
		
		//姓氏
        works += '<div class="item">';
        works += '<p>' + child[i].recommend_1["itemName"] + '<span>*</span></p>';
        works += '<div id="' + data.itemId + child[i].recommend_1["itemId"] + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';	
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_1["itemId"]+'" value="'+child[i].recommend_1["value"]+'" class="text1">';
        works += '</div></div>';
        
        //名字
        works += '<div class="item" '+(child[i].recommend_17["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_17["itemName"] + '<span>*</span></p>';
        works += '<div id="' + data.itemId + child[i].recommend_17["itemId"] + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';	
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_17["itemId"]+'" value="'+child[i].recommend_17["value"]+'" class="text1">';
        works += '</div></div>';
        
        //单位
        works += '<div class="item">';
        works += '<p>' + child[i].recommend_2["itemName"] + '<span>*</span></p>';
        works += '<div id="' + data.itemId + child[i].recommend_2["itemId"] + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';	
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+'  id="' + data.itemId + child[i].recommend_2["itemId"]+'" value="'+child[i].recommend_2["value"]+'" class="text1">';
        works += '</div></div>';
        
		//职务
        works += '<div class="item">';
        works += '<p>' + child[i].recommend_3["itemName"] + '<span>*</span></p>';
        works += '<div id="' + data.itemId + child[i].recommend_3["itemId"] + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';	
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_3["itemId"]+'" value="'+child[i].recommend_3["value"]+'" class="text1">';
        works += '</div></div>';
        
        //手机
        works += '<div class="item"  '+(child[i].recommend_4["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_4["itemName"] + '<span>*</span></p>';
        works += '<div id="' + data.itemId + child[i].recommend_4["itemId"] + 'Tip" class="tips" style="display: none;"><i></i><span></span></div>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_4["itemId"]+'" value="'+child[i].recommend_4["value"]+'" class="text1">';
        works += '</div></div>';
        
		//邮箱
        works += '<div class="item" '+(child[i].recommend_5["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_5["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_5["itemId"]+'" value="'+child[i].recommend_5["value"]+'" class="text1">';
        works += '</div></div>';
        
		//性别
        works += '<div class="item" '+(child[i].recommend_15["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_15["itemName"] + '<span>*</span></p>';
        works += '<ul class="sex">';
        works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '"  id="' + data.itemId + child[i].recommend_15["itemId"] + '_M">';

        works += '<li>';
        works += '<input type="radio" name="' + data.itemId + child[i].recommend_15["itemId"] +'" '+(child[i].recommend_15["option"]["MAN"]["checked"] == "Y" ? "checked='checked'": "")+' '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' class="radio" value="M">';
        works += '<label for="MAN">' + MsgSet["SEX_M"] + '</label>';
        works += '</li>';
        works += '</div>';
        
        works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '"  id="' + data.itemId + child[i].recommend_15["itemId"] + '_F">';
        works += '<li style="margin-left:0;">';
        works += '<input type="radio" name="' + data.itemId + child[i].recommend_15["itemId"] +'" '+(child[i].recommend_15["option"]["WOMAN"]["checked"] == "Y" ? "checked='checked'": "")+' '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' class="radio" value="F">';
        works += '<label for="WOMAN">' + MsgSet["SEX_F"] + '</label>';
        works += '</li>';
        works += '</div>';
        works += '</ul>';
        works += '</div>';
		
		//申请人关系
        works += '<div class="item" '+(child[i].recommend_6["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_6["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_6["itemId"]+'" value="'+child[i].recommend_6["value"]+'" class="text1">';
        works += '</div></div>';
        
		//备用字段一
        works += '<div class="item" '+(child[i].recommend_10["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_10["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_10["itemId"]+'" value="'+child[i].recommend_10["value"]+'" class="text1">';
        works += '</div></div>';

		//备用字段二
        works += '<div class="item" '+(child[i].recommend_11["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_11["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_11["itemId"]+'" value="'+child[i].recommend_11["value"]+'" class="text1">';
        works += '</div></div>';

		//备用字段三
        works += '<div class="item" '+(child[i].recommend_12["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_12["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_12["itemId"]+'" value="'+child[i].recommend_12["value"]+'" class="text1">';
        works += '</div></div>';

		//备用字段四
        works += '<div class="item" '+(child[i].recommend_13["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_13["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_13["itemId"]+'" value="'+child[i].recommend_13["value"]+'" class="text1">';

        works += '</div></div>';

		//备用字段五
        works += '<div class="item" '+(child[i].recommend_14["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_14["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_14["itemId"]+'" value="'+child[i].recommend_14["value"]+'" class="text1">';
        works += '</div></div>';
        
        //备用字段六
        works += '<div class="item" '+(child[i].recommend_19["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_19["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+'  id="' + data.itemId + child[i].recommend_19["itemId"]+'" value="'+child[i].recommend_19["value"]+'" class="text1">';
        works += '</div></div>';
        
        //备用字段七
        works += '<div class="item" '+(child[i].recommend_20["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_20["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_20["itemId"]+'" value="'+child[i].recommend_20["value"]+'" class="text1">';
        works += '</div></div>';
        
        //备用字段八
        works += '<div class="item" '+(child[i].recommend_21["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_21["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_21["itemId"]+'" value="'+child[i].recommend_21["value"]+'" class="text1">';
        works += '</div></div>';

        //备用字段九
        works += '<div class="item" '+(child[i].recommend_22["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_22["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_22["itemId"]+'" value="'+child[i].recommend_22["value"]+'" class="text1">';
        works += '</div></div>';
        
        //备用字段十
        works += '<div class="item" '+(child[i].recommend_23["useby"] == "Y" ? "style='display:block'" : "style='display:none'")+'>';
        works += '<p>' + child[i].recommend_23["itemName"] + '<span>*</span></p>';
        works += '<div class="text-box">';
        works += '<input  type="text" '+((_zd=="Y"||_zd=="Z")?"readonly=true" : "")+' id="' + data.itemId + child[i].recommend_23["itemId"]+'" value="'+child[i].recommend_23["value"]+'" class="text1">';
        works += '</div></div>';
        
		//推荐信语言
        console.log("_qy_zhs:"+_qy_zhs+"_qy_eng:"+_qy_eng);
		if (_qy_zhs=="Y"&&_qy_eng=="Y")
		{
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
	        works += '<div class="item">';
	        works += '<p>' + child[i].recommend_7["itemName"] + '<span>*</span></p>';
	        works += '<ul class="sex">';
	        works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '"  id="' + data.itemId + child[i].recommend_7["itemId"] + '_C">';
	        works += '<li>';
	        works += '<input type="radio"  name="' + data.itemId + child[i].recommend_7["itemId"] +'" '+ (child[i].recommend_7["option"]["ZHS"]["checked"] == "Y" ? "checked='checked'": "")+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' class="radio" value="C">';
	        works += '<label for="FILL">' + MsgSet["LANGUAGE_C"] + '</label>';
	        works += '</li>';
	        works += '</div>';
	        works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '"  id="' + data.itemId + child[i].recommend_7["itemId"] + '_E">';
	        works += '<li style="margin-left:0;">';
	        works += '<input type="radio"  name="' + data.itemId + child[i].recommend_7["itemId"] +'" '+ (child[i].recommend_7["option"]["ENG"]["checked"] == "Y" ? "checked='checked'": "")+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' class="radio" value="E">';
	        works += '<label for="radio-3">' + MsgSet["LANGUAGE_E"] + '</label>';
	        works += '</li>';
	        works += '</div>';
	        works += '</ul>';
	        works += '</div>';
	        
		}else if (_qy_zhs=="Y"&&_qy_eng!="Y")
		{
			child[i].recommend_7["option"]["ZHS"]["checked"] = "Y";
			child[i].recommend_7["option"]["ENG"]["checked"] = "N";
			works += '<div class="input-list" style="display:none">';
			works += '<div class="input-list-info left">'+ child[i].recommend_7["itemName"] +'：</div>';
			works += '<div class="input-list-text left">';
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="C">';
			works += '</div>';
			works += '</div>';
		}else if (_qy_zhs!="Y"&&_qy_eng=="Y")
		{
			child[i].recommend_7["option"]["ZHS"]["checked"] = "N";
			child[i].recommend_7["option"]["ENG"]["checked"] = "Y";
			works += '<div class="input-list" style="display:none">';
			works += '<div class="input-list-info left">'+ child[i].recommend_7["itemName"] +'：</div>';
			works += '<div class="input-list-text left">';
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_7["itemId"] + '" value="E">';
			works += '</div>';
			works += '</div>';
		}
		
		//推荐信类型
		// itemLx=L  2个都可以
		
		if (data.itemLx=="L"){
			
	        works += '<div class="item">';
	        works += '<p>' + child[i].recommend_8["itemName"] + '<span>*</span></p>';
			
			//如果发送邮件被选择了  recommend_8.value =S 
			if(child[i].recommend_8["option"]["SEND"]["checked"] == "Y"){
				child[i].recommend_8["option"]["SEND"]["checked"] = "Y";
				child[i].recommend_8["option"]["UPLOAD"]["checked"] = "N";
				child[i].recommend_8["value"] = "S";
			}
			//如果 上传附件被选择了   recommend_8.value =U
			else if(child[i].recommend_8["option"]["UPLOAD"]["checked"] == "Y")
			{
				child[i].recommend_8["option"]["SEND"]["checked"] = "N";
				child[i].recommend_8["option"]["UPLOAD"]["checked"] = "Y";
				child[i].recommend_8["value"] = "U";
			}
			//默认 发送邮件
			else{
				child[i].recommend_8["option"]["SEND"]["checked"] = "Y";
				child[i].recommend_8["option"]["UPLOAD"]["checked"] = "N";
				child[i].recommend_8["value"] = "S";
			}
			
	        works += '<ul class="tjx">'; 
	        works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '"  id="' + data.itemId + child[i].recommend_8["itemId"] + '_S">';

	        works += '<li>';
	        works +='<input ' + (child[i].recommend_8["option"]["SEND"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["title"] +'" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="S">';
	        /*works += '<input type="radio" name="square-radio" class="radio"   >';*/
	        works += '<label for="FILL">' + MsgSet["Send_mail"] + '</label>';
	        works += '</li>';
	        works += '</div>';
	        works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '" id="' + data.itemId + child[i].recommend_8["itemId"] + '_U"  >';

	        works += '<li>';
	        works +='<input  ' + (child[i].recommend_8["option"]["UPLOAD"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["title"] +'" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="U" >';
	        /*works += '<input type="radio" name="square-radio" class="radio" >';*/
	        works += '<label for="UPLOAD">' + MsgSet["Upload"] + '</label>';
	        works += '</li>';
	        works +='</div>';
	        works += '</ul>';
			works += '</div>';
			
			
/*			works += '	<ul class="tjx">'; 
			works += '	<li>'; 
			works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '" class="radio-btn '+(child[i].recommend_8["option"]["SEND"]["checked"] == "Y" ? "checkedRadio" : "")+'" onclick="SurveyBuild.clickOnRadio(this);" id="' + data.itemId + child[i].recommend_8["itemId"] + '_S">';
			works += '<i><input ' + (child[i].recommend_8["option"]["SEND"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["title"] +'" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="S"></i>';
			works += '</div><span style="margin-left:3px;">' + MsgSet["Send_mail"]+'</span>&nbsp;&nbsp;&nbsp;&nbsp;';
			works += '<div readonlyflag="'+ ((_readOnlyRadio=="Y") ? "Y" : "N") + '" class="radio-btn '+(child[i].recommend_8["option"]["UPLOAD"]["checked"] == "Y" ? "checkedRadio" : "")+'" onclick="SurveyBuild.clickOnRadio(this);" id="' + data.itemId + child[i].recommend_8["itemId"] + '_U">';
			works += '<i><input ' + (child[i].recommend_8["option"]["UPLOAD"]["checked"] == "Y" ? "checked='checked'": "") + ' type="radio" align="'+i+'" '+((_zd=="Y"||_zd=="Z")?"disabled=true" : "")+' title="'+ child[i].recommend_8["title"] +'" name="' + data.itemId + child[i].recommend_8["itemId"] +'" value="U" ></i>';
			works += '</div><span style="margin-left:3px;">' + MsgSet["Upload"] + '</sapn>';
			works += '	</li>';
			works += '<div class="clear"></div></ul>';
			works += '<input type="hidden" id="' + data.itemId + child[i].recommend_8["itemId"] + '" value="' + child[i].recommend_8["value"] + '">';
			works += '</div>';
			works += '<div class="input-list-suffix left">';
			works += '	<div id="' + data.itemId + child[i].recommend_8["itemId"] + 'Tip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent;" class="onShow">';
			works += '		<div class="onShow">&nbsp;</div>';
			works += '	</div>';
			works += '</div>';
			works += '<div class="clear"></div>';
			works += '</div>';*/
			
		}
		// 如果 设置 itemLx=F  发送邮件
		else if(data.itemLx == "F"){
			child[i].recommend_8["option"]["SEND"]["checked"] = "Y";
			child[i].recommend_8["option"]["UPLOAD"]["checked"] = "N";
            works += '<input type="hidden" id="' + data.itemId + child[i].recommend_8["itemId"] + '" value="S">';
        }
		// 如果 itemLx=S  上传附件
		else{
			child[i].recommend_8["option"]["SEND"]["checked"] = "N";
			child[i].recommend_8["option"]["UPLOAD"]["checked"] = "Y";
            works += '<input type="hidden" id="' + data.itemId + child[i].recommend_8["itemId"] + '" value="U">';
        }
		
		//上传附件
		
		 if ((data.itemLx == "L" && child[i].recommend_8["value"] == "U") || data.itemLx == "S"){
			 
        
		 	works += '<div class="item" id="Tjxfj_show_'+i+'">';
		 	if(SurveyBuild._readonly != true){
		
		 		works += '<p>'+ child[i].recommend_9["itemName"] + '<span>*</span></p>';
		 		works += '<div class="text-box" style="border:none;display:' + (SurveyBuild._readonly?'none':'block') +' " >';
		 		works += '<div class="handle">';
		 		works += '<div class="ncsc-upload-btn">';
		 		works += '<a href="#" class="ncsc-upload-btn-a">';
		 		works += '<span class="ncsc-upload-btn-span">';
		 		works += '<input type="file" hidefocus="true" size="1" class="input-file" name="goods_image" onchange=SurveyBuild.TjxUpload(this,"recommend_9",'+i+')></span>';
		 		works += '<div class="ncsc-upload-btn-p">'+ MsgSet["UPLOAD_BTN_MSG"] +'<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png'+'"></div>';
		 		works += '</a>';
		 		works += '</div>';
		 		works += '</div>';
		 		works += '</div>';
		 	}else{
		 		
		 	}
        
		 	works += '</div>';
        
		 	/*附件显示 开始*/
		 	if(SurveyBuild._readonly != true){
		
	 		works += ' <div class="upload_list"  id="'+data.itemId+i+'_AttList">';
			 	}else{
	
	 		works += '<div id="'+data.itemId+i+'_AttList">';
		 	}
		 	if (child[i].recommend_9["viewFileName"]!="")
		 	{
		 		if(SurveyBuild._readonly!=true){
		
		 			works += '<div class="upload_list">';
	 			works += '<li class="fileLi"><span>';
		 			works += '<a onclick=SurveyBuild.TjxdownLoad(this,"recommend_9",'+i+') file-index="'+i+'">'+child[i].recommend_9["viewFileName"]+'</a></span>';
		 			works += '<i onclick="onclick=SurveyBuild.Tjxdelete(this,"recommend_9",'+i+')" style="background:url(/statics/images/appeditor/m/de.png) no-repeat center center"></i></li>';
 	 			works += '</div>';
		
		 		}else{
		 			works += '<div class="input-list-uploadcon-list">';
		 			works += '	<div class="input-list-uploadcon-listl left">';
		 			works += '	<a class="input-list-uploadcon-list-a" onclick=SurveyBuild.TjxdownLoad(this,"recommend_9",'+i+') file-index="'+i+'">'+child[i].recommend_9["viewFileName"]+'</a>';
		 			works += '</div>';
		 			works += '</div>';
		 		}
		 	}
		 	works += '</div>';
		
		 	/*附件显示 结束*/
		 	works += '	</div>';
		 	works += '	<input id="'+data.itemId+child[i].recommend_9["itemId"]+'" type="hidden" name="'+data.itemId+child[i].recommend_9["itemId"]+'" value="'+child[i].recommend_9["value"]+'">';
		 	works += '	<div class="clear"></div>';
		 	works += '</div>';
		 	
		 }else{
			 
			 works += '<div class="item" style="display:none" id="Tjxfj_show_'+i+'">';
			 	if(SurveyBuild._readonly != true){
			
			 		works += '<p>'+ child[i].recommend_9["itemName"] + '<span>*</span></p>';
			 		works += '<div class="text-box" style="border:none;display:' + (SurveyBuild._readonly?'none':'block') +' " >';
			 		works += '<div class="handle">';
			 		works += '<div class="ncsc-upload-btn">';
			 		works += '<a href="#" class="ncsc-upload-btn-a">';
			 		works += '<span class="ncsc-upload-btn-span">';
			 		works += '<input type="file" hidefocus="true" size="1" class="input-file" name="goods_image" onchange=SurveyBuild.TjxUpload(this,"recommend_9",'+i+') ></span>';
			 		works += '<div class="ncsc-upload-btn-p">'+ MsgSet["UPLOAD_BTN_MSG"] +'<img src="' + TzUniversityContextPath + '/statics/images/appeditor/new/upload.png'+'"></div>';
			 		works += '</a>';
			 		works += '</div>';
			 		works += '</div>';
			 		works += '</div>';
			 	}else{
			 		
			 	}
	        
			 	works += '</div>';
        
		 }
		//上传附件-结束

		//发送邮件 -开始
			if ((data.itemLx=="L"&&child[i].recommend_8["value"]!="U")||data.itemLx=="F"){

				/*更换推荐人一直显示*/
				if(SurveyBuild._readonly){
					works += '<div id="changeRecS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+'">';
					works += '</div>';
				}else{
					
					
		
						//发送
						works += '<div id="sendEmailS_'+i+'"  style="'+((_zd!="Y"&&_zd!="Z")?"":"display:none")+';width: 45%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:left;">';
						works += '<div id="sendEmail_'+i+'">'+MsgSet["Send"]+'</div><a href="#" class="alpha"></a>';
						works += '</div>';
						
						//灰色的发送邮件
						works += '<div id="sendEmailH_'+i+'" style="display:none;width: 45%;padding: 10px 0;background-color: #999;color: #fff;text-align: center;border-radius: 5px;float:left;">';
						works += '<div >'+MsgSet["Send"]+'</div><a href="#" class="alpha"></a>';
						works += '</div>';
						
						//发送给自己
						works += '<div id="sendEmailToMeS_'+i+'" style="'+((_zd!="Y"&&_zd!="Z")?"":"display:none")+';width: 45%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:right;">';
						works += '<div id="sendEmailToMe_'+i+'">'+MsgSet["SendToMe"]+'</div><a href="#" class="alpha"></a>';
						works += '</div>';
						
						//发送给自己灰色
						works += '<div id="sendEmailToMeH_'+i+'" style="display:none;width: 45%;padding: 10px 0;background-color: #999;color: #fff;text-align: center;border-radius: 5px;float:right;">';
						works += '<div >'+MsgSet["SendToMe"]+'</div><a href="#" class="alpha"></a>';
						works += '</div>';
						
					
						
						//重新发送  ，如果没有发送，那么显示
						works += '<div id="reSendEmailS_'+i+'" style="'+((_zd!="Y"&&_zd!="Z")?"display:none":"")+';width:30%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:left;">';
						works += '<div id="reSendEmail_'+i+'">'+MsgSet["Resend"]+'</div><a href="#" class="alpha"></a>';
						works += '</div>';
						//重新发送灰色
						works += '<div id="reSendEmailH_'+i+'" style="display:none;width:30%;padding: 10px 0;background-color: #999;color: #fff;text-align: center;border-radius: 5px;float:left;">';
						works += '<div >'+MsgSet["Resend"]+'</div><a href="#" class="alpha"></a>';
						works += '</div>';
						
						//重新发送给自己
						works += '<div id="reSendEmailToMeS_'+i+'" style="'+((_zd!="Y"&&_zd!="Z")?"display:none":"")+';width:30%;margin-left:5%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:left;">';
						works += '<div id="reSendEmailToMe_'+i+'">'+MsgSet["ResendToMe"]+'</div><a href="#" class="alpha"></a>';
						works += '</div>';
						//重新发送给自己灰色
						works += '<div id="reSendEmailToMeH_'+i+'" style="display:none;width:30%;margin-left:5%;padding: 10px 0;background-color: #999;color: #fff;text-align: center;border-radius: 5px;float:left;">';
						works += '<div >'+MsgSet["ResendToMe"]+'</div><a href="#" class="alpha"></a>';
						works += '</div>';
						
						
						
						works += '<div id="changeRecS_'+i+'" style="'+((_zd!="Y"&&_zd!="Z")?"display:none":"")+';width:30%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:right;">';
						works += '<div id="changeRec_'+i+'">'+MsgSet["RepRecom"]+'</div><a href="#" class="alpha"></a>';
						works += '</div>';
						//更换推荐人灰色
					/*	works += '<div id="changeRecH_'+i+'" style="display:none;width:30%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:right;">';
						works += '<div >'+MsgSet["RepRecom"]+'</div><a href="#" class="alpha"></a>';
						works += '</div>';*/
					
				}
				
				works += '<div class="clear"></div>';
			//推荐信状态
		  works += '<p class="finish" style="margin-top: 15px" id="tjxzt_desc_'+i+'">' + MsgSet["ReLeSt"] + '：<span>' + _tjx_zt + '</span></p>';
			
			
		}else{
			/*更换推荐人一直显示*/
			if(SurveyBuild._readonly){
				works += '<div id="changeRecS_'+i+'" style="cursor:pointer;padding-left:15px;padding-top:5px;float:left;'+'">';
				works += '</div>';
			}else{
				
				
					
					//发送
					works += '<div id="sendEmailS_'+i+'" style="display:none;width: 45%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:left;">';
					works += '<div id="sendEmail_'+i+'">'+MsgSet["Send"]+'</div><a href="#" class="alpha"></a>';
					works += '</div>';
					//发送灰色
					works += '<div id="sendEmailH_'+i+'" style=" display:none ;width: 45%;padding: 10px 0;background-color: #999;color: #fff;text-align: center;border-radius: 5px;float:left;">';
					works += '<div id="sendEmail_'+i+'">'+MsgSet["Send"]+'</div><a href="#" class="alpha"></a>';
					works += '</div>';
					
					
					//发送给自己
					works += '<div id="sendEmailToMeS_'+i+'" style="display:none; width: 45%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:right;">';
					works += '<div id="sendEmailToMe_'+i+'">'+MsgSet["SendToMe"]+'</div><a href="#" class="alpha"></a>';
					works += '</div>';
					//发送给自己灰色
					works += '<div id="sendEmailToMeH_'+i+'" style="display:none; width: 45%;padding: 10px 0;background-color: #999;color: #fff;text-align: center;border-radius: 5px;float:right;">';
					works += '<div id="sendEmailToMe_'+i+'">'+MsgSet["SendToMe"]+'</div><a href="#" class="alpha"></a>';
					works += '</div>';
					

					//重新发送  ，如果没有发送，那么显示
					works += '<div id="reSendEmailS_'+i+'" style="display:none;width:30%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:left;">';
					works += '<div id="reSendEmail_'+i+'">'+MsgSet["Resend"]+'</div><a href="#" class="alpha"></a>';
					works += '</div>';
					//重新发送  ，如果没有发送，那么显示灰色
					works += '<div id="reSendEmailH_'+i+'" style=" display:none;width:30%;padding: 10px 0;background-color: #999;color: #fff;text-align: center;border-radius: 5px;float:left;">';
					works += '<div id="reSendEmail_'+i+'">'+MsgSet["Resend"]+'</div><a href="#" class="alpha"></a>';
					works += '</div>';
					
					//重新发送给自己
					works += '<div id="reSendEmailToMeS_'+i+'" style="display:none;width:30%;margin-left:5%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:left;">';
					works += '<div id="reSendEmailToMe_'+i+'">'+MsgSet["ResendToMe"]+'</div><a href="#" class="alpha"></a>';
					works += '</div>';
					//重新发送给自己灰色
					works += '<div id="reSendEmailToMeS_'+i+'" style="display:none;width:30%;margin-left:5%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:left;">';
					works += '<div id="reSendEmailToMe_'+i+'">'+MsgSet["ResendToMe"]+'</div><a href="#" class="alpha"></a>';
					works += '</div>';
					
					works += '<div id="changeRecS_'+i+'" style="display:none;width:30%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:right;">';
					works += '<div id="changeRec_'+i+'">'+MsgSet["RepRecom"]+'</div><a href="#" class="alpha"></a>';
					works += '</div>';
					works += '<div id="changeRecH_'+i+'" style="display:none; width:30%;padding: 10px 0;background-color: #ff7b05;color: #fff;text-align: center;border-radius: 5px;float:right;">';
					works += '<div id="changeRec_'+i+'">'+MsgSet["RepRecom"]+'</div><a href="#" class="alpha"></a>';
					works += '</div>';
				
			}
			
			works += '<div class="clear"></div>';
		//推荐信状态

		works += '<p class="finish" style="margin-top: 15px" id="tjxzt_desc_'+i+'">' + MsgSet["ReLeSt"] + '：<span>' + _tjx_zt + '</span></p>';
			
			
		}
		
        works += '</div>';
        works += '</div>';
        
        works += '</div>';
        return works;
    },
    _eventbind: function(data) {
		
        var children = data.children,
        len = children.length;

		for (var i=1;i<=len;i++)
		{
			var num=i;
			
			//发送邮件  包括发送给 推荐人 以及发送给自己
			$("#sendEmail_"+(Number(num)-1)).unbind("click");  
			$("#sendEmailToMe_"+(Number(num)-1)).unbind("click"); 
			
			
			$.each([$("#sendEmail_"+(Number(num)-1)),$("#sendEmailToMe_"+(Number(num)-1))],function(i,el){
				el.click(function(e) {
				var lineno = "";
		    	//修改判断判断是否是手机版
		    	if(SurveyBuild.accessType=='M'){
		    		lineno=parseInt($(this).closest(".next_record").index());
		    		
		    	}else{
		    		lineno=parseInt($(this).closest(".main_inner_content_para").index());
		    		
		    	}
				var m=Number(lineno)+1;
				var mm = this.id.split("_")[1];
				var mm=Number(mm)+1;
				//错误代码
				var _yz="";
				var _desc="";
				//console.log("Number(num)-1):");
				var rec_title = $("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).val();
				if (children[m-1].recommend_18["useby"]=="Y"&&rec_title==""){
					_yz="1";
					_desc =   children[m-1].recommend_18["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).blur();
				}
				
				var rec_gname = $("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).val();
				if (children[m-1].recommend_17["useby"]=="Y"&&rec_gname==""){
					_yz="1";
					_desc =  children[m-1].recommend_17["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).blur();
				}
				
				var rec_name = $("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).val();
				if (children[m-1].recommend_1["useby"]=="Y"&&rec_name==""){
					_yz="1";
					_desc =  children[m-1].recommend_1["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).blur();
				}
				var rec_company = $("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).val();
				if (children[m-1].recommend_2["useby"]=="Y"&&rec_company==""){
					_yz="1";
					_desc =  children[m-1].recommend_2["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).blur();
				}
				
				var rec_post = $("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).val();
				if (children[m-1].recommend_3["useby"]=="Y"&&rec_post==""){
					_yz="1";
					_desc =  children[m-1].recommend_3["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).blur();
				}
				
				var rec_phone_area = $("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).val();
				if (children[m-1].recommend_16["useby"]=="Y"&&rec_phone_area==""){
					_yz="1";
					_desc =  children[m-1].recommend_16["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).blur();	
				} else{
					rec_phone_area="";
				}

				var rec_phone_no = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
				if (children[m-1].recommend_4["useby"]=="Y"&&rec_phone_no==""){
					_yz="1";
					_desc =   children[m-1].recommend_4["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).blur();
				}
				var rec_email = $("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).val();
				if (children[m-1].recommend_5["useby"]=="Y"&&rec_email==""){
					_yz="1";
					_desc =  children[m-1].recommend_5["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).blur();
				}
				
				//页面性别数据				
				var rec_sex
				if(SurveyBuild.accessType=='M'){
					
					rec_sex = $('input[name="'+data["itemId"] + children[m-1].recommend_15["itemId"]+'"]:checked').val();
				}else{
					rec_sex = $("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).val();
				}
				//var rec_sex = $("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).val();
				//console.log("rec_sex:"+rec_sex);
				
				if (children[m-1].recommend_15["useby"]=="Y"&&rec_sex==""){
					_yz="1";
					_desc =  children[m-1].recommend_15["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).blur();
				}else if (children[m-1].recommend_15["useby"]!="Y")
				{
					rec_sex="";
				}
				var rec_relation = $("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).val();
				if (children[m-1].recommend_6["useby"]=="Y"&&rec_relation==""){
					_yz="1";
					_desc =  children[m-1].recommend_6["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).blur();
				}
				
				//页面语言数据				
				var rec_language
				if(SurveyBuild.accessType=='M'){
					
					rec_language = $('input[name="'+data["itemId"] + children[m-1].recommend_7["itemId"]+'"]:checked').val();
				}else{
					rec_language = $("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).val();
				}
								
				//console.log("rec_language:"+rec_language);
				if (children[m-1].recommend_7["useby"]=="Y"&&rec_language==""){
					_yz="1";
					_desc =   children[m-1].recommend_7["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).blur();
				}
				var rec_num = children[m-1].recommend_99["value"];
				var rec_by1 = $("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).val();
				if (children[m-1].recommend_10["useby"]=="Y"&&rec_by1==""){
					_yz="1";
					_desc = children[m-1].recommend_10["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).blur();
				}
				var rec_by2 = $("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).val();
				if (children[m-1].recommend_11["useby"]=="Y"&&rec_by2==""){
					_yz="1";
					_desc =  children[m-1].recommend_11["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).blur();
				}
				var rec_by3 = $("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).val();
				if (children[m-1].recommend_12["useby"]=="Y"&&rec_by3==""){
					_yz="1";
					_desc =   children[m-1].recommend_12["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).blur();
				}
				var rec_by4 = $("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).val();
				if (children[m-1].recommend_13["useby"]=="Y"&&rec_by4==""){
					_yz="1";
					_desc =   children[m-1].recommend_13["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).blur();
				}
				var rec_by5 = $("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).val();
				if (children[m-1].recommend_14["useby"]=="Y"&&rec_by5==""){
					_yz="1";
					_desc =  children[m-1].recommend_14["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).blur();
				}
				var rec_by6 = $("#" + data["itemId"] + children[m-1].recommend_19["itemId"]).val();
				if (children[m-1].recommend_19["useby"]=="Y"&&rec_by6==""){
					_yz="1";
					_desc =   children[m-1].recommend_19["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_19["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_19["itemId"]).blur();
				}
				var rec_by7 = $("#" + data["itemId"] + children[m-1].recommend_20["itemId"]).val();
				if (children[m-1].recommend_20["useby"]=="Y"&&rec_by7==""){
					_yz="1";
					_desc =   children[m-1].recommend_20["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_20["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_20["itemId"]).blur();
				}
				var rec_by8 = $("#" + data["itemId"] + children[m-1].recommend_21["itemId"]).val();
				if (children[m-1].recommend_21["useby"]=="Y"&&rec_by8==""){
					_yz="1";
					_desc =  children[m-1].recommend_21["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_21["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_21["itemId"]).blur();
				}
				var rec_by9 = $("#" + data["itemId"] + children[m-1].recommend_22["itemId"]).val();
				if (children[m-1].recommend_22["useby"]=="Y"&&rec_by9==""){
					_yz="1";
					_desc =  children[m-1].recommend_22["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_22["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_22["itemId"]).blur();
				}
				var rec_by10 = $("#" + data["itemId"] + children[m-1].recommend_23["itemId"]).val();
				if (children[m-1].recommend_23["useby"]=="Y"&&rec_by10==""){
					_yz="1";
					_desc =  children[m-1].recommend_23["itemName"]+ MsgSet["REQUIRE"];
					$("#" + data["itemId"] + children[m-1].recommend_23["itemId"]).focus();
					$("#" + data["itemId"] + children[m-1].recommend_23["itemId"]).blur();
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
				rec_by6 = SurveyBuild.specialCharReplace(rec_by6);
				rec_by7 = SurveyBuild.specialCharReplace(rec_by7);
				rec_by8 = SurveyBuild.specialCharReplace(rec_by8);
				rec_by9 = SurveyBuild.specialCharReplace(rec_by9);
				rec_by10 = SurveyBuild.specialCharReplace(rec_by10);
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
						_desc = children[m-1].recommend_5["itemName"]+ MsgSet["FORMAT_ERROR_MSG"];
						alert(_desc);
					}else{
						
						var _tz_app_ins_id=SurveyBuild.appInsId;
						var _tz_app_version_id=SurveyBuild.appInsVersion;
						var _email_tx = $("#yincang_tx").val();
						_email_tx = SurveyBuild.specialCharReplace(_email_tx);
				
						var _Url = SurveyBuild.tzGeneralURL + "?tzParams=";

						//区分发送给自己 还是发送给 推荐人
						var sendFlag ="";
						
						
						var sssid = $(this).attr("id");
						
						var index = sssid.indexOf('sendEmail_');
						if(index==0){
							sendFlag= "Y";  //发送给推荐人
						} else {
							sendFlag= "N";  //发送给自己
						}
						$("#sendEmailH_"+(Number(mm)-1)).css("display","block");
						$("#sendEmailS_"+(Number(mm)-1)).css("display","none");
						$("#sendEmailToMeH_"+(Number(mm)-1)).css("display","block");
						$("#sendEmailToMeS_"+(Number(mm)-1)).css("display","none");
						
						
						if(_tz_app_ins_id == "0"){
							/*提示先保存报名表*/
							alert(MsgSet["SAVEBMBFIRST"]); 
						}else{
							
							//modity by caoy 修改逻辑 先保存推荐信 然后在发送邮件 然后掉调用 保存推荐信
							processing();
							var appInsId = SurveyBuild.appInsId;
							var appInsVersionId = SurveyBuild.appInsVersion;
							var _a = getData();
							var classId = $("#ClassId").val();
							var batchId = $("#BatchId").val();
							var pwd="";
							var isPwd="N";
							console.log("appInsId:"+appInsId);
							console.log("appInsVersionId:"+appInsVersionId);
							console.log("currentPageId:"+currentPageId);
							
							var tzSaveParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"U","comParams":{"update":[{"TZ_APP_C_TYPE":"SAVE","isEdit":"","TZ_REF_LETTER_ID":"","PASSWORD":"'+pwd+'","ISPWD":"'+isPwd+'","TZ_APP_INS_ID":' + appInsId + ',"TZ_APP_INS_VERSION":"' + appInsVersionId + '","TZ_PAGE_ID":"' + currentPageId + '","TZ_LANGUAGE":"ZHS","TZ_CLASS_ID":"'+SurveyBuild.classId+'","TZ_BATCH_ID":"'+batchId+'","TZ_NEW_CLASS_ID":"'+classId+'","data":' + $.toJSON(_a) + '}]}}';
							$.ajax({
								type: "POST",
								url: SurveyBuild.tzGeneralURL,
								data: {
									tzParams: tzSaveParams
								},
								dataType: "JSON",
								success: function(f) {
									console.log(f.state.errcode);
									if (f.state.errcode == "0") {
										SurveyBuild.appInsId = f.comContent.insid;
										SurveyBuild.appInsVersion = f.comContent.appInsVersionId;
										SurveyBuild.classId = classId;
										
										/*设置样式*/
										/*设置页面完成状态*/
										if (f.comContent.code == "0") { 
									 	/*当前页签设置为完成*/
											$("#" + currentPageId).find(".menu-gou.right img").attr("src",TzUniversityContextPath + "/statics/images/appeditor/new/check-white.png");
											$("#" + currentPageId).find(".menu-gou.right img").show();
										}else{
											/*当前页签设置为未完成*/
											$("#" + currentPageId).find(".menu-gou.right img").attr("src","");
											$("#" + currentPageId).find(".menu-gou.right img").hide();
										}
										layer.closeAll();
										noteing(MsgSet.SAVE_SUCCEED);
										
										
										var param = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"SEND","comParams":{"send_falg":"'+sendFlag+'","rec_app_ins_id":"'+SurveyBuild.appInsId+'","TZ_APP_INS_VERSION":"'+SurveyBuild.appInsVersion+'","rec_num":"'+rec_num+'","rec_title":"'+rec_title+'","rec_gname":"'+rec_gname+'","rec_name":"'+rec_name+'","rec_company":"'+rec_company+'","rec_post":"'+rec_post+'","rec_phone_area":"'+rec_phone_area+'","rec_phone_no":"'+rec_phone_no+'","rec_email":"'+rec_email+'","rec_sex":"'+rec_sex+'","rec_relation":"'+rec_relation+'","rec_language":"'+rec_language+'","email_tx":"'+_email_tx+'","rec_by1":"'+rec_by1+'","rec_by2":"'+rec_by2+'","rec_by3":"'+rec_by3+'","rec_by4":"'+rec_by4+'","rec_by5":"'+rec_by5+'","rec_by6":"'+rec_by6+'","rec_by7":"'+rec_by7+'","rec_by8":"'+rec_by8+'","rec_by9":"'+rec_by9+'","rec_by10":"'+rec_by10+'","accessPath":"'+_accessPath+'","filename":"'+_file+'","sysfilename":"'+_sysfile+'"}}';
										
										//报名报保存成功，发送邮件
										layer.load('推荐信发送中，请不要刷新或者关闭页面！');
										
										
										
										
										
										$.ajax({
											type: "post",
											url: _Url + encodeURIComponent(param),
											dataType: "json",
											success: function(result){
												if (result.comContent=="SUCCESS"){
													layer.closeAll();
													$("#sendEmailS_"+(Number(mm)-1)).css("display","none");
													$("#sendEmailToMeS_"+(Number(mm)-1)).css("display","none");
													$("#reSendEmailS_"+(Number(mm)-1)).css("display","block");
													$("#reSendEmailToMeS_"+(Number(mm)-1)).css("display","block");
													
													$("#sendEmailH_"+(Number(mm)-1)).css("display","none");
													$("#sendEmailToMeH_"+(Number(mm)-1)).css("display","none");
													
													$("#changeRecS_"+(Number(mm)-1)).css("display","block");
													alert(MsgSet["SEND_SC"]);
													$("#tjxzt_desc_"+(Number(mm)-1)).html(MsgSet["ReLeSt"]+"：<span class='blue'>"+MsgSet["SendEmail"]+"</span>");
													$("#tjxzt_info_"+(Number(mm)-1)).val("Y");
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
													$("#" + data["itemId"] + children[m-1].recommend_19["itemId"]).prop("readonly", true);
													$("#" + data["itemId"] + children[m-1].recommend_20["itemId"]).prop("readonly", true);
													$("#" + data["itemId"] + children[m-1].recommend_21["itemId"]).prop("readonly", true);
													$("#" + data["itemId"] + children[m-1].recommend_22["itemId"]).prop("readonly", true);
													$("#" + data["itemId"] + children[m-1].recommend_23["itemId"]).prop("readonly", true);
													
													$("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).prop("readonly", true);
													$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).next(".chosen-container").unbind();
											
													$("#"+data.itemId + children[m-1].recommend_7["itemId"]+"_E").attr("readonlyflag","Y");
													$("#"+data.itemId + children[m-1].recommend_7["itemId"]+"_C").attr("readonlyflag","Y");
													$("#"+data.itemId + children[m-1].recommend_8["itemId"]+"_U").attr("readonlyflag","Y");
													$("#"+data.itemId + children[m-1].recommend_8["itemId"]+"_S").attr("readonlyflag","Y");
													$("#"+data.itemId + children[m-1].recommend_15["itemId"]+"_M").attr("readonlyflag","Y");
													$("#"+data.itemId + children[m-1].recommend_15["itemId"]+"_F").attr("readonlyflag","Y");
													$("#tjx_delete_"+(Number(mm)-1)).hide();
													SurveyBuild.saveTjx_fj();
												}else {
													layer.closeAll();
													$("#sendEmailH_"+(Number(mm)-1)).css("display","none");
													$("#sendEmailS_"+(Number(mm)-1)).css("display","block");
													$("#sendEmailToMeH_"+(Number(mm)-1)).css("display","none");
													$("#sendEmailToMeS_"+(Number(mm)-1)).css("display","block");
													alert(result.comContent);
												}
											}
										});
										
				
									} else {
										$("#sendEmailH_"+(Number(mm)-1)).css("display","none");
										$("#sendEmailS_"+(Number(mm)-1)).css("display","block");
										$("#sendEmailToMeH_"+(Number(mm)-1)).css("display","none");
										$("#sendEmailToMeS_"+(Number(mm)-1)).css("display","block");
										layer.closeAll();
										noteing(MsgSet.SAVE_FAILD + "," + f.state.errdesc, 2);
									}
								}
							});
							
							//发送等待弹窗
							/*layer.load('推荐信发送中，请不要刷新或者关闭页面！');
							
							
							
							
							
							$.ajax({
								type: "post",
								url: _Url + encodeURIComponent(param),
								dataType: "json",
								success: function(result){
									if (result.comContent=="SUCCESS"){
										layer.closeAll();
										$("#sendEmailS_"+(Number(mm)-1)).css("display","none");
										$("#sendEmailToMeS_"+(Number(mm)-1)).css("display","none");
										$("#reSendEmailS_"+(Number(mm)-1)).css("display","block");
										$("#reSendEmailToMeS_"+(Number(mm)-1)).css("display","block");
										
										$("#sendEmailH_"+(Number(mm)-1)).css("display","none");
										$("#sendEmailToMeH_"+(Number(mm)-1)).css("display","none");
										
										$("#changeRecS_"+(Number(mm)-1)).css("display","block");
										alert(MsgSet["SEND_SC"]);
										$("#tjxzt_desc_"+(Number(mm)-1)).html(MsgSet["ReLeSt"]+"：<span class='blue'>"+MsgSet["SendEmail"]+"</span>");
										$("#tjxzt_info_"+(Number(mm)-1)).val("Y");
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
										$("#" + data["itemId"] + children[m-1].recommend_19["itemId"]).prop("readonly", true);
										$("#" + data["itemId"] + children[m-1].recommend_20["itemId"]).prop("readonly", true);
										$("#" + data["itemId"] + children[m-1].recommend_21["itemId"]).prop("readonly", true);
										$("#" + data["itemId"] + children[m-1].recommend_22["itemId"]).prop("readonly", true);
										$("#" + data["itemId"] + children[m-1].recommend_23["itemId"]).prop("readonly", true);
										
										$("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).prop("readonly", true);
										//$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).prop("disabled", true);
										//console.log($("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).closest(".main_inner_content_para").find(".chosen-container").html());
										$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).next(".chosen-container").unbind();
										
										//console.log($("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).html());
										//$("#" + data["itemId"] + children[m-1].recommend_18["itemId"] + "_chosen").find(".chosen-results").hide();
										//$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).chosen("destroy");
										$("#"+data.itemId + children[m-1].recommend_7["itemId"]+"_E").attr("readonlyflag","Y");
										$("#"+data.itemId + children[m-1].recommend_7["itemId"]+"_C").attr("readonlyflag","Y");
										$("#"+data.itemId + children[m-1].recommend_8["itemId"]+"_U").attr("readonlyflag","Y");
										$("#"+data.itemId + children[m-1].recommend_8["itemId"]+"_S").attr("readonlyflag","Y");
										$("#"+data.itemId + children[m-1].recommend_15["itemId"]+"_M").attr("readonlyflag","Y");
										$("#"+data.itemId + children[m-1].recommend_15["itemId"]+"_F").attr("readonlyflag","Y");
										$("#tjx_delete_"+(Number(mm)-1)).hide();
										$("#app_save").click();
									}else {
										layer.closeAll();
										$("#sendEmailH_"+(Number(mm)-1)).css("display","none");
										$("#sendEmailS_"+(Number(mm)-1)).css("display","block");
										$("#sendEmailToMeH_"+(Number(mm)-1)).css("display","none");
										$("#sendEmailToMeS_"+(Number(mm)-1)).css("display","block");
										alert(result.comContent);
									}
								}
							}); */
						}
					}
				}else if (_yz="2")
				{
					alert(_desc);
				}else{
					alert(_desc);
				}
				});
			});
			
			
			//重发邮件
			$("#reSendEmail_"+(Number(num)-1)).unbind("click");
			$("#reSendEmailToMe_"+(Number(num)-1)).unbind("click"); 
			
			$.each([$("#reSendEmail_"+(Number(num)-1)),$("#reSendEmailToMe_"+(Number(num)-1))],function(i,el){
				el.click(function(e) {
			
				var lineno ="";
				
		    	//修改判断判断是否是手机版
		    	if(SurveyBuild.accessType=='M'){
		    		lineno=parseInt($(this).closest(".next_record").index());
		    		
		    	}else{
		    		lineno=parseInt($(this).closest(".main_inner_content_para").index());
		    		
		    	}
				var m=Number(lineno)+1;
				var mm = this.id.split("_")[1];
				var mm=Number(mm)+1;
				var rec_title = $("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).val();
				var rec_gname = $("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).val();
				var rec_name = $("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).val();
				var rec_company = $("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).val();
				var rec_post = $("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).val();
				var rec_phone_area = $("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).val();
				if (children[m-1].recommend_16["useby"]=="Y"&&rec_phone_area==""){
				}else{
					rec_phone_area="";
				}
				var rec_phone_no = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
				var rec_email = $("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).val();
				
				//修改 手机版取值方式不一样
				var rec_sex="",rec_language="";
				if(SurveyBuild.accessType=='M'){
					rec_sex = $('input[name="'+data["itemId"] + children[m-1].recommend_15["itemId"]+'"]:checked').val();
					rec_language=rec_sex = $('input[name="'+data["itemId"] + children[m-1].recommend_7["itemId"]+'"]:checked').val();
				}else{
					//性别
				    rec_sex = $("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).val();
					
					//语言
					 rec_language = $("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).val();
					
				}
				
				if (children[m-1].recommend_15["useby"]=="Y"&&rec_sex!=""){
				}else{
					rec_sex="";
				}
				
				var rec_relation = $("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).val();
				
				var rec_num = children[m-1].recommend_99["value"];
				var rec_by1 = $("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).val();
				var rec_by2 = $("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).val();
				var rec_by3 = $("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).val();
				var rec_by4 = $("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).val();
				var rec_by5 = $("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).val();
				var rec_by6 = $("#" + data["itemId"] + children[m-1].recommend_19["itemId"]).val();
				var rec_by7 = $("#" + data["itemId"] + children[m-1].recommend_20["itemId"]).val();
				var rec_by8 = $("#" + data["itemId"] + children[m-1].recommend_21["itemId"]).val();
				var rec_by9 = $("#" + data["itemId"] + children[m-1].recommend_22["itemId"]).val();
				var rec_by10 = $("#" + data["itemId"] + children[m-1].recommend_23["itemId"]).val();
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
				rec_by6 = SurveyBuild.specialCharReplace(rec_by6);
				rec_by7 = SurveyBuild.specialCharReplace(rec_by7);
				rec_by8 = SurveyBuild.specialCharReplace(rec_by8);
				rec_by9 = SurveyBuild.specialCharReplace(rec_by9);
				rec_by10 = SurveyBuild.specialCharReplace(rec_by10);
				
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
				
				//区分发送给自己 还是发送给 推荐人
				var sendFlag ="";
				var sssid = $(this).attr("id");
				
				var index = sssid.indexOf('reSendEmail_');
				if(index==0){
					sendFlag= "Y";  //发送给推荐人
				} else {
					sendFlag= "N";  //发送给自己
				}
				$("#reSendEmailH_"+(Number(mm)-1)).css("display","block");
				$("#reSendEmailS_"+(Number(mm)-1)).css("display","none");
				$("#reSendEmailToMeH_"+(Number(mm)-1)).css("display","block");
				$("#reSendEmailToMeS_"+(Number(mm)-1)).css("display","none");
				$("#changeRecH_"+(Number(mm)-1)).css("display","block");
				$("#changeRecS_"+(Number(mm)-1)).css("display","none");
				
				//modity by caoy 修改逻辑 先保存推荐信 然后在发送邮件 然后掉调用 保存推荐信
				processing();
				var appInsId = SurveyBuild.appInsId;
				var appInsVersionId = SurveyBuild.appInsVersion;
				var _a = getData();
				var classId = $("#ClassId").val();
				var batchId = $("#BatchId").val();
				var pwd="";
				var isPwd="N";
				console.log("appInsId:"+appInsId);
				console.log("appInsVersionId:"+appInsVersionId);
				console.log("currentPageId:"+currentPageId);
				
				var tzSaveParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"U","comParams":{"update":[{"TZ_APP_C_TYPE":"SAVE","isEdit":"","TZ_REF_LETTER_ID":"","PASSWORD":"'+pwd+'","ISPWD":"'+isPwd+'","TZ_APP_INS_ID":' + appInsId + ',"TZ_APP_INS_VERSION":"' + appInsVersionId + '","TZ_PAGE_ID":"' + currentPageId + '","TZ_LANGUAGE":"ZHS","TZ_CLASS_ID":"'+SurveyBuild.classId+'","TZ_BATCH_ID":"'+batchId+'","TZ_NEW_CLASS_ID":"'+classId+'","data":' + $.toJSON(_a) + '}]}}';
				$.ajax({
					type: "POST",
					url: SurveyBuild.tzGeneralURL,
					data: {
						tzParams: tzSaveParams
					},
					dataType: "JSON",
					success: function(f) {
						if (f.state.errcode == "0") {
							SurveyBuild.appInsId = f.comContent.insid;
							SurveyBuild.appInsVersion = f.comContent.appInsVersionId;
							SurveyBuild.classId = classId;
							
							/*设置样式*/
							/*设置页面完成状态*/
							if (f.comContent.code == "0") { 
						 	/*当前页签设置为完成*/
								$("#" + currentPageId).find(".menu-gou.right img").attr("src",TzUniversityContextPath + "/statics/images/appeditor/new/check-white.png");
								$("#" + currentPageId).find(".menu-gou.right img").show();
							}else{
								/*当前页签设置为未完成*/
								$("#" + currentPageId).find(".menu-gou.right img").attr("src","");
								$("#" + currentPageId).find(".menu-gou.right img").hide();
							}
							layer.closeAll();
							noteing(MsgSet.SAVE_SUCCEED);
							
							//报名报保存成功，发送邮件
							var param = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"SEND","comParams":{"send_falg":"'+sendFlag+'","rec_app_ins_id":"'+SurveyBuild.appInsId+'","TZ_APP_INS_VERSION":"'+SurveyBuild.appInsVersion+'","rec_num":"'+rec_num+'","rec_title":"'+rec_title+'","rec_gname":"'+rec_gname+'","rec_name":"'+rec_name+'","rec_company":"'+rec_company+'","rec_post":"'+rec_post+'","rec_phone_area":"'+rec_phone_area+'","rec_phone_no":"'+rec_phone_no+'","rec_email":"'+rec_email+'","rec_sex":"'+rec_sex+'","rec_relation":"'+rec_relation+'","rec_language":"'+rec_language+'","email_tx":"'+_email_tx+'","rec_by1":"'+rec_by1+'","rec_by2":"'+rec_by2+'","rec_by3":"'+rec_by3+'","rec_by4":"'+rec_by4+'","rec_by5":"'+rec_by5+'","rec_by6":"'+rec_by6+'","rec_by7":"'+rec_by7+'","rec_by8":"'+rec_by8+'","rec_by9":"'+rec_by9+'","rec_by10":"'+rec_by10+'","accessPath":"'+_accessPath+'","filename":"'+_file+'","sysfilename":"'+_sysfile+'"}}';
							layer.load('推荐信发送中，请不要刷新或者关闭页面！');
							$.ajax({
								type: "post",
								url: _Url + encodeURIComponent(param),
								dataType: "json",
								success: function(result){
									if (result.comContent=="SUCCESS"){
										layer.closeAll();
										$("#reSendEmailH_"+(Number(mm)-1)).css("display","none");
										$("#reSendEmailS_"+(Number(mm)-1)).css("display","block");
										$("#reSendEmailToMeH_"+(Number(mm)-1)).css("display","none");
										$("#reSendEmailToMeS_"+(Number(mm)-1)).css("display","block");
										$("#changeRecH_"+(Number(mm)-1)).css("display","none");
										$("#changeRecS_"+(Number(mm)-1)).css("display","block");
										alert(MsgSet["SEND_SC"]);
										SurveyBuild.saveTjx_fj();
									}else {
										layer.closeAll();
										$("#reSendEmailH_"+(Number(mm)-1)).css("display","none");
										$("#reSendEmailS_"+(Number(mm)-1)).css("display","block");
										$("#reSendEmailToMeH_"+(Number(mm)-1)).css("display","none");
										$("#reSendEmailToMeS_"+(Number(mm)-1)).css("display","block");
										$("#changeRecH_"+(Number(mm)-1)).css("display","none");
										$("#changeRecS_"+(Number(mm)-1)).css("display","block");
										alert(result.comContent);
									}
								}
							});
							
	
						} else {
							$("#reSendEmailH_"+(Number(mm)-1)).css("display","none");
							$("#reSendEmailS_"+(Number(mm)-1)).css("display","block");
							$("#reSendEmailToMeH_"+(Number(mm)-1)).css("display","none");
							$("#reSendEmailToMeS_"+(Number(mm)-1)).css("display","block");
							$("#changeRecH_"+(Number(mm)-1)).css("display","none");
							$("#changeRecS_"+(Number(mm)-1)).css("display","block");
							layer.closeAll();
							noteing(MsgSet.SAVE_FAILD + "," + f.state.errdesc, 2);
						}
					}
				});

				/*var param = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"SEND","comParams":{"send_falg":"'+sendFlag+'","rec_app_ins_id":"'+_tz_app_ins_id+'","TZ_APP_INS_VERSION":"'+_tz_app_version_id+'","rec_num":"'+rec_num+'","rec_title":"'+rec_title+'","rec_gname":"'+rec_gname+'","rec_name":"'+rec_name+'","rec_company":"'+rec_company+'","rec_post":"'+rec_post+'","rec_phone_area":"'+rec_phone_area+'","rec_phone_no":"'+rec_phone_no+'","rec_email":"'+rec_email+'","rec_sex":"'+rec_sex+'","rec_relation":"'+rec_relation+'","rec_language":"'+rec_language+'","email_tx":"'+_email_tx+'","rec_by1":"'+rec_by1+'","rec_by2":"'+rec_by2+'","rec_by3":"'+rec_by3+'","rec_by4":"'+rec_by4+'","rec_by5":"'+rec_by5+'","rec_by6":"'+rec_by6+'","rec_by7":"'+rec_by7+'","rec_by8":"'+rec_by8+'","rec_by9":"'+rec_by9+'","rec_by10":"'+rec_by10+'","accessPath":"'+_accessPath+'","filename":"'+_file+'","sysfilename":"'+_sysfile+'"}}';
				layer.load('推荐信发送中，请不要刷新或者关闭页面！');
				$.ajax({
					type: "post",
					url: _Url + encodeURIComponent(param),
					dataType: "json",
					success: function(result){
						if (result.comContent=="SUCCESS"){
							layer.closeAll();
							$("#reSendEmailH_"+(Number(mm)-1)).css("display","none");
							$("#reSendEmailS_"+(Number(mm)-1)).css("display","block");
							$("#reSendEmailToMeH_"+(Number(mm)-1)).css("display","none");
							$("#reSendEmailToMeS_"+(Number(mm)-1)).css("display","block");
							$("#changeRecH_"+(Number(mm)-1)).css("display","none");
							$("#changeRecS_"+(Number(mm)-1)).css("display","block");
							alert(MsgSet["SEND_SC"]);
							$("#app_save").click();
						}else {
							layer.closeAll();
							$("#reSendEmailH_"+(Number(mm)-1)).css("display","none");
							$("#reSendEmailS_"+(Number(mm)-1)).css("display","block");
							$("#reSendEmailToMeH_"+(Number(mm)-1)).css("display","none");
							$("#reSendEmailToMeS_"+(Number(mm)-1)).css("display","block");
							$("#changeRecH_"+(Number(mm)-1)).css("display","none");
							$("#changeRecS_"+(Number(mm)-1)).css("display","block");
							alert(result.comContent);
						}
					}
				}); */
				});
			});
				
				
			//保存推荐信信息
			$("#saveRec_"+(Number(num)-1)).unbind("click"); 
			$("#saveRec_"+(Number(num)-1)).click(function(){
				var lineno = parseInt($(this).closest(".main_inner_content_para").index());
				var m;
				m=Number(lineno)+1;

				var _yz="";
				var _desc="";
				var _name_yz = "";
				
				var rec_title = $("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).val();
				if (children[m-1].recommend_18["useby"]=="Y"&&rec_title==""){
					_desc =   children[m-1].recommend_18["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
				}
				var rec_gname = $("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).val();
				if (children[m-1].recommend_17["useby"]=="Y"&&rec_gname==""){
					_desc =   children[m-1].recommend_17["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
				}
				var rec_name = $("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).val();
				if (children[m-1].recommend_1["useby"]=="Y"&&rec_name==""){
					_desc =  children[m-1].recommend_1["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
					_name_yz = "1";
				}
				var rec_company = $("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).val();
				if (children[m-1].recommend_2["useby"]=="Y"&&rec_company==""){
					_desc =  children[m-1].recommend_2["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
				}
				var rec_post = $("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).val();
				if (children[m-1].recommend_3["useby"]=="Y"&&rec_post==""){
					_desc =  children[m-1].recommend_3["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
				}
				var rec_phone_area = $("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).val();
				if (children[m-1].recommend_16["useby"]=="Y"&&rec_phone_area==""){
					_yz="1";
					_desc =   children[m-1].recommend_16["itemName"]+ MsgSet["REQUIRE"];
					
				}else{
					rec_phone_area = "";
				}
				
				var rec_phone_no = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
				if (children[m-1].recommend_4["useby"]=="Y"&&rec_phone_no==""){
					_yz="1";
					_desc =  children[m-1].recommend_4["itemName"]+ MsgSet["REQUIRE"];
				}
				var rec_email = $("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).val();
				if (children[m-1].recommend_5["useby"]=="Y"&&rec_email==""){
					_yz="1";
					_desc =   children[m-1].recommend_5["itemName"]+ MsgSet["REQUIRE"];
				}
				var rec_sex = $("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).val();
				if (children[m-1].recommend_15["useby"]=="Y"&&rec_sex==""){
					_yz="1";
					_desc =   children[m-1].recommend_15["itemName"]+ MsgSet["REQUIRE"];
				}else if (children[m-1].recommend_15["useby"]!="Y")
				{
					rec_sex="";
				}
				var rec_relation = $("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).val();
				if (children[m-1].recommend_6["useby"]=="Y"&&rec_relation==""){
					_desc =  children[m-1].recommend_6["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
				}
				var rec_language = $("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).val();
				if (children[m-1].recommend_7["useby"]=="Y"&&rec_language==""){
					_desc =   children[m-1].recommend_7["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
				}
				var rec_by1 = $("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).val();
				if (children[m-1].recommend_10["useby"]=="Y"&&rec_by1==""){
					_desc =  children[m-1].recommend_10["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
				}
				var rec_by2 = $("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).val();
				if (children[m-1].recommend_11["useby"]=="Y"&&rec_by2==""){
					_desc =  children[m-1].recommend_11["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
				}
				var rec_by3 = $("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).val();
				if (children[m-1].recommend_12["useby"]=="Y"&&rec_by3==""){
					_desc =   children[m-1].recommend_12["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
				}
				var rec_by4 = $("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).val();
				if (children[m-1].recommend_13["useby"]=="Y"&&rec_by4==""){
					_desc =  children[m-1].recommend_13["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
				}
				var rec_by5 = $("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).val();
				if (children[m-1].recommend_14["useby"]=="Y"&&rec_by5==""){
					_desc =  children[m-1].recommend_14["itemName"]+ MsgSet["REQUIRE"];
					_yz="1";
				}
				var rec_by6 = $("#" + data["itemId"] + children[m-1].recommend_19["itemId"]).val();
				if (children[m-1].recommend_19["useby"]=="Y"&&rec_by6==""){
					_yz="1";
					_desc =  children[m-1].recommend_19["itemName"]+ MsgSet["REQUIRE"];
				}
				var rec_by7 = $("#" + data["itemId"] + children[m-1].recommend_20["itemId"]).val();
				if (children[m-1].recommend_20["useby"]=="Y"&&rec_by7==""){
					_yz="1";
					_desc =  children[m-1].recommend_20["itemName"]+ MsgSet["REQUIRE"];
				}
				var rec_by8 = $("#" + data["itemId"] + children[m-1].recommend_21["itemId"]).val();
				if (children[m-1].recommend_21["useby"]=="Y"&&rec_by8==""){
					_yz="1";
					_desc =   children[m-1].recommend_21["itemName"]+ MsgSet["REQUIRE"];
				}
				var rec_by9 = $("#" + data["itemId"] + children[m-1].recommend_22["itemId"]).val();
				if (children[m-1].recommend_22["useby"]=="Y"&&rec_by9==""){
					_yz="1";
					_desc =   children[m-1].recommend_22["itemName"]+ MsgSet["REQUIRE"];
				}
				var rec_by10 = $("#" + data["itemId"] + children[m-1].recommend_23["itemId"]).val();
				if (children[m-1].recommend_23["useby"]=="Y"&&rec_by10==""){
					_yz="1";
					_desc =  children[m-1].recommend_23["itemName"]+ MsgSet["REQUIRE"];
				}
				
				if (_yz=="" && rec_email!="") {
					var std=/^([\w\-\.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\w\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$/;
					if (!std.test(rec_email)){
						_yz="1";
						_desc =   children[m-1].recommend_5["itemName"]+ MsgSet["FORMAT_ERROR_MSG"];
					}
				}
				
				/*使用新增的行序号*/
				var rec_num = children[m-1].recommend_99["value"];
				var rec_type = $("#" + data["itemId"] + children[m-1].recommend_8["itemId"]).val();
				
				rec_type = "";
				
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
				rec_by6 = SurveyBuild.specialCharReplace(rec_by6);
				rec_by7 = SurveyBuild.specialCharReplace(rec_by7);
				rec_by8 = SurveyBuild.specialCharReplace(rec_by8);
				rec_by9 = SurveyBuild.specialCharReplace(rec_by9);
				rec_by10 = SurveyBuild.specialCharReplace(rec_by10);
				
				_file = SurveyBuild.specialCharReplace(_file);
				_sysfile = SurveyBuild.specialCharReplace(_sysfile);
				_accessPath = SurveyBuild.specialCharReplace(_accessPath);
				
				/*任何情况都可以保存推荐信*/
				if (rec_type=="U" || true)
				{
					console.log("_file"+_file);
					console.log("_sysfile"+_sysfile);
					console.log("_accessPath"+_accessPath);
					console.log("rec_gname"+rec_gname);
					
					rec_language = "";
					var _tz_tjx_valid = "N";
					if(_yz=="" || _name_yz == ""){
						_tz_tjx_valid = "Y";
					}
					
					var _tz_app_ins_id=SurveyBuild.appInsId;
					var _tz_app_version_id=SurveyBuild.appInsVersion;
					var _email_tx = $("#yincang_tx").val();
					var _Url = SurveyBuild.tzGeneralURL + "?tzParams=";
					var param = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"SAVE","comParams":{"rec_app_ins_id":"'+_tz_app_ins_id+'","TZ_APP_INS_VERSION":"'+_tz_app_version_id+'","tjx_valid":"'+_tz_tjx_valid+'","rec_num":"'+rec_num+'","rec_title":"'+rec_title+'","rec_gname":"'+rec_gname+'","rec_name":"'+rec_name+'","rec_company":"'+rec_company+'","rec_post":"'+rec_post+'","rec_phone_area":"'+rec_phone_area+'","rec_phone_no":"'+rec_phone_no+'","rec_email":"'+rec_email+'","rec_sex":"'+rec_sex+'","rec_relation":"'+rec_relation+'","rec_language":"'+rec_language+'","email_tx":"'+_email_tx+ '","rec_type":"'+rec_type+ '","rec_by1":"'+rec_by1+'","rec_by2":"'+rec_by2+'","rec_by3":"'+rec_by3+'","rec_by4":"'+rec_by4+'","rec_by5":"'+rec_by5+'","rec_by6":"'+rec_by6+'","rec_by7":"'+rec_by7+'","rec_by8":"'+rec_by8+'","rec_by9":"'+rec_by9+'","rec_by10":"'+rec_by10+'","accessPath":"'+_accessPath+'","filename":"'+_file+'","sysfilename":"'+_sysfile+'"}}';
					
					$.ajax({
						type: "post",
						url: _Url + encodeURIComponent(param),
						dataType: "json",
						async: false,
						success: function(result){
							if (result.comContent=="SUCCESS"){
								
							}else {
							}
						}
					});
				}
			});
			//更换推荐人
			$("#changeRec_"+(Number(num)-1)).unbind("click");
			$("#changeRec_"+(Number(num)-1)).click(function(){
			
				var lineno ="";
				
		    	//修改判断判断是否是手机版
		    	if(SurveyBuild.accessType=='M'){
		    		lineno=parseInt($(this).closest(".next_record").index());
		    		
		    	}else{
		    		lineno=parseInt($(this).closest(".main_inner_content_para").index());
		    		
		    	}
				var m=Number(lineno)+1;
				var mm = this.id.split("_")[1];
				var mm=Number(mm)+1;
				
				var _tjx_zt_info = $("#tjxzt_info_"+(Number(mm)-1)).val();
				var confirmValue;
				if(_tjx_zt_info=="Z"||_tjx_zt_info=="Y"){
					confirmValue = confirm(MsgSet["CHANGE_TJX_CONFIRM_MSG"]);
				}else{
					confirmValue=true;
				}
				if(confirmValue==true){
					
					var rec_title = $("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).val();
					var rec_gname = $("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).val();
					var rec_name = $("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).val();
					var rec_company = $("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).val();
					var rec_post = $("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).val();
					var rec_phone_area = $("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).val();
					var rec_phone_no = $("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).val();
					var rec_email = $("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).val();
					
					var rec_relation = $("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).val();
					//修改 手机版取值方式不一样
					var rec_sex="",rec_language="";
					if(SurveyBuild.accessType=='M'){
						rec_sex = $('input[name="'+data["itemId"] + children[m-1].recommend_15["itemId"]+'"]:checked').val();
						rec_language=rec_sex = $('input[name="'+data["itemId"] + children[m-1].recommend_7["itemId"]+'"]:checked').val();
					}else{
						//性别
					    rec_sex = $("#" + data["itemId"] + children[m-1].recommend_15["itemId"]).val();
						
						//语言
						 rec_language = $("#" + data["itemId"] + children[m-1].recommend_7["itemId"]).val();
						
					}
				
					var rec_num = children[m-1].recommend_99["value"];
					var rec_by1 = $("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).val();
					var rec_by2 = $("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).val();
					var rec_by3 = $("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).val();
					var rec_by4 = $("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).val();
					var rec_by5 = $("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).val();
					var rec_by6 = $("#" + data["itemId"] + children[m-1].recommend_19["itemId"]).val();
					var rec_by7 = $("#" + data["itemId"] + children[m-1].recommend_20["itemId"]).val();
					var rec_by8 = $("#" + data["itemId"] + children[m-1].recommend_21["itemId"]).val();
					var rec_by9 = $("#" + data["itemId"] + children[m-1].recommend_22["itemId"]).val();
					var rec_by10 = $("#" + data["itemId"] + children[m-1].recommend_23["itemId"]).val();

					
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
					rec_by6 = SurveyBuild.specialCharReplace(rec_by6);
					rec_by7 = SurveyBuild.specialCharReplace(rec_by7);
					rec_by8 = SurveyBuild.specialCharReplace(rec_by8);
					rec_by9 = SurveyBuild.specialCharReplace(rec_by9);
					rec_by10 = SurveyBuild.specialCharReplace(rec_by10);
					
					var _tz_app_ins_id=SurveyBuild.appInsId;
					var _tz_app_version_id=SurveyBuild.appInsVersion;
					var _Url = SurveyBuild.tzGeneralURL + "?tzParams=";
					var param = '{"ComID":"TZ_GD_TJX_COM","PageID":"TZ_SEND_REF_STD","OperateType":"CHANGE","comParams":{"rec_app_ins_id":"'+_tz_app_ins_id+'","TZ_APP_INS_VERSION":"'+_tz_app_version_id+'","rec_num":"'+rec_num+'","rec_title":"'+rec_title+'","rec_gname":"'+rec_gname+'","rec_name":"'+rec_name+'","rec_company":"'+rec_company+'","rec_post":"'+rec_post+'","rec_phone_area":"'+rec_phone_area+'","rec_phone_no":"'+rec_phone_no+'","rec_email":"'+rec_email+'","rec_sex":"'+rec_sex+'","rec_relation":"'+rec_relation+'","rec_language":"'+rec_language+'","email_tx":"'+_email_tx+'","rec_by1":"'+rec_by1+'","rec_by2":"'+rec_by2+'","rec_by3":"'+rec_by3+'","rec_by4":"'+rec_by4+'","rec_by5":"'+rec_by5+'","rec_by6":"'+rec_by6+'","rec_by7":"'+rec_by7+'","rec_by8":"'+rec_by8+'","rec_by9":"'+rec_by9+'","rec_by10":"'+rec_by10+'"}}';
					var _email_tx = $("#yincang_tx").val();
					
					$("#reSendEmailH_"+(Number(mm)-1)).css("display","block");
					$("#reSendEmailS_"+(Number(mm)-1)).css("display","none");
					$("#reSendEmailToMeH_"+(Number(mm)-1)).css("display","block");
					$("#reSendEmailToMeS_"+(Number(mm)-1)).css("display","none");
					$("#changeRecH_"+(Number(mm)-1)).css("display","block");
					$("#changeRecS_"+(Number(mm)-1)).css("display","none");

					$.ajax({
						type: "post",
						url: _Url + encodeURIComponent(param),
						dataType: "json",
						success: function(result){
							if (result.comContent=="SUCCESS"){
								$("#sendEmailS_"+(Number(mm)-1)).css("display","block");
								$("#reSendEmailS_"+(Number(mm)-1)).css("display","none");
								$("#reSendEmailH_"+(Number(mm)-1)).css("display","none");
								
								$("#sendEmailToMeS_"+(Number(mm)-1)).css("display","block");
								$("#reSendEmailToMeS_"+(Number(mm)-1)).css("display","none");
								$("#reSendEmailToMeH_"+(Number(mm)-1)).css("display","none");
								
								$("#changeRecH_"+(Number(mm)-1)).css("display","none");
								$("#changeRecS_"+(Number(mm)-1)).css("display","none");
								$("#tjxzt_desc_"+(Number(mm)-1)).html(MsgSet["ReLeSt"]+"：<span class='blue'>"+MsgSet["Unsent"]+"</span>");
								$("#tjxzt_info_"+(Number(mm)-1)).val("");
								$("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_17["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_1["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_2["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_3["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_4["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_16["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_5["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_6["itemId"]).removeAttr("disabled");
								$("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]").removeAttr("disabled");
								$("input[name="+data["itemId"]+children[m-1].recommend_7["itemId"]+"]").removeAttr("disabled");
								$("input[name="+data["itemId"]+children[m-1].recommend_8["itemId"]+"]").removeAttr("disabled");
								$("input[name="+data["itemId"]+children[m-1].recommend_15["itemId"]+"]").removeAttr("readonlyflag");
								$("input[name="+data["itemId"]+children[m-1].recommend_7["itemId"]+"]").removeAttr("readonlyflag");
								$("input[name="+data["itemId"]+children[m-1].recommend_8["itemId"]+"]").removeAttr("readonlyflag");
								$("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).removeAttr("readonly");
								
								$("#" + data["itemId"] + children[m-1].recommend_19["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_20["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_21["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_22["itemId"]).removeAttr("readonly");
								$("#" + data["itemId"] + children[m-1].recommend_23["itemId"]).removeAttr("readonly");
								
								$("#" + data["itemId"] + children[m-1].recommend_10["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_11["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_12["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_13["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_14["itemId"]).removeAttr("disabled");
								
								$("#" + data["itemId"] + children[m-1].recommend_19["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_20["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_21["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_22["itemId"]).removeAttr("disabled");
								$("#" + data["itemId"] + children[m-1].recommend_23["itemId"]).removeAttr("disabled");
								
								$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).chosen("destroy");
								$("#" + data["itemId"] + children[m-1].recommend_18["itemId"]).chosen();
								
								$("#"+data.itemId + children[m-1].recommend_7["itemId"]+"_E").attr("readonlyflag","N");
								$("#"+data.itemId + children[m-1].recommend_7["itemId"]+"_C").attr("readonlyflag","N");
								$("#"+data.itemId + children[m-1].recommend_8["itemId"]+"_U").attr("readonlyflag","N");
								$("#"+data.itemId + children[m-1].recommend_8["itemId"]+"_S").attr("readonlyflag","N");
								$("#"+data.itemId + children[m-1].recommend_15["itemId"]+"_M").attr("readonlyflag","N");
								$("#"+data.itemId + children[m-1].recommend_15["itemId"]+"_F").attr("readonlyflag","N");
								
								$("#tjx_delete_"+(Number(mm)-1)).show();
								$("#app_save").click();
							}else {
								//$("#reSendEmailH_"+(Number(mm)-1)).css("display","none");
								//$("#reSendEmailS_"+(Number(mm)-1)).css("display","block");
								//$("#reSendEmailToMeH_"+(Number(mm)-1)).css("display","none");
								//$("#reSendEmailToMeS_"+(Number(mm)-1)).css("display","block");
								$("#changeRecH_"+(Number(mm)-1)).css("display","none");
								$("#changeRecS_"+(Number(mm)-1)).css("display","block");
								alert(result.comContent);
							}
						}
					});
				}
			});
			$("#"+data.itemId + children[num-1].recommend_15["itemId"]+"_M").click(function(){
				var readOnly = $(this).attr("readonlyflag");
			    if(readOnly!="Y"){
			    	var lineno = "";
					//判断是否是手机版
					if(SurveyBuild.accessType=='M'){
			    		lineno=parseInt($(this).closest(".next_record").index());
			    		
			    	}else{
			    		lineno=parseInt($(this).closest(".main_inner_content_para").index());
			    		
			    	}
					children[lineno].recommend_15["option"]["MAN"]["checked"] = "Y";
					children[lineno].recommend_15["option"]["WOMAN"]["checked"] = "N";

					var child_M = $(this).find('input:radio');
					var m = child_M.attr("name");
					var value=child_M.val();
					
					$("#"+m).val(value);
					
					$("#"+m).focus();
					$("#"+m).blur();
				}
				
			});
			$("#"+data.itemId + children[num-1].recommend_15["itemId"]+"_F").click(function(){
				var readOnly = $(this).attr("readonlyflag");
			    if(readOnly!="Y"){
					var lineno = "";
					//判断是否是手机版
					if(SurveyBuild.accessType=='M'){
			    		lineno=parseInt($(this).closest(".next_record").index());
			    		
			    	}else{
			    		lineno=parseInt($(this).closest(".main_inner_content_para").index());
			    		
			    	}
					children[lineno].recommend_15["option"]["MAN"]["checked"] = "N";
					children[lineno].recommend_15["option"]["WOMAN"]["checked"] = "Y";
					var child_f = $(this).find('input:radio');

					var m = child_f.attr("name");
					var value=child_f.val();
					
					$("#"+m).val(value);
					$("#"+m).focus();
					$("#"+m).blur();
				}
			});
			$("#"+data.itemId + children[num-1].recommend_8["itemId"]+"_U").click(function(){
				var readOnly = $(this).attr("readonlyflag");
			    if(readOnly!="Y"){
			    	var lineno = "";
			    	//修改判断判断是否是手机版
			    	if(SurveyBuild.accessType=='M'){
			    		lineno=parseInt($(this).closest(".next_record").index());
			    		
			    	}else{
			    		lineno=parseInt($(this).closest(".main_inner_content_para").index());
			    		
			    	}
					
					
					//console.log("lineno"+lineno);
					
					children[lineno].recommend_8["option"]["SEND"]["checked"] = "N";
					children[lineno].recommend_8["option"]["UPLOAD"]["checked"] = "Y";
					var child_U = $(this).find('input:radio');
					var m = child_U.attr("name");
					var value=child_U.val();
					$("#"+m).val(value);
					$("#"+m).val(value);
					var n = child_U.attr("align");
					//手机版推荐信按钮
					$("#sendEmailS_"+n).css("display","none");
					$("#sendEmailToMeS_"+n).css("display","none");
					
					$("#Tjx_SendEmail_"+n).css("display","none");
					$("#Tjxzt_"+n).css("display","none");
					$("#Tjxfj_show_"+n).css("display","block");
				}
			});

			$("#"+data.itemId + children[num-1].recommend_8["itemId"]+"_S").click(function(){
				var readOnly = $(this).attr("readonlyflag");
			    if(readOnly!="Y"){
			    	var lineno = "";
			    	//修改判断判断是否是手机版
			    	if(SurveyBuild.accessType=='M'){
			    		lineno=parseInt($(this).closest(".next_record").index());
			    		
			    	}else{
			    		lineno=parseInt($(this).closest(".main_inner_content_para").index());
			    		
			    	}
			    	
					
					//console.log("lineno"+lineno);
					children[lineno].recommend_8["option"]["SEND"]["checked"] = "Y";
					children[lineno].recommend_8["option"]["UPLOAD"]["checked"] = "N";
					var child_S = $(this).find('input:radio');
					var m = child_S.attr("name");
					var value=child_S.val();
					
					$("#"+m).val(value);
					var n = child_S.attr("align");

					var instanceId = $(this).closest(".dhcontainer").attr("data-instancid");
					var data = SurveyBuild._items[instanceId];
					var child = data.children[n];
					var itemId = data.itemId;
					children[lineno]["recommend_9"].filename = "";
					children[lineno]["recommend_9"].sysFileName = "";
					children[lineno]["recommend_9"].orderby = "";
					children[lineno]["recommend_9"].viewFileName = "";
					$("#"+data.itemId+n+"_AttList").html("");
					//手机版推荐信按钮
					$("#sendEmailS_"+n).css("display","block");
					$("#sendEmailToMeS_"+n).css("display","block");
					//SurveyBuild.Tjxdelete(this,\"'+cins+'\",'+index+')
					console.log("下标"+n);
					$("#Tjx_SendEmail_"+n).css("display","block");
					$("#Tjxzt_"+n).css("display","block");
					$("#Tjxfj_show_"+n).css("display","none");
				}
			});

			$("#"+data.itemId + children[num-1].recommend_7["itemId"]+"_C").click(function(){
				
					var lineno = "";
					//判断是否是手机版
					if(SurveyBuild.accessType=='M'){
			    		lineno=parseInt($(this).closest(".next_record").index());
			    		
			    	}else{
			    		lineno=parseInt($(this).closest(".main_inner_content_para").index());
			    		
			    	}
	                children[lineno].recommend_7["option"]["ZHS"]["checked"] = "Y";
	                children[lineno].recommend_7["option"]["ENG"]["checked"] = "N";
					
					var child_C = $(this).find('input:radio');
					var m = child_C.attr("name");
					var value=child_C.val();
					
					$("#"+m).val(value);
				
					
			
			});
			$("#"+data.itemId + children[num-1].recommend_7["itemId"]+"_E").click(function(){
				var readOnly = $(this).attr("readonlyflag");
			    if(readOnly!="Y"){
			    	var lineno = "";
					//判断是否是手机版
					if(SurveyBuild.accessType=='M'){
			    		lineno=parseInt($(this).closest(".next_record").index());
			    		
			    	}else{
			    		lineno=parseInt($(this).closest(".main_inner_content_para").index());
			    		
			    	}
					children[lineno].recommend_7["option"]["ZHS"]["checked"] = "N";
					children[lineno].recommend_7["option"]["ENG"]["checked"] = "Y";
					
					var child_E = $(this).find('input:radio');
					var m = child_E.attr("name");
					var value=child_E.val();
					
					$("#"+m).val(value);
				}
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
				console.log("行序号"+lineno);
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
			var $tjr_by6 = $("#" + data.itemId + children[i - 1].recommend_19["itemId"]);
			var $tjr_by7 = $("#" + data.itemId + children[i - 1].recommend_20["itemId"]);
			var $tjr_by8 = $("#" + data.itemId + children[i - 1].recommend_21["itemId"]);
			var $tjr_by9 = $("#" + data.itemId + children[i - 1].recommend_22["itemId"]);
			var $tjr_by10 = $("#" + data.itemId + children[i - 1].recommend_23["itemId"]);
			
			var _checkHtml=  function(val,elem,Regular){
				if (val == "") { //判断 是否为为空
					return elem.title+MsgSet["REQUIRE"];
				}  else {
					//正则表达式判断
					if (Regular == 'phone_area') {

					} else if (Regular == 'phone_no') {

					} else if (Regular == 'email') {
						var std=/^([\w\-\.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\w\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$/;
						if (!std.test(val)){
							return elem.title+MsgSet["FORMAT_ERROR_MSG"];
						}
					}
				}	
			};
		    
			
			//称呼
			$tjr_title.formValidator({tipID:data["itemId"] + children[i - 1].recommend_18["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_title.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
				}
			});
			
			//姓名
			$tjr_name.formValidator({tipID:data["itemId"] + children[i - 1].recommend_1["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_name.functionValidator({
				fun:function(val,elem){
					
					return _checkHtml(val,elem,"none");
				}
			});
			
			//名字
			$tjr_gname.formValidator({tipID:data["itemId"] + children[i - 1].recommend_17["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
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
			
			//区号
			$tjr_phone_area.formValidator({tipID:data["itemId"] + children[i - 1].recommend_4["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_phone_area.functionValidator({
				fun:function(val,elem){
					
					return this._checkHtml(val,elem,"phone_area");
				}
			});
			
			//手机
			$tjr_phone_no.formValidator({tipID:data["itemId"] + children[i - 1].recommend_4["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_phone_no.functionValidator({
				fun:function(val,elem){
					return _checkHtml(val,elem,"phone_no");
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
					
					return _checkHtml(val,elem,"none");
				}
			});
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
			$tjr_by6.formValidator({tipID:data["itemId"] + children[i - 1].recommend_19["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by6.functionValidator({
				fun:function(val,elem){
					return _checkHtml(val,elem,"none");
				}
			});
			
			//备用字段七
			$tjr_by7.formValidator({tipID:data["itemId"] + children[i - 1].recommend_20["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by7.functionValidator({
				fun:function(val,elem){
					return _checkHtml(val,elem,"none");
				}
			});
			
			//备用字段八
			$tjr_by8.formValidator({tipID:data["itemId"] + children[i - 1].recommend_21["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by8.functionValidator({
				fun:function(val,elem){
					return _checkHtml(val,elem,"none");
				}
			});
			
			//备用字段九
			$tjr_by9.formValidator({tipID:data["itemId"] + children[i - 1].recommend_22["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by9.functionValidator({
				fun:function(val,elem){
					return _checkHtml(val,elem,"none");
				}
			});
			
			//备用字段十
			$tjr_by10.formValidator({tipID:data["itemId"] + children[i - 1].recommend_23["itemId"] + 'Tip',onShow:"",onFocus:"&nbsp;",onCorrect:"&nbsp;"});
			$tjr_by10.functionValidator({
				fun:function(val,elem){
					return _checkHtml(val,elem,"none");
				}
			});
		}
    }
    
})