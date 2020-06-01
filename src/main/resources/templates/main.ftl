<#import "parts/common.ftl" as c>

<@c.page>

    <div>
    <strong>${errorMessage}</strong>

    <form method="post" action="filter">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="text" name="filter" value=${filter}>
        <button type="submit">Отфильтровать</button>
    </form>
    <div>
        Список лотов
    </div>

    <div class="card-columns">
    <#list points as point>
        <div class="card" style="width: 18rem;">
            <strong>${point.name}</strong>

            <form method="post" action="order">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <input type="hidden" name="id" value="${point.id}">
                <button type="submit">Выбрать</button>
            </form>
        </div>
    <#else>
        No Points
    </#list>
    </div>

</@c.page>