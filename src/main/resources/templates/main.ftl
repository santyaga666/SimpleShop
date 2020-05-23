<#import "parts/common.ftl" as c>

<@c.page>

    <form action="/logout" method="post">
        <input type="submit" value="Sign Out"/>
    </form>
    <div>
    <strong>${errorMessage}</strong>
    <strong></strong>
    <div>
        <form method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <input type="text" name="photo" placeholder="Добавьте фото">
            <input type="text" name="price" placeholder="Введите цену">
            <input type="text" name="name" placeholder="Введите имя">
            <button type="submit">Добавить</button>
        </form>
    </div>
    <form method="post" action="filter">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="text" name="filter" value=${filter}>
        <button type="submit">Отфильтровать</button>
    </form>
    <form method="post" action="clear">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Очистить</button>
    </form>
    <div>Список лотов</div>

    <#list points as point>
        <div>
            <strong>${point.photo}</strong>
            <strong>${point.price}</strong>
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


</@c.page>