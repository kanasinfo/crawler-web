<%@tag pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs.jsp"%>
<nav class="navbar navbar-default">
	<div class="container">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
					data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="/test">供应链金融</a>
		</div>

		<c:if test="${systemUserKey eq 'sysadmin'}">
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a href="/dashboard/config">配置项列表</a></li>
				</ul>
                <c:if test="${!empty(systemUserKey)}">
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="/dashboard/logout">登出</a></li>
                    </ul>
                </c:if>
			</div>

		</c:if>
	</div>
</nav>