$(document).ready(function(){
   
   // 切换
     tab(".zhaos .list",".zhaos .tabNote","list_on","mousedown");
     tab(".Tab_school .tit_tab",".Tab_school .tabN_school","school_on","mousedown");
    
      $(".tit_tab  li").click(function(){
                $(this).parents("").find("li").removeClass("school_on");
                $(this).parents("").find(".school_t").hide();
                $(this).addClass("school_on");
                $(this).find(".school_t").show();
              });
    //关闭弹窗
     $("#pop_btn").click(function(){
       $(".shade1").hide();
       $(".pop").hide();
      
    });

      $(".share_remind").click(function(){
       $(".shade1").show();
       $(".pop").show();
      
    });

     //查看流程信息
   $(".search_step").click(function(){
         
         $(this).children('i').toggleClass('step_down');
         $(this).parents().next(".record_body").toggle();

     });
   //展开文字
   $(".slide").click(function(){

         $(this).children('i').toggleClass('slide_up');
         $(this).prev().toggleClass('slide_wz');
     });
    //修改默认radio样式
  $("input[type='radio'].inp-radio").each(function(){
    var _inpRadio = $(this),
      _bRadio = _inpRadio.next(),
      name = _inpRadio.attr("name") || '';
    if(_inpRadio.is(":checked")){
      _bRadio.addClass("bon-radio");
    }else{
      if(_bRadio.hasClass("bon-radio"))_bRadio.removeClass("bon-radio");
    }
    _bRadio.click(function(){
      _inpRadio.trigger("click"); 
      if($("input[type='radio']:checked")){
        $("input[type='radio']:checked").next().addClass("bon-radio");  
        $("input[type='radio']:not(:checked)").next().removeClass("bon-radio");
      }
    });
  });

  //虚拟下来菜单
  $(".select_box").live("click",function(event){
    event.stopPropagation();
    $(this).find(".option").toggle();
    $(this).siblings().find(".option").hide();
    $(this).parents().siblings().find(".option").hide();
    $(".select_box").css("z-index", "0");
    $(this).css("z-index", "20");
    });
  $(document).live("click",function(event){
    var eo = $(event.target);
    if ($(".select_box").is(":visible") && eo.attr("class") != "option" && !eo.parent(".option").length)
      $('.option').hide();
  });
  /*赋值给文本框*/
  $(".option a").live("click",function(){       
    var value = $(this).text();
    $(this).parent().siblings(".select_txt").children("em").text(value);    
  });
  /*弹出国家选择*/
   $("#country").click(function(){
      $("#body").css("position","fixed");
      $(".shade").show();
      $(".country_pop").show();
  });
   /*弹出学校选择*/
   $("#school").click(function(){
      $("#body").css("position","fixed");
      $(".shade").show();
      $(".school_pop").show();
  });

  /*弹窗洲选择*/
   $(".chose_list li").click(function(){
    $(this).parents("ul").find("li").removeClass("chose_on");
    $(this).addClass("chose_on");
  });

    /*弹窗国家选择*/
   $(".country_list li").click(function(){
    $(this).parents("ul").find("li").removeClass("chose_on");
    $(this).addClass("chose_on");
  });
  /*关闭注册弹窗*/
   $(".pop_close").click(function(){
      $(".shade").hide();
      $(".country_pop").hide();
      $(".school_pop").hide();
       $("#body").css("position","relative");
  });




});
$(window).load(function(){
  initStyles();
});

$(window).resize(function(){
  initStyles();
});


function tab(nav,content,on,type)
      {
        $(nav).children().bind(type,(function(){
          var $tab=$(this);
          var tab_index=$tab.prevAll().length;
          var $content = $(content).children();
          $(nav).children().removeClass(on);
          $tab.addClass(on);
          $content.hide();
          $content.eq(tab_index).show();
        }));
      }



function initStyles() {
  $("#body_bg").css("height",$("#bg_img").height()+"px");
  $("#body_login").css("height",$("#login_bg").height()+"px");

}