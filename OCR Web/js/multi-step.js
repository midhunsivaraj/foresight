
var current_step;
var next_step;

$(".next").click(function(){
current_step = $(this).parent();
next_step = $(this).parent().next();
$("#progressbar li").eq($("fieldset").index(next_step)).addClass("active");
$("#progressbar li").eq($("fieldset").index(current_step)).removeClass("active");
next_step.fadeIn();
current_step.fadeOut();
});