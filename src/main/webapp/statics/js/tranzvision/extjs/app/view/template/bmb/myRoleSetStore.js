/**
 * Created by admin on 2015/9/7.
 */
Ext.define('KitchenSink.view.template.bmb.myRoleSetStore', {
    extend: 'Ext.data.Store',
    alias: 'store.myRoleSetStore',
    model: 'KitchenSink.view.template.bmb.myRoleSetModel',
    autoLoad: true,
    pageSize: 0,
    comID: 'TZ_ONLINE_REG_COM',
    pageID: 'TZ_ONREG_ROLE_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
    /*proxy: {
        type: 'ajax',
        url : '/tranzvision/kitchensink/app/view/template/bmb/myRoleSet.json',
        reader: {
            type: 'json',
            rootProperty: 'comContent.root',
            totalProperty:'comContent.total'
        }
    }*/
});