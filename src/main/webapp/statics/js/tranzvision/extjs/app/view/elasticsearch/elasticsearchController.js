Ext.define('KitchenSink.view.elasticsearch.elasticsearchController',{
    extend: 'Ext.app.ViewController',
    alias: 'controller.elasticsearchController',
    //创建索引
    addIndex:function(btn) {
        var tzParams='{"ComID":"TZ_ELASTIC_SRH_COM","PageID":"TZ_ELASTIC_SRH_STD","OperateType":"tzCreateIndex",comParams:{}}';
        Ext.tzSubmit(tzParams,function(responseData) {
            console.log(responseData);
        },"创建索引成功。",true,this);
    },
    //查询
    searchArticle:function(btn) {
        var me = this,
            view = me.getView();
        var panelHtml = view.down("panel").down("panel");
        var form = btn.findParentByType("form").getForm();
        var searchText = form.findField("searchText").getValue();

        var tzParams='{"ComID":"TZ_ELASTIC_SRH_COM","PageID":"TZ_ELASTIC_SRH_STD","OperateType":"tzQueryArticle",comParams:{"searchText":"'+searchText+'"}}';
        Ext.tzLoad(tzParams, function (responseData) {
            console.log(responseData);
            var total = responseData.total;
            var html = responseData.html;

            var showHtml = "<div style='margin:10px;'>共有数据：" + total + "条</div>";
            panelHtml.setHtml(showHtml + html);
        });
    },
    //关闭
    closeElastic:function(btn) {
        this.getView().close();
    }
});
