Ext.define('KitchenSink.view.uniPrint.uniPrintTplFieldModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'TZ_JG_ID'},
        {name: 'TZ_DYMB_ID'},
        {name: 'TZ_DYMB_FIELD_ID'},
        {name: 'TZ_DYMB_FIELD_SM'},
        {name: 'TZ_DYMB_FIELD_QY', type: 'boolean', defaultValue: false},
        {name: 'TZ_DYMB_FIELD_PDF'}
	]
});
