Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'batchID'},
		{name: 'msJxNo'},
		{name: 'msGroupId'},
        {name: 'msGroupSn'},
        {name: 'msDate',type:'date'},
	    {name: 'bjMsStartTime',convert:function(v){
            if (v!=""){
                if(Ext.isDate(v)){
                    return v;
                }else{
                    var dt = new Date("January 01, 1900 "+v+":00");
                    return dt;
                }
            }else{
                return "";
            }
        }},
        {name: 'bjMsEndTime',convert:function(v){
            if (v!=""){
                if(Ext.isDate(v)){
                    return v;
                }else{
                    var dt = new Date("January 01, 1900 "+v+":00");
                    return dt;
                }
            }else{
                return "";
            }
        }},
		{name: 'msXxBz'},
		{name: 'msOprId'},
        {name: 'msClearOprId'},
		{name: 'msOprName'},
		{name: 'msOrderState'},
        {name: 'msConfirmState'},
        {name: 'city'},
        {name: 'country'},
        {name: 'skypeId'},
        {name: 'lxEmail'},
        {name: 'timezone'},
        {name: 'timezoneDiff'},
        {name: 'localStartDate'},
        {name: 'localStartTime'},
        {name: 'localFinishDate'},
        {name: 'localFinishTime'},
        {name: 'sort'},
        {name: 'releaseOrUndo'}
    ]
});
