<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/taglibs.jsp" %>
<layout:template >
    <jsp:attribute name="main">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">设置抓取任务参数</h3>
                        </div>
                        <div class="panel-body">
                            <form role="form" action="/fetch" method="post">
                                <div class="form-group">
                                    <label for="params">查询参数</label>
                                    <input type="text" class="form-control" id="params" name="params" placeholder="eg: q=&s=ka&s=sr&s=jr&s=mr&s=accept360&p=kwbw">
                                </div>
                                <div class="form-group">
                                    <label for="start">开始条目序号</label>
                                    <input type="number" class="form-control" id="start" name="start" placeholder="从第几条开始，不小于1">
                                </div>
                                <%--<div class="form-group">
                                    <label for="pageSize">一页多少条</label>
                                    <input type="number" class="form-control" id="pageSize" name="pageSize"
                                           placeholder="默认15条">
                                </div>--%>
                                <div class="form-group">
                                    <label for="count">抓取多少页</label>
                                    <input type="number" id="count" name="count" class="form-control" placeholder="不小于1">
                                </div>
                                <button type="submit" class="btn btn-primary">启动</button>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">重试未完成任务</h3>
                        </div>
                        <div class="panel-body">
                            <p>未完成任务数量：${count}</p>
                            <p><a href="/fetchUnsuccess" class="btn btn-primary">重试</a></p>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:template>
