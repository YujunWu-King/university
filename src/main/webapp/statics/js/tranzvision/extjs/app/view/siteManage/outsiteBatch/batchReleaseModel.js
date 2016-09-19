/**
 * Created by caoy on 2016/9/18.
 */
Ext.define('KitchenSink.view.siteManage.outsiteBatch.batchReleaseModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'batchId'},
        {name: 'batchType'},
        {name: 'objectName'},
        {name: 'batchDate'},
        {name: 'opr'},
        {name: 'endDate'},
        {name: 'batchStatus'}
    ]
});

