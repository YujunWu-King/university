try{
	var $mainlistform = $("#main_list");
	var $targetObj1 = $("div[data-instancid='A1435904002639']",$mainlistform);
	var $targetObj2 = $("div[data-instancid='A1435904005385']",$mainlistform);
	//console.log($targetObj1.html());
	//console.log($targetObj2.html());
	function AssociatedShowHideAction($this){
		var selectVal = $this.attr("value");
		//console.log($this);
		//console.log("selectVal=" + selectVal);
		//console.log($this.is(":checked"));
		switch(selectVal){
			case "1":
				if($this.is(":checked")){
					$targetObj1.show().attr("showStatus","1");
				}else{
					$targetObj1.hide().attr("showStatus","0");
				}
				break;
			case "2":
				if($this.is(":checked")){
					$targetObj2.show().attr("showStatus","1");
				}else{
					$targetObj2.hide().attr("showStatus","0");
				}
				break;
		}
	}
	
	if(typeof($this)==="undefined"){
		//页面初始化时执行以下代码
		/*对于checkbox和radio使用以下代码*/
		//$inputObjects以在SurveyBuild_4.js的校验规则事件绑定中定义
		$inputObjects.each(function(){
			AssociatedShowHideAction($(this));
		});
		
		/*对于text和select使用以下代码*/
		//$inputObject以在SurveyBuild_4.js的校验规则事件绑定中定义
		//AssociatedShowHideAction($inputObject);
	}else{
		//点击控件时执行以下代码
		AssociatedShowHideAction($this);
	}
}catch(e){
	console.log('关联操作异常');
	console.log(e);
}