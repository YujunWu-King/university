Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewAudienceStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewAudienceStore',
    model: new Ext.data.Model({
    	fields: [
            {name: 'id'},
            {name: 'desc'}
        ]
    }),
	autoLoad: true,
    pageSize: 100,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_MS_ARR_SSTU_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy(),
});
