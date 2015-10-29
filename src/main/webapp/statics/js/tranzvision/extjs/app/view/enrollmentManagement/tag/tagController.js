Ext.define('KitchenSink.view.enrollmentManagement.tag.tagController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.tagSet',
    addTag: function(btn) {
        var tagGrid = btn.findParentByType("grid");
        var tagCellEditing = tagGrid.getPlugin('tagCellEditing');
        var tagStore =  tagGrid.getStore();
        var rowCount = tagStore.getCount();
        tagCellEditing.cancelEdit();
            var r = Ext.create('KitchenSink.view.enrollmentManagement.tag.tagModel', {
                tagID:"NEXT",
                tagName: "",
                tagDesc: ""
            });

        tagStore.insert(rowCount,r);
        tagCellEditing.startEdit(r, 1);
    },
    addTagRear: function(view,rowIndex) {
        var tagGrid = view.findParentByType("grid");
        var tagCellEditing = tagGrid.getPlugin('tagCellEditing');
        var tagStore =  tagGrid.getStore();
        var rowCount = rowIndex+1;
        tagCellEditing.cancelEdit();
        var r = Ext.create('KitchenSink.view.enrollmentManagement.tag.tagModel', {
            tagID:"NEXT",
            tagName: "",
            tagDesc: ""
        });

        tagStore.insert(rowCount,r);
        tagCellEditing.startEdit(r, 1);
    },
    deleteTags: function(){
	   //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
           Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
			return;
	   }else{
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.confirm","确认"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.deleteConfirm","您确定要删除所选记录吗?"), function(btnId){
				if(btnId == 'yes'){					   
				   var tagStore = this.getView().store;
                    tagStore.remove(selList);
				}												  
			},this);   
	   }
	},
    deleteTag: function(view, rowIndex){
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.confirm","确认"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.deleteConfirm","您确定要删除所选记录吗?"), function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    saveTags: function(btn){
        //更新操作参数
        var comParams = "";

        //修改json字符串
        var editJson = "";

        //标签信息列表
        var grid = btn.findParentByType("grid");

        //标签信息数据
        var store = grid.getStore();

        //修改记录
        var mfRecs = store.getModifiedRecords();
        var tagCellEditing = grid.getPlugin('tagCellEditing');

        for(var i=0;i<mfRecs.length;i++){
            /*标签名称不能为空*/
            var tagLength=mfRecs[i].get("tagName").length;
                 if(tagLength>30){
                    Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.tagNameIn30Character ","标签名称不能大于30个字符"),
                        function(e){
                            if(e == "ok"|| e == "OK" || e == "确定"){
                            tagCellEditing.startEdit(mfRecs[i], 1);
                            }
                        }
                    )
                return;
                }else{
                     if(mfRecs[i].get("tagID")=="NEXT"){
                         if(tagLength<1){
                             continue;
                         }
                     }
                 }

            //记录查重
            var tagName = mfRecs[i].get("tagName");
            var tagNameCount =0;
            var recIndex = store.findBy(function(record,id){
                if(record.get("tagName")==tagName){
                    tagNameCount++;
                    if(tagNameCount>1){
                        return true;
                    }
                }
            },0);

            if(tagNameCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.tagNameRepeated","标签名称出现重复"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            tagCellEditing.startEdit(mfRecs[i], 1);
                        }
                    }
                )
                return;
            }

            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }

        if(editJson != "")comParams = '"update":[' + editJson + "]";

        //删除json字符串
        var removeJson = "";
        //删除记录
        var removeRecs = store.getRemovedRecords();
        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if(removeJson != ""){
            if(comParams == ""){
                comParams = '"delete":[' + removeJson + "]";
            }else{
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_BMGL_TAG_COM","PageID":"TZ_BMGL_TAG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据

        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    }
});

