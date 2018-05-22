Ext.define('KitchenSink.view.enrollProject.userMg.userMgModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'OPRID'},		
        {name: 'userName'},   //姓名
        {name: 'nationId'},	  //身份证号
        {name: 'mshId'},  
        {name: 'userSex'},
        {name: 'userPhone'},
        {name: 'userEmail'},
        {name: 'applyInfo'},   //报考信息
        {name: 'jihuoZt'},		//激活状态
        {name: 'zcTime'},		//注册时间
        {name: 'bitch'},		//批次名称
        {name: 'acctlock'},
        {name: 'hmdUser'}
        /*{name: 'fieldStr_1'},
        {name: 'fieldStr_2'},
        {name: 'fieldStr_3'},
        {name: 'fieldStr_4'},
        {name: 'fieldStr_5'},
        {name: 'fieldStr_6'},
        {name: 'fieldStr_7'},
        {name: 'fieldStr_8'}*/
	]
});
