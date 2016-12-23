Ext.define('KitchenSink.view.payment.accountStore', {
    extend: 'Ext.data.Store',
    alias: 'store.accountStore',
    model: 'KitchenSink.view.payment.accountModel',
    autoLoad: true,
    pageSize: 10,
    comID:'TZ_ZFZHGL_COM',
    pageID:'TZ_ZFZHGL_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ZFZHGL_COM.TZ_ZFZHGL_STD.TZ_ZFZHGL_VM","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
//    data:[{
//    	TZ_ACCOUNT_ID:"1001",
//    	TZ_ACCOUNT_NAME:"zhansan",
//    	TZ_ACCOUNT_KEY:"1002kcujd53",
//    	TZ_ACCOUNT_STATE:"0",
//    	TZ_ACCOUNT_DESCRIBE:"建行",
//    	TZ_ACCOUNT_INTERFACE:"WWWW.BAIDU.COM"
//    }]
})
