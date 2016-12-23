Ext.define('KitchenSink.view.payment.platformStore', {
    extend: 'Ext.data.Store',
    alias: 'store.platformStore',
    model: 'KitchenSink.view.payment.platformModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_ZFPTGL_COM',
    pageID:'TZ_ZFPTGL_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ZFPTGL_COM.TZ_ZFPTGL_STD.TZ_ZFPTGL_VM","condition":{"TZ_JG_ID-operator": "01"}}',
    proxy: Ext.tzListProxy()
//    data:[{
//    	TZ_platform_ID:"1001",
//    	TZ_platform_NAME:"zhansan",
//    	TZ_platform_KEY:"1002kcujd53",
//    	TZ_platform_STATE:"0",
//    	TZ_platform_DESCRIBE:"建行",
//    	TZ_platform_INTERFACE:"WWWW.BAIDU.COM"
//    }]
})