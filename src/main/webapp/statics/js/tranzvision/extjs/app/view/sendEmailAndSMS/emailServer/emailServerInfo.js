Ext.define('KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'emailServerInfo', 
	controller: 'emailServerInfoMth',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfoMth',
        'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerModel'
	],
    title: '邮件参数定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'emailServerForm',
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
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_SET_STD.emailservid','邮箱服务器编号'),
			name: 'emailservid',
			value:'NEXT',
			hidden:true
        },{
            xtype: 'combobox',
            fieldLabel: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_SET_STD.emailorg','所属机构'),
			forceSelection: true,
			editable: false,
			store: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_JG_BASE_T',
				/*condition:{
					"TZ_JG_EFF_STA":{
						"value":"Y",
						"operator":"01",
						"type":"01"
					}
				},*/
				condition:{
					TZ_JG_EFF_STA:{
						value:"Y",
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_JG_ID,TZ_JG_NAME'
			}),
			value:Ext.tzOrgID,
			valueField: 'TZ_JG_ID',
            displayField: 'TZ_JG_NAME',
            //typeAhead: true,
            queryMode: 'local',
			name: 'emailorg',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
			listeners:{
            	afterrender: function(combox){
            		//当前登录人机构id
					var orgId = Ext.tzOrgID;
					if(orgId.toUpperCase() != "ADMIN")
					{
						combox.readOnly = true;
					}
	            }
            },
            allowBlank: false
        },{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_SET_STD.emailaddr','电子邮箱'),
			name: 'emailaddr',
			vtype: 'email',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },{
			xtype: 'checkbox',
			boxLabel: '机构默认邮箱',
			name:'isdefault',
			inputValue:'Y',
			uncheckedValue:'N',
			style:'margin-left:104px'
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_SET_STD.chnsname','中文简称'),
			name: 'chnsname'
		}, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_SET_STD.smtpaddr','SMTP服务器'),
			name: 'smtpaddr'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_SET_STD.emlalias','邮箱别名'),
			name: 'emlalias'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_SET_STD.usrname','登录用户名'),
			name: 'usrname',
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_SET_STD.usrpwd','密码'),
			name: 'usrpwd',
			inputType: 'password'
        }, {
            xtype: 'textarea',
            fieldLabel: Ext.tzGetResourse('TZ_EMLSER_MG_COM.TZ_EMLSER_SET_STD.desc','描述'),
			name:'desc'
		}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
