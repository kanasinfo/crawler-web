<%@tag pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/taglibs.jsp" %>
<%@ attribute name="main" fragment="true" description="主体信息，注意：用jsp:attribute调用，不要把此标签设置到属性上" %>
<%@ attribute name="css" fragment="true" description="需要引入的额外的css信息或自定义的css，注意：用jsp:attribute调用，不要把此标签设置到属性上" %>
<%@ attribute name="js" fragment="true" description="需要引入的额外的js信息或自定义的js，注意：用jsp:attribute调用，不要把此标签设置到属性上" %>
<%@ attribute name="noHead" type="java.lang.Boolean" description="是否有菜单" %>
<!DOCTYPE html>
<html>
<head>
    <common:metaTitle></common:metaTitle>
    <common:css></common:css>

    <jsp:invoke fragment="css"/>
</head>
<body>
<jsp:invoke fragment="main"/>
<common:js></common:js>
<jsp:invoke fragment="js"/>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"></div>
</body>
</html>