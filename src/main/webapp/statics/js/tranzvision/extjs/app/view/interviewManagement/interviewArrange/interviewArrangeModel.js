Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'batchID'},
		{name: 'msJxNo'},
		{name: 'maxPerson'},
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
		{name: 'msOrderState'},
        {name: 'releaseOrUndo'}
    ]
});
