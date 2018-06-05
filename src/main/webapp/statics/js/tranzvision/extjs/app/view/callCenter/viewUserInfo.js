    
Ext.define('KitchenSink.view.callCenter.viewUserInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'viewUserInfo', 
	controller: 'viewUserController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.ux.DataView.DragSelector',
        'Ext.ux.DataView.LabelEditor',
        'tranzvision.extension.grid.column.Link', 
        'KitchenSink.view.callCenter.userAppListStore',
        'KitchenSink.view.callCenter.viewUserController'
	],
	title: '用户接待单信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	ignoreChangesFlag:true,
	actType: 'update',//默认新增
	initComponent:function(){	
		var callXh = getCookie("callCenterXh");
		console.log("000="+callXh);
		var phone = getCookie("callCenterPhone");
		var type = getCookie("callCenterType");	
		var oprid11 = getCookie("callCenterOprid");
	//	console.log("5555="+oprid11);
		//为避免查询无关人员，如果无phone
		if(phone==null||phone==undefined||phone==""){
			phone = "999999999999999";
		}
		
		var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"GETUSER","comParams":{"phone":"' + phone + '","type":"' + type + '","callXh":"' + callXh + '"}}';
		var oprid = "";
		var historyCount;
		var bmrBmActCount;
		
		Ext.tzLoadAsync(tzParams,function(response){
			oprid = response.OPRID;
			console.log("1111="+oprid);
			historyCount = response.viewHistoryCall;
			console.log("2222="+historyCount);
			bmrBmActCount = response.bmrBmActCount;
		});
		var button = "";
		var buttonHidden = true;
		if(historyCount==null||historyCount==''||historyCount==undefined){
			historyCount = "0";
		}
		if(bmrBmActCount==null||bmrBmActCount==''||bmrBmActCount==undefined){
			bmrBmActCount = "0"
		}
		button = '<span style="text-decoration:underline;color:blue;">查看历史来电记录（' + historyCount + '）</span>';
		
		var bmActButtonText = '<span style="text-decoration:underline;color:blue;">' + bmrBmActCount + '</span>';
		
		var userAppListStore = new KitchenSink.view.callCenter.userAppListStore();
		var formData;		
		
		tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"QF","comParams":{"OPRID":"' + oprid + '","type":"' + type + '","callXh":"' + callXh + '","phone":"' + phone +'"}}';
		
		Ext.tzLoadAsync(tzParams,function(response){
			formData = response;
		});	
		
		var tzStoreParams = '{"cfgSrhId":"TZ_CALLCR_USER_COM.TZ_CALLC_USER_STD.TZ_USER_CALL1_VW","condition":{"OPRID-operator": "01","OPRID-value": "'+ oprid+'"}}';
		
		var buttonDisabled = true;
		if(oprid!=null&&oprid!=""&&oprid!=undefined){
			userAppListStore.tzStoreParams = tzStoreParams;
			userAppListStore.load();
			
			buttonDisabled = false;
		}		
		
		//短信模板
		var smsVarData;
	//	var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"SMSMODEL","comParams":{"ORGID":"SEM"}}';
		var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"SMSMODEL","comParams":{"ORGID":"' + Ext.tzOrgID + '"}}';
		Ext.tzLoadAsync(tzParams,function(response){
			smsVarData = response.root;
		});
		
		var smsVarStore = Ext.create('Ext.data.Store', {
		    fields: ['smsId', 'smsName'],
		    data : smsVarData
		});
		
		var applicantColumns = [{
            text: "序号",
            xtype: 'rownumberer',
            width: 50,
            align: 'center'
        },{
            text: "报名表编号",
            width:120,
            dataIndex: 'appInsId',
            xtype: 'linkcolumn',
            handler: 'viewThisApplicationForm',
            renderer: function(v) {
                this.text = v;
                return;
            }
        },
        {
            text: "班级编号",
            dataIndex: 'classId',
            hidden:true
        },
        {
            text: "批次编号",
            dataIndex: 'batchId',
            hidden:true
        },
        {
            text: "报考方向",
            dataIndex: 'className',
            flex:1
        },{
            text: "报考批次",
            width:120,
            dataIndex: 'batchName'
        },{
            text: "创建时间",
            width:150,
            dataIndex: 'appCreateDtime'
        },{
            text: "报名表状态",
            width:120,
            dataIndex: 'appBmStatus',
            editor: {
                xtype: 'combobox',
                store: new KitchenSink.view.common.store.appTransStore("TZ_APPFORM_STATE"),
                displayField: 'TSDesc',
                valueField: 'TValue',
                editable: false
            },
            width:'10%',
            renderer:function(value,metadata,record){
                if(value=="U"){
                    return "已提交";
                }else if(value=="OUT"){
                    return "撤销";
                }else if(value=="BACK"){
                    return "退回修改";
                }else if(value=="P"){
                	return "预提交"
                }else{
                    return "新建";
                }
            }
        }];
	
		
