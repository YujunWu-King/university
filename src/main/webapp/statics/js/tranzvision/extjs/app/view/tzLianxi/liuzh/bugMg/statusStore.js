Ext.define('KitchenSink.view.tzLianxi.liuzh.bugMg.statusStore',{
    extend : "Ext.data.Store",
    alias:'store.statusStore',
    fields:[{name:'condition'}],
    data:[
        {condition:'新建',conValue:'0'},
        {condition:'已分配',conValue:'1'},
        {condition:'已修复',conValue:'2'},
        {condition:'成功关闭',conValue:'3'},
        {condition:'重新打开',conValue:'4'},
        {condition:'取消',conValue:'5'}
    ],
    autoLoad:true
});