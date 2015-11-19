/**
 * Created by carmen on 2015/11/9.
 */
Ext.define('KitchenSink.view.ZNX.MsgSentListModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'MsgID'},
        {name: 'MsgFlag'},
        {name: 'SendID'},
        {name: 'ViewStatus'},
        {name: 'MsgSub'},
        {name: 'MsgDate'},
        {name: 'MsgText'},
        {name: 'RecID'}



    ]
});
