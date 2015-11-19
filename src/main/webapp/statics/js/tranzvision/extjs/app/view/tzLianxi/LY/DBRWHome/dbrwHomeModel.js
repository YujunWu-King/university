/**
 * Created by luyan on 2015/11/19.
 */

Ext.define('KitchenSink.view.tzLianxi.LY.DBRWHome.dbrwHomeModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'wflinsId'},
        {name: 'taskName'},
        {name: 'priority'},
        {name: 'stepTmOut'},
        {name: 'stepTmOutFlag'},
        {name: 'flowTmOut'},
        {name: 'flowTmOutFlag'},
        {name: 'taskType'},
        {name: 'taskCreateOprid'},
        {name: 'taskCreateDate'},
        {name: 'taskCreateTime'},
        {name: 'dealDttm'},
        {name: 'taskStatus'}
    ]
});
