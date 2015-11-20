<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="col-sm-4">
  <a href="#${param.url}" ng-click="selectedTab = '${param.url}'">
    <div class="jumbotron text-center">
      <h1><span class="${param.icon}"></span></h1>
      <p>
        <spring:message code="${param.label}" />
      </p>
    </div>
  </a>
</div>