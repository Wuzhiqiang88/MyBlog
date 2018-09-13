$(function () {
    $('#date1').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn')
    });

    $("#userForm").bootstrapValidator({
        message: 'This value is not valid',
//            定义未通过验证的状态图标
        feedbackIcons: {
            /*输入框不同状态，显示图片的样式*/
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
//            字段验证
        fields: {
            //                电子邮箱
            email: {
                validators: {
                    notEmpty: {
                        message: '邮箱地址不能为空'
                    },
                    emailAddress: {
                        message: '请输入正确的邮箱地址'
                    }
                }
            },
            nick: {
                message: '昵称非法',
                validators: {
                    notEmpty: {
                        message: '昵称不能为空'
                    },
                    //                        限制字符串长度
                    stringLength: {
                        min: 2,
                        max: 20,
                        message: '昵称长度必须位于3到20之间'
                    }
                }
            }

        }
    })

    $("#changePwdForm").bootstrapValidator({
        message: 'This value is not valid',
//            定义未通过验证的状态图标
        feedbackIcons: {
            /*输入框不同状态，显示图片的样式*/
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
//            字段验证
        fields: {
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
                        regexp:/^[a-zA-Z0-9_\.-]+$/,
                        message:'密码由数字字母下划线、减号和.组成'
                    }
                },
            },

            repeatPassword: {
                validators: {
                    notEmpty: {
                        message: '请填写确认密码'
                    },
                    identical: {
                        field: 'password',
                        message: '确认密码与密码不一致'
                    }
                }
            }
        }
    });

    $("#changeAvatar-a").on('click', function(){
        $('.cropped').html('');
        var avatar=$("#avatar").attr("src");
        var options =
            {
                thumbBox: '.thumbBox',
                spinner: '.spinner',
                imgSrc: avatar
            }
        var cropper = $('.imageBox').cropbox(options);
        $('#upload-file').on('change', function(){
            var reader = new FileReader();
            reader.onload = function(e) {
                options.imgSrc = e.target.result;
                cropper = $('.imageBox').cropbox(options);
            }
            reader.readAsDataURL(this.files[0]);
            this.files = [];
        })
        $('#btnCrop').on('click', function(){
            var img = cropper.getDataURL();
            $('.cropped').html('');
            $('.cropped').append('<img src="'+img+'" align="absmiddle" style="width:65px;margin-top:35px;border-radius:65px;box-shadow:0px 0px 12px #7E7E7E;" ><p>65px*65px</p>');
            $('.cropped').append('<img src="'+img+'" align="absmiddle" style="width:100px;margin-top:35px;border-radius:100px;box-shadow:0px 0px 12px #7E7E7E;"><p>100px*100px</p>');
        })
        $('#btnZoomIn').on('click', function(){
            cropper.zoomIn();
        })
        $('#btnZoomOut').on('click', function(){
            cropper.zoomOut();
        })

        $("#submitEditAvatar").on('click', function(){
            // 获取 CSRF Token
            var csrfToken = $("meta[name='_csrf']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");

            var username=$("#changeAvatar-a").attr("data-username");
            var url="/u/"+username+"/avatar";
            var img = cropper.getBlob();
            console.log(img);
            var formdata = new FormData();
            formdata.append("imagefile", img);
            console.log(formdata);
            $.ajax({
                url: url,
                type: 'POST',
                cache: false,
                data: formdata,
                processData: false,
                contentType: false,
                beforeSend: function(request) {
                    request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
                },
                success: function(data){
                    if (data.success) {
                        bootoast({
                            message: data.message,
                            type: 'success',
                            position:'bottom',
                            timeout:2
                        });
                        location.reload(true);
                    } else {
                        bootoast({
                            message: data.message,
                            type: 'danger',
                            position:'bottom',
                            timeout:2
                        });
                    }

                },
                error : function() {

                }
            })
        })
    })

    $("#changeAvatar-a2").on('click', function(){
        $('.cropped').html('');
        var avatar=$("#avatar").attr("src");
        var options =
            {
                thumbBox: '.thumbBox',
                spinner: '.spinner',
                imgSrc: avatar
            }
        var cropper = $('.imageBox').cropbox(options);
        $('#upload-file').on('change', function(){
            var reader = new FileReader();
            reader.onload = function(e) {
                options.imgSrc = e.target.result;
                cropper = $('.imageBox').cropbox(options);
            }
            reader.readAsDataURL(this.files[0]);
            this.files = [];
        })
        $('#btnCrop').on('click', function(){
            var img = cropper.getDataURL();
            $('.cropped').html('');
            $('.cropped').append('<img src="'+img+'" align="absmiddle" style="width:65px;margin-top:35px;border-radius:65px;box-shadow:0px 0px 12px #7E7E7E;" ><p>65px*65px</p>');
            $('.cropped').append('<img src="'+img+'" align="absmiddle" style="width:100px;margin-top:35px;border-radius:100px;box-shadow:0px 0px 12px #7E7E7E;"><p>100px*100px</p>');
        })
        $('#btnZoomIn').on('click', function(){
            cropper.zoomIn();
        })
        $('#btnZoomOut').on('click', function(){
            cropper.zoomOut();
        })

        $("#submitEditAvatar").on('click', function(){
            // 获取 CSRF Token
            var csrfToken = $("meta[name='_csrf']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");

            var username=$("#changeAvatar-a").attr("data-username");
            var url="/u/"+username+"/avatar";
            var img = cropper.getBlob();
            console.log(img);
            var formdata = new FormData();
            formdata.append("imagefile", img);
            console.log(formdata);
            $.ajax({
                url: url,
                type: 'POST',
                cache: false,
                data: formdata,
                processData: false,
                contentType: false,
                beforeSend: function(request) {
                    request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
                },
                success: function(data){
                    if (data.success) {
                        bootoast({
                            message: data.message,
                            type: 'success',
                            position:'bottom',
                            timeout:2
                        });
                        location.reload(true);
                    } else {
                        bootoast({
                            message: data.message,
                            type: 'danger',
                            position:'bottom',
                            timeout:2
                        });
                    }

                },
                error : function() {

                }
            })
        })
    })


});




