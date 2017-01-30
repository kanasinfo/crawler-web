<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/taglibs.jsp" %>
<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                    aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">推文预览</h4>
        </div>
        <div class="modal-body">
            <h3>${tweet.content}</h3>
            <hr>
            <div style="height: 500px; overflow: auto;">
                <ol>
                    <c:forEach items="${tweets}" var="t">
                        <li>${t.content}</li>
                    </c:forEach>
                </ol>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        </div>
    </div>
</div>