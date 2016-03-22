var imgWin;
Ext.define('KitchenSink.view.orgmgmt.createOrgStepPanel',{
	 extend: 'Ext.panel.Panel',
	 title: '新建机构步骤引导',
	 autoHeight: true,
     layout: 'fit',
     resizable: true,
	 autoScroll: true,
	 listeners: {
		beforerender: function(panel) {
			
		}
	 },
	 items:[{
		xtype:'dataview',
		frame:true,
	    autoHeight: true,
		layout:'hbox',
		frame        : false,
		cls          : 'dd',
		itemSelector : 'dd',
		overItemCls  : 'over',
		trackOver    : true,
		tpl : Ext.create('Ext.XTemplate',
            '<div id="all-style" >','<div id="style-ct">',
                    '<div class="thumb-list3">',
						'<h2>【平台端】</h2>',
                        '<ul>',
							'<li>1. 新增机构</li>',
							'<li>2. 管理机构，复制模板角色</li>',
							'<li>3. 管理机构，新增机构管理员，设置角色为机构管理员</li>',
							'<li>4. 创建机构菜单（复制“平台管理机构”的菜单）</li>',
							'<li>5. 配置机构邮件服务器参数</li>',
                        '</ul>',
						'<h2>【机构端，使用机构管理员登录机构系统】</h2>',
                        '<ul>',
							'<li>1. 创建机构邮件模板（使用“复制功能”，复制功能在列表的“更多操作”中），菜单路径：基础设置 > 业务模板设置 > 邮件模板配置</li>',
							'<li>2. 创建机构短信模板（使用“复制功能”，复制功能在列表的“更多操作”中），菜单路径：基础设置 > 业务模板设置 > 短信模板配置</li>',
							'<li>3. 配置用户注册项，用户注册激活方式，菜单路径：基础设置 > 会员数据项管理</li>',
                        '</ul>',
						'<h2>【开通机构招生网站，使用机构管理员登录机构系统】</h2>',
                        '<ul>',
							'<li>1. 开通网站，菜单路径：基础设置 > 招生网站设置 > 站点信息设置</li>',
							'<li>2. 选择皮肤，菜单路径：基础设置 > 招生网站设置 > 站点风格选择</li>',
							'<li>3. 编辑/发布网站，菜单路径：基础设置 > 招生网站设置 > 站点页面设置</li>',
                        '</ul>',
                    '</div>',
            '</div>','</div>'),
		listeners:{
			
		}
	 }],
	 buttons: [
		{ text: '关闭',iconCls:"close",handler: function(){
			this.up("panel").close();
	 }}
	]
});