Ext.define('KitchenSink.view.activity.applicants.weChatPanel', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.activity.applicants.sendHistoryGrid',
		'KitchenSink.view.activity.applicants.applicantsController'
    ],
    xtype: 'weChatInfo',
	id:'formPanel',
	controller: 'applicantsMg',
	title: '发送微信',
	sendType: 'W',
	bmrIdArr:[],
	 items: [{
        xtype: 'form',
        reference: 'weChatSendForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		autoScroll : true,
  		bodyStyle : 'overflow-x:visible; overflow-y:scroll',
		
        fieldDefaults: {
            msgTarget: 'under',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
             xtype: 'displayfield',
            fieldLabel: Ext.tzGetResourse("TZ_GD_BMRGL_COM.TZ_GD_WECHAT_STD.weChatCont","发送内容"),
			name: 'weChatCont', 
			value:'张三你好,欢迎你存二维码，并提前半小时到现场，根据二维码到场签到就解决单身解决圣诞节刷减速电机就多说几句就开始打进决赛就低级四级会计师的考经济UI问你计算机的家口街道上几十块的教师就开始的教师就看'
        },{
			xtype:'displayfield',
			fieldLabel: '',
			hideFiled:true,
			name:'editContent',
			fieldStyle: 'color:#ff0000',
			margin: '0 0 0 105',
			value: '请进入详情保存二维码，并提前半小时到现场，根据二维码到场签到'	
		},{xtype:'label',
			text:"修改以上红色字体的内容",
			margin: '10 0 0 105'
		},{
			xtype: 'textarea',
			fieldLabel: '修改以上红色字体的内容',
			hideLabel:true,
			labelAlign:'top',
			margin: '0 0 0 105',
			value:'请进入详情保存二维码，并提前半小时到现场，根据二维码到场签到',
			listeners : {"change" : function() { 
				//Ext.getCmp('textCont').setValue(this.value);
				this.findParentByType('form').getForm().findField('editContent').setValue(this.value);
				} 
			}
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
