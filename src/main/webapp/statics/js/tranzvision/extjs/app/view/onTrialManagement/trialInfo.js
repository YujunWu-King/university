Ext.define('KitchenSink.view.onTrialManagement.trialInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'trialInfo', 
	controller: 'trialManagementController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.onTrialManagement.trialManagementController'
	],
    title: '试用申请人信息',
	bodyStyle:'overflow-y:auto;overflow-x:hidden', 
	actType: 'update',
    items: [{
        xtype: 'form',
        reference: 'trialInfo',
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
            labelWidth: 150,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.seqNum","序号"),
			name: 'seqNum',
			hidden:true,
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.orgName","机构名称"),
			name: 'orgName'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.website","机构网址"),
			name: 'website'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.submitTime","申请提交时间"),
			name: 'submitTime'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.contactName","联系人姓名"),
			name: 'contactName'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.contactPhone","联系人电话"),
			name: 'contactPhone'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.contactTel","座机"),
			name: 'contactTel'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.contactEmail","联系人邮箱"),
			name: 'contactEmail'
        },{
			xtype: 'button',
			text: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.sendEmailButton","发送邮件"),
			ignoreChangesFlag: true,
			maxWidth: 210,
			margin: '0 0 10 155',
			handler: function(btn){
				var fm = btn.findParentByType("form");
				var form = fm.getForm();
				var seqNum = form.findField("seqNum").getValue();
				
				var personList = [];
				personList.push({"seqNum":seqNum});

				var params = {
				        "ComID":"TZ_TRIAL_MNG_COM",
				         "PageID":"TZ_TRIAL_MNG_STD",
				         "OperateType":"U",
				         "comParams":{"add":[{"personList":personList}]}
				};
				
				Ext.tzLoad(Ext.JSON.encode(params),function(audID){
					 Ext.tzSendEmail({
						//发送的邮件模板;
				        "EmailTmpName": ["TZ_ON_TRIAL_TG","TZ_ON_TRIAL_GQ"],
				         //创建的需要发送的听众ID;
				         "audienceId": audID,
				         //是否有附件: Y 表示可以发送附件,"N"表示无附件;
				         "file": "N"
					 });
				});
			}
		}, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.hmsr","来源"),
			name: 'hmsr'
        },{
			xtype: 'combobox',
        	fieldLabel: '审核结果',
        	editable:false,
        	valueField: 'TValue',
        	displayField: 'TLDesc',
        	store: new KitchenSink.view.common.store.appTransStore("TZ_SH_RST"),
        	queryMode: 'local',
    		name: 'shRst'
        },{
			 xtype: 'textarea',
	         fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.remark","备注"),
	         name: 'remark'	
		},{
            xtype: 'combobox',
			fieldLabel:Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.orgId","试用机构"),
			forceSelection: true,
			editable: false,
			store: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_JG_BASE_T',
				condition:{
					TZ_JG_EFF_STA:{
						value:"Y",
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_JG_ID,TZ_JG_NAME'
			}),
            valueField: 'TZ_JG_ID',
            displayField: 'TZ_JG_NAME',
            queryMode: 'remote',
			name: 'orgId',
			listeners:{
                select: function(combo,newValue, eOpts){
                	var form = this.findParentByType("form").getForm();
					form.findField("dlzh").setValue("");
					form.findField("dlzhName").setValue("");
                }
			}
        },{
            layout: {
                type: 'column'
            },
            items:[{
                columnWidth:.5,
                xtype: 'textfield',
                fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.dlzh","试用账号"),
                name: 'dlzh',
                editable: false,
                triggers: {
                    search: {
                        cls: 'x-form-search-trigger',
                        handler: "searchDlzh"
                    }
                }
            },{
                columnWidth:.5,
                xtype: 'displayfield',
                hideLabel: true,
                name: 'dlzhName',
                style:'margin-left:8px'
            }]
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.startTime","试用开始时间"),
			name: 'startTime'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_INF_STD.endTime","试用结束时间"),
			name: 'endTime'
        },{
			xtype: 'combobox',
        	fieldLabel: '是否购买',
        	editable:false,
        	valueField: 'TValue',
        	displayField: 'TLDesc',
        	store: new KitchenSink.view.common.store.appTransStore("TZ_SF_SALE"),
        	queryMode: 'local',
    		name: 'sfSale'
        }
	]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onTrialSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onTrialEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onTrialClose'
	}]
});
