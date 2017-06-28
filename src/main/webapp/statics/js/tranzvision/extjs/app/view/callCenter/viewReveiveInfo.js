    
Ext.define('KitchenSink.view.callCenter.viewReveiveInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'viewReveiveInfo', 
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
		//短信模板
		var smsVarData;
	
		var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"SMSMODEL","comParams":{"ORGID":"' + Ext.tzOrgID + '"}}';
		Ext.tzLoadAsync(tzParams,function(response){
			smsVarData = response.root;
		});
		
		var smsVarStore = Ext.create('Ext.data.Store', {
		    fields: ['smsId', 'smsName'],
		    data : smsVarData
		});
		
		var userAppListStore = new KitchenSink.view.callCenter.userAppListStore();
		
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
	
		
		var materialsColumns = [{
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
	        text: "是否进入材料评审",
	        width:150,
	        dataIndex: 'isOnMaterials'
	    },{
	        text: "材料评审结果",
	        width:120,
	        dataIndex: 'materialResult'
	    }];
	   
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
						type : 'vbox',
						align : 'stretch'
					},
					items : [ {
						xtype : 'displayfield',
						fieldLabel : '来电时间',
						name : 'callDTime'
					}]
				},{
					xtype : 'fieldset',
					title : '报名人信息',
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
							text:'历史来电信息',
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
                            text: '查看更多信息',
                            defaultColor: '',
                            name: 'viewUserBtn',
                            flagType: 'positive',
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
							width : 140,
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
							},{
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
                            handler: 'activeAccount',
                            width: 100
                        },
                        {
                            style: 'margin-left:10px',
                            xtype: 'button',
                            text: '修改密码',
                            defaultColor: '',
                            name: 'updatePsw',
                            flagType: 'positive',
                            setType: 0,
                            handler: 'updatePsw',
                            width: 100
                        },
                        {
                            style: 'margin-left:10px',
                            xtype: 'button',
                            text: '失效当前账号',
                            defaultColor: '',
                            name: 'invalidAccount',
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
                            flagType: 'positive',
                            setType: 0,
                            handler: 'addBlackList',
                            width: 100
                        },
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
                        }]
                    }]
				},{
					xtype : 'fieldset',
					title : '搜索',
					layout : {
						type : 'column'
					},
					style:'padding-bottom:10px',
					items : [ {
						xtype : 'textfield',
						fieldLabel : '姓名',
						name : 'searchName'
					}, {
						xtype : 'textfield',
						fieldLabel : '手机',
						style:'margin-left:10px',
						name : 'searchPhone'
					}, {
						xtype : 'textfield',
						fieldLabel : '邮箱',
						style:'margin-left:10px',
						name : 'searchEmail'
					},{
                        style: 'margin-left:10px',
                        xtype: 'button',
                        text: '搜索',
                        defaultColor: '',
                        name: 'search',
                        flagType: 'positive',
                        setType: 0,
                        handler: 'search',
                        width: 100
                    }]
				},{
                    xtype: 'grid',
                    title: '报名信息',
                    minHeight: 100,
                    name: 'bmInfoList',
                    reference: 'bmInfoList',
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
                },{
                    xtype: 'grid',
                    title: '面试资格',
                    minHeight: 100,
                    style:"margin-top:15px",
                    name: 'materialsGrid',
                    reference: 'materialsGrid',
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
                    columns: materialsColumns,
                    store:userAppListStore,                    
                },{
                    xtype: 'grid',
                    title: '面试结果',
                    minHeight: 100,
                    style:"margin-top:15px",
                    name: 'interviewGrid',
                    reference: 'interviewGrid',
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
			text: '关闭',
			iconCls:"close",
			handler: 'onClose'
		}]
});

function getCookie(name){
    var arrstr = document.cookie.split("; ");
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
    var domain = "." + window.location.host.split(":")[0];

    var cookie = name + "=;path=/;domain=" + domain + ";expires=" + myDate.toGMTString();

    document.cookie = cookie;
}
