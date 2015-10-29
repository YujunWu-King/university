Ext.define('KitchenSink.view.template.bmb.myBmbStore', {
    extend: 'Ext.data.Store',
    alias: 'store.myBmbStore',
    model: 'KitchenSink.view.template.bmb.myBmbModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_ONLINE_REG_COM',
    pageID: 'TZ_ONREG_MNG_STD',
    tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});