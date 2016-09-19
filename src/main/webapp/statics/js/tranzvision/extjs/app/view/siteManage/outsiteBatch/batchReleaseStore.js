/**
 * Created by caoy on 2016/9/18.
 */
Ext.define('KitchenSink.view.siteManage.outsiteBatch.batchReleaseStore', {
    extend : 'Ext.data.Store',
    alias : 'store.batchReleaseStore',
    model : 'KitchenSink.view.siteManage.outsiteBatch.batchReleaseModel',// json格式
    comID : 'TZ_GD_WWZDPLFB_COM',
    pageID : 'TZ_GD_PLFB_STD',
    tzStoreParams : '',
    autoLoad : true,
    pageSize : 10,
    proxy : Ext.tzListProxy()
});
