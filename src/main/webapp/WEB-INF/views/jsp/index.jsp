<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/taglibs.jsp" %>
<layout:template>
    <jsp:attribute name="main">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">抓取用户推文</h3>
                        </div>
                        <div class="panel-body">
                            <form role="form" action="${ctx}/twitter" method="post" id="formFetchTweet">
                                <div class="form-group">
                                    <label for="account">用户账号</label>
                                        <input type="text" class="form-control" id="account" name="account"
                                               value="${account}" placeholder="eg: tim_cook">
                                </div>
                                <div class="form-group">
                                    <label for="twitterId">推文ID</label>
                                    <input type="text" class="form-control" id="twitterId" name="twitterId"
                                           value="${twitterId}" placeholder="eg: 805103901418536960">
                                </div>
                                <button type="button" id="fetchTwitterBtn" class="btn btn-primary">启动</button>
                            </form>
                        </div>
                        <div class="panel-footer hide" id="fetchTwitterFooter" style="max-height: 150px; overflow: auto;">
                            正在获取数据，请不要进行任何操作....
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">抓取HashTag</h3>
                        </div>
                        <div class="panel-body">
                            <form role="form" action="/hashtag" method="post">
                                <div class="form-group">
                                    <label for="hashTag">HashTag</label>
                                    <input type="text" class="form-control" id="hashTag" name="hashTag"
                                           value="${hashTag}" placeholder="eg: MacBook Pro">
                                </div>
                                <div class="form-group">
                                    <label for="page">页数</label>
                                    <input type="text" class="form-control" id="page" name="page" value="${page}"
                                           placeholder="eg: 不填则代表获取全部，可能会花很长时间">
                                </div>
                                <button type="submit" class="btn btn-primary">启动</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
    <jsp:attribute name="js">
        <script>
            var $fetchTwitterFooter = $('#fetchTwitterFooter');
            var fetchCmts = function(position, twitterId) {
                $fetchTwitterFooter.append('<div>position: ' + position + ', twitterId: ' + twitterId + '</div>');
                $fetchTwitterFooter.scrollTop(1000000000);
                $.post('${ctx}/twitter/comments', {
                    minPosition: position,
                    twitterId: twitterId
                }, function(data) {
                    if(data) {
                        fetchCmts(data, twitterId);
                    }else {
                        $fetchTwitterFooter.append('<div>抓取结束</div>');
                        $fetchTwitterFooter.scrollTop(1000000000)
                        alert('抓取结束！');
                    }
                });
            };

            $('#fetchTwitterBtn').on('click', function () {
                $('#fetchTwitterFooter').removeClass('hide');
                $('#formFetchTweet').ajaxSubmit({
                    success: function(data) {
                        console.log('data==>' + data);
                        if(data) {
                            fetchCmts(data, $('#twitterId').val());
                        }else{
                            alert('抓取结束！');
                            $fetchTwitterFooter.append('<div>抓取结束</div>');
                            $fetchTwitterFooter.scrollTop(1000000000)
                        }
                    },
                    error: function(data) {
                        console.log(data);
                    }
                })
            });
        </script>
    </jsp:attribute>
</layout:template>
