Ext.define('KitchenSink.view.judgeMaterialsReview.examineeEvaluateController',{
	extend:'Ext.app.ViewController',
	alias:'controller.examineeEvaluateController',
	//进行评审
	materialsReviewTest:function() {
		var me = this;
		
		//是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PW_CLPS_COM"]["TZ_CLPS_DF_STD"];
        if(pageResSet=="" || pageResSet==undefined) {
            Ext.MessageBox.alert('提示','您没有访问或修改数据的权限');
        }
        //改功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className==""||className==undefined){
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID：TZ_CLPS_DF_STD，请检查配置。');
            return;
        }
        
        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        clsProto = ViewClass.prototype;
        
        if (clsProto.themes) {
            clsProto.themeInfo = clsProto.themes[themeName];

            if (themeName === 'gray') {
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
            } else if (themeName !== 'neptune' && themeName !== 'classic') {
                if (themeName === 'crisp-touch') {
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                }
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
            }
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        
        var classId,className,classStartYear,applyBatchId,applyBatchName,prgName,bmbId;
        classId='124';
        className='全日制招生';
        classStartYear='2018';
        applyBatchId='46';
        applyBatchName='第三批次';
        prgName='在职项目（P班）';
        bmbId='228';
        
        var batchInfo = classStartYear+"年"+applyBatchName+prgName;
        
        var tzParamsBmbUrl='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+bmbId+'"}}';
		var bmbUrl = Ext.tzGetGeneralURL() + "?tzParams=" + encodeURIComponent(tzParamsBmbUrl);
		
        
    	var tzParams = '{"ComID":"TZ_PW_CLPS_COM","PageID":"TZ_CLPS_DF_STD","OperateType":"QF","comParams":{"classId":"'+classId+'","applyBatchId":"'+applyBatchId+'","bmbId":"'+bmbId+'"}}';
    	Ext.tzLoad(tzParams,function(responseData){
    		console.log(responseData);
    		
    		//成绩项拼接
    		var scoreItemFields = [];
    		var leafFieldTips = [];
    		var scoreContent = responseData.scoreContent;
    		
    		for(var i=0;i<scoreContent.length;i++) {
    			var itemId = scoreContent[i].itemId,
    				itemName = scoreContent[i].itemName,
    				itemType = scoreContent[i].itemType,
    				itemIsLeaf = scoreContent[i].itemIsLeaf,
    				itemLevel = scoreContent[i].itemLevel,
    				itemUpperLimit = scoreContent[i].itemUpperLimit,
    				itemLowerLimit = scoreContent[i].itemLowerLimit,
    				itemValue = scoreContent[i].itemValue,
    				itemCommentUpperLimit = scoreContent[i].itemCommentUpperLimit,
    				itemCommentLowerLimit = scoreContent[i].itemCommentLowerLimit ,
    				itemOptions = scoreContent[i].itemOptions,
    				itemComment = scoreContent[i].itemComment,
    				itemDfsm = scoreContent[i].itemDfsm,
    				itemCkwt = scoreContent[i].itemCkwt,
    				itemCkzl = scoreContent[i].itemCkzl;
 
    			var arrRet = [];
    			var fieldcontainer;
    			var tips = [];
    			
    			if(itemType=="C") {
					//叶子节点的评语类型
    				arrRet = me.createLeafCommentFieldContainer(itemId,itemName,itemLevel,itemCommentUpperLimit,itemCommentLowerLimit,itemComment,itemDfsm,itemCkwt,itemCkzl);
    				fieldcontainer = arrRet[0];
    				tips = arrRet[1];
    			} else {
    				if(itemType=="D") {
    					//叶子节点的下拉框类型
    					arrRet = me.createLeafDropdownFieldContainer(itemId,itemName,itemLevel,itemValue,itemOptions,itemDfsm,itemCkwt,itemCkzl);
    					fieldcontainer = arrRet[0];
        				tips = arrRet[1];
    				} else {
    					if(itemIsLeaf=="Y") {
            				//叶子节点
    						arrRet = me.createLeafFieldContainer(itemId,itemName,itemLevel,itemUpperLimit,itemLowerLimit,itemValue,itemDfsm,itemCkwt,itemCkzl);
            				fieldcontainer = arrRet[0];
            				tips = arrRet[1];
    					} else {
            				//非叶子节点
    						fieldcontainer = me.createParentFieldContainer(itemId,itemName,itemLevel,itemValue);	
            			}
    				}
    			}
    			
    			
    			scoreItemFields.push(fieldcontainer);
    			
    			if(tips.length>0) {
    				for(var j=0;j<tips.length;j++) {
        				leafFieldTips.push(tips[j]);
        			}
    			}
    		}
    		
	        cmp = new ViewClass({
	        	classId:classId,
	        	applyBatchId:applyBatchId,
	        	bmbId:bmbId,
	        	bmbUrl:bmbUrl,
	        	scoreItemFields:scoreItemFields
	        });
        
	        cmp.on('afterrender',function(){
	        	//项目批次信息
	        	cmp.down("toolbar").down("displayfield").setValue(batchInfo);
	        	
	        	//考生列表
	        	var examineeList = cmp.down("panel").down("grid[name=examineeListGrid]");
	        	var examineeListStore = examineeList.store;
	        	var tzStoreParams='{"classId":"'+classId+'","applyBatchId":"'+applyBatchId+'","queryType":"KSLB"}';
	        	examineeListStore.tzStoreParams = tzStoreParams;
	        	examineeListStore.load();
        
        	
	        	//报名表区域
	        	//var examineeBmbArea = cmp.down("panel").down("panel[name=examineeBmbArea]");
	        	
	        	
        		//打分区域
        		var scoreForm = cmp.down("panel").down("form[name=scoreForm]").getForm();
        		scoreForm.setValues(responseData);
        		
        		var tips = [];
        		for(var k=0;k<leafFieldTips.length;k++) {
        			var title,target,html;
        			title = leafFieldTips[k].title;
        			target = leafFieldTips[k].target;
        			html = leafFieldTips[k].html;
        			
        			if(title!="" && title!=undefined) {
        				var tipConfig = {
            					title:title,
            					target:scoreForm.findField(target).el,
            					anchor:'left',
            					html:"JUST TEST",
            					width:415,
            					trackMouse:true,
            					autoHide:true,
            					closable:false,	
            			}
        				tips.push(tipConfig);
        			}
        		}
        		
        		scoreForm.tips = Ext.Array.map(tips,function(cfg){
        			cfg.showOnTap = true;
        			cfg.targetOffset = [0-200];
        			return new Ext.tip.ToolTip(cfg);
        		})
        		

        	});
	        
	        tab = contentPanel.add(cmp);
            contentPanel.setActiveTab(tab);
            Ext.resumeLayouts(true);
            if(cmp.floating){
            	cmp.show();
            }
        });

	},
	//返回评审主页面
	returnMaterialsReview:function() {
		
	},
	//考生列表-点击考生姓名
	changeExaminee:function(view,rowIndex) {
		var me = this;
		
		var panel = view.findParentByType("panel[name=materialScore]");
		var scoreForm = panel.down("form[name=scoreForm]").getForm();
		var classIdNow = scoreForm.findField("classId").getValue();
		var applyBatchIdNow = scoreForm.findField("applyBatchId").getValue();
		var bmbIdNow = scoreForm.findField("bmbId").getValue();
		
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var classIdNew = selRec.get("classId");
		var applyBatchIdNew = selRec.get("applyBatchId");
		var bmbIdNew = selRec.get("examineeBmbId");
		
		if (classIdNow==classIdNew && applyBatchIdNow==applyBatchIdNew && bmbIdNow==bmbIdNew) {
			
		} else {
			//报名表区域
        	var examineeBmbArea = panel.down("panel[name=examineeBmbArea]");
        	me.bmbAreaRefresh(examineeBmbArea,bmbIdNew);
			
			
			//打分区域
			var scoreForm = panel.down("form[name=scoreForm]").getForm();
			
			var tzParams = '{"ComID":"TZ_PW_CLPS_COM","PageID":"TZ_CLPS_DF_STD","OperateType":"QF","comParams":{"classId":"'+classIdNew+'","applyBatchId":"'+applyBatchIdNew+'","bmbId":"'+bmbIdNew+'"}}';
	    	Ext.tzLoad(tzParams,function(responseData){
	    		console.log(responseData);
	    		
	    		//成绩项更新
	    		me.scoreItemValueRefresh(scoreForm,responseData.scoreContent);
	    		
	    		//页面赋值
        		scoreForm.setValues(responseData);
	    	});
			
		}
		
		
		
	},
	//保存
	saveExamineeEvaluate:function(btn) {
		var me = this,
			view = me.getView();
		
		var form = view.down("form").getForm();
		form.findField("type").setValue("SAV");

		var saveRet = me.saveEvaluate(form);
		return saveRet;
	},
	//保存并获取下一个考生
	saveAndGetNext:function(btn) {
		var me = this,
			view = me.getView();
	
		var form = view.down("form").getForm();
		form.findField("type").setValue("SGN");
		
		var panel = btn.findParentByType("panel[name=materialScore]");
		
		//保存
		var saveRet = me.saveEvaluate(form);
		
		var messageCode = saveRet.messageCode;
		if(messageCode=="1") {
			
		} else {
					
			var classId = saveRet.classId;
			var applyBatchId = saveRet.applyBatchId;
			var bmbId = saveRet.bmbId;
		
		
			//考生列表刷新
			var examineeList = panel.down("grid[name=examineeListGrid]");
			var examineeListStore = examineeList.store;
			var tzStoreParams='{"classId":"'+classId+'","applyBatchId":"'+applyBatchId+'","queryType":"KSLB"}';
			examineeListStore.tzStoreParams = tzStoreParams;
			examineeListStore.load();
		
		
			//报名表区域更新为新考生信息
			var examineeBmbArea = panel.down("panel[name=examineeBmbArea]");
			me.bmbAreaRefresh(examineeBmbArea,bmbId);
			
			
			//打分区域更新成绩项信息
			var scoreForm = panel.down("form[name=scoreForm]").getForm();
			me.scoreItemValueRefresh(scoreForm,saveRet.scoreContent);
		}
		
	},
	//保存数据
	saveEvaluate:function(form) {
		var strRet;
		if(form.isValid()) {
			var formParams = form.getValues();
			var formJson = Ext.JSON.encode(formParams);
			
			var comParams = "";
			comParams = '"update":['+formJson+']';
			
			var tzParams = '{"ComID":"TZ_PW_CLPS_COM","PageID":"TZ_CLPS_DF_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData) {
				console.log(responseData);
				strRet=responseData;
				if(responseData.messageCode=="1") {
					Ext.MessageBox.alert('提示',responseData.message);
					return;
				}
			},"",true,this);
			
		}
		
		return strRet;
	},
	//打分区，非叶子节点生产
	createParentFieldContainer:function(itemId,itemName,itemLevel,itemValue) {
		var paddingLeft = 40*(itemLevel-1); //每个层级以 40px的倍数向右缩进
		
		var filedLabel = '<span style="padding-left: '+ paddingLeft +'px">'+ itemName +'</span>';
	
		if(itemValue=="") {
			itemValue="--";
		}
		
		//若是第一项“总分”，则分数加粗红色
		if((itemLevel-1)==0 && itemValue!="--") {
			itemValue='<span style="color:red;font-weight:bold;">' + itemValue + '</span>'; 
		}
		
		var fieldcontainer = {
				xtype:'fieldcontainer',
				fieldLabel:filedLabel,
                labelStyle: 'font-weight:bold',
				layout:'hbox',
				labelWidth:150,
				height:30,
				defaults:{
					hideLabel:true,
				},
				items:[{
					xtype:'displayfield',
					name : itemId,
					value: itemValue
				}]		
		}
		
		return fieldcontainer;
	
	},
	//打分区，叶子节点生成，非评语、下拉框类型
	createLeafFieldContainer:function(itemId,itemName,itemLevel,itemUpperLimit,itemLowerLimit,itemValue,itemDfsm,itemCkwt,itemCkzl) {
		var paddingLeft = 40*(itemLevel-1); //每个层级以 40px的倍数向右缩进
		
		var filedLabel = '<span style="padding-left: '+ paddingLeft +'px">'+ itemName +'</span>';
		
		if(itemValue=="") {
			if(itemLowerLimit<0) {
				itemValue=0;
			} else {
				itemValue=itemLowerLimit;
			}
		} 
	
		var fieldcontainer = {
				xtype:'fieldcontainer',
				fieldLabel:filedLabel,
				layout:'hbox',
				labelWidth:200,
				height:30,
				defaults:{
					hideLabel:true,
				},
				items:[{
					xtype:'numberfield',
					name:itemId,
					fieldLabel:itemName,
					value:itemValue,
					margin:'0 5 0 0',
					allowBlank:false,
					maxValue:itemUpperLimit,
					minValue:itemLowerLimit,
					width:120,
				},{
					xtype:'displayfield',
					value:'（'+ itemLowerLimit +' ~ '+ itemUpperLimit +'）',
					width: 130
				},{
                    xtype: 'displayfield',
                    fieldStyle:'text-align:right;',
                    value:'<span style="cursor:pointer;">标准</span>',
                    name:itemId+'BZ',
                    reference:itemId+'BZ',
                    width: 80
                },{
                    xtype: 'displayfield',
                    fieldStyle:'text-align:right;',
                    value:'<span style="cursor:pointer;">说明</span>',
                    name:itemId+'SM',
                    reference:itemId+'SM',
                    width: 80
                },{
                    xtype:'displayfield',
                    fieldStyle:'text-align:right;',
                    value:'<span style="cursor:pointer;">面试方法</span>',
                    name:itemId+'MSFF',
                    reference:itemId+'MSFF',
                    width: 80
                }]		
		}
		
		var titleBz,targetBz,htmlBz,titleSm,targetSm,htmlSm,titleMsff,targetMsff,htmlMsff;
		titleBz = "标准";
		targetBz = itemId+'BZ';
		htmlBz = itemDfsm;
		titleSm = "说明";
		targetSm = itemId+'SM';
		htmlSm = itemCkwt;
		titleMsff = "面试方法";
		targetMsff = itemId+'MSFF';
		htmlMsff = itemCkzl;
		
		var tips = this.createLeafTipsConfig(titleBz,targetBz,htmlBz,titleSm,targetSm,htmlSm,titleMsff,targetMsff,htmlMsff);
		
		var arrRet = [];
		arrRet.push(fieldcontainer);
		arrRet.push(tips);
		
		return arrRet;
	},
	//打分区，叶子节点的评语类型
	createLeafCommentFieldContainer:function(itemId,itemName,itemLevel,itemCommentUpperLimit,itemCommentLowerLimit,itemComment,itemDfsm,itemCkwt,itemCkzl) {
		var paddingLeft = 40*(itemLevel-1); //每个层级以 40px的倍数向右缩进
		
		var filedLabel = '<span style="padding-left: '+ paddingLeft +'px">'+ itemName +'</span>';
		
		itemComment = itemComment.replace('\\n',"\n");
		
		//根据评语字数下限，判断该字段是否必填
		var allowBlank = true;
		if(itemCommentLowerLimit > 0){
			allowBlank = false;
		}
		
		var fieldcontainer = {
				xtype:'fieldcontainer',
				fieldLabel:filedLabel,
				layout:'hbox',
				labelWidth:200,
				defaults:{
					hideLabel:true,
				},
				items:[{
					xtype:'textareafield',
					name:itemId,
					fieldLabel:itemName,
					value:itemComment,
					maxLength:itemCommentUpperLimit,
					minLength:itemCommentLowerLimit,
					margin:'0 5 0 0',
					allowBlank:allowBlank,
					grow: true,
					width:416
				},{
					xtype: 'displayfield',
                    fieldStyle:'text-align:right;',
                    value:'<span style="cursor:pointer;">说明</span>',
                    name:itemId+'SM',
                    reference:itemId+'SM',
                    width: 74
				}]
		}
		
		var titleBz,targetBz,htmlBz,titleSm,targetSm,htmlSm,titleMsff,targetMsff,htmlMsff;
		titleBz = "";
		targetBz = "";
		htmlBz = "";
		titleSm = "说明";
		targetSm = itemId+'SM';
		htmlSm = itemCkwt;
		titleMsff = "";
		targetMsff = "";
		htmlMsff = "";
		
		var tips = this.createLeafTipsConfig(titleBz,targetBz,htmlBz,titleSm,targetSm,htmlSm,titleMsff,targetMsff,htmlMsff);
		
		var arrRet = [];
		arrRet.push(fieldcontainer);
		arrRet.push(tips);
		
		return arrRet;
	
	},
	//打分区，叶子节点的下拉框类型
	createLeafDropdownFieldContainer:function(itemId,itemName,itemLevel,itemValue,itemOptions,itemDfsm,itemCkwt,itemCkzl) {
		var paddingLeft = 40*(itemLevel-1); //每个层级以 40px的倍数向右缩进
		
		var filedLabel = '<span style="padding-left: '+ paddingLeft +'px">'+ itemName +'</span>';
		
		var store = Ext.create("Ext.data.Store",{
			fields:['itemOptionName','itemOptionValue'],
			data:itemOptions
		})
		
		var fieldcontainer = {
				xtype:'fieldcontainer',
				fieldLabel:filedLabel,
				layout:'hbox',
				labelWidth:200,
				height:30,
				defaults:{
					hideLabel:true,
				},
				items:[{
					xtype:'combobox',
					name:itemId,
					fieldLabel:itemName,
					store:store,
					autoShow:true,
					valueField:'itemOptionValue',
					displayField:'itemOptionName',
					queryMode:'local',
					editable:false,
					value:itemValue,
					margin:'0 5 0 0',
					allowBlank:false,
					width:250
				},{
                    xtype: 'displayfield', 
                    fieldStyle:'text-align:right;',
                    value: '<span style="cursor:pointer;">标准</span>',
                    name:itemId+'BZ',
                    reference:itemId+'BZ',
                    width: 80
                },{
                    xtype: 'displayfield',
                    fieldStyle:'text-align:right;',
                    value: '<span style="cursor:pointer;">说明</span>',
                    name:itemId+'SM',
                    reference:itemId+'SM',
                    width: 80
                },{
                    xtype:'displayfield',
                    fieldStyle:'text-align:right;',
                    value: '<span style="cursor:pointer;">面试方法</span>',
                    name:itemId+'MSFF',
                    reference:itemId+'MSFF',
                    width: 80
                }]		
		}
		
		var titleBz,targetBz,htmlBz,titleSm,targetSm,htmlSm,titleMsff,targetMsff,htmlMsff;
		titleBz = "标准";
		targetBz = itemId+'BZ';
		htmlBz = itemDfsm;
		titleSm = "说明";
		targetSm = itemId+'SM';
		htmlSm = itemCkwt;
		titleMsff = "面试方法";
		targetMsff = itemId+'MSFF';
		htmlMsff = itemCkzl;
		
		var tips = this.createLeafTipsConfig(titleBz,targetBz,htmlBz,titleSm,targetSm,htmlSm,titleMsff,targetMsff,htmlMsff);
		
		var arrRet = [];
		arrRet.push(fieldcontainer);
		arrRet.push(tips);
		
		return arrRet;
		
	},
	//创建提示信息
	createLeafTipsConfig:function(titleBz,targetBz,htmlBz,titleSm,targetSm,htmlSm,titleMsff,targetMsff,htmlMsff) {
		var tips = [{
			title:titleBz,
			target:targetBz,
			html:htmlBz
		},{
			title:titleSm,
			target:targetSm,
			html:htmlSm
		},{
			title:titleMsff,
			target:targetMsff,
			html:htmlMsff
		}]
		
		return tips;
	},
	//报名表区域更新
	bmbAreaRefresh:function(panel,bmbId) {		
		var tzParamsBmbUrl='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+bmbId+'"}}';
		var bmbUrl = Ext.tzGetGeneralURL() + "?tzParams=" + encodeURIComponent(tzParamsBmbUrl);
		
		panel.down("uxiframe").load(bmbUrl);
	},
	//成绩项数值更新
	scoreItemValueRefresh:function(form,scoreContent) {
		
		for(var i=0;i<scoreContent.length;i++) {
			var itemId = scoreContent[i].itemId,
				itemName = scoreContent[i].itemName,
				itemType = scoreContent[i].itemType,
				itemIsLeaf = scoreContent[i].itemIsLeaf,
				itemLevel = scoreContent[i].itemLevel,
				itemUpperLimit = scoreContent[i].itemUpperLimit,
				itemLowerLimit = scoreContent[i].itemLowerLimit,
				itemValue = scoreContent[i].itemValue,
				itemCommentUpperLimit = scoreContent[i].itemCommentUpperLimit,
				itemCommentLowerLimit = scoreContent[i].itemCommentLowerLimit ,
				itemOptions = scoreContent[i].itemOptions,
				itemComment = scoreContent[i].itemComment,
				itemDfsm = scoreContent[i].itemDfsm,
				itemCkwt = scoreContent[i].itemCkwt,
				itemCkzl = scoreContent[i].itemCkzl;
			
			
			var scoreFormField = form.findField(itemId);
			var itemValueNew = itemValue;
			if(scoreFormField!=null) {
				//判断是否存在该字段，若存在则更新数值
				if(itemIsLeaf=="N") {
					if(itemValue=="") {
						itemValueNew="--";
					} else {
						if((itemLevel-1)==0 && i==0) {
							itemValueNew = '<span style="color:red;font-weight:bold;">' + itemValue + '</span>';
						}
					}
				} else {
					if(itemType=="B") {
						//数字成绩录入项
						if(itemValue=="") {
							if(itemLowerLimit<0) {
								itemValueNew=0;
							} else {
								itemValueNew=itemLowerLimit;
							}
						} 
					} else {
						if(itemType=="C") {
    						//评语类型
							itemValueNew = itemComment.replace('\\n',"\n");
    					}	
					}
				}
				scoreFormField.setValue(itemValueNew);
			} 
		}
	}
});