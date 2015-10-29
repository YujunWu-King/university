Ext.define('KitchenSink.view.activity.applicants.smsPanel', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.activity.applicants.smsModStore',
		'KitchenSink.view.activity.applicants.sendHistoryGrid',
		'KitchenSink.view.activity.applicants.applicantsController'
    ],
    xtype: 'smsInfoPanel',
	controller: 'applicantsMg',
	title: '发送短信',
	sendType: 'M',
	bmrIdArr:[],
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	 items: [{
        xtype: 'form',
        reference: 'smsSendForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		autoScroll : true,
  		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
             xtype: 'combobox',
            fieldLabel: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_SMS_STD.smsModel","短信模板"),
			emptyText: '请选择短信模板',
			allowBlank: false,
			blankText: '短信模板模板不能为空',
			/*
			store: {
                type: 'smsModStore'
            },
			*/
            valueField: 'TValue',
            displayField: 'TSDesc',
            typeAhead: true,
            mode:"remote",
			name: 'smsModel',
			listeners:{"change":function(){
					var mode = this.value;
					/*
					if (mode=="GXH"){
						this.findParentByType('form').getForm().findField("smsContent").setValue("");
					}else if(mode=="DXMP"){
						this.findParentByType('form').getForm().findField("smsContent").setValue("张三你好，欢迎报名上海交通大学安泰经济与管理学院活动");
					}
					*/
					var obj=this;
					var tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_SMS_STD","OperateType":"smsMode","comParams":{"smsMode":"'+mode+'"}}';
					Ext.tzLoad(tzParams,function(respDate){
						var smsContent = respDate.smsTmpContent;
						obj.findParentByType('form').getForm().findField("smsContent").setValue(smsContent);	
					})
				}
			}
        },{
			xtype:'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_SMS_STD.acceptPhoneNum","收信人"),
			name:'acceptPhoneNum',
			emptyText: '收信人手机号码',
			allowBlank: false,
			blankText: '收信人不能为空',
			regex:/^1\d{10}(?:\;1\d{10})*$/,
			regexText: '手机号码格式不正确'
			//value: '13372637736'	
		},{
			xtype: 'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_SMS_STD.otherPhoneNum","其他收信人"),
			name:'otherPhoneNum',
			emptyText: '其他收信人手机号码'	,
			regex:/^1\d{10}(?:\;1\d{10})*$/,
			regexText: '手机号码格式不正确'
		},{
			xtype: 'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_SMS_STD.smsContent","短信内容"),
			name: 'smsContent',
			height: 200,
			minHeight: 150,
			emptyText: '在此处编辑短信内容',
			allowBlank:false,
			blankText:'短信内容不能为空'
		},{
			xtype: 'hidden',
			name: 'activetyId'	
		}]
	}],
    buttons: [{
		text: '发送',
		iconCls:"save",
		handler: 'onSendMsg'
	}, {
		text: '查看发送历史',
		iconCls:"ensure",
		handler: 'onCheckHistory'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
	
});
