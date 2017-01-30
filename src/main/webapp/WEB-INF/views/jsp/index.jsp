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
                            <form role="form" action="${ctx}/twitter" method="post">
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
                                <button type="submit" class="btn btn-primary">启动</button>
                                <a href="${ctx}/view/${account}/${twitterId}" data-toggle="modal-remote" class="btn btn-default" type="button">查看结果</a>
                            </form>
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
</layout:template>
