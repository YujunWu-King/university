Ext.define('KitchenSink.view.bzScoreMathCalcuter.bzScoreMathDetailStore', {
    extend: 'Ext.data.Store',
    alias: 'store.bzdetailinfoStore',
    model: 'KitchenSink.view.bzScoreMathCalcuter.bzScoreMathDetailModel',
    pageSize:200,
    comID: 'TZ_BZCJ_SRC_COM',
    pageID: 'TZ_BZCJ_YSF_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});