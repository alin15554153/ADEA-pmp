(function ($) {
    $.aede.pmp.rsmt.resourceUploader = {

        /**
         * 暂时仅支持单文件
         * @param id   web upload 容器ID
         * @param func   回调函数
         * @param company   公司
         * @param procode   项目编号
         * @param editoptions 编辑时参数
         * @param fileNum   文件数量
         * @param fileSingleSize    单个文件大小
         * @param totalFileSize 总文件大小
         * @returns {*}
         */
        initUploader: function (company,procode,id,func,editoptions,fileNum,fileSingleSize,totalFileSize) {
            fileNum = !!fileNum ? fileNum : 1 ;
            var $wrap = $("#" + id);
            $wrap.html("");
            $wrap.append('<div class="queueList"></div>').append('<div id="btnContainer" class="btns"></div>');

            var $list = $wrap.find(".queueList");
            var $table = $('<table style="width: 100%;padding: 15px;font-size: 16px;font-weight: bold;"></table>');

            var fileCount = 0,
                fileSize = 0;

            var uploader = WebUploader.create({
                paste: document.body,   //指定监听paste事件的容器，如果不指定，不启用此功能。此功能为通过粘贴来添加截屏的图片。建议设置为document.body.
                disableGlobalDnd: true,
                fileNumLimit: fileNum,    //验证文件总数量, 超出则不允许加入队列。
                fileSizeLimit:  !!totalFileSize ? totalFileSize : 100*1024*1024 ,  //验证文件总大小是否超出限制, 超出则不允许加入队列
                fileSingleSizeLimit: !!fileSingleSize ? fileSingleSize : 5*1024*1024 ,  //验证单个文件大小是否超出限制, 超出则不允许加入队列。
                fileVal: "files",    //设置文件上传域的name
                formData: {
                	'company' : company,
                	'procode' : procode
                },
                method: "POST", //文件上传方式，POST或者GET
                swf: $.aede.expand.getContextPath() + '/resources/js/plugins/webuploader/Uploader.swf',  // swf文件路径
                server: $.aede.expand.getContextPath() + '/management/pmp/rsmt/resource/upload', // 文件接收服务端。
                //pick: '#filepicker',    // 选择文件的按钮。可选. 内部根据当前运行是创建，可能是input元素，也可能是flash.
                resize: false,
            });

            setTimeout(function(){
                uploader.addButton({
                    id: '#btnContainer',
                    label: '选择文件'
                });

            }, 200);

            uploader.onFileQueued = function( file ) {
                if ( fileCount < fileNum ) {
                    addFile( file );
                    fileCount++;
                    fileSize += file.size;
                } else {
                    $.aede.comm.toastr.warning("最多只能上传"+fileNum+"个文件！");
                }
            };

            uploader.onFileDequeued = function( file ) {
                removeFile( file );
                fileCount--;
                fileSize -= file.size;

            };

            uploader.on( 'uploadSuccess', function( file, obj ) {
                if (obj["responseCode"] == "1") {
                    var fileData = obj["datas"];
                    func(fileData);
                    $.aede.comm.toastr.success("文件" + fileData.fileNames + "上传成功！");
                } else {
                    $.aede.comm.toastr.error(data["responseMsg"]);
                }

            });

            uploader.on( 'uploadComplete', function( file ) {
                $( '#p_'+file.id ).parent().fadeOut();
                if (!!delData) {
                    //
                	
                    $.ajax({
                        type: "POST",
                        async: true,// 同步请求
                        url: $.aede.expand.getContextPath() + '/management/pmp/rsmt/resource/delUploadFile',
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        data : JSON.stringify(delData),
                        success: function (res) {
                            return;
                        },
                        error: function () {
                            return;
                        }
                    });
                }
            });//
            
            uploader.on( 'startUpload', function() {
            	//console.info(uploader.getFiles().length);
            	var fc = uploader.getFiles().length;
            	if(fc==0){
            		func();
            	}
            });

            // 文件上传过程中创建进度条实时显示。
            uploader.onUploadProgress = function( file, percentage ) {
                $("#p_" + file.id).css( 'width', percentage * 100 + '%' );
            };

            uploader.on( 'error', function( type ) {

                switch( type ) {
                    case 'Q_EXCEED_SIZE_LIMIT':
                        $.aede.comm.toastr.warning('总文件大小超出');
                        break;

                    case 'Q_EXCEED_NUM_LIMIT':
                        $.aede.comm.toastr.warning('文件数量超出');
                        break;
                    case 'Q_TYPE_DENIED' :
                        $.aede.comm.toastr.warning('文件类型不匹配');
                        break;
                    default:
                        $.aede.comm.toastr.warning('单个文件大小超出');
                        break;
                }

            });

            //编辑时原上传文件
            var delData = [];

            function editFile(editoptions) {
                var path = editoptions.path;
                var id =  new Date().getTime();
                if(path.lastIndexOf(".") > 0) {
                    id = path.substring(0,path.lastIndexOf("."));
                }

                $table.append( '<tr class="item">' +
                    '<td class="info">' + editoptions.name + ' </td>' +
                    '<td align="right"><button class="btn btn-danger btn-xs" id="'+id+'"><i class="fa fa-trash-o"></i></button> </td>' +
                    '</tr>' );
                $list.append($table);
                path = company+"/"+procode+"/"+path;
                delData.push(path);

                $("#" + id).bind( 'click', function() {
                    fileCount--;
                    var $tr = $('#'+id).parent().parent();
                    $tr.off().find('.table').off().end().remove();
                });
            }
            if(!!editoptions) {
                editFile(editoptions);
                fileCount++;
            }



            // 当有文件添加进来时执行，负责view的创建
            function addFile( file ) {

                var percent = '<div style="height: 5px;" class="progress progress-striped active">' +
                    '<div id="p_'+file.id+'" class="progress-bar" role="progressbar" style="width: 0%">' +
                    '</div>' +
                    '</div>';

                $table.append( '<tr class="item">' +
                    '<td class="info">' + file.name + ' - '+WebUploader.formatSize( file.size, 0, ['B', 'KB', 'MB', 'GB'] )+ '<br />' + percent +  ' </td>' +
                        '<td align="right"><button class="btn btn-danger btn-xs" id="'+file.id+'"><i class="fa fa-trash-o"></i></button> <br><br> </td>' +
                    '</tr>' );
                $list.append($table);

                $("#" + file.id).bind( 'click', function() {
                    uploader.removeFile( file );
                    return;
                });
                uploader.refresh();
            }
            // 负责view的销毁
            function removeFile( file ) {
                var $tr = $('#'+file.id).parent().parent();
                $tr.off().find('.table').off().end().remove();
            }
            return uploader;
        }

    }
})(jQuery);