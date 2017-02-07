Ext.define('KitchenSink.view.basicData.import.importTplFieldModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'tplId'},
        {name: 'field'},
        {name: 'fieldName'},
        {name: 'seq',type:'number'},
        {name: 'required'},
        {name: 'cover'},
        {name: 'display'},
        {name: 'columnTitle'}
	],
	formatCheckbox:function(){
		this.set("required",this.get("required")=="Y");
		this.set("cover",this.get("cover")=="Y");
		this.set("display",this.get("display")=="Y");
		this.commit();
	}
});
