Ext.define('KitchenSink.view.common.drExcel.drExcelModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'taskDesc'},
        {name: 'ProcessID'},
        {name: 'drState'},
        {name: 'startTime'},
        {name: 'endTime'},
        {name: 'totalNum'},
        {name: 'successNum'},
        {name: 'faildNum'}
    ]
});
