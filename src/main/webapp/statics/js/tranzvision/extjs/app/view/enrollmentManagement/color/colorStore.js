Ext.define('KitchenSink.view.enrollmentManagement.color.colorStore', {
    extend: 'Ext.data.Store',
    alias: 'store.colorStore',
    model: 'KitchenSink.view.enrollmentManagement.color.colorModel',
	autoLoad: true,
    pageSize: 10,
    comID: 'TZ_BMGL_COLOR_COM',
    pageID: 'TZ_BMGL_COLOR_STD',
    tzStoreParams: '{}',
    //sorters : [{
       // property : 'colorSortID', // 指定要排序的列索引
        //direction : 'ASC'
   // }],
    proxy: Ext.tzListProxy()
});
