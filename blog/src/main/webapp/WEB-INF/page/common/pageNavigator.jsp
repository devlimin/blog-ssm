<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
    <c:when test="${empty pageResult || pageResult.totalPage >= pageResult.pageNum}">
        <nav class="text-center">
            <ul class="pagination">
                <li>
                    <a>
                        ${pageResult.totalRow }条
                        ${pageResult.totalPage }页
                    </a>
                </li>
                <c:if test="${pageResult.pageNum != 1 }">
                    <li>
                        <a href="javascript:getPage(1)" aria-label="Previous">
                            <span aria-hidden="true">首页</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:getPage(${pageResult.pageNum - 1})" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </c:if>
                <c:forEach begin="${pageResult.startIndex }" end="${pageResult.endIndex }" var="index">
                    <li>
                        <c:choose>
                            <c:when test="${pageResult.pageNum == index }">
                                <a>${index }</a>
                            </c:when>
                            <c:otherwise>
                                <a href="javascript:getPage(${index})">${index }</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:forEach>
                <c:if test="${pageResult.pageNum != pageResult.totalPage }">
                    <li>
                        <a href="javascript:getPage(${pageResult.pageNum + 1})" aria-label="Previous">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:getPage(${pageResult.totalPage})" aria-label="Previous">
                            <span aria-hidden="true">尾页</span>
                        </a>
                    </li>
                </c:if>
            </ul>
        </nav>
    </c:when>
    <c:otherwise>
        暂无数据！
    </c:otherwise>
</c:choose>
<script>
    function getPage(page) {
        page_url = page_url + "?page=" + page;
        window.location.href=page_url;
    }
</script>