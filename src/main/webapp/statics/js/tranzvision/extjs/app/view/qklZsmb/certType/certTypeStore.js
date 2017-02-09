Ext.define('KitchenSink.view.qklZsmb.certType.certTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.certTypeStore',
    model:'KitchenSink.view.qklZsmb.certType.certTypeModle',
	autoLoad: true,
    pageSize: 50,
    comID: 'TZ_CERTTYPE_COM',
    pageID: 'TZ_TYPE_LIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_CERTTYPE_COM.TZ_TYPE_LIST_STD.TZ_CERT_TYPE_TBL","condition":{}}',
    proxy: Ext.tzListProxy()
});