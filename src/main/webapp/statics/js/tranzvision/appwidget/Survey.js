var Survey = {
	mode:function(){
		$.fancybox.open({
			type:'ajax',
			href:'/ajax/define?op=mode',
			helpers:{overlay:{closeClick:false}},
			afterShow:function(){
				$(".mode-list a").on("click",function(){
					$(this).tab('show');
				});
				$("#mode-copy,#mode-template").on("change",function(){
					var $this=$(this),$next=$this.parent().next(".mode-question");
					$next.html('数据加载中...')
					if($this.val()!=""){
						$.get("/ajax/define",{op:"survey",sid:$this.val()},function(data){
							var html='';
							for(var i in data.content){
								html+='<li><span class="pull-left">Q'+data.content[i]['orderby']+'.</span><span class="pull-left" style="width:420px">'+data.content[i]['question']+'</span></li>';
							}
							$next.html(html);
						},"JSON");
					}else{
						$next.html('');
					}
				});
			}
		});
	},
	create:function(){
		var mode=$(".mode-list>li.active").index(),sid='',title='',msg='';
		switch(mode){
			case 0:title=$.trim($("#mode-new").val());msg='请填写问卷名称';break;
			case 1:sid=$("#mode-copy").val();msg='请选择问卷';break;
			case 2:sid=$("#mode-template").val();msg='请选择模板';break;
		}
		if(title=='' && sid==''){
			alert(msg);
		}else{
			$.get("/ajax/define",{op:"create",sid:sid,title:title},function(data){
				if(data.code==0){
					location.href='/survey/build/'+data.content;
				}else{
					alert(data.content);
				}
			},"JSON");
		}
	},
	clear:function(sid){
		if(confirm("请慎重操作，答案一旦清空不可恢复？")){
			$.get("/ajax/survey",{op:"clear",sid:sid},function(data){
				noteing("清除数据成功");
			},"JSON");
		}
	},
	copy:function(sid){
		$.get("/ajax/survey",{op:"copy",sid:sid},function(data){
			if(data.code>0){
				alert(data.content);
			}else{
				noteing("复制问卷成功");
				location.reload();
			}
		},"JSON");
	},
	remove:function(sid){
		if(confirm("请慎重操作，问卷一旦删除不可恢复!")){
			$.get("/ajax/survey",{op:"remove",sid:sid},function(data){
				if(data.code==1){
					alert(data.content);
				}else{
					location.reload();
				}
			},"JSON");
		}
	},
	settings:function(sid){
		$.fancybox.open({
			type:'ajax',
			href:'/ajax/survey?op=settings&sid='+sid,
			helpers:{overlay:{closeClick:false}},
			beforeShow:function(){
				window.end_editor=KindEditor.create("#end_text",{
					items:[
						'source','fontname', 'fontsize', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image','flash', 'link', 'wordpaste'
					],
					allowFileManager: true,
					filterMode:false
				});
				window.screen_editor=KindEditor.create("#screen_text",{
					items:[
						'source','fontname', 'fontsize', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image','flash', 'link', 'wordpaste'
					],
					allowFileManager: true,
					filterMode:false
				});
				window.quota_editor=KindEditor.create("#quota_text",{
					items:[
						'source','fontname', 'fontsize', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline', 'strikethrough',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image','flash', 'link', 'wordpaste'
					],
					allowFileManager: true,
					filterMode:false
				});
				$("#is_expire").on("change",function(){
					$(this).prop("checked") ? $("#is_expore_btw").show() : $("#is_expore_btw").hide();
				});
				var startTimeBox = $('#start_time');
				var endTimeBox = $('#end_time');
				
				startTimeBox.datetimepicker({ 
					timeFormat: 'HH:mm',
					onSelect: function (selectedDateTime){
						endTimeBox.datetimepicker('option', 'minDate', startTimeBox.datetimepicker('getDate') );
					}
				});
				endTimeBox.datetimepicker({ 
					timeFormat: 'HH:mm',
					onSelect: function (selectedDateTime){
						startTimeBox.datetimepicker('option', 'maxDate', endTimeBox.datetimepicker('getDate') );
					}
				});
			},
			beforeClose:function(){
				KindEditor.remove("#end_text");
				KindEditor.remove("#screen_text");
				KindEditor.remove("#quota_text");
			}
		});
	},
	edit:function(sid){
		var $is=true,title=$.trim($("#title").val()),stype=$("#stype").val(),template=$("#template").val(),lang=$("#lang").val(),is_code=$("input[name=is_code]:checked").val(),is_progress=$("#is_progress").prop("checked")?1:0,is_back=$("#is_back").prop("checked")?1:0,is_autorederict=$("#is_autorederict").prop("checked")?1:0,is_pm=$("#is_pm").prop("checked")?1:0,is_ip=$("#is_ip").prop("checked")?1:0,is_cookie=$("#is_cookie").prop("checked")?1:0,is_token=$("#is_token").prop("checked")?1:0,is_expire=$("#is_expire").prop("checked")?1:0,start_time=$("#start_time").val(),end_time=$("#end_time").val(),end_text=end_editor.html(),end_url=$("#end_url").val(),screen_text=screen_editor.html(),screen_url=$("#screen_url").val(),quota_text=quota_editor.html(),quota_url=$("#quota_url").val();
		if(title==""){
			$("#title").popover({content:"问卷标题不能为空",placement:"right"}).popover("show");
			setTimeout(function(){$(".popover").remove();},2000);
			$is=false;
		}
		if(is_expire && (start_time=="" || end_time=="")){
			$("#is_expire").siblings(".muted").popover({content:"请定义问卷有效期",placement:"right"}).popover("show");
			setTimeout(function(){$(".popover").remove();},2000);
			$is=false;
		}
		if($is){
			loading($(".fancybox-inner"));
			$.post("/ajax/define",{op:"edit",sid:sid,title:title,stype:stype,template:template,lang:lang,is_code:is_code,is_progress:is_progress,is_back:is_back,is_autorederict:is_autorederict,is_pm:is_pm,is_ip:is_ip,is_cookie:is_cookie,is_token:is_token,is_expire:is_expire,start_time:start_time,end_time:end_time,end_text:end_text,end_url:end_url,screen_text:screen_text,screen_url:screen_url,quota_text:quota_text,quota_url:quota_url},function(data){
				if(data.code>0){
					loaded($(".fancybox-inner"));
					noteing(data.content);
				}else{
					$.fancybox.close();
					noteing('保存成功');
				}
			},"JSON");
		}
	},
	updateStatus:function(obj){
		loading($('body'));
		var sid = $(obj).attr('data-sid');
		var status = $(obj).attr('data-status');
		var newStatus = status== '1' ? '2': '1';
		$.post("/ajax/survey",{op:"status",sid:sid,status:newStatus},function(data){
			if(data.code>0){
				if(status == '1'){
					$(obj).attr({"title":"停止","data-status":"2"}).children('i').removeClass('icon-play').addClass('icon-stop');
				}
				else{
					$(obj).attr({"title":"进行中","data-status":"1"}).children('i').removeClass('icon-pause').removeClass('icon-stop').addClass('icon-play');
				}
				$(obj).children('i').toggleClass('status-live');
			}
			loaded($('body'));
		},"json");
	},
	gotoBulid:function(sid,status){
		if(status == '1'){
			if(confirm('此项目为进行状态，建议暂停后再修改。\n坚持修改请点击确定，放弃修改请点击取消。')){
				window.location.href='/survey/build/'+sid;
			}
		}else{
			window.location.href='/survey/build/'+sid;
		}
	},
	getUser:function(){
		var email=$("#email").val();
		if(email!=""){
			loading($("#modal-survey-settings"));
			$.get("/ajax/survey",{op:"getUser",email:email},function(data){
				var $html='';
				for(var i in data.content){
					$html+='<li id="'+data.content[i].user_id+'">'+data.content[i].email+'<a href="javascript:;" onclick="$(this).parent().remove()"><i class="icon-remove"></i></a></li>';
				}
				$("#share-list").append($html);
				loaded($("#modal-survey-settings"));
			},"JSON");
		}
	},
	getShareBox:function(sid){
		$.fancybox.open({
			type:'ajax',
			href:'/ajax/survey?op=getShareBox&sid='+sid,
			helpers:{overlay:{closeClick:false}}
		});
	},
	addShare:function(obj){
		var $parent=$(obj).parent().clone();
		$parent.find("a").replaceWith('<a href="javascript:;" onclick="Survey.removeShare(this)"><i class="icon-remove"></i></a>');
		$("#share-list").append($parent);
		$(obj).parent().remove();
	},
	removeShare:function(obj){
		var $parent=$(obj).parent().clone();
		$parent.find("a").replaceWith('<a onclick="Survey.addShare(this)" href="javascript:;"><i class="icon-share-alt"></i></a>');
		$("#user-list").append($parent);
		$(obj).parent().remove();
	},
	setShare:function(sid){
		var share_id='';
		$("#share-list li").each(function(){
			share_id+=','+$(this).attr("id");
		});
		if(share_id!="") share_id=share_id.substr(1);
		$.get("/ajax/survey",{op:"setShare",sid:sid,share_id:share_id},function(data){
			if(data.code==0){
				$.fancybox.close();
				noteing("保存成功");
			}else{
				noteing("保存失败");
			}
		},"JSON");
	},
	removeLogo:function(sid,logo){
		$.get("/ajax/survey",{op:"removeLogo",sid:sid,logo:logo},function(data){
			if(data.code==0){
				$("#surveyLogo").empty();
				noteing("删除成功");
			}else{
				noteing("删除失败");
			}
		},"JSON");
	}
};