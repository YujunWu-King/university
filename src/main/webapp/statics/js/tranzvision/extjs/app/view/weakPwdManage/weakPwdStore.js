Ext.define('KitchenSink.view.weakPwdManage.weakPwdStore', {
    extend: 'Ext.data.Store',
    alias: 'store.weakPwdStore',
    model: 'KitchenSink.view.weakPwdManage.weakPwdModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_WEAK_PWD_COM',
	pageID: 'TZ_WEAK_PWD_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_WEAK_PWD_COM.TZ_WEAK_PWD_STD.TZ_WEAK_PASSWORD","condition":{}}',
	proxy: Ext.tzListProxy()
});