//		var materialsColumns = [{
//	        text: "序号",
//	        xtype: 'rownumberer',
//	        width: 50,
//	        align: 'center'
//        },{
//	        text: "报名表编号",
//	        width:120,
//	        dataIndex: 'appInsId',
//	        xtype: 'linkcolumn',
//            handler: 'viewThisApplicationForm',
//            renderer: function(v) {
//                this.text = v;
//                return;
//            }
//	    },{
//            text: "班级编号",
//            dataIndex: 'classId',
//            hidden:true
//        },
//        {
//            text: "批次编号",
//            dataIndex: 'batchId',
//            hidden:true
//        },
//        {
//            text: "报考方向",
//            dataIndex: 'className',
//            flex:1
//        },{
//            text: "报考批次",
//            width:120,
//            dataIndex: 'batchName'
//        },{
//	        text: "是否进入材料评审",
//	        width:150,
//	        dataIndex: 'isOnMaterials'
//	    },{
//	        text: "材料评审结果",
//	        width:120,
//	        dataIndex: 'materialResult'
//	    }];
//	   
		var interviewResultColumns = [{
	        text: "序号",
	        xtype: 'rownumberer',
	        width: 50,
	        align: 'center'
        },{
	        text: "报名表编号",
	        width:120,
	        dataIndex: 'appInsId',
	        xtype: 'linkcolumn',
            handler: 'viewThisApplicationForm',
            renderer: function(v) {
                this.text = v;
                return;
            }
	    },{
            text: "班级编号",
            dataIndex: 'classId',
            hidden:true
        },
        {
            text: "批次编号",
            dataIndex: 'batchId',
            hidden:true
        },
        {
            text: "报考方向",
            dataIndex: 'className',
            flex:1
        },{
            text: "报考批次",
            width:120,
            dataIndex: 'batchName'
        },{
	        text: "面试报到时间",
	        width:150,
	        dataIndex: 'interviewDtime'
	    },{
	        text: "面试报到地点",
	        width:150,
	        dataIndex: 'interviewLocation'
	    },{
	        text: "面试结果",
	        width:150,
	        dataIndex: 'interviewResult'
	    }];
	   
		//获取基本信息后删除cookie
		DelCookie("callCenterXh");
		DelCookie("callCenterType");
		DelCookie("callCenterPhone");
		DelCookie("callCenterOprid");
		
		Ext.apply(this,{
			items: [{
				xtype: 'form',
				reference: 'userForm',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyPadding: 10,
				//heigth: 600,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 130,
					labelStyle: 'font-weight:bold'
				},
				listeners:{
					added:function(_this, container, pos, eOpts ){
						var form = _this.getForm();
						if(formData!=""&&formData!=null){
							form.setValues(formData);						
							
							if(formData.titleImageUrl){
								_this.down('image[name=titileImage]').setSrc(TzUniversityContextPath + formData.titleImageUrl);	
							}else{
								_this.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/images/tranzvision/mrtx02.jpg");
							}
						}						
					}
				},
				items: [{
					xtype: 'textfield',
					name: 'receiveId',
					hidden: true
				},{
					xtype: 'textfield',
					name: 'oprId',
					hidden: true
				},{
					xtype: 'textfield',
					name:'titleImageUrl',
					hidden:true
				},{
					xtype: 'textfield',
					name:'phoneNum',
					hidden:true
				},{
					xtype : 'fieldset',
					title : '电话控制区',
					layout : {
						type : 'column',
						align : 'stretch'
					},
					fieldDefaults: {
						msgTarget: 'side',
						labelWidth: 130,
						labelStyle: 'font-weight:bold'
					},
					items : [ {
						xtype : 'displayfield',
						fieldLabel : '来电时间',
						name : 'callDTime'
					},{
						xtype : 'displayfield',
						style :'margin-left:80px',
						fieldLabel : '接听人',
						name : 'staffName'
					}]
				},{
					xtype : 'fieldset',
					title : '报名人信息',
					style : 'padding-top:5px;padding-bottom:10px;',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					items : [{
						layout : {
							type : 'column',
							align:'stretch'
						},
						items:[
							{
								xtype : 'displayfield',
								fieldLabel : '主叫号码',
								labelWidth : 77,
								width:355,
								name : 'callPhoneNum',
								fieldStyle:'color:red;'
							},{
								xtype : 'displayfield',
								name : 'registerStatus'
							},{
				        		xtype:'button',
								text:button,
								textAlign: 'right',
								name:'historyCount',
								border:false,
								width: 160,
								style:{
									background: 'white',
									boxShadow:'none',
									marginLeft:'301px'
								},
								handler: 'viewHistoryCall'
				        	},{
	                            style: 'float:right;margin-right:10px',
	                            xtype: 'button',
	                            text: "查看更多信息",
	                            defaultColor: '',
	                            name: 'viewUserBtn',
	                            flagType: 'positive',
	                            disabled:buttonDisabled,
	                            setType: 0,
	                            handler: 'viewUserBtn',
	                            width: 100
                        }]
					},{
						layout : {
							type : 'column'
						},
						items : [ {
							columnWidth : .15,
							xtype : "image",
							src : "",
							height : 193,
							width : 'auto',
							name : "titileImage"
						}, 
						{
							columnWidth : .8,
							bodyStyle : 'padding-left:30px',
							layout : {
								type : 'vbox',
								align : 'stretch'
							},
							items : [ 
							{
								layout : {
									type : 'column'
								},
								items:[
									{
										xtype : 'displayfield',	
										fieldLabel : '报名人姓名',
										columnWidth:.5,
										name : 'bmrName'										
									},
									{
										xtype : 'displayfield',
										columnWidth:.5,
										fieldLabel : '性别',
										name : 'bmrGender'										
									}
								]
							},{
								layout : {
									type : 'column'
								},
								items:[
									{
										xtype : 'displayfield',					
										fieldLabel : '出生日期',
										columnWidth:.5,
										name : 'bmrBirthdate'										
									},
									{
										xtype : 'displayfield',
										columnWidth:.5,
										fieldLabel : '注册邮箱',
										name : 'bmrRegEmail'	
									}
								]
							},{
								layout : {
									type : 'column'
								},
								items:[
									{
										xtype : 'displayfield',					
										fieldLabel : '注册时间',
										columnWidth:.5,
										name : 'bmrRegDtime'										
									},
									{
										xtype : 'displayfield',
										fieldLabel : '激活状态',
										columnWidth:.5,
										name : 'bmrAccActiveStatus'	
									}
								]
							},{
								layout : {
									type : 'column'
								},
								items:[									
									{
										xtype : 'displayfield',
										columnWidth:.5,
										fieldLabel : '黑名单',
										name : 'bmrBlackList'										
									},
									{
										xtype : 'displayfield',
										columnWidth:.5,
										fieldLabel : '锁定状态',
										name : 'bmrLockStatus'										
									}
								]
							},{
								xtype : 'displayfield',
								fieldLabel : '面试申请号',
								name : 'bmrMshId'	
							},{
								xtype : 'displayfield',
								fieldLabel : '报考方向及面试批次',
								name : 'bmrBkProject'	
							},/*{
								xtype : 'displayfield',
								fieldLabel : '参与活动数',
								name : 'bmrBmActCount',
								renderer:function(v){									
									if(v=="0"){
										return v;
									}else{
										return '<a href="javascript:void(0)">' + v + '</a>'
									}
								}
							}*/{
								layout : {
									type : 'column'
								},
								items:[
									{
										xtype:'label',
										style:'font-weight:bold;',
										width:128,
										text:'参与的活动数:'
									},{
						        		xtype:'button',
										textAlign: 'left',
										text:bmActButtonText,
										name:'bmrBmActCount',
										border:false,
										width: 160,
										style:{
											background: 'white',
											paddingTop: '0px',
											boxShadow:'none'
										},
										handler: 'viewHistoryAct'
						        	}
								]
							}]
						}]
					}]
					
				},{
					xtype : 'fieldset',
					title : '功能操作',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					items : [ {
						xtype : 'textareafield',						
						fieldLabel : '备注',
						name : 'callDesc'
					},{
						xtype:'combobox',
						name:'dealwithZT',
						fieldLabel : '处理状态',
						valueField: 'TValue',
				        displayField: 'TSDesc',
				        store: new KitchenSink.view.common.store.appTransStore("TZ_RXDH_DW_ZT"),
				        queryMode: 'local'
					},{
                        layout: {
                            type: 'column'
                        },
                        padding: '0 0 8px 0',
                        items: [{
                            style: 'margin-left:135px',
                            xtype: 'button',
                            flagType: 'positive',
                            name: 'activeAccount',
                            defaultColor: '',
                            setType: 0,
                            text: '激活账号',
                            disabled:buttonDisabled,
                            handler: 'activeAccount',
                            width: 100
                        },
                        {
                            style: 'margin-left:10px',
                            xtype: 'button',
                            text: '修改密码',
                            defaultColor: '',
                            disabled:buttonDisabled,
                            name: 'updatePsw',
                            flagType: 'positive',
                            setType: 0,
                            handler: 'updatePsw',
                            width: 100
                        },
                        {
                            style: 'margin-left:10px',
                            xtype: 'button',
                            text: '锁定当前账号',
                            defaultColor: '',
                            name: 'invalidAccount',
                            disabled:buttonDisabled,
                            flagType: 'positive',
                            setType: 0,
                            handler: 'invalidAccount',
                            width: 100
                        },
                        {
                            style: 'margin-left:10px',
                            xtype: 'button',
                            text: '加入黑名单',
                            defaultColor: '',
                            name: 'addBlackList',
                            disabled:buttonDisabled,
                            flagType: 'positive',
                            setType: 0,
                            handler: 'addBlackList',
                            width: 100
                        }/*,
                        {
                            style: 'margin-left:10px',
                            xtype: 'button',
                            text: '保存',
                            defaultColor: '',
                            name: 'saveInfo',
                            flagType: 'positive',
                            setType: 0,
                            handler: 'saveInfo',
                            width: 100
                        }*/]
                    }]
				},{
					xtype : 'fieldset',
					title : '搜索',
					layout : {
						type : 'column',
						align: 'stretch'
					},
					fieldDefaults: {
						labelWidth: 80
					},
					style:'padding-bottom:10px;',
					items : [ {
						xtype : 'textfield',
						fieldLabel : '姓名',
						columnWidth: .2,
						name : 'searchName'
					}, {
						xtype : 'textfield',
						fieldLabel : '注册手机',
						style:'margin-left:10px',
						columnWidth: .2,
						name : 'searchPhone'
					}, {
						xtype : 'textfield',
						fieldLabel : '注册邮箱',
						style:'margin-left:10px',
						columnWidth: .25,
						name : 'searchEmail'
					},{
						xtype : 'textfield',
						fieldLabel : '面试申请号',	
						style:'margin-left:10px',
						columnWidth: .2,
						name : 'searchMshId'
					},{
                        style: 'margin-left:10px',
                        xtype: 'button',
                        text: '搜索',
                        defaultColor: '',
                        name: 'search',
                        flagType: 'positive',
                        setType: 0,
                        handler: 'search',
                        columnWidth: .15
                    }]
				},{
                    xtype: 'grid',
                    title: '报名信息',
                    minHeight: 100,
                    name: 'bmInfoList',
                    reference: 'bmInfoList',
                    scrollable:false,
                    columnLines: true,
                    autoHeight: true,
                    plugins: [{
                        ptype: 'cellediting',
                        pluginId: 'judgeCellEditing',
                        clicksToEdit: 1,
                        listeners: {
                            beforeedit: function(editor, context, eOpts) {
                                if (context.field == "judgeID" && context.value.length > 0 && !context.record.isModified('judgeID')) {
                                    return false;
                                }
                            }
                        }
                    }],
                    columns: applicantColumns,
                    store:userAppListStore,                    
                },
 //                   {
//                    xtype: 'grid',
//                    title: '面试资格',
//                    scrollable:false,
//                    minHeight: 100,
//                    style:"margin-top:15px",
//                    name: 'materialsGrid',
//                    reference: 'materialsGrid',
//                    columnLines: true,
//                    autoHeight: true, 
//                    plugins: [{
//                        ptype: 'cellediting',
//                        pluginId: 'judgeCellEditing',
//                        clicksToEdit: 1,
//                        listeners: {
//                            beforeedit: function(editor, context, eOpts) {
//                                if (context.field == "judgeID" && context.value.length > 0 && !context.record.isModified('judgeID')) {
//                                    return false;
//                                }
//                            }
//                        }
//                    }],
//                    columns: materialsColumns,
//                    store:userAppListStore,                    
//                },
                {
                    xtype: 'grid',
                    title: '面试结果',
                    minHeight: 100,
                    style:"margin-top:15px",
                    name: 'interviewGrid',
                    reference: 'interviewGrid',
                    scrollable:false,
                    columnLines: true,
                    autoHeight: true,                        
                    plugins: [{
                        ptype: 'cellediting',
                        pluginId: 'judgeCellEditing',
                        clicksToEdit: 1,
                        listeners: {
                            beforeedit: function(editor, context, eOpts) {
                                if (context.field == "judgeID" && context.value.length > 0 && !context.record.isModified('judgeID')) {
                                    return false;
                                }
                            }
                        }
                    }],
                    columns: interviewResultColumns,
                    store:userAppListStore,
                },{
					xtype : 'fieldset',
					title : '发送短信',
					style : 'margin-top:20px',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					items : [ 
						{
	                        layout: {
	                            type: 'column'
	                        },
	                        padding: '0 0 8px 0',
	                        items: [{
	                        	xtype:'combobox',
	    						name:'smsModel',
	    						width:'70%',
	    						fieldLabel : '短信模板',
	    						valueField: 'smsId',
	    				        displayField: 'smsName',
	    				        store: smsVarStore,
	    				        queryMode: 'local'
	                        },
	                        {
	                            style: 'margin-left:10px',
	                            xtype: 'button',
	                            text: '发送短信',
	                            defaultColor: '',
	                            name: 'sendSms',
	                            flagType: 'positive',
	                            setType: 0,
	                            handler: 'sendSms',
	                            width: 100
	                        }]
						}
					]
				}/*添加位置*/]
			}]
		});
		this.callParent();
	},
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'saveInfo'
	},{
		text: '关闭',
		iconCls:"close",
		handler: 'onClose'
	}]
});

function getCookie(name){
    var arrstr = document.cookie.split("; ");
    console.log(document.cookie);
    for(var i = 0;i < arrstr.length;i ++){
        var temp = arrstr[i].split("=");
        if(temp[0] == name) return unescape(temp[1]);
    }
    return "";
}

function DelCookie(name){

    var me = this;
    var myDate=new Date();
    myDate.setTime(-1000);//设置时间
    var domain = window.location.host.split(":")[0];
    
    var cookie = name + "=;path=/;domain=" + domain + ";expires=" + myDate.toGMTString();
    document.cookie = cookie;
}
