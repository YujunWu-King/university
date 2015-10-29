Ext.define('KitchenSink.view.orgmgmt.initOrgInfo', {
    extend: 'Ext.form.Panel',
    xtype: 'initOrgInfo',
	controller: 'orgMg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
	],
    title: '机构初始化设置',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    frame:true,
    renderTo:Ext.getBody(),
    bodyPadding:50,
    items: [{
            fieldLabel:'外部网站与发布',
            xtype:'displayfield',
            value:'<a href="#websiteRelease">外部网站与发布</a>',
            labelWidth:300,
            labelSeparator:': '
        },{
            fieldLabel:'注册信息用户模型',
            xtype:'displayfield',
            value:'<a href="#regManage">注册信息用户模型</a>',
            labelWidth:300,
            labelSeparator:': '
        },{
            fieldLabel:'报名表模板',
            xtype:'displayfield',
            value:'<a href="#enrollTemp">报名表模板</a>',
            labelWidth:300,
            labelSeparator:': '
        },{
            fieldLabel:'报名项目、班级对象及层次关系',
            xtype:'displayfield',
            value:'<a href="#proSet">报名项目、班级对象及层次关系</a>',
            labelWidth:300,
            labelSeparator:': '
        },{
            fieldLabel:'申请进度模型设置',
            xtype:'displayfield',
            value:'<a href="#">申请进度模型设置</a>',
            labelWidth:300,
            labelSeparator:': '
        },{
            fieldLabel:'提交资料内容设置',
            xtype:'displayfield',
            value:'<a href="#">提交资料内容设置</a>',
            labelWidth:300,
            labelSeparator:': '
        },{
            fieldLabel:'邮件模板设置',
            xtype:'displayfield',
            value:'<a href="#emlTempletDef">邮件模板设置</a>',
            labelWidth:300,
            labelSeparator:': '
        },{
            fieldLabel:'短信模板设置',
            xtype:'displayfield',
            value:'<a href="#smsTempletDef">短信模板设置</a>',
            labelWidth:300,
            labelSeparator:': '
        }
    ],
    buttons: [ {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
