Ext.define('KitchenSink.view.classManage.PcglModel', {//班级详情-批次管理列表json格式
    extend: 'Ext.data.Model',
    fields: [
        {name: 'bj_id'},
        {name: 'pc_id'},
        {name: 'pc_name'},
		{name: 'pc_st_time'},
		{name: 'pc_sp_time'},
		{name: 'pc_stbm_time'},
		{name: 'pc_fb'}
	]
});
