$(function () {
    <!--数据验证-->
    $("#loginForm").bootstrapValidator({
        message:'This value is not valid',
//            定义未通过验证的状态图标
        feedbackIcons: {/*输入框不同状态，显示图片的样式*/
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
//            字段验证
        fields:{
//                密码
            password:{
                message:'密码非法',
                validators:{
                    notEmpty:{
                        message:'密码不能为空'
                    },
//                        限制字符串长度
                    stringLength:{
                        min:3,
                        max:20,
                        message:'密码长度必须位于3到20之间'
                    },
//                        基于正则表达是的验证
                    regexp:{
                        regexp:/^[a-zA-Z0-9_\.]+$/,
                        message:'密码由数字字母下划线和.组成'
                    }
                }
            },

//                用户名
            username:{
                validators:{
                    notEmpty:{
                        message:'用户名不能为空'
                    }
                }
            },
            tryCode: {
                validators: {
                    notEmpty: {
                        message: '请输入验证码'
                    },
                    remote: {
                        type:"POST",
                        message: '验证码错误',
                        url: '/checkCode',
                        data : '',//这里默认会传递该验证字段的值到后端,
                        delay: 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                    }
                }
            }
        }
    })
    changeImg();
    showErrorMsg();
})

function clearError() {
    $("#error").css("display","none");
}

function changeImg() {
    $("#tryCode").val("");
    $.ajax({
        url: "/defaultKaptcha?d="+new Date()*1 ,
    });
    $("#codImg").attr("src", "/defaultKaptcha");
}

function showErrorMsg() {
    var loginError=$("#error").val();
    var errorMsg=$("#error").attr("data-error-msg");
    if (loginError){
        bootoast({
            message: errorMsg,
            type: 'danger',
            position:'bottom',
            timeout:2
        });
    }

}