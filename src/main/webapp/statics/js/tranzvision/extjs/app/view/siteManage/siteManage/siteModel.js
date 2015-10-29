Ext.define('KitchenSink.view.siteManage.siteManage.siteModel', {//站点管理列表json格式
    extend: 'Ext.data.Model',
    fields: [
        {name: 'usAccNum'},
        {name: 'usName'},
        {name: 'mobile'},
        {name: 'email'},
        {name: 'birthday', type: 'date'}
	]
});
