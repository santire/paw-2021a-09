<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="card mt-2 p-0 mx-0">
  <div class="card-body">
    <div class="border-bottom pb-2 ml-2">
      <h4 id="burgundy">
        <spring:message code="restaurant.filters"/>
      </h4>
    </div>
    <form>
      <input type="hidden" id="seatchinput" name="search" value="${searchString}">
      <div class="form-group">
        <label for="tags">
          <spring:message code="restaurant.filters.tags"/>
        </label>
        <select 
          name="tags"
          class="my-select selectpicker" 
          title="<spring:message code="restaurant.filters.allTags"/>"
          multiple
          data-live-search="true"
          >
            <c:forEach var="tagId" items="${tags.keySet()}">
              <c:choose>
                <c:when test="${tagsChecked.contains(tagId)}"> 
                  <option value="${tagId}" selected="selected">
                      <spring:message code="restaurant.tag.${tagId}"/>
                  </option>
                </c:when>
                <c:otherwise>
                  <option value="${tagId}">
                      <spring:message code="restaurant.tag.${tagId}"/>
                  </option>
                </c:otherwise>
              </c:choose>
            </c:forEach>
          </select>
      </div>

      <div class="form-group">
        <label for="minprice"><spring:message code="restaurant.filters.minPrice"/></label>
        <input value="${minPrice}" type="number" id="minprice" name="min" min="0">
        <label for="maxprice"><spring:message code="restaurant.filters.maxPrice"/></label>
        <input value="${maxPrice}" type="number" id="maxprice" name="max" min="0">
      </div>

      <div class="form-group">
        <label for="sortBy">
          <spring:message code="restaurant.filters.sortBy"/>
        </label>
        <select 
          name="sortBy"
          class="my-select selectpicker show-tick" 
          title="<spring:message code="restaurant.filters.${defaultSortType.getSortType()}"/>"
          data-live-search="true"
          >
          <c:forEach var="sortType" items="${sortTypes}">
            <c:choose>
              <c:when test="${sortBy eq sortType.getSortType() }"> 
                <option value="${sortType.getSortType()}" selected="selected">
                    <spring:message code="restaurant.filters.${sortType.getSortType()}"/>
                </option>
              </c:when>
              <c:otherwise>
                <option value="${sortType.getSortType()}">
                    <spring:message code="restaurant.filters.${sortType.getSortType()}"/>
                </option>
              </c:otherwise>
            </c:choose>
          </c:forEach>
        </select>
      </div>

      <div class="form-group">
        <label for"order">
          <spring:message code="restaurant.filters.orderBy"/>
        </label>
        <select
            name="order"
            class="my-select selectpicker show-tick" 
            title="<spring:message code="restaurant.filters.${defaultOrder}"/>"
            >
            <c:choose>
              <c:when test="${desc}">
                <option value="DESC" selected="selected">
                  <spring:message code="restaurant.filters.desc"/>
                </option>
              </c:when>
              <c:otherwise>
                <option value="DESC">
                  <spring:message code="restaurant.filters.desc"/>
                </option>
              </c:otherwise>
            </c:choose>
            <c:choose>
              <c:when test="${!desc}">
                <option value="ASC" selected="selected">
                  <spring:message code="restaurant.filters.asc"/>
                </option>
              </c:when>
              <c:otherwise>
                <option value="ASC">
                  <spring:message code="restaurant.filters.asc"/>
                </option>
              </c:otherwise>
            </c:choose>
        </select>
      </div>
      <div>
        <input
                type="submit"
                class="btn btn-outline-secondary btn-block w-100 px-0 mx-auto"
                style="width: 100%;"
                value='<spring:message code="restaurant.filters.search"/>'
        />
      </div>

    </form>
  </div>
</div>
