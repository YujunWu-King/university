Ext.define('KitchenSink.view.security.menu.organStore', {
    extend: 'Ext.data.Store',
    alias: 'store.organStore',
    model: 'KitchenSink.view.security.menu.organModel',
    autoLoad: true,
    pageSize: 10,

    /*
    comID: 'TZ_GD_MENU_COM',
    pageID: 'TZ_GD_MENUADD_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()*/

    proxy: {
        type: 'ajax',
        url : '/tranzvision/kitchensink/app/view/security/menu/organ.json',
        reader: {
            type: 'json',
            rootProperty: 'root'
        }
    }
});
